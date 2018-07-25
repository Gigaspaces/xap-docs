package com.gigaspaces.jarvis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortcodeAnalyzer {

	private static final boolean VERBOSE = Boolean.parseBoolean(System.getenv("VERBOSE_REPORT"));
	
    public static void scanAndReport(String path, String target) throws IOException {
        ShortcodeAnalyzer f = new ShortcodeAnalyzer();
        f.initShortcodes("site-overrides\\layouts\\shortcodes");
        f.scanPath(new File(path));
        f.report(new File(target));
    }

    private final Map<String, ShortcodeCounter> statsWithOverrides = new HashMap<>();
    private final Map<String, ShortcodeCounter> statsWithoutOverrides = new HashMap<>();
    private final Set<String> shortcodesWithOverrides = new HashSet<>();

    private void initShortcodes(String path) {
        for (File f : new File(path).listFiles()) {
            shortcodesWithOverrides.add(f.getName().replace(".html",""));
        }
    }

    public void scanPath(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                scanPath(f);
            }
        } else if (file.isFile()) {
            try {
                Files.lines(file.toPath()).forEach(line -> processLine(file, line));
            } catch (RuntimeException e) {
                throw new IOException("Failed to process file " + file, e);
            }
        }
    }

    public void report(File target) throws IOException {
        System.out.println(target.getAbsolutePath());
        if (target.exists())
            target.delete();

        try (BufferedWriter writer = Files.newBufferedWriter(target.toPath(), StandardOpenOption.CREATE_NEW)) {
            writer.write("*** Shortcodes with overrides ***" + System.lineSeparator());
            writer.write("---------------------------------" + System.lineSeparator());
            report(writer, statsWithOverrides);
            writer.write(System.lineSeparator());
            writer.write("*** Shortcodes without overrides ***" + System.lineSeparator());
            writer.write("------------------------------------" + System.lineSeparator());
            report(writer, statsWithoutOverrides);
        }
        System.out.println("Created " + target.toString());
    }

    private static void report(BufferedWriter writer, Map<String, ShortcodeCounter> stats) throws IOException {
        Map.Entry<String, ShortcodeCounter>[] shortcodes = stats.entrySet().toArray(new Map.Entry[0]);
        Arrays.sort(shortcodes, (Comparator<Map.Entry<String, ShortcodeCounter>>) (e1, e2) -> Integer.compare(e2.getValue().counter.get(), e1.getValue().counter.get()));
        for (Map.Entry shortcode : shortcodes) {
            writer.write(shortcode.getKey() + "," + shortcode.getValue() + System.lineSeparator());
        }
    }

    private void processLine(File file, String line) {
        processLine(file, line, "{{%", "%}}");
        processLine(file, line, "{{<", ">}}");
    }

    private void processLine(File file, String line, String prefix, String suffix) {
        Scanner s = new Scanner(line);
        while (s.findNext(prefix)) {
            s.skipWhiteSpaces();
            if (s.peek() == '/')
                continue;
            String shortcode = s.nextWord();
            if (shortcode == null || shortcode.isEmpty())
                throw new IllegalStateException("Empty shortcode - [" + line + "]");
            if (s.findNext(suffix)) {
                Map<String, ShortcodeCounter> stats = shortcodesWithOverrides.contains(shortcode) ? statsWithOverrides : statsWithoutOverrides;
				stats.putIfAbsent(shortcode, new ShortcodeCounter());
			    stats.get(shortcode).counter.incrementAndGet();
                stats.get(shortcode).locations.add(file.toString());
            } else {
                throw new IllegalStateException("Missing close " + suffix + System.lineSeparator() + line);
            }
        }
    }

    private static class Scanner {
        private final String s;
        private int location;

        private Scanner(String s) {
            this.s = s;
        }

        char peek() {
            return s.charAt(location);
        }

        void skipWhiteSpaces() {
            // Skip white spaces
            while (Character.isWhitespace(s.charAt(location)))
                location++;
        }

        boolean findNext(String token) {
            int index = s.indexOf(token, location);
            if (index == -1)
                return false;
            location = index + token.length();
            return true;
        }

        String nextWord() {
            // Get word:
            int pos = location;
            while (Character.isLetterOrDigit(s.charAt(pos)) || s.charAt(pos) == '-'|| s.charAt(pos) == '_')
                pos++;
            String result = s.substring(location, pos);
            location = pos;
            return result;
        }
    }
	
	public static class ShortcodeCounter {
		private final AtomicInteger counter = new AtomicInteger();
		private final Set<String> locations = new HashSet<String>();
				
		@Override
		public String toString() {
			return String.valueOf(counter.get()) + toString(locations, VERBOSE);
		}
		
		private static String toString(Set<String> locations, boolean verbose) {
			StringBuilder sb = new StringBuilder();
			sb.append(" (#locations: " + locations.size());
			if (verbose) {
				sb.append(")");
				for (String location : locations) {
					sb.append(System.lineSeparator());
					sb.append("    " + location);
				}
			} else {
				sb.append(" " + locations.iterator().next() + ")");
			}
			return sb.toString();
		}
	}

	/*
    public static void main(String[] args) {
        String line = "| maxLeaseDuration | The time the system waits between every lease renewal, for example: if the parameter value is `8000`, the system renews the space lease every 8000 `[milliseconds]`.<br>{{<infosign>}} As this value is reduced, renewal requests are performed more frequently while the service is up, and lease expiration occurs sooner when the service goes down. | 8000 |";
        new ShortcodeAnalyzer().processLine(line);
    }
	*/
}

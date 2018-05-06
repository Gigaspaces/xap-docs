package com.gigaspaces.jarvis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortcodeAnalyzer {

    public static void scanAndReport(String path, String target) throws IOException {
        ShortcodeAnalyzer f = new ShortcodeAnalyzer();
        f.scanPath(new File(path));
        f.report(new File(target));
    }

    private final Map<String, AtomicInteger> stats = new HashMap<>();

    public void scanPath(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                scanPath(f);
            }
        } else if (file.isFile()) {
            Files.lines(file.toPath()).forEach(this::processLine);
        }
    }

    public void report(File target) throws IOException {
        System.out.println(target.getAbsolutePath());
        Map.Entry<String, AtomicInteger>[] shortcodes = stats.entrySet().toArray(new Map.Entry[0]);
        Arrays.sort(shortcodes, (Comparator<Map.Entry<String, AtomicInteger>>) (e1, e2) -> Integer.compare(e2.getValue().get(), e1.getValue().get()));

        try (BufferedWriter writer = Files.newBufferedWriter(target.toPath(), StandardOpenOption.CREATE)) {
            for (Map.Entry shortcode : shortcodes) {
                writer.write(shortcode.getKey() + "," + shortcode.getValue() + System.lineSeparator());
            }
        }
        System.out.println("Created " + target.toString());
    }

    private void processLine(String line) {
        Scanner s = new Scanner(line);
        while (s.findNext("{{%")) {
            s.skipWhiteSpaces();
            if (s.peek() == '/')
                continue;
            String shortcode = s.nextWord();
            if (shortcode == null || shortcode.isEmpty())
                throw new IllegalStateException("Empty shortcode - [" + line + "]");
            if (s.findNext("%}}")) {
                stats.putIfAbsent(shortcode, new AtomicInteger());
                stats.get(shortcode).incrementAndGet();
            } else {
                throw new IllegalStateException("Missing close");
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
            while (Character.isAlphabetic(s.charAt(pos)))
                pos++;
            String result = s.substring(location, pos);
            location = pos;
            return result;
        }
    }
}

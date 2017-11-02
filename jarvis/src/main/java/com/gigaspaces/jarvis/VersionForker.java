package com.gigaspaces.jarvis;

import com.gigaspaces.jarvis.model.ContentSection;
import com.gigaspaces.jarvis.model.VersionContainer;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 *
 * @author niv
 */
public class VersionForker {

    private static final Logger logger = Logger.getInstance();
    private static final String EARLY_ACCESS = " Early Access";
    private final Config config;
    private final Path srcPath;
    private final Path destPath;
    private final Map<String, String> prefixToReplace = new HashMap<>();
    private final Map<String, String> patternsToReplace = new HashMap<>();
    private final String srcVersion;
    private final String dstVersion;
    private final String srcVersionRaw;
    private final String dstVersionRaw;

    public VersionForker(Config config, String srcVersion, String dstVersion) {
        this.config = config;
        File xapPath = new File(config.getContentPath(), "xap");
        this.srcVersion = srcVersion;
        this.dstVersion = dstVersion;
        this.srcPath = new File(xapPath, srcVersion).toPath();
        this.destPath = new File(xapPath, dstVersion).toPath();
        this.srcVersionRaw = srcVersion.replace(".", "");
        this.dstVersionRaw = dstVersion.replace(".", "");
        patternsToReplace.put(srcVersion, dstVersion);
        patternsToReplace.put(srcVersionRaw, dstVersionRaw);
        prefixToReplace.put("type: post" + srcVersionRaw, "type: post" + dstVersionRaw);
        prefixToReplace.put("categories: XAP" + srcVersionRaw, "categories: XAP" + dstVersionRaw);
    }

    public ContentSection fork() {
        if (Files.exists(destPath)) {
            logger.warning("Version fork aborted - path already exists: " + destPath);
            return null;
        }
        logger.info("Forking " + srcVersion + " to " + dstVersion);
        try {
            //forkContent();
            //forkLayouts();
            //forkNavbar();
            //updateConfigToml();
            updateShortcodes();
            logger.info("Forking completed");
            VersionContainer result = new VersionContainer(destPath.toFile());
            result.load(config);
            return result;
        } catch (IOException ex) {
            logger.warning("Failed to fork " + srcVersion + " to " + dstVersion + ": " + ex);
            return null;
        }
    }

    private void forkContent() throws IOException {
        logger.info("Forking content: " + srcPath + " => " + destPath);
        CopyStats stats = copyRecursively(srcPath, destPath, p -> {
            if (p.toString().endsWith(".markdown"))
                processFile(p, l -> replacePrefix(l, prefixToReplace));
        });
        logger.info("Content forked: " + stats.foldersCounter + " folders and " + stats.filesCounter + " files");
    }

    private void forkLayouts() throws IOException {
        File layouts = new File(config.getSitePath(), "layouts");

        Path srcHome = new File(layouts, "home" + srcVersionRaw).toPath();
        Path dstHome = new File(layouts, "home" + dstVersionRaw).toPath();
        logger.info("Forking layouts: " + srcHome + " => " + dstHome);
        copyRecursively(srcHome, dstHome, p -> processFile(p, l -> replace(l, patternsToReplace)));

        Path srcPost = new File(layouts, "post" + srcVersionRaw).toPath();
        Path dstPost = new File(layouts, "post" + dstVersionRaw).toPath();
        logger.info("Forking layouts: " + srcPost + " => " + dstPost);
        copyRecursively(srcPost, dstPost, p -> processFile(p, l -> replace(l, patternsToReplace)));
    }

    private static void processFile(Path p, Function<String, String> lineProcessor) throws IOException {
        Collection<String> lines = processLines(p, lineProcessor);
        if (lines != null)
            Files.write(p, lines, StandardOpenOption.CREATE);
    }

    private static Collection<String> processLines(Path p, Function<String, String> lineProcessor) throws IOException {
        List<String> lines = new ArrayList<>();
        AtomicBoolean modified = new AtomicBoolean(false);
        Files.lines(p).forEach(line -> {
            String modLine = lineProcessor.apply(line);
            lines.add(modLine);
            // NOTE: intentional ref comparison to see if string has changed.
            if (line != modLine)
                modified.set(true);            
        });

        return modified.get() ? lines : null;
    }

    private static CopyStats copyRecursively(Path src, Path dst, IOConsumer<? super Path> action) throws IOException {
        CopyStats stats = new CopyStats();
        Files.walk(src).forEach(s -> {
            try {
                Path d = dst.resolve(src.relativize(s));
                Files.copy(s, d);
                stats.increment(d);
                if (action != null && !Files.isDirectory(s)) {
                    action.accept(d);
                }
            } catch (IOException e) {
                logger.warning("Failed to copy " + s + ": " + e);
            }
        });
        return stats;
    }

    private static String replacePrefix(String line, Map<String, String> prefixes) {
        for (Map.Entry<String, String> entry : prefixes.entrySet()) {
            if (line.startsWith(entry.getKey())) {
                return entry.getValue() + line.substring(entry.getKey().length());
            }
        }
        return line;
    }

    private static String replace(String line, Map<String, String> patterns) {
        for (Map.Entry<String, String> entry : patterns.entrySet())
            line = line.replace(entry.getKey(), entry.getValue());
        return line;
    }

    private void forkNavbar() throws IOException {
        Path partials = Paths.get(config.getSitePath().toString(), "themes", "hugo-bootswatch", "layouts", "partials");
        logger.info("Forking navbars: " + partials);
        Path src = Paths.get(partials.toString(), "navbar" + srcVersionRaw + ".html");
        Path dst = Paths.get(partials.toString(), "navbar" + dstVersionRaw + ".html");
        copyRecursively(src, dst, p -> processFile(p, l -> l.replace(srcVersion, dstVersion + EARLY_ACCESS)));
        Files.list(partials)
                .filter(p -> p.getFileName().toString().startsWith("navbar"))
                .forEach(p -> navbarProcesor(p));
    }
    
    private void navbarProcesor(Path p) {
        final String vNext = "<!--<li><a href=\"/xap/vNext\">vNext</a></li>-->";
        try {
            processFile(p, l -> {
                if (l.contains(vNext)) {
                    String vNextLine = l.replace("<!--", "").replace("-->", "");
                    String srcLine = vNextLine.replace("vNext\">vNext", srcVersion + "\">" + srcVersion);
                    String dstLine = vNextLine.replace("vNext\">vNext", dstVersion + "\">" + dstVersion + EARLY_ACCESS);
                    String fileName = p.getFileName().toString();
                    if (fileName.equals("navbar" + srcVersionRaw + ".html")) {
                        l+= System.lineSeparator() + dstLine + System.lineSeparator() + srcLine;                        
                    } else if (fileName.equals("navbar" + dstVersionRaw + ".html")) {
                        l+= System.lineSeparator() + srcLine;
                    } else {
                        l+= System.lineSeparator() + dstLine;
                    }                    
                }
                return l;
            });
        } catch (IOException e) {
            logger.warning("Failed to process " + p + ": " + e);
        }
    }

    private void updateConfigToml() throws IOException {
        Path configToml = new File(config.getSitePath(), "config.toml").toPath();
        // Collect 12.2 lines:
        List<String> newLines = new ArrayList<>();
        AtomicBoolean include = new AtomicBoolean(false);
        AtomicBoolean replace = new AtomicBoolean(false);
        Files.readAllLines(configToml).forEach(l -> {
            if (l.startsWith("[")) {
                boolean relevant = l.equals("[params.xap" + srcVersionRaw + "]");
                include.set(relevant);                                
                replace.set(relevant);
            }        
            if (l.startsWith("#"))
                replace.set(l.contains(srcVersion));
            if (include.get()) {
                if (replace.get()) {
                    l = l.replace("xap" + srcVersionRaw, "xap" + dstVersionRaw);
                    l = l.replace(srcVersion, dstVersion);
                    if (l.contains("-ga-")) {
                        String suffix;
                        if (l.endsWith(".zip\""))
                            suffix = ".zip\"";
                        else if (l.endsWith(".msi\""))
                            suffix = ".msi\"";
                        else
                            suffix = "\"";

                        l = l.substring(0, l.indexOf("-ga")) + "-m1-b?????" + suffix;
                    }
                }
                newLines.add(l);
            }
        });
        // Append modified lines
        Files.write(configToml, newLines, StandardOpenOption.APPEND);
    }

    private void shortcodeProcesor(Path p) {
        try {
            Collection<String> lines = processLines(p, l -> {
                if (l.contains(srcVersion)) {
                    String dstLine = l.replace(srcVersion, dstVersion)
                            .replace("xap" + srcVersionRaw, "xap" + dstVersionRaw);
                    String srcLine = l.replace("{{ if ", "{{ else if ");
                    l = dstLine + System.lineSeparator() + srcLine;
                }
                return l;
            });
            if (lines != null) {
                String text = String.join(System.lineSeparator(), lines);
                Files.write(p, text.getBytes(Charset.forName("UTF-8")));
            }
        } catch (IOException e) {
            logger.warning("Failed to process " + p + ": " + e);
        }
    }

    private void updateShortcodes() throws IOException {
        logger.info("Updating shortcodes...");        
        Path shortcodes = Paths.get(config.getSitePath().getPath(), "layouts", "shortcodes");
        Files.list(shortcodes).forEach(p -> shortcodeProcesor(p));
    }
    
    private interface IOConsumer<T> {

        void accept(T t) throws IOException;
    }

    private static class CopyStats {

        public final AtomicInteger foldersCounter = new AtomicInteger();
        public final AtomicInteger filesCounter = new AtomicInteger();

        private void increment(Path p) {
            if (Files.isDirectory(p)) {
                foldersCounter.incrementAndGet();
            } else {
                filesCounter.incrementAndGet();
            }
        }
    }
}

package com.gigaspaces.jarvis;

import com.gigaspaces.jarvis.model.CanonicalUrlReport;
import com.gigaspaces.jarvis.model.ContentSection;
import com.gigaspaces.jarvis.model.FileUtils;
import com.gigaspaces.jarvis.model.MenuTree;
import com.gigaspaces.jarvis.ui.MainUI;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Program {
    public static void main(String args[]) throws Exception {
        // Get first arg (if exist), and remove it from args:        
        String command = args.length == 0 ? "" : args[0];
        args = args.length == 0 ? args : Arrays.copyOfRange(args, 1, args.length);
        // Start based on command
        switch (command) {
            case "generate-navbar":
                generateNavBar(getConfig(args));
                break;
            case "generate-noindex":
                generateNoindex(getConfig(args));
                break;
            case "generate-canonical-url":
                generateCanonicalUrl(getConfig(args));
                break;
            case "auto-generate-canonical-url":
                generateAutoCanonicalUrl(getConfig(args));
                break;
            case "canonical-url-report":
                generateCanonicalUrlReport(getConfig(args));
                break;
            default:
                MainUI.main(args);
                break;
        }
    }

    private static void generateNavBar(Config config) throws IOException {
        MenuTree.generateNavbar(config);
    }

    private static void generateNoindex(Config config) throws IOException {
        final long startTime = System.currentTimeMillis();

		Path outputPath = config.getOutputPath().toPath().resolve("xap");
        String element = "    <meta name=\"robots\" content=\"noindex\" />";
        FileUtils.processFiles(outputPath,
                p -> p.getFileName().toString().endsWith(".html"),
                line -> FileUtils.lineAppender(line, l -> l.equals("<head>"), element));

        long duration = System.currentTimeMillis() - startTime;
        Logger.getInstance().info(String.format("Finished generating noindex (duration=%sms)", duration));
    }

    private static void generateCanonicalUrl(Config config) {
        AtomicInteger counter = new AtomicInteger();
        final long startTime = System.currentTimeMillis();
        MenuTree.loadAllSections(config).forEach(section ->
                section.loadRootPages().forEach(page ->
                        page.generateCanonicalUrl(counter)));

        long duration = System.currentTimeMillis() - startTime;
        Logger.getInstance().info(String.format("Finished generating canonical url (duration=%sms, folders=%s, pages=%s, canonical urls=%s)",
                duration, config.getTotalFolders(), config.getTotalPages(), counter));
    }

    private static void generateAutoCanonicalUrl(Config config) {
        AtomicInteger counter = new AtomicInteger();
        final long startTime = System.currentTimeMillis();
        MenuTree.loadAllSections(config).forEach(section ->
                section.loadRootPages().forEach(page ->
                        page.generateAutoCanonicalUrl(counter)));

        long duration = System.currentTimeMillis() - startTime;
        Logger.getInstance().info(String.format("Finished generating auto canonical url (duration=%sms, folders=%s, pages=%s, canonical urls=%s)",
                duration, config.getTotalFolders(), config.getTotalPages(), counter));

    }

    private static void generateCanonicalUrlReport(Config config) throws IOException {
        CanonicalUrlReport report = new CanonicalUrlReport(config);
        for (ContentSection section : MenuTree.loadAllSections(config)) {
            report.processSection(section, section.toString());
        }

        report.generate(Paths.get("canonical-url-report.txt"));
    }

    private static Config getConfig(String[] args) {
        if (args.length < 1) {
            Logger.getInstance().warning("Incorrect number of arguments: " + args.length);
            System.exit(1);
        }
        Config config = new Config(args[0], args.length > 1 ? args[1] : null);
        Logger.getInstance().info("Starting with base path " + config.getPath());
        return config;
    }
}

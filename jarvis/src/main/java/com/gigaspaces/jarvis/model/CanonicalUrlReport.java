package com.gigaspaces.jarvis.model;

import com.gigaspaces.jarvis.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class CanonicalUrlReport {
    private final Config config;
    private final Collection<File> exists = new ArrayList<>();
    private final Collection<File> missing = new ArrayList<>();
    private final ArrayList<String> report = new ArrayList<>();
    private final long startTime;

    public CanonicalUrlReport(Config config) {
        this.config = config;
        this.startTime = System.currentTimeMillis();
    }

    private static String shortPath(File file) {
        String result = file.getName();
        while (!file.getParentFile().getName().equals("content")) {
            file = file.getParentFile();
            result = file.getName() + File.separator + result;
        }
        return result;
    }

    public void processSection(ContentSection section, String sectionName) {
        int existsBefore = exists.size();
        int missingBefore = missing.size();
        section.loadRootPages().forEach(this::generateCanonicalUrlReport);
        int existsDelta = exists.size() - existsBefore;
        int missingDelta = missing.size() - missingBefore;
        report.add(String.format("Section %s - canonical: %s, non-canonical: %s", sectionName, existsDelta, missingDelta));
    }

    private void generateCanonicalUrlReport(Page page) {
        page.getChildren().forEach(this::generateCanonicalUrlReport);
        (page.getCanonicalUrl() != null ? exists : missing).add(page.getFile());
        if (page.getCanonicalUrl() == null)
            System.out.println(shortPath(page.getFile()));
    }

    public void generate(Path target) throws IOException {
        long duration = System.currentTimeMillis() - startTime;
        String summary = String.format("Summary: duration: %sms, folders: %s, pages: %s, canonical: %s, non-canonical: %s)",
                duration, config.getTotalFolders(), config.getTotalPages(), exists.size(), missing.size());
        report.add(0, summary);
        missing.forEach(f -> report.add(String.format("%s", shortPath(f))));
        report.add("---------- END OF REPORT ----------");
        Files.write(target, report);
    }
}

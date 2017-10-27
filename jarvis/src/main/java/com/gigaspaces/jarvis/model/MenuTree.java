package com.gigaspaces.jarvis.model;

import com.gigaspaces.jarvis.Config;
import com.gigaspaces.jarvis.Logger;
import java.io.*;
import java.util.*;

public class MenuTree {

    private static final Logger logger = Logger.getInstance();

    private static final String[] SHARED_DIRS = new String[]{
        "product_overview", "faq", "api_documentation", "release_notes", "howto", "videos", "sbp"};

    private final long startTime = System.currentTimeMillis();
    private int totalFolders;
    private int totalPages;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            logger.warning("Incorrect number of arguments: " + args.length);
            System.exit(1);
        }
        Config config = new Config(args[0]);
        logger.info("Starting with base path " + config.getPath());
        generateNavbar(config);
    }
    
    public static void generateNavbar(Config config) throws IOException {
        MenuTree instance = new MenuTree();
        for (ContentSection section : loadSection(config)) {
            section.generateSidenav(instance, config);
        }
        
        for (VersionContainer section : loadVersions(config)) {
            section.generateSidenav(instance, config);
        }

        long duration = System.currentTimeMillis() - instance.startTime;
        logger.info("Finished generating navbar (duration=" + duration + "ms"
                + ", folders=" + instance.totalFolders
                + ", pages=" + instance.totalPages + ")");
    }

    public static Collection<ContentSection> loadSection(Config config) {
        Collection<ContentSection> result = new ArrayList<>();
        for (String dir : SHARED_DIRS) {
            result.add(new ContentSection(new File(config.getContentPath(), dir)));
        }
        return result;
    }
    
    public static TreeSet<VersionContainer> loadVersions(Config config) {
        TreeSet<VersionContainer> result = new TreeSet<>();
        File[] files = new File(config.getContentPath(), "xap").listFiles();
        if (files != null) {
            for (File versionDir : files) {
                if (versionDir.isDirectory()) {
                    result.add(new VersionContainer(versionDir));
                }
            }
        }
        return result;
    }

    public Collection<Page> loadPages(File folder, boolean groupingMode) throws IOException {
        if (!folder.exists())
            throw new RuntimeException("No such folder: " + folder);
        logger.debug("Processing dir : " + folder.getName());
        final Collection<Page> roots = new TreeSet<>();
        final Map<String, Page> pages = new HashMap<>();
        for (File file : folder.listFiles()) {
            // make sure we only process markdown files
            if (file.isFile() && file.getName().endsWith(".markdown")) {
                Page p = new Page(file, groupingMode);
                pages.put(p.getId(), p);
                if (p.getParent().isEmpty()) {
                    roots.add(p);
                }
            }
        }

        totalFolders++;
        totalPages += pages.size();

        buildTree(pages);
        return roots;
    }

    private static void buildTree(Map<String, Page> pages) {
        // now lets order them according to the weight
        for (Page p : pages.values()) {
            if (p.getWeight() == null) {
                if (!p.isIndex()) {
                    logger.warning(p.getSource() + "  has no weight");
                }
            } else {
                if (!p.getParent().isEmpty()) {
                    Page parent = pages.get(p.getParent());
                    if (parent != null) {
                        parent.addChild(p);
                    } else {
                        logger.warning(p.getSource() + " - invalid parent [" + p.getParent() + "]");
                    }
                }
            }
        }
    }
}

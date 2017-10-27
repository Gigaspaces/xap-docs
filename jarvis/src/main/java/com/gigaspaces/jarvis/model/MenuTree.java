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
        final File contentPath = config.getContentPath();
        MenuTree instance = new MenuTree();
        for (String dir : SHARED_DIRS) {
            File folder = new File(contentPath, dir);
            instance.generateSidenav(config, folder.getName(), instance.loadPages(folder, false));
        }
        
        for (VersionContainer vc : VersionContainer.find(contentPath)) {
            instance.generateSidenav(config, "xap" + vc.getVersion(), vc.load(instance));
        }

        long duration = System.currentTimeMillis() - instance.startTime;
        logger.info("Finished generating navbar (duration=" + duration + "ms"
                + ", folders=" + instance.totalFolders
                + ", pages=" + instance.totalPages + ")");
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

    private void generateSidenav(Config config, String suffix, Collection<Page> roots) throws IOException {
        String path = config.getPath() + "/site/themes/hugo-bootswatch/layouts/partials/sidenav-" + suffix + ".html";
        // write the html to the file system
        try ( 
            PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            roots.forEach((root) -> printPage(writer, root));
        }
    }

    private static void printPage(PrintWriter writer, Page page) {
        String link = "<a href='/" + page.getHref() + "'>" + page.getTitle() + "</a>";
        if (page.getChildren().isEmpty()) {
            writer.println("<li>" + link + "</li>");
        } else {
            writer.println("<li class='expandable'><div class='hitarea expandable-hitarea'></div>" + link);
            writer.println("<ul style='display: none'>");
            page.getChildren().forEach((child) -> printPage(writer, child));
            writer.println("</ul>");
            writer.println("</li>");
        }
    }
}

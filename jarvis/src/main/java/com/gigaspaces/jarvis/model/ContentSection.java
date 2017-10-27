package com.gigaspaces.jarvis.model;

import com.gigaspaces.jarvis.Config;
import com.gigaspaces.jarvis.Logger;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ContentSection {

    private static final Logger logger = Logger.getInstance();

    private final File path;

    public ContentSection(File path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path.getName();
    }

    public File getPath() {
        return path;
    }
    
    public Collection<Page> load(Config config) throws IOException {
        return loadPages(config, path, false);
    }

    protected Collection<Page> loadPages(Config config, File folder, boolean groupingMode) throws IOException {
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

        config.getTotalFolders().incrementAndGet();
        config.getTotalPages().addAndGet(pages.size());

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

    public void generateSidenav(Config config) throws IOException {
        generateSidenav(config, path.getName(), load(config));
    }
    
    protected void generateSidenav(Config config, String suffix, Collection<Page> roots) throws IOException {
        String outputPath = config.getPath() + "/site/themes/hugo-bootswatch/layouts/partials/sidenav-" + suffix + ".html";
        // write the html to the file system
        try ( 
            PrintWriter writer = new PrintWriter(outputPath, "UTF-8")) {
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

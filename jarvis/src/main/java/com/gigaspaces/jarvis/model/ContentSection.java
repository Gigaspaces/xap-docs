package com.gigaspaces.jarvis.model;

import com.gigaspaces.jarvis.Config;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class ContentSection {

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
    
    public Collection<Page> load(MenuTree menuTree) throws IOException {
        return menuTree.loadPages(path, false);
    }

    public void generateSidenav(MenuTree instance, Config config) throws IOException {
        generateSidenav(config, path.getName(), load(instance));
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

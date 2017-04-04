package xapdoc.parser;

import java.io.*;
import java.util.*;

public class MenuTree {

    private static final boolean DEBUG_ENABLED = false;
    private static final int NEW_STRUCTURE_VERSION = 121;
    private static String BASE_PATH;

    private static final String[] SHARED_DIRS = new String[] {
            "product_overview", "faq", "api_documentation", "release_notes", "howto", "videos", "sbp"};

    private final long startTime = System.currentTimeMillis();
    private int totalFolders;
    private int totalPages;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            warning("Incorrect number of arguments: " + args.length);
            System.exit(1);
        }
        // e.g. BASE_PATH = "/Users/xxxxx/Documents/xap-docs";
        BASE_PATH = args[0];
        info("Starting with base path " + BASE_PATH);

        final String contentPath = BASE_PATH + "/site/content/";
        MenuTree instance = new MenuTree();
        for (String dir : SHARED_DIRS)
            instance.processDir(new File(contentPath + dir));
        Map<Integer, Collection<File>> xapFolders = getProductFolders(contentPath);
        for (Map.Entry<Integer, Collection<File>> entry : xapFolders.entrySet()) {
            if (entry.getKey() < NEW_STRUCTURE_VERSION) {
                for (File folder : entry.getValue()) {
                    instance.processDir(folder);
                }
            } else {
                instance.processVersion(entry.getKey(), entry.getValue());
            }
        }

        long duration = System.currentTimeMillis() - instance.startTime;
        info("Finished generating navbar (duration=" + duration + "ms" +
                ", folders=" + instance.totalFolders +
                ", pages=" + instance.totalPages + ")");
    }

    private void processVersion(Integer version, Collection<File> folders) throws IOException {
        Map<String, Page> rootsMap = new HashMap<String, Page>();
        for (File folder : folders) {
            final Collection<Page> folderRoot = loadPages(folder, true);
            if (folderRoot.isEmpty())
                warning("No root for " + folder.getName());
            else if (folderRoot.size() != 1)
                warning("Ambiguous root for " + folder.getName());
            else
                rootsMap.put(folder.getName(), folderRoot.iterator().next());
        }
        // Relocate java tutorial from root under java dev guide:
        rootsMap.get("xap" + version).addChild(rootsMap.remove("xap" + version + "tut"));
        // Relocate .NET tutorial from root under .NET dev guide:
        rootsMap.get("xap" + version + "net").addChild(rootsMap.remove("xap" + version + "nettut"));

        // Sort and generate roots:
        generateSidenav("xap" + version, new TreeSet<Page>(rootsMap.values()));
    }

    private static Map<Integer, Collection<File>> getProductFolders(String path) {
        Map<Integer, Collection<File>> result = new HashMap<Integer, Collection<File>>();
        for (File file : new File(path).listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("xap")) {
                Integer version = extractVersion(file.getName());
                if (!result.containsKey(version))
                    result.put(version, new ArrayList<File>());
                result.get(version).add(file);
            }
        }

        return result;
    }

    private static Integer extractVersion(String name) {
        String result = "";
        for (int i="xap".length() ; i < name.length() && Character.isDigit(name.charAt(i)) ; i++)
            result += name.charAt(i);

        return Integer.parseInt(result);
    }

    private void processDir(File folder) throws IOException {
        generateSidenav(folder.getName(), loadPages(folder, false));
    }

    private void generateSidenav(String suffix, Collection<Page> roots) throws IOException {
        // write the html to the file system
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(BASE_PATH + "/site/themes/hugo-bootswatch/layouts/partials/sidenav-" + suffix + ".html", "UTF-8");
            for (Page root : roots) {
                printPage(writer, root);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private Collection<Page> loadPages(File folder, boolean groupingMode) throws IOException {
        debug("Processing dir : " + folder.getName());
        final Collection<Page> roots = new TreeSet<Page>();
        final Map<String, Page> pages = new HashMap<String,Page>();
        for (File file : folder.listFiles()) {
            // make sure we only process markdown files
            if (file.isFile() && file.getName().endsWith(".markdown")) {
                Page p = new Page(file, groupingMode);
                pages.put(p.getId(), p);
                if (p.getParent().isEmpty())
                    roots.add(p);
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
                if (!p.isIndex())
                    warning(p.getSource() + "  has no weight");
            } else {
                if (!p.getParent().isEmpty()) {
                    Page parent = pages.get(p.getParent());
                    if (parent != null)
                        parent.addChild(p);
                    else
                        warning(p.getSource() + " - invalid parent [" + p.getParent() + "]");
                }
            }
        }
    }

    private static void printPage(PrintWriter writer, Page page) {
        String link = "<a href='/" + page.getHref() + "'>" + page.getTitle() + "</a>";
        if (page.getChildren().size() == 0) {
            writer.println("<li>" + link + "</li>");
        } else {
            writer.println("<li class='expandable'><div class='hitarea expandable-hitarea'></div>" + link);
            writer.println("<ul style='display: none'>");
            for (Page child : page.getChildren())
                printPage(writer, child);
            writer.println("</ul>");
            writer.println("</li>");
        }
    }

    private static void info(String message)  {
        System.out.println(message);
    }

    private static void warning(String message)  {
        System.out.println("WARNING: " + message);
    }

    private static void debug(String message)  {
        if (DEBUG_ENABLED)
            System.out.println("DEBUG: " + message);
    }

}

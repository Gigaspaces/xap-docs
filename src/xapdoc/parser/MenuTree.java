package xapdoc.parser;

import java.io.*;
import java.util.*;

public class MenuTree {

    private static final boolean DEBUG_ENABLED = false;
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
        Map<String, Collection<File>> xapFolders = getProductFolders(contentPath);
        for (Collection<File> xapVersionFolders : xapFolders.values()) {
            for (File folder : xapVersionFolders) {
                instance.processDir(folder);
            }
        }

        long duration = System.currentTimeMillis() - instance.startTime;
        info("Finished generating navbar (duration=" + duration + "ms" +
                ", folders=" + instance.totalFolders +
                ", pages=" + instance.totalPages + ")");
    }

    private static Map<String, Collection<File>> getProductFolders(String path) {
        Map<String, Collection<File>> result = new HashMap<String, Collection<File>>();
        for (File file : new File(path).listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("xap")) {
                String version = extractVersion(file.getName());
                if (!result.containsKey(version))
                    result.put(version, new ArrayList<File>());
                result.get(version).add(file);
            }
        }

        return result;
    }

    private static String extractVersion(String name) {
        String result = "";
        for (int i="xap".length() ; i < name.length() && Character.isDigit(name.charAt(i)) ; i++)
            result += name.charAt(i);

        return result;
    }

    private void processDir(File folder) throws IOException {
        String path = folder.getName();
        debug("Processing dir : " + path);
        Map<String, Page> pagesMap = new HashMap<String,Page>();
        for (File file : folder.listFiles()) {
            // make sure we only process markdown files
            if (file.isFile() && file.getName().contains(".markdown")) {
                Page p = createPage(file);
                pagesMap.put(file.getName().replace(".markdown", ".html"), p);
            }
        }
        // now lets order them according to the weight
        TreeSet<Page> pagesTree = new TreeSet<Page>();
        for (Page p : pagesMap.values()) {
            if ( p.getWeight() != null){
                if (p.getParent() == null)
                    pagesTree.add(p);
                else {
                    Page parent = pagesMap.get(p.getParent());
                    if (parent != null)
                        parent.addChild(p);
                }
            }
            else{
                if (!p.getFileName().equals("index.markdown"))
                    warning("[" + p.getCategory() + "]/" + p.getFileName() + "  has no weight");
            }
        }

        // write the html to the file system
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(BASE_PATH + "/site/themes/hugo-bootswatch/layouts/partials/sidenav-" + path + ".html", "UTF-8");
            for (Page p : pagesTree)
                printPage(writer, p);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        totalFolders++;
        totalPages += pagesMap.size();
        debug("Processed: " + path);
    }

    private static void printPage(PrintWriter writer, Page page) {
        String fileName = page.getFileName().replace(".markdown", ".html");
        if (page.getChildren().size() != 0) {
            writer.println("<li class='expandable'><div class='hitarea expandable-hitarea'></div><a href='./" + fileName + "'>" + page.getTitle() + "</a>");
            writer.println("<ul style='display: none'>");
            for (Page child : page.getChildren())
                printPage(writer, child);
            writer.println("</ul>");
            writer.println("</li>");

        } else {
            writer.println("<li><a href='./" + fileName + "'>" + page.getTitle() + "</a></li>");
        }
    }

    private static Page createPage(File file) throws IOException {

        if (file.getName().contains("--"))
            throw new IOException("File names can't conatin more then one '-' :" + file.getName());

        Scanner scanner = new Scanner(file);
        Properties properties = new Properties();
        boolean foundHeader = false;
        boolean foundFooter = false;

        debug("*** Scanning " + file.getName());
        try {
            while (scanner.hasNextLine() && !foundFooter) {
                final String line = scanner.nextLine().trim();
                if (line.equals("---")) {
                    if (!foundHeader)
                        foundHeader = true;
                    else
                        foundFooter = true;
                } else if (foundHeader) {
                    int pos = line.indexOf(':');
                    if (pos != -1) {
                        String name = line.substring(0, pos).trim();
                        String value = line.substring(pos + 1).trim();
                        properties.setProperty(name, value);
                    }
                }
            }
        } finally {
            scanner.close();
        }

        Page p = new Page();
        p.setFileName(file.getName());
        p.setTitle(properties.getProperty("title"));
        String weight = properties.getProperty("weight");
        if (weight != null)
            p.setWeight(Long.parseLong(weight));
        String parent = properties.getProperty("parent");
        if (parent != null && !parent.equals("none"))
            p.setParent(parent);
        String category = properties.getProperty("categories");
        if (category != null )
            p.setCategory(category);

        return p;
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

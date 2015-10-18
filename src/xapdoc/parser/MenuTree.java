package xapdoc.parser;

/**
 * Created with IntelliJ IDEA.
 * User: chris Roffler
 * Date: 10/18/15
*/

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;



public class MenuTree {


    private static final String INPUT_PATH  = "/Users/chrisroffler/Documents/xap-docs/site/content/";

    private static final String OUTPUT_PATH = "/Users/chrisroffler/Documents/xap-docs/site/themes/hugo-bootswatch/layouts/partials/sidenav-";



    public static void main(String[] args) throws Exception {

        List<String> dirs = new ArrayList<String>();
        dirs.add("xap97");
        dirs.add("xap97net");
        dirs.add("xap97adm");

        dirs.add("xap100");
        dirs.add("xap100net");
        dirs.add("xap100adm");
        dirs.add("xap100sec");

        dirs.add("xap101");
        dirs.add("xap101net");
        dirs.add("xap101adm");
        dirs.add("xap101sec");

        dirs.add("xap102");
        dirs.add("xap102net");
        dirs.add("xap102adm");
        dirs.add("xap102sec");

        dirs.add("xap110");
        dirs.add("xap110net");
        dirs.add("xap110adm");
        dirs.add("xap110sec");

        dirs.add("product_overview");
        dirs.add("faq");
        dirs.add("api_documentation");
        dirs.add("release_notes");
        dirs.add("sbp");
        dirs.add("howto");
        dirs.add("videos");

        dirs.add("xap101tut");
        dirs.add("xap102tut");
        dirs.add("xap110tut");

        dirs.add("xap101nettut");
        dirs.add("xap102nettut");
        dirs.add("xap110nettut");

        // Read all pages
        for (String path : dirs) {
            Map<String, Page> originalList = new HashMap<String,Page>();

            String str = INPUT_PATH + path;

            System.out.println(str);

            File folder = new File(str);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {

                    String fileName = file.getName();
                    List<String> lines = readMarkupFile(file);

                    Page p = createPage(lines);
                    p.setFileName(fileName);

                    originalList.put(StringUtils.replace(fileName, ".markdown", ".html"), p);
                }
            }


            Collection<Page> values = originalList.values();

            // check pages for errors
            checkPages(values);

            // now lets order them according to the weight
            TreeSet<Page> pages = new TreeSet<Page>();
            for (Page p : values) {
                String parent = p.getParent();

                if (parent != null) {
                    Page page = originalList.get(parent);

                    if (page == null) {
                        // nothing to do
                    } else {
                        page.addChild(p);
                    }
                } else {
                    pages.add(p);
                }
            }

            // create the HTML output
            String line = createMenu(pages);

            // write the html to the file system
            PrintWriter writer = null;
            try {

                String fileName = OUTPUT_PATH + path + ".html";
                writer = new PrintWriter(fileName, "UTF-8");

                writer.println(line);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }

           System.out.println("Processed :" + path);
        }
    }

    private static String createMenu(TreeSet<Page> pages) {

        String line = "";

        for (Page p : pages) {
            line = createMenuItem(line, p);
        }

        return line;
    }

    public static String createMenuItem(String line, Page page) {
        String fileName = StringUtils.replace(page.getFileName(), ".markdown", ".html");

        TreeSet<Page> children = page.getChildren();

        if (children.size() > 0) {

            line += "<li class='expandable'><div class='hitarea expandable-hitarea'></div><a href='./" + fileName + "'>"
                    + page.getTitle() + "</a>\n";

            line += "<ul style='display: none'>\n";

            for (Page p1 : children) {
                line = createMenuItem(line, p1);
            }

            line += "</ul>\n";

            line += "</li>\n";
        } else {
            line += "<li><a href='./" + fileName + "'>" + page.getTitle() + "</a></li>\n";
        }
        return line;
    }

    static void checkPages(Collection<Page> pages) throws Exception {

        for (Page p : pages) {
            if (StringUtils.contains(p.getFileName(), "--")) {
                throw new Exception("File names can't conatin more then one '-' :"+p.getFileName());
            }
        }

    }

    static Page createPage(List<String> lines) {

        int counter = 0;

        Page p = new Page();

        for (String line : lines) {
            if (StringUtils.contains(line, "---")) {
                counter++;
            }

            if (StringUtils.contains(line, "title:")) {
                String str = StringUtils.substringAfter(line, "title:");
                str = StringUtils.trim(str);
                p.setTitle(str);
            }

            if (StringUtils.contains(line, "parent:")) {
                if (!StringUtils.contains(line, "none")) {
                    String str = StringUtils.substringAfter(line, "parent:");
                    str = StringUtils.trim(str);
                    p.setParent(str);
                }
            }

            if (StringUtils.contains(line, "weight:")) {

                String str = StringUtils.substringAfter(line, "weight:");
                str = StringUtils.trim(str);

                if (!(str == null) && !(str.isEmpty())) {
                    p.setWeight(new Long(str));
                }
            }

            if (counter > 1) {
                break;
            }
        }

        return p;

    }

    static List<String> readMarkupFile(final File file) throws IOException {

        Scanner scanner = new Scanner(file);

        List<String> lines = new ArrayList<String>();

        try {
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                lines.add(nextLine);
            }
            return lines;
        } finally {
            scanner.close();
        }
    }

}

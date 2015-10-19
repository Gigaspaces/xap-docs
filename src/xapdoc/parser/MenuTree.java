package xapdoc.parser;

/**
 * Created with IntelliJ IDEA.
 * User: chris Roffler
 * Date: 10/18/15
*/

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

	private static String BASE_PATH;
	
	private static final String[] dirs = new String[] {
		"product_overview", "faq", "api_documentation", "release_notes", "howto", "videos", "sbp",
		"xap97",  "xap97net",  "xap97adm", 
		"xap100", "xap100net", "xap100adm", "xap100sec", 
		"xap101", "xap101net", "xap101adm", "xap101sec", "xap101tut", "xap101nettut", 
		"xap102", "xap102net", "xap102adm", "xap102sec", "xap102tut", "xap102nettut", 
		"xap110", "xap110net", "xap110adm", "xap110sec", "xap110tut", "xap110nettut"
	};

    public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Incorrect number of arguments: " + args.length);			
			System.exit(1);
		}

		// e.g. BASE_PATH = "/Users/chrisroffler/Documents/xap-docs";
		BASE_PATH = args[0];
		System.out.println("Starting with base path" + BASE_PATH);			
		long startTime = System.currentTimeMillis();
        // Read all pages
        for (String path : dirs) {
            Map<String, Page> originalList = new HashMap<String,Page>();

            String str = BASE_PATH + "/site/content/" + path;

            //System.out.println(str);

            File folder = new File(str);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {

                    String fileName = file.getName();
                    List<String> lines = readMarkupFile(file);

                    Page p = createPage(lines);
                    p.setFileName(fileName);

                    originalList.put(fileName.replace(".markdown", ".html"), p);
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

                String fileName = BASE_PATH + "/site/themes/hugo-bootswatch/layouts/partials/sidenav-" + path + ".html";
                writer = new PrintWriter(fileName, "UTF-8");

                writer.println(line);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }

           System.out.println("Processed: " + path);
        }
		long duration = System.currentTimeMillis() - startTime;
        System.out.println("Finished generating navbar (duration=" + duration + "ms)");
    }

    private static String createMenu(TreeSet<Page> pages) {

        String line = "";

        for (Page p : pages) {
            line = createMenuItem(line, p);
        }

        return line;
    }

    public static String createMenuItem(String line, Page page) {
        String fileName = page.getFileName().replace(".markdown", ".html");

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
            if (p.getFileName().contains("--")) {
                throw new Exception("File names can't conatin more then one '-' :"+p.getFileName());
            }
        }

    }

    static Page createPage(List<String> lines) {

        Page p = new Page();
        int counter = 0;

        for (String line : lines) {
            if (line.contains("title:"))
                p.setTitle(substringAfter(line, "title:").trim());

            if (line.contains("parent:") && !line.contains("none"))
                p.setParent(substringAfter(line, "parent:").trim());

            if (line.contains("weight:")) {
                String str = substringAfter(line, "weight:").trim();
                if (!(str == null) && !(str.isEmpty())) {
                    p.setWeight(new Long(str));
                }
            }

			if (line.contains("---"))
                counter++;

            if (counter > 1)
                break;
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

	private static String substringAfter(String s, String separator) {
		return s.substring(s.indexOf(separator) + separator.length());
	}
}

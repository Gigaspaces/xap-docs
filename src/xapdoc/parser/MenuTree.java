package xapdoc.parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: chris Roffler
 * Date: 10/18/15
*/
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
		System.out.println("Starting with base path " + BASE_PATH);			
		long startTime = System.currentTimeMillis();
        // Read all pages
        for (String path : dirs) {
            Map<String, Page> pagesMap = new HashMap<String,Page>();
            File folder = new File(BASE_PATH + "/site/content/" + path);
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    Page p = createPage(file);
                    pagesMap.put(file.getName().replace(".markdown", ".html"), p);
                }
            }

            // now lets order them according to the weight
            TreeSet<Page> pagesTree = new TreeSet<Page>();
            for (Page p : pagesMap.values()) {
                if (p.getParent() == null)
					pagesTree.add(p);
				else {
                    Page parent = pagesMap.get(p.getParent());
                    if (parent != null)
                        parent.addChild(p);
                }
            }

            // create the HTML output
            String line = createMenu(pagesTree);

            // write the html to the file system
            PrintWriter writer = null;
            try {

                writer = new PrintWriter(BASE_PATH + "/site/themes/hugo-bootswatch/layouts/partials/sidenav-" + path + ".html", "UTF-8");
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

    private static Page createPage(File file) throws IOException {

		if (file.getName().contains("--"))
			throw new IOException("File names can't conatin more then one '-' :" + file.getName());            
	
		Scanner scanner = new Scanner(file);
		Properties properties = new Properties();
        boolean foundHeader = false;
        boolean foundFooter = false;
	
		//System.out.println("*** Scanning " + file.getName());	
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

        return p;
    }
}

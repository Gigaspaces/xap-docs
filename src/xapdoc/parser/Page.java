package xapdoc.parser;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeSet;

public class Page implements Comparable<Page> {

    private final boolean index;
    private final String id;
    private final String title;
    private final Long weight;
    private final String parent;
    private final TreeSet<Page> children = new TreeSet<Page>();

    public Page(File file, boolean groupingMode) throws IOException {
        String category = file.getParentFile().getName();
        this.index = file.getName().equals("index.markdown");
        this.id = category + "/" + (index ? "" : file.getName().replace(".markdown", ""));
        Properties properties = extractProperties(file);
        this.title = properties.getProperty("title");
        this.weight = properties.containsKey("weight") ? Long.valueOf(properties.getProperty("weight")) : null;
        String parent = properties.getProperty("parent", "none");
        if (parent.equals("none")) {
            this.parent = groupingMode && !index ? category + "/" : "";
        } else {
            this.parent = category + "/" + (parent.startsWith("index.") ? "" : parent.replace(".html", ""));
        }
    }

    private static Properties extractProperties(File file) throws IOException {
        if (file.getName().contains("--"))
            throw new IOException("File names can't contain more then one '-' :" + file.getName());

        Scanner scanner = new Scanner(file);
        Properties properties = new Properties();
        boolean foundHeader = false;
        boolean foundFooter = false;

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

        return properties;
    }

    public void addChild(Page p) {
        children.add(p);
    }

    public String getTitle() {
        return title;
    }

    public String getParent() {
        return parent;
    }

    public Long getWeight() {
        return weight;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return id + ".markdown";
    }

    public boolean isIndex() {
        return index;
    }

    public String getHref() {
        return index ? id : id + ".html";
    }

    public TreeSet<Page> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Page [title=" + title + ", weight=" + weight + "]";
    }

    @Override
    public int compareTo(Page o) {
        if (this == o || this.getWeight() == null || o.getWeight() == null)
            return 0;

        int c1 = this.getWeight().intValue();
        int c2 = o.getWeight().intValue();

        if (c1 > c2)
            return 1;
        if (c1 < c2)
            return -1;

        System.out.println(this.id + " => " + c1 + ", " + o.id + " => " + c2);
        return 0;
    }
}

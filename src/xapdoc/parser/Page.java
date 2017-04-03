package xapdoc.parser;

import java.util.Properties;
import java.util.TreeSet;

public class Page implements Comparable<Page> {

    private String fileName;
    private final String title;
    private final String category;
    private final Long weight;
    private final String parent;
    private final TreeSet<Page> children = new TreeSet<Page>();

    public Page(Properties properties) {
        this.title = properties.getProperty("title");
        this.category = properties.getProperty("categories");
        this.weight = properties.containsKey("weight") ? Long.valueOf(properties.getProperty("weight")) : null;
        String parent = properties.getProperty("parent");
        this.parent = !"none".equals(parent) ? parent : null;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TreeSet<Page> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Page [title=" + title + ", weight=" + weight + "]";
    }

    public String getCategory() {
        return category;
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

        System.out.println(this.getParent());
        System.out.println(this.fileName + "   " + c1);
        System.out.println(o.fileName + "   " + c2);
        System.out.println();
        return 0;
    }
}

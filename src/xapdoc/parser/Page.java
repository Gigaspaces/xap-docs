package xapdoc.parser;

/**
 * Created with IntelliJ IDEA.
 * User: chrisroffler
 * Date: 10/18/15
 * Time: 10:07 PM
 */


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Page implements Comparable<Page> {

    private String title;

    private String parent;

    private Long weight;

    private String fileName;

    private String category;

    private List<String> breadcrumbs = new ArrayList<String>();

    private List<String> urls = new ArrayList<String>();

    private Page parentPage = null;

    private String html;

    private TreeSet<Page> children = new TreeSet<Page>();

    public void addChild(Page p) {
        p.setParentPage(this);
        children.add(p);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public TreeSet<Page> getChildren() {
        return children;
    }

    public void setChildren(TreeSet<Page> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Page [title=" + title + ", weight=" + weight + "]";
    }

    public void addBreadcrumb(String bc) {
        breadcrumbs.add(bc);
    }

    public void addUrl(String bc) {
        urls.add(bc);
    }

    public List<String> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<String> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public Page getParentPage() {
        return parentPage;
    }

    public void setParentPage(Page parenPaget) {
        this.parentPage = parenPaget;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

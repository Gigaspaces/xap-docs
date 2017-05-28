package xapdoc.parser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Page implements Comparable<Page> {

    private static final Collection<String> NEW_VERSIONS = Arrays.asList("10.1", "10.2", "11.0");

    private final boolean index;
    private final String id;
    private final String title;
    private final Long weight;
    private final String href;
    private final String parent;
    private final TreeSet<Page> children = new TreeSet<Page>();

    public Page(File file, boolean groupingMode) throws IOException {
        String category = file.getParentFile().getName();
		String[] tokens = parseCategory(category);
        this.index = file.getName().equals("index.markdown");
		String filename = index ? "" : file.getName().replace(".markdown", "");
        this.id = category + "/" + filename;
		
        Properties properties = extractProperties(file);
        this.title = properties.getProperty("title");
        this.weight = properties.containsKey("weight") ? Long.valueOf(properties.getProperty("weight")) : null;
		
		if (NEW_VERSIONS.contains(tokens[1])) {
			String prefix = tokens[0] + "/" + tokens[1] + "/" + tokens[2] + "/";
			this.href = index ? prefix : prefix + file.getName().replace(".markdown", ".html");
		} else {
			this.href = index ? id : id + ".html";			
		}
        String parent = properties.getProperty("parent", "none");
        if (parent.equals("none")) {
            this.parent = groupingMode && !index ? category + "/" : "";
        } else {
            this.parent = category + "/" + (parent.startsWith("index.") ? "" : parent.replace(".html", ""));
        }
    }
	
    private static String[] parseCategory(String s) {
		if (!s.startsWith("xap"))
			return new String[] {"xap", "", s};
        final String product  = s.substring(0, 3); // "xap"
        s = s.substring(product.length());

        String version = "";
        for (;s.length() != 0 && Character.isDigit(s.charAt(0)) ; s = s.substring(1)) {
            version += s.charAt(0);
        }
        // "nnn" => "nn.n"
        version = version.substring(0, version.length() - 1) + "." + version.charAt(version.length()-1);

        String category = toCategory(s);
        return new String[] {product, version, category};
    }
		
    private static String toCategory(String s) {
        if (s.equals("adm"))
            return "admin";
        if (s.equals("sec"))
            return "security";
        if (s.equals(""))
            return "dev-java";
        if (s.equals("net"))
            return "dev-dotnet";
        if (s.equals("tut"))
            return "tut-java";
        if (s.equals("nettut"))
            return "tut-dotnet";

        return s;
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
		return href;
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

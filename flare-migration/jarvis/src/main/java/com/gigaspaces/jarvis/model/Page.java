package com.gigaspaces.jarvis.model;

import com.gigaspaces.jarvis.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Page implements Comparable<Page> {

    private static final String YAML_SEPARATOR = "---";
    private final File file;
    private final boolean index;
    private final String id;
    private final String title;
    private Long weight;
    private final String href;
    private final String parent;
    private final TreeSet<Page> children = new TreeSet<>();

    public Page(File file, boolean groupingMode) throws IOException {
        this.file = file;
        String category = initCategory(file);
        this.index = file.getName().equals("index.markdown");
        String filename = index ? "" : file.getName().replace(".markdown", "");
        this.id = category + "/" + filename;

        Properties properties = loadProperties();
        this.title = properties.getProperty("title");
        this.weight = properties.containsKey("weight") ? Long.valueOf(properties.getProperty("weight")) : null;

        String[] tokens = parseCategory(category);
        if (tokens != null) {
            String prefix = tokens[0] + "/" + tokens[1] + "/" + (tokens[2].isEmpty() ? file.getName().replace(".markdown", ".html") : tokens[2] + "/");
            //String prefix = tokens[0] + "/" + tokens[1] + "/" + (tokens[2].isEmpty() ? "" : tokens[2] + "/");
            this.href = index ? prefix : prefix + file.getName().replace(".markdown", ".html");
        } else {
            this.href = index ? id : id + ".html";
        }
        String parentProp = properties.getProperty("parent", "none");
        if (parentProp.equals("none")) {
            this.parent = groupingMode && !index ? category + "/" : "";
        } else {
            this.parent = category + "/" + (parentProp.startsWith("index.") ? "" : parentProp.replace(".html", ""));
        }
    }

    private static String initCategory(File file) {
        if (file.getParentFile().getParentFile().getName().equals("content")) {
            return file.getParentFile().getName();
        }
        String category = file.getParentFile().getName();
        String version = file.getParentFile().getParentFile().getName();
        String product = file.getParentFile().getParentFile().getParentFile().getName();
        if (product.equals("content")) {
            product = version;
            version = category;
            category = "";
        }
        return product + version.replace(".", "") + category;
    }

    private static String[] parseCategory(String s) {
        if (!s.startsWith("xap"))
            return null;
        
        final String product = s.substring(0, 3); // "xap"
        s = s.substring(product.length());

        String version = "";
        for (; s.length() != 0 && Character.isDigit(s.charAt(0)); s = s.substring(1)) {
            version += s.charAt(0);
        }
        // "nnn" => "nn.n"
        version = version.substring(0, version.length() - 1) + "." + version.charAt(version.length() - 1);

        return new String[]{product, version, s};
    }

    private Properties loadProperties() throws IOException {
        if (file.getName().contains("--")) {
            throw new IOException("File names can't contain more then one '-' :" + file.getName());
        }

        Properties properties = new Properties();
        AtomicInteger separators = new AtomicInteger(0);
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                if (line.equals(YAML_SEPARATOR)) {
                    if (separators.incrementAndGet() > 1)
                        break;
                } else if (separators.get()  == 1) {
                    int pos = line.indexOf(':');
                    if (pos != -1) {
                        String name = line.substring(0, pos).trim();
                        String value = line.substring(pos + 1).trim();
                        properties.setProperty(name, value);
                    }
                }
            }
        }

        return properties;
    }
    
    private void flushProperties(Map<String, String> proeprties) {
        try {
            AtomicInteger separators = new AtomicInteger(0);
            ArrayList<String> lines = new ArrayList<>();
            Files.lines(file.toPath()).forEach(l -> {
                String result = l;
                if (l.equals(YAML_SEPARATOR))
                    separators.incrementAndGet();
                else if (separators.get() == 1) {
                    for (Map.Entry entry : proeprties.entrySet()) {
                        if (l.startsWith(entry.getKey() + ":"))
                            result = entry.getKey() + ": " + entry.getValue();
                    } 
                }
                lines.add(result);
            });
        Files.write(file.toPath(), lines, StandardOpenOption.CREATE);
        } catch (IOException e) {
            Logger.getInstance().warning("Failed to flush changes to " + file);
        }
    }
    

    public void addChild(Page p) {
        children.add(p);
    }

    public File getFile() {
        return file;
    }

    public String getMarkdown() {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            return ex.toString();
        }
        return new String(bytes);
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

    public void setWeight(long weight) {
        this.weight = weight;
        Map<String, String> properties = new HashMap<>();
        properties.put("weight", String.valueOf(weight));
        flushProperties(properties);
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
        return getTitle();
    }

    @Override
    public int compareTo(Page o) {
        if (this == o || this.getWeight() == null || o.getWeight() == null) {
            return 0;
        }

        int c1 = this.getWeight().intValue();
        int c2 = o.getWeight().intValue();

        if (c1 > c2) {
            return 1;
        }
        if (c1 < c2) {
            return -1;
        }

        System.out.println("Ambigous comparison: " + this.id + " => " + c1 + ", " + o.id + " => " + c2);
        return 0;
    }
}

package com.gigaspaces.jarvis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Config {

    private final Logger logger = Logger.getInstance();
    private final File path;
    private final File sitePath;
    private final File contentPath;
	private final File outputPath;
    private final Map<String, String> properties = new TreeMap<>();
    private final Map<String, String> pagePluging = new TreeMap<>();
    private final AtomicInteger totalFolders = new AtomicInteger();
    private final AtomicInteger totalPages= new AtomicInteger();

    public Config(String path) {
        this(new File(path), null);
    }

    public Config(String path, String siteFolder) {
        this(new File(path), siteFolder);
    }

    public Config(File path) {
        this(path, null);
    }

    public Config(File path, String siteFolder) {
        this.path = path;
        this.sitePath = new File(path, siteFolder != null ? siteFolder : "site-hugo");
        this.contentPath = new File(sitePath, "content");
        this.outputPath = new File(path, "output");
        loadProperties();
        groupByPrefix(properties, "plugin.").forEach(this::addPlugin);
    }

    private void loadProperties() {
        File configFile = Paths.get(System.getProperty("user.home"),
                ".jarvis",
                "jarvis.properties").toFile();
        if (!configFile.exists()) {
            loadDefaultConfig();
            createDefaultConfigFile(configFile);
        } else {
            loadProperties(configFile.toPath());
        }
    }

    private void loadProperties(Path file) {
        logger.info("Loading configuration from " + file);            
        try (Stream<String> stream = Files.lines(file)) {
            stream.forEach((line) -> {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    int pos = line.indexOf('=');
                    if (pos != -1) {
                        properties.put(line.substring(0, pos), line.substring(pos + 1));
                    }
                }
            });
        } catch (IOException e) {
            logger.warning("Failed to load config file " + file + " - " + e);            
        }
    }

    private void loadDefaultConfig() {
        final boolean isWindows = File.separatorChar == '\\';
        if (isWindows) {
            addDefaultPluginIfExists("notepad", "Open With Notpad...", "notepad");
            addDefaultPluginIfExists("notepadPlusPlus", "Open With Notpad++...", "C:\\Program Files\\Notepad++\\notepad++.exe");
            addDefaultPluginIfExists("notepadPlusPlus", "Open With Notpad++...", "C:\\Program Files (x86)\\Notepad++\\notepad++.exe");
        } else {
            // TODO: default linux/mac tools
        }        
    }
    
    private void createDefaultConfigFile(File configFile) {
        logger.info("Config file not found - creating at " + configFile);
        // Create file's container folder if not exists
        File parent = configFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
            if (!parent.exists()) {
                logger.warning("Failed to create config file folder: " + parent);
                return;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(configFile.toPath())) {
            writer.write("# Font properties" + System.lineSeparator());
            writer.write("#font.name=..." + System.lineSeparator());
            writer.write("#font.style=..." + System.lineSeparator());
            writer.write("#font.size=..." + System.lineSeparator());
            writer.write("# Plugins properties" + System.lineSeparator());
            writer.write("#plugin.foo.title=..." + System.lineSeparator());
            writer.write("#plugin.foo.path=..." + System.lineSeparator());
            writer.write(System.lineSeparator());
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            logger.warning("Failed to create config file " + configFile + " - " + e);
        }
    }

    private void addPlugin(String name, Properties p) {
        String title = p.getProperty("title", "Title not configured for " + name);
        String path = p.getProperty("path", "Title not configured for " + name);
        pagePluging.put(title, path);
    }

    private void addDefaultPluginIfExists(String name, String title, String path) {
        File f = new File(path);
        if (!f.isAbsolute() || f.exists()) {
            properties.put("plugin." + name + ".title", title);
            properties.put("plugin." + name + ".path", path);
        }
    }

    public File getPath() {
        return path;
    }

    public File getSitePath() {
        return sitePath;
    }

    public File getContentPath() {
        return contentPath;
    }
    
    public File getOutputPath() {
        return outputPath;
    }
    
    public Map<String, String> getPagePluging() {
        return pagePluging;
    }

    public String getFontName(String defaultValue) {
        return getProperty("font.name", defaultValue);
    }

    public int getFontStyle(int defaultValue) {
        String s = properties.get("font.style");
        return s != null ? Integer.parseInt(s) : defaultValue;
    }

    public int getFontSize(int defaultValue) {
        String s = properties.get("font.size");
        return s != null ? Integer.parseInt(s) : defaultValue;
    }

    public AtomicInteger getTotalFolders() {
        return totalFolders;
    }

    public AtomicInteger getTotalPages() {
        return totalPages;
    }
    
    private String getProperty(String key, String defaultValue) {
        String s = properties.get(key);
        return s != null ? s : defaultValue;        
    }

    private static Map<String, Properties> groupByPrefix(Map<String, String> properties, String prefix) {
        Map<String, Properties> result = new HashMap<>();
        for (String key : properties.keySet()) {
            if (key.startsWith(prefix)) {
                String category = key.substring(0, key.indexOf('.', prefix.length()));

                if (!result.containsKey(category)) {
                    result.put(category, new Properties());
                }
                String newKey = key.substring(category.length() + 1);
                String value = properties.get(key);
                result.get(category).setProperty(newKey, value);
            }
        }
        return result;
    }
}

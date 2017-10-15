package com.gigaspaces.jarvis;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Config {
    private final File path;
    private final String contentPath;
    private final Map<String, String> pagePluging = new TreeMap<>();
    
    public Config() {
        this(System.getProperty("user.dir"));
    }
    public Config(String path) {
        this.path = new File(path);
        this.contentPath = path + File.separatorChar + 
                "site" + File.separatorChar + 
                "content" + File.separatorChar;
        // TODO: read from config file
        pagePluging.put("Open With Notpad...", "notepad");
        pagePluging.put("Open With Notpad++...", "C:\\Program Files\\Notepad++\\notepad++.exe");
    }
    
    public File getPath() {
        return path;
    }
    
    public String getContentPath() {
        return contentPath;
    }
    
    public Map<String, String>getPagePluging() {
        return pagePluging;
    }
}

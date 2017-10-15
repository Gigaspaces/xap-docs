package com.gigaspaces.jarvis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

public class Logger {
    
    private static final Logger instance = new Logger();
    private final Collection<Consumer<String>> listeners = new ArrayList<>();
    private final boolean DEBUG_ENABLED = false;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Logger getInstance() {
        return instance;
    }
    
    private Logger() {
    }
    
    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }
    
    public void info(String message) {
        log(message);
    }

    public void warning(String message) {
        log("WARNING: " + message);
    }

    public void debug(String message) {
        if (DEBUG_ENABLED) {
            log("DEBUG: " + message);
        }
    }
    
    private void log(String s) {
        String now = dateFormat.format(new Date());
        String message = now + " - " + s;
        System.out.println(message);
        listeners.forEach(listener -> listener.accept(message));
    }
}

package com.gigaspaces.jarvis;

import com.gigaspaces.jarvis.model.MenuTree;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import javax.swing.SwingWorker;

public class HugoContainer implements Closeable{

    private final Logger logger = Logger.getInstance();
    private final Config config;
    private final ProcessBuilder processBuilder;
    private Process currProcess;
    
    public HugoContainer(Config config) {
        this.config = config;
        this.processBuilder = new ProcessBuilder()
                .command("hugo", "server")
                .directory(new File(config.getPath(), "site"))
                .redirectErrorStream(true);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() { 
                close();
            }
        });
    }
    
    public boolean isAlive() {
        return currProcess != null;
    }
    
    public void start() { 
        synchronized (processBuilder) {
            if (currProcess != null) 
                logger.warning("Hugo already started");
            else {
                logger.info("Generating navbar...");
                try {
                    MenuTree.main(new String[] {config.getPath().getPath()});
                } catch (Exception e) {
                    logger.warning("Failed to generate navbar: " + e.toString());
                }
                logger.info("Starting hugo...");
                try {
                    this.currProcess = processBuilder.start();
                    new StreamGobbler(this.currProcess.getInputStream()).execute();
                } catch (IOException e) {
                    logger.warning("Failed to start process: " + e.toString());
                }
            }
        }
    }
    
    public void stop() {
        synchronized (processBuilder) {
            if (currProcess == null)
                logger.warning("Hugo is not running");
            else {
                logger.info("Stopping Hugo...");
                currProcess.destroy();
                currProcess = null;
            }
        }
    }
    
    @Override
    public void close() {
        stop();
    }
    
    private static class StreamGobbler extends SwingWorker<Void, String> {

        private final InputStream stream;

        public StreamGobbler(InputStream stream) {
            this.stream = stream;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                reader.lines().forEach(this::publish);
                publish("Hugo stopped");
            } catch (IOException e) {
                publish(e.toString());
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            chunks.forEach(Logger.getInstance()::info);
        }        
    }
}

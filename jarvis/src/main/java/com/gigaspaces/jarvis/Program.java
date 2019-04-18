package com.gigaspaces.jarvis;

import com.gigaspaces.jarvis.model.MenuTree;
import com.gigaspaces.jarvis.ui.MainUI;

import java.io.IOException;
import java.util.Arrays;

public class Program {
    public static void main(String args[]) throws Exception {
        // Get first arg (if exist), and remove it from args:        
        String command = args.length == 0 ? "" : args[0];
        args = args.length == 0 ? args : Arrays.copyOfRange(args, 1, args.length);
        // Start based on command
        switch (command) {
            case "generate-navbar":
                generateNavBar(args);
                break;
            case "generate-canonical-url":
                generateCanonicalUrl(args);
                break;
            default:
                MainUI.main(args);
                break;
        }
    }

    private static void generateNavBar(String[] args) throws IOException {
        if (args.length < 1) {
            Logger.getInstance().warning("Incorrect number of arguments: " + args.length);
            System.exit(1);
        }
        Config config = new Config(args[0], args.length > 1 ? args[1] : null);
        Logger.getInstance().info("Starting with base path " + config.getPath());
        MenuTree.generateNavbar(config);
    }

    private static void generateCanonicalUrl(String[] args) throws IOException {
        if (args.length < 1) {
            Logger.getInstance().warning("Incorrect number of arguments: " + args.length);
            System.exit(1);
        }
        Config config = new Config(args[0], args.length > 1 ? args[1] : null);
        Logger.getInstance().info("Starting with base path " + config.getPath());
        MenuTree.generateCanonicalUrl(config);
    }
}

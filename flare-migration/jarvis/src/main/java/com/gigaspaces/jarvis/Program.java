package com.gigaspaces.jarvis;

import com.gigaspaces.jarvis.model.MenuTree;
import java.util.Arrays;

public class Program {
    public static void main(String args[]) throws Exception {
		if (args.length < 1) {
			Logger.getInstance().warning("Incorrect number of arguments: " + args.length);
			System.exit(1);
		}
		if (args[0].equals("analyze-shortcodes")) {
			ShortcodeAnalyzer.scanAndReport(args[1], args[2]);
		} else {
			Config config = new Config(args[0], args.length > 1 ? args[1] : null);
			Logger.getInstance().info("Starting with base path " + config.getPath());
			MenuTree.generateNavbar(config);
		}
    }    
}

package com.gigaspaces.jarvis;

import com.gigaspaces.jarvis.model.MenuTree;
import com.gigaspaces.jarvis.ui.MainUI;
import java.util.Arrays;

public class Program {
    public static void main(String args[]) throws Exception {
        // Get first arg (if exist), and remove it from args:        
        String command = args.length == 0 ? "" : args[0];
        args = args.length == 0 ? args : Arrays.copyOfRange(args, 1, args.length);
        // Start based on command
        switch (command) {
            case "generate-navbar":
                MenuTree.main(args);
                break;
            default:
                MainUI.main(args);
                break;
        }
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gigaspaces.jarvis.ui;

import com.gigaspaces.jarvis.Config;
import com.gigaspaces.jarvis.HugoContainer;
import com.gigaspaces.jarvis.Logger;
import com.gigaspaces.jarvis.VersionForker;
import com.gigaspaces.jarvis.model.ContentSection;
import com.gigaspaces.jarvis.model.MenuTree;
import com.gigaspaces.jarvis.model.Page;
import java.io.IOException;
import java.net.URI;
import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author niv
 */
public class MainUI extends javax.swing.JFrame {

    private final Logger logger = Logger.getInstance();
    private final Config config;
    private final HugoContainer hugoContainer;
    private final StringBuilder tempLog = new StringBuilder();

    /**
     * Creates new form MainUI
     */
    public MainUI() {
        // Initialize logger
        logger.addListener(this::log);
        File jarvisHome = findJarvisHome();
        logger.info("Starting Jarvis at " + jarvisHome);
        this.config = new Config(jarvisHome);
        this.hugoContainer = new HugoContainer(config);
        changeDefaultFont(config);
        initComponents();
        pagesTree.setTransferHandler(new PageTransferHandler());
        outputTextArea.setText(tempLog.toString());
    }
    
    private static File findJarvisHome() {
        File path = new File(System.getProperty("user.dir"));
        while (!"xap-docs".equals(path.getName()) && path.getParent() != null) {
            path = path.getParentFile();
        }
        return path;
    }
    
    private void log(String s) {
        if (outputTextArea != null) {
            outputTextArea.append(s + System.lineSeparator());
        } else {
            tempLog.append(s).append(System.lineSeparator());
        }
    }
    
    private static void changeDefaultFont(Config config) {
        Font font = (Font) UIManager.getLookAndFeelDefaults().get("defaultFont");
        String name = config.getFontName(font.getName());
        int style = config.getFontStyle(font.getStyle());
        int size = config.getFontSize(font.getSize());
        UIManager.getLookAndFeelDefaults().put("defaultFont", new Font(name, style, size));
    }

    private void initialize() {
        // Register pluging...
        config.getPagePluging().forEach(this::registerPlugin);
        // Add versions sections in descending order:
        MenuTree.loadVersions(config).descendingSet().forEach(s -> versionComboBox.addItem(s));
        // Append other sections:
        MenuTree.loadSection(config).forEach(s -> versionComboBox.addItem(s));
    }

    private Page getContextPage() {
        return getPageIfExists(pagesTree.getSelectionPath());
    }

    private Page getPageIfExists(TreePath treePath) {
        if (treePath == null) {
            return null;
        }
        return getPageIfExists((DefaultMutableTreeNode) treePath.getLastPathComponent());
    }

    private Page getPageIfExists(DefaultMutableTreeNode node) {
        return node != null && node.getUserObject() instanceof Page ? (Page) node.getUserObject() : null;
    }

    private void appendPage(DefaultTreeModel tree, Page page, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(page);
        //parent.add(pageNode);
        tree.insertNodeInto(pageNode, parent, parent.getChildCount());
        page.getChildren().forEach((child) -> appendPage(tree, child, pageNode));
    }

    private void registerPlugin(String name, String command) {
        javax.swing.JMenuItem menuItem = new javax.swing.JMenuItem();
        menuItem.setText(name);
        menuItem.addActionListener((java.awt.event.ActionEvent e) -> openWith(getContextPage(), command));
        pagePopupMenu.add(menuItem);

    }

    private void openWith(Page page, String path) {
        if (page != null) {
            try {
                new ProcessBuilder(path, page.getFile().toString()).start();
            } catch (IOException ex) {
                logger.warning(ex.toString());
            }
        }
    }

    private void browse(Page page, String prefix) {
        if (page != null) {
            String url = prefix + page.getHref();
            try {
                java.awt.Desktop.getDesktop().browse(URI.create(url));
            } catch (IOException ex) {
                logger.warning(ex.toString());
            }
        }
    }

    private class PageTransferHandler extends TransferHandler {

        private TreePath selectedPath;
        
        @Override
        public int getSourceActions(JComponent c) {
            //logger.info("getSourceActions");
            return TransferHandler.COPY_OR_MOVE;
        }

        /**
         *  This method bundles up the data to be exported into a Transferable object in preparation for the transfer. c
         */
        @Override
        protected Transferable createTransferable(JComponent c) {
            this.selectedPath = ((JTree)c).getSelectionPath();
            String result = selectedPath.toString();
            //logger.info("createTransferable: result=" + result);
            return new StringSelection(result);
        }


        /**
         * This method is called repeatedly during a drag gesture and returns true if the area below the cursor 
         * can accept the transfer, or false if the transfer will be rejected. 
         */
        @Override
        public boolean canImport(TransferSupport support) {
            //return super.canImport(support); //To change body of generated methods, choose Tools | Templates.
            return true;
        }
        
        /**
         * This method is called on a successful drop (or paste) and initiates the transfer of data to the 
         * target component. This method returns true if the import was successful and false otherwise.
         * @param support
         * @return 
         */
        @Override
        public boolean importData(TransferSupport support) {
            if (!support.isDrop())
                return false;
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            DefaultMutableTreeNode srcNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            DefaultMutableTreeNode dstNode = (DefaultMutableTreeNode) dl.getPath().getLastPathComponent();
            String errorMessage = processDragDrop(srcNode, dstNode, dl.getChildIndex());
            if (errorMessage != null)
                logger.warning("Cannot move " + selectedPath + " to " + dl.getPath() + ": " + errorMessage);
            return errorMessage == null;
        }
        
        private String processDragDrop(DefaultMutableTreeNode srcNode, DefaultMutableTreeNode dstNode, int dstIndex) {
            //logger.info("Node: " +srcNode + " => " + dstNode);
            if (srcNode.getChildCount() != 0) 
                return "Moving pages with children is not supported (yet)";
            if (dstIndex == -1)
                return "Changing hierarchy is not supported (yet)";
            if (srcNode.getParent() != dstNode) 
                return "Moving a page to a different parent is not supported (yet)";
            
            Page srcPage = getPageIfExists(srcNode);
            Page pageBefore = getPageIfExists(dstIndex == 0 ? null : (DefaultMutableTreeNode)dstNode.getChildAt(dstIndex-1));
            Page pageAfter = getPageIfExists((DefaultMutableTreeNode)dstNode.getChildAt(dstIndex));
            // Skip if move is redundant
            if (srcPage == pageBefore || srcPage == pageAfter)
                return "Skipped - nothing to move";
            long weightBefore = pageBefore != null ? pageBefore.getWeight() : 0;
            long weightAfter = pageAfter.getWeight();
            long newWeight = (weightBefore + weightAfter) / 2;
            if (pageBefore != null)
                logger.info("Moving " + pageWithWeight(srcPage) + " between " + pageWithWeight(pageBefore) + " and " + pageWithWeight(pageAfter) + " - new weight " + newWeight);
            else 
                logger.info("Moving " + pageWithWeight(srcPage) + " before " + pageWithWeight(pageAfter) + " - new weight " + newWeight);
            srcPage.setWeight(newWeight);
            
            DefaultTreeModel model = (DefaultTreeModel) pagesTree.getModel();            
            int srcIndex = dstNode.getIndex(srcNode);
            model.removeNodeFromParent(srcNode);
            model.insertNodeInto(srcNode, dstNode, srcIndex < dstIndex ? dstIndex - 1 : dstIndex);           
            return null;
        }

        private String pageWithWeight(Page p) {
            return p.getTitle() + " [" + p.getWeight() + "]";
        }
        
        /**
         * This method is invoked after the export is complete. When the action is a MOVE, the data needs to 
         * be removed from the source after the transfer is complete — this method is where any necessary 
         * cleanup occurs.
         */
        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            // Since we're doing drag-n-drop on the same component, everything is handled in importData
        }        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pagePopupMenu = new javax.swing.JPopupMenu();
        pageBrowseLocalMenuItem = new javax.swing.JMenuItem();
        pageBrowseStagingMenuitem = new javax.swing.JMenuItem();
        pageBrowsePublicMenuItem = new javax.swing.JMenuItem();
        pageEditSeparator = new javax.swing.JPopupMenu.Separator();
        mainSplitPane = new javax.swing.JSplitPane();
        outputPanel = new javax.swing.JPanel();
        outputScrollPane = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        pagesSplitPane = new javax.swing.JSplitPane();
        pagePanel = new javax.swing.JPanel();
        pageScrollPane = new javax.swing.JScrollPane();
        pageTextArea = new javax.swing.JTextArea();
        pagesPanel = new javax.swing.JPanel();
        versionComboBox = new javax.swing.JComboBox<>();
        pagesScrollPane = new javax.swing.JScrollPane();
        pagesTree = new javax.swing.JTree();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        hugoMenu = new javax.swing.JMenu();
        hugoStartMenuItem = new javax.swing.JMenuItem();
        hugoStopMenuItem = new javax.swing.JMenuItem();
        hugoRestartMenuItem = new javax.swing.JMenuItem();
        adminMenu = new javax.swing.JMenu();
        newVersionMenuItem = new javax.swing.JMenuItem();

        pageBrowseLocalMenuItem.setText("Browse Local...");
        pageBrowseLocalMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageBrowseLocalMenuItemActionPerformed(evt);
            }
        });
        pagePopupMenu.add(pageBrowseLocalMenuItem);

        pageBrowseStagingMenuitem.setText("Browse Staging...");
        pageBrowseStagingMenuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageBrowseStagingMenuitemActionPerformed(evt);
            }
        });
        pagePopupMenu.add(pageBrowseStagingMenuitem);

        pageBrowsePublicMenuItem.setText("Browse Public...");
        pageBrowsePublicMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageBrowsePublicMenuItemActionPerformed(evt);
            }
        });
        pagePopupMenu.add(pageBrowsePublicMenuItem);
        pagePopupMenu.add(pageEditSeparator);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jarvis - GigaSpaces Documentation Assistant");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/com/gigaspaces/jarvis/jarvis.png")).getImage());

        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(1.0);
        mainSplitPane.setToolTipText("");

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        outputTextArea.setEditable(false);
        outputTextArea.setColumns(20);
        outputTextArea.setRows(5);
        outputScrollPane.setViewportView(outputTextArea);

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(outputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE)
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(outputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
        );

        mainSplitPane.setBottomComponent(outputPanel);

        pagesSplitPane.setDividerLocation(400);

        pagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Page"));

        pageTextArea.setEditable(false);
        pageTextArea.setColumns(20);
        pageTextArea.setRows(5);
        pageTextArea.setComponentPopupMenu(pagePopupMenu);
        pageScrollPane.setViewportView(pageTextArea);

        javax.swing.GroupLayout pagePanelLayout = new javax.swing.GroupLayout(pagePanel);
        pagePanel.setLayout(pagePanelLayout);
        pagePanelLayout.setHorizontalGroup(
            pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        pagePanelLayout.setVerticalGroup(
            pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pageScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        );

        pagesSplitPane.setRightComponent(pagePanel);

        versionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versionComboBoxActionPerformed(evt);
            }
        });

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        pagesTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        pagesTree.setDragEnabled(true);
        pagesTree.setDropMode(javax.swing.DropMode.ON_OR_INSERT);
        pagesTree.setRootVisible(false);
        pagesTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pagesTreeMouseClicked(evt);
            }
        });
        pagesTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                pagesTreeValueChanged(evt);
            }
        });
        pagesScrollPane.setViewportView(pagesTree);

        javax.swing.GroupLayout pagesPanelLayout = new javax.swing.GroupLayout(pagesPanel);
        pagesPanel.setLayout(pagesPanelLayout);
        pagesPanelLayout.setHorizontalGroup(
            pagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pagesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(versionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pagesPanelLayout.setVerticalGroup(
            pagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pagesPanelLayout.createSequentialGroup()
                .addComponent(versionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pagesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
        );

        pagesSplitPane.setLeftComponent(pagesPanel);

        mainSplitPane.setTopComponent(pagesSplitPane);

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        mainMenuBar.add(fileMenu);

        hugoMenu.setText("Hugo");

        hugoStartMenuItem.setText("Start");
        hugoStartMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hugoStartMenuItemActionPerformed(evt);
            }
        });
        hugoMenu.add(hugoStartMenuItem);

        hugoStopMenuItem.setText("Stop");
        hugoStopMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hugoStopMenuItemActionPerformed(evt);
            }
        });
        hugoMenu.add(hugoStopMenuItem);

        hugoRestartMenuItem.setText("Restart");
        hugoRestartMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hugoRestartMenuItemActionPerformed(evt);
            }
        });
        hugoMenu.add(hugoRestartMenuItem);

        mainMenuBar.add(hugoMenu);

        adminMenu.setText("Admin");

        newVersionMenuItem.setText("Fork new version...");
        newVersionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newVersionMenuItemActionPerformed(evt);
            }
        });
        adminMenu.add(newVersionMenuItem);

        mainMenuBar.add(adminMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pagesTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_pagesTreeValueChanged
        Page currPage = getPageIfExists(evt.getNewLeadSelectionPath());
        String text = currPage != null ? currPage.getMarkdown() : "";
        pageTextArea.setText(text);
        pageTextArea.setSelectionStart(0);
        pageTextArea.setSelectionEnd(0);
    }//GEN-LAST:event_pagesTreeValueChanged

    private void versionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_versionComboBoxActionPerformed

        DefaultTreeModel treeModel = (DefaultTreeModel) pagesTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        root.removeAllChildren();

        ContentSection section = (ContentSection)versionComboBox.getSelectedItem();
        section.loadRootPages().forEach((page) -> appendPage(treeModel, page, root));

        treeModel.reload();
        //pagesTree.updateUI();
    }//GEN-LAST:event_versionComboBoxActionPerformed

    private void pageBrowseLocalMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageBrowseLocalMenuItemActionPerformed
        browse(getContextPage(), "http://localhost:1313/");
    }//GEN-LAST:event_pageBrowseLocalMenuItemActionPerformed

    private void pageBrowseStagingMenuitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageBrowseStagingMenuitemActionPerformed
        browse(getContextPage(), "https://docs-staging.gigaspaces.com/");
    }//GEN-LAST:event_pageBrowseStagingMenuitemActionPerformed

    private void pageBrowsePublicMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageBrowsePublicMenuItemActionPerformed
        browse(getContextPage(), "https://docs.gigaspaces.com/");
    }//GEN-LAST:event_pageBrowsePublicMenuItemActionPerformed

    private void hugoStartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hugoStartMenuItemActionPerformed
        hugoContainer.start();
    }//GEN-LAST:event_hugoStartMenuItemActionPerformed

    private void hugoStopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hugoStopMenuItemActionPerformed
        hugoContainer.stop();
    }//GEN-LAST:event_hugoStopMenuItemActionPerformed

    private void hugoRestartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hugoRestartMenuItemActionPerformed
        if (hugoContainer.isAlive()) {
            hugoContainer.stop();
        }
        hugoContainer.start();
    }//GEN-LAST:event_hugoRestartMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void pagesTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pagesTreeMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            TreePath selPath = pagesTree.getPathForLocation(evt.getX(), evt.getY());
            if (selPath != null) {
                pagesTree.setSelectionPath(selPath);
                pagePopupMenu.show(pagesTree, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_pagesTreeMouseClicked

    private void newVersionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newVersionMenuItemActionPerformed
        String latestVersion = versionComboBox.getItemAt(0).toString();
        String newVersion = JOptionPane.showInputDialog(this, "New version:", "Forking new version from " + latestVersion, JOptionPane.QUESTION_MESSAGE);
        if (newVersion != null && newVersion.length() != 0) {
            ContentSection newSection = new VersionForker(config, latestVersion, newVersion).fork();
            if (newSection != null) {
                versionComboBox.insertItemAt(newSection, 0);
                versionComboBox.setSelectedIndex(0);
            }
        } else {
            logger.info("Cancelled");
        }
    }//GEN-LAST:event_newVersionMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainUI main = new MainUI();
                main.setLocationRelativeTo(null);
                main.setVisible(true);
                main.initialize();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu adminMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu hugoMenu;
    private javax.swing.JMenuItem hugoRestartMenuItem;
    private javax.swing.JMenuItem hugoStartMenuItem;
    private javax.swing.JMenuItem hugoStopMenuItem;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JMenuItem newVersionMenuItem;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JScrollPane outputScrollPane;
    private javax.swing.JTextArea outputTextArea;
    private javax.swing.JMenuItem pageBrowseLocalMenuItem;
    private javax.swing.JMenuItem pageBrowsePublicMenuItem;
    private javax.swing.JMenuItem pageBrowseStagingMenuitem;
    private javax.swing.JPopupMenu.Separator pageEditSeparator;
    private javax.swing.JPanel pagePanel;
    private javax.swing.JPopupMenu pagePopupMenu;
    private javax.swing.JScrollPane pageScrollPane;
    private javax.swing.JTextArea pageTextArea;
    private javax.swing.JPanel pagesPanel;
    private javax.swing.JScrollPane pagesScrollPane;
    private javax.swing.JSplitPane pagesSplitPane;
    private javax.swing.JTree pagesTree;
    private javax.swing.JComboBox<ContentSection> versionComboBox;
    // End of variables declaration//GEN-END:variables
}

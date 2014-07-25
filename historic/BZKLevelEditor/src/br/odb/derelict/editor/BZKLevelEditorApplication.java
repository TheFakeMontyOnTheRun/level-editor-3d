/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.derelict.editor;

import br.odb.libscene.Sector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author monty
 */
public class BZKLevelEditorApplication extends javax.swing.JFrame implements LevelEditorContextListener {

    ToolboxFrame toolbox;
    private EditorContext currentContext;

    /**
     * Creates new form BZKLevelEditorApplication
     */
    public BZKLevelEditorApplication() {
        
        initComponents();    

        toolbox = new ToolboxFrame();
        toolbox.setVisible(true);
        desktopPane.add(toolbox);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        importObjMenuItem = new javax.swing.JMenuItem();
        importCompiledMenuItem = new javax.swing.JMenuItem();
        exportObjMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        exportCompiledMenuItem = new javax.swing.JMenuItem();
        ReCenterLevelMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        toolsMenu.setMnemonic('f');
        toolsMenu.setText("Tools");

        importObjMenuItem.setText("Import OBJ as Sectors");
        importObjMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importObjMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(importObjMenuItem);

        importCompiledMenuItem.setText("Import compiled leaf sectors");
        importCompiledMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importCompiledMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(importCompiledMenuItem);

        exportObjMenuItem.setMnemonic('o');
        exportObjMenuItem.setText("Export OBJ with sectors as mesh");
        exportObjMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportObjMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(exportObjMenuItem);

        jMenuItem1.setText("Export current map as SVG");
        jMenuItem1.setActionCommand("exportMapAsSVG");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        toolsMenu.add(jMenuItem1);

        exportCompiledMenuItem.setMnemonic('s');
        exportCompiledMenuItem.setText("Compile and export sectors");
        exportCompiledMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCompiledMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(exportCompiledMenuItem);

        ReCenterLevelMenuItem.setText("Re-center level");
        ReCenterLevelMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReCenterLevelMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(ReCenterLevelMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(desktopPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(desktopPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed

        exit();
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        startNewFile();
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        openFileWithPrompt();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed

        save(currentContext);
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        this.saveWithNewName();
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void importObjMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importObjMenuItemActionPerformed

        String filePath;

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showDialog(this, "Import");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            System.out.println(filePath);
            this.startNewFile();
            currentContext.importFromObj(filePath);
        }
    }//GEN-LAST:event_importObjMenuItemActionPerformed

    private void exportObjMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportObjMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportObjMenuItemActionPerformed

    private void exportCompiledMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCompiledMenuItemActionPerformed

        ExportLevelDialog dialog = new ExportLevelDialog( this, true );
        dialog.setContext(currentContext);
        dialog.setVisible( true );
    }//GEN-LAST:event_exportCompiledMenuItemActionPerformed

    private void importCompiledMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importCompiledMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_importCompiledMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        
        StringBuilder outBuffer = new StringBuilder();
        int indexForIgnoringFirst = 0;
        
        outBuffer.append( "<svg>\n" );
        
        for ( Sector s : currentContext.getWorld() ) {
            if ( indexForIgnoringFirst++ != 0 ) {
                outBuffer.append( getSVGRectForSector( s ) );
            }            
        }
        outBuffer.append( "</svg>" );
        
        String filePath;

        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG );
        fileChooser.showDialog(this, "Export SVG");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            try {
                FileOutputStream fos = new FileOutputStream( filePath );
                fos.write( outBuffer.toString().getBytes() );
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BZKLevelEditorApplication.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BZKLevelEditorApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void ReCenterLevelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReCenterLevelMenuItemActionPerformed
        currentContext.reCenterLevel();
        this.repaint();
    }//GEN-LAST:event_ReCenterLevelMenuItemActionPerformed

    String getSVGRectForSector(Sector s) {

        return SVGSectorAdapter.toTopViewRect( s );        
    }
    
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
            java.util.logging.Logger.getLogger(BZKLevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BZKLevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BZKLevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BZKLevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BZKLevelEditorApplication().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ReCenterLevelMenuItem;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem exportCompiledMenuItem;
    private javax.swing.JMenuItem exportObjMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importCompiledMenuItem;
    private javax.swing.JMenuItem importObjMenuItem;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables

    
    MapViewInternalForm frm;
            
    public EditorContext startNewFile() {
        
        if ( frm != null ) {
            frm.dispose();
        }
        
        frm = new MapViewInternalForm(this);
        frm.setVisible(true);
        desktopPane.add(frm);
        toolbox.setCurrentContext(frm.getContext());
        currentContext = frm.getContext();
        return frm.getContext();
    }

    public void openFileWithPrompt() {

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showDialog(this, "Open");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            String filePath = file.getAbsolutePath();
            EditorContext c = startNewFile();
            c.loadMap(filePath);
        }
    }

    @Override
    public void contextModified(EditorContext context) {
        currentContext = context;
        toolbox.contextModified(context);
    }

    @Override
    public void setCurrentSector(int id) {
        toolbox.setCurrentSector(id);
    }

    private void saveWithNewName() {
        String filePath;

        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.showDialog(this, "Save");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            currentContext.saveMap(filePath);
        }

    }

    private void save(EditorContext ec) {

        if (ec.isCommitNeeded()) {

            if (ec.getCurrentFilename() == null) {

                saveWithNewName();
            } else {
                ec.commit();
            }
        }
    }

    @Override
    public void willTerminateContext(EditorContext context) {

        if (context.isCommitNeeded()) {
            switch (JOptionPane.showConfirmDialog(this, "You have unsaved changes. Want to save?", context.getCurrentFilename() == null ? "Untitled" : context.getCurrentFilename(), JOptionPane.YES_NO_OPTION)) {
                case JOptionPane.YES_OPTION:
                    save(context);
            }
        }
        
        startNewFile();
    }

    private void exit() {
        currentContext.terminate();
    }
}

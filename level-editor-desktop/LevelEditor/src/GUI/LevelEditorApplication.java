/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import app.Portalizer;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.portalizer.ConsoleGeometryCompiler;
import br.odb.utils.Utils;
import br.odb.utils.math.Vec2;
import br.odb.utils.math.Vec3;
import java.awt.Cursor;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.Timer;

/**
 *
 * @author monty
 */
public class LevelEditorApplication extends javax.swing.JFrame implements LevelEditorContextListener {

    SwingWorldRenderer[] views;
    EditorContext context;
    Timer timer;
    ShadowManager shadowManager;

    /**
     * Creates new form LevelEditorApplication
     */
    public LevelEditorApplication() {
        initComponents();

        views = new SwingWorldRenderer[3];

        views[ 0] = new WorldXZRenderer(XZViewPanel);
        views[ 2] = new WorldIso3DRenderer(XZViewPanel);
        views[ 1] = new WorldXYRenderer(XZViewPanel);

        context = new EditorContext(this);
        context.createNew();
        context.getCursor().set(128, 128, 128);
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
        editorDesktop = new javax.swing.JDesktopPane();
        frameWindow = new javax.swing.JInternalFrame();
        scrollPane = new javax.swing.JScrollPane();
        XZViewPanel = new javax.swing.JPanel();
        editFrame = new javax.swing.JInternalFrame();
        tabPages = new javax.swing.JTabbedPane();
        tabEntity = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbSectors = new javax.swing.JComboBox();
        btnDeleteEntity = new javax.swing.JButton();
        btnDuplicateEntity = new javax.swing.JButton();
        btnCreateEntity = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtExtraData = new javax.swing.JTextField();
        tabFace = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        sliderG = new javax.swing.JSlider();
        sliderR = new javax.swing.JSlider();
        sliderB = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        colorPanel = new javax.swing.JPanel();
        cmbFace = new javax.swing.JComboBox();
        cmbLinkedSector = new javax.swing.JComboBox();
        chkDoor = new javax.swing.JCheckBox();
        tabOptions = new javax.swing.JPanel();
        cmbAlternateBehaviour = new javax.swing.JComboBox();
        cmbSelectedSectorRenderingOptions = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbViewMode = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        spnZoom = new javax.swing.JSpinner();
        tabCursor = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        openCompiledMap = new javax.swing.JMenuItem();
        importFromObj = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exportCompiledMap = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setName("frame2"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                onWindowResized_FormGen(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown_FormGen(evt);
            }
        });

        editorDesktop.setForeground(new java.awt.Color(0, 0, 0));
        editorDesktop.setFont(new java.awt.Font("Lucida Grande", 0, 13)); // NOI18N
        editorDesktop.setPreferredSize(new java.awt.Dimension(135, 0));

        frameWindow.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        frameWindow.setForeground(new java.awt.Color(0, 0, 0));
        frameWindow.setIconifiable(true);
        frameWindow.setMaximizable(true);
        frameWindow.setResizable(true);
        frameWindow.setTitle("XZ View");
        frameWindow.setPreferredSize(new java.awt.Dimension(255, 255));
        frameWindow.setVisible(true);

        scrollPane.setPreferredSize(new java.awt.Dimension(1024, 1024));
        scrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                onScrollPanelComponentMoved_FormGen(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                scrollPaneComponentShown_FormGen(evt);
            }
        });

        XZViewPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        XZViewPanel.setPreferredSize(new java.awt.Dimension(1024, 1024));
        XZViewPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                onPanelComponentMoved_FormGen(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                onPanelComponentShown_FormGen(evt);
            }
        });
        XZViewPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onXZViewMouseClicked_FormGen(evt);
            }
        });
        scrollPane.setViewportView(XZViewPanel);

        org.jdesktop.layout.GroupLayout frameWindowLayout = new org.jdesktop.layout.GroupLayout(frameWindow.getContentPane());
        frameWindow.getContentPane().setLayout(frameWindowLayout);
        frameWindowLayout.setHorizontalGroup(
            frameWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, frameWindowLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE))
        );
        frameWindowLayout.setVerticalGroup(
            frameWindowLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        frameWindow.setBounds(240, 5, 990, 470);
        editorDesktop.add(frameWindow, javax.swing.JLayeredPane.DEFAULT_LAYER);

        editFrame.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        editFrame.setForeground(new java.awt.Color(0, 0, 0));
        editFrame.setIconifiable(true);
        editFrame.setMaximizable(true);
        editFrame.setResizable(true);
        editFrame.setTitle("Palette");
        editFrame.setPreferredSize(new java.awt.Dimension(355, 622));
        editFrame.setVisible(true);

        tabPages.setMinimumSize(new java.awt.Dimension(305, 285));
        tabPages.setPreferredSize(new java.awt.Dimension(323, 556));
        tabPages.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabPagesComponentShown_FormGen(evt);
            }
        });
        tabPages.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                tabPagesHierarchyChanged_FormGen(evt);
            }
        });

        tabEntity.setPreferredSize(new java.awt.Dimension(302, 492));
        tabEntity.setMinimumSize(new java.awt.Dimension(184, 162));

        jLabel5.setText("Sectors:");
        tabEntity.add(jLabel5);

        cmbSectors.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onCurrentSectorItemSelected_FormGen(evt);
            }
        });
        tabEntity.add(cmbSectors);

        btnDeleteEntity.setText("Delete entity");
        btnDeleteEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEntityActionPerformed_FormGen(evt);
            }
        });
        tabEntity.add(btnDeleteEntity);

        btnDuplicateEntity.setText("Duplicate Entity");
        btnDuplicateEntity.setToolTipText("");
        btnDuplicateEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDuplicateEntityActionPerformed_FormGen(evt);
            }
        });
        tabEntity.add(btnDuplicateEntity);

        btnCreateEntity.setText("Create entity");
        btnCreateEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCreateEntity_FormGen(evt);
            }
        });
        tabEntity.add(btnCreateEntity);

        jLabel8.setText("Extra data:");
        tabEntity.add(jLabel8);
        tabEntity.add(txtExtraData);

        tabPages.addTab("Entity", tabEntity);

        tabFace.setPreferredSize(new java.awt.Dimension(275, 503));
        tabFace.setMinimumSize(new java.awt.Dimension(275, 233));
        tabFace.setVisible(false);

        jLabel9.setText("R:");
        tabFace.add(jLabel9);

        jLabel10.setText("G:");
        tabFace.add(jLabel10);

        jLabel11.setText("B:");
        tabFace.add(jLabel11);

        sliderG.setMaximum(255);
        sliderG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderGStateChanged_FormGen(evt);
            }
        });
        tabFace.add(sliderG);

        sliderR.setMaximum(255);
        sliderR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderRStateChanged_FormGen(evt);
            }
        });
        tabFace.add(sliderR);

        sliderB.setMaximum(255);
        sliderB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderBStateChanged_FormGen(evt);
            }
        });
        tabFace.add(sliderB);

        jLabel7.setText("Color:");
        tabFace.add(jLabel7);

        colorPanel.setMaximumSize(new java.awt.Dimension(32765, 32765));
        colorPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(-1, -1, -1, -1, new java.awt.Color(0, 0, 0)));
        colorPanel.setBackground(new java.awt.Color(204, 51, 0));
        tabFace.add(colorPanel);

        cmbFace.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "(-Z) North", "(+X) East", "(+Z) South", "(-X) West", "(-Y) Floor", "(+Y) Ceiling" }));
        tabFace.add(cmbFace);

        cmbLinkedSector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onLinkedSectorItemSelected_FormGen(evt);
            }
        });
        tabFace.add(cmbLinkedSector);

        chkDoor.setText("Door");
        chkDoor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onChkDoorToggled_FormGen(evt);
            }
        });
        tabFace.add(chkDoor);

        tabPages.addTab("Face", tabFace);

        tabOptions.setMinimumSize(new java.awt.Dimension(142, 239));
        tabOptions.setPreferredSize(new java.awt.Dimension(286, 508));

        cmbAlternateBehaviour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Move", "Resize", " " }));
        tabOptions.add(cmbAlternateBehaviour);

        cmbSelectedSectorRenderingOptions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fill selected, hollow others", "Mark selected, All filled", " " }));
        tabOptions.add(cmbSelectedSectorRenderingOptions);

        jLabel4.setText("Rendering mode:");
        tabOptions.add(jLabel4);

        jLabel6.setText("ALT + click behaviour:");
        tabOptions.add(jLabel6);

        cmbViewMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XZView", "XYView", "Iso3DView" }));
        cmbViewMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onViewModeChanged_FormGen(evt);
            }
        });
        tabOptions.add(cmbViewMode);

        jLabel12.setText("Zoom:");
        tabOptions.add(jLabel12);

        spnZoom.setPreferredSize(new java.awt.Dimension(41, 28));
        spnZoom.setMinimumSize(new java.awt.Dimension(41, 28));
        spnZoom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnZoomStateChanged_FormGen(evt);
            }
        });
        tabOptions.add(spnZoom);

        tabPages.addTab("Options", tabOptions);

        tabCursor.setPreferredSize(new java.awt.Dimension(297, 510));
        tabCursor.setMinimumSize(new java.awt.Dimension(284, 68));
        tabCursor.setVisible(false);

        jLabel1.setText("X:");
        tabCursor.add(jLabel1);
        tabCursor.add(jSpinner1);

        jLabel2.setText("Y:");
        tabCursor.add(jLabel2);
        tabCursor.add(jSpinner2);

        jLabel3.setText("Z:");
        tabCursor.add(jLabel3);
        tabCursor.add(jSpinner3);

        tabPages.addTab("Cursor", tabCursor);

        editFrame.getContentPane().add(tabPages, java.awt.BorderLayout.PAGE_START);

        editFrame.setBounds(0, 0, 240, 470);
        editorDesktop.add(editFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        getContentPane().add(editorDesktop, java.awt.BorderLayout.CENTER);

        menuBar.setMaximumSize(new java.awt.Dimension(135, 32768));
        menuBar.setMinimumSize(new java.awt.Dimension(3, 2));
        menuBar.setPreferredSize(new java.awt.Dimension(135, 22));

        fileMenu.setText("File");
        fileMenu.setMnemonic('F');
        fileMenu.setPreferredSize(new java.awt.Dimension(42, 21));
        fileMenu.setMinimumSize(new java.awt.Dimension(1, 1));
        fileMenu.setMaximumSize(new java.awt.Dimension(42, 32767));

        newMenuItem.setText("New");
        newMenuItem.setMnemonic('O');
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setText("Open");
        openMenuItem.setMnemonic('O');
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(openMenuItem);

        openCompiledMap.setText("Open compiled map");
        openCompiledMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCompiledMapActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(openCompiledMap);

        importFromObj.setText("Import from WavefrontObj");
        importFromObj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFromObjActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(importFromObj);

        saveMenuItem.setText("Save");
        saveMenuItem.setMnemonic('S');
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setMnemonic('A');
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        exportCompiledMap.setText("Export compiled map");
        exportCompiledMap.setToolTipText("");
        exportCompiledMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCompiledMapActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(exportCompiledMap);

        exitMenuItem.setText("Exit");
        exitMenuItem.setMnemonic('X');
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed_FormGen(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");
        editMenu.setMnemonic('E');
        editMenu.setPreferredSize(new java.awt.Dimension(44, 21));
        editMenu.setMinimumSize(new java.awt.Dimension(1, 1));
        editMenu.setMaximumSize(new java.awt.Dimension(44, 32767));

        cutMenuItem.setText("Cut");
        cutMenuItem.setMnemonic('T');
        editMenu.add(cutMenuItem);

        copyMenuItem.setText("Copy");
        copyMenuItem.setMnemonic('Y');
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText("Paste");
        pasteMenuItem.setMnemonic('P');
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setText("Delete");
        deleteMenuItem.setMnemonic('D');
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText("Help");
        helpMenu.setMnemonic('H');
        helpMenu.setPreferredSize(new java.awt.Dimension(49, 21));
        helpMenu.setMinimumSize(new java.awt.Dimension(1, 1));
        helpMenu.setMaximumSize(new java.awt.Dimension(49, 32767));

        contentsMenuItem.setText("Contents");
        contentsMenuItem.setMnemonic('C');
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("About");
        aboutMenuItem.setMnemonic('A');
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed

        if (context.isCommitNeeded()) {
            //show message and ask for save
        }
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed

        String filePath;

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showDialog(this, "Open");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            System.out.println(filePath);
            context.loadMap(filePath);
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void scrollPaneComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_scrollPaneComponentShown
        repaint();
    }//GEN-LAST:event_scrollPaneComponentShown

    private void onScrollPanelComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_onScrollPanelComponentMoved
        repaint();
    }//GEN-LAST:event_onScrollPanelComponentMoved

    private void onPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_onPanelComponentShown
        repaint();

    }//GEN-LAST:event_onPanelComponentShown

    private void onPanelComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_onPanelComponentMoved
        repaint();
    }//GEN-LAST:event_onPanelComponentMoved

    private void onChkDoorToggled(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onChkDoorToggled
    }//GEN-LAST:event_onChkDoorToggled

    private void onLinkedSectorItemSelected(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_onLinkedSectorItemSelected

        context.setLinkForCurrentSector(cmbFace.getSelectedIndex(), cmbLinkedSector.getSelectedIndex());
        //updateWidgets();
    }//GEN-LAST:event_onLinkedSectorItemSelected

    private void onCurrentSectorItemSelected(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_onCurrentSectorItemSelected
        context.setCurrentSectorFromId(cmbSectors.getSelectedIndex());
        updateWidgets();
        repaint();
    }//GEN-LAST:event_onCurrentSectorItemSelected

    private void onXZViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onXZViewMouseClicked

        Vec3 pointInSpace = views[ this.cmbViewMode.getSelectedIndex()].getVec3for(new Vec2(evt.getX(), evt.getY()));
        context.getCursor().setProtectNaN(pointInSpace);

        if (evt.getButton() == 1) {
            context.trySelectingSectorFromCursor();

            cmbSectors.setSelectedIndex(context.getCurrentSector().getId());

        } else {

            int behaviour = this.cmbAlternateBehaviour.getSelectedIndex() + 2;

            if ((evt.getButton()) != behaviour) {
                context.moveCurrentSectorTo();
            } else {
                context.resizeCurrentSectorTo();
            }
        }

        repaint();
    }//GEN-LAST:event_onXZViewMouseClicked

    private void onViewModeChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_onViewModeChanged
        repaint();
    }//GEN-LAST:event_onViewModeChanged

    private void onViewResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_onViewResized
        repaint();
    }//GEN-LAST:event_onViewResized

    private void onCreateEntity(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCreateEntity
        Sector s = context.addNewSector();
        cmbSectors.setSelectedIndex(s.getId());
    }//GEN-LAST:event_onCreateEntity

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        context.createNew();
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void onWindowResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_onWindowResized
        this.editFrame.setLocation(0, 0);
        this.editFrame.setSize(editFrame.getWidth(), editorDesktop.getHeight());
        this.frameWindow.setLocation(editFrame.getWidth(), 0);
        this.frameWindow.setSize(editorDesktop.getWidth() - this.editFrame.getWidth(), editorDesktop.getHeight());
    }//GEN-LAST:event_onWindowResized

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        repaint();
    }//GEN-LAST:event_formComponentShown

    private void tabPagesComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabPagesComponentShown
        repaint();
    }//GEN-LAST:event_tabPagesComponentShown

    private void tabPagesHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_tabPagesHierarchyChanged
        repaint();
    }//GEN-LAST:event_tabPagesHierarchyChanged

    private void btnDeleteEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEntityActionPerformed
        if (context.getCurrentSector().getId() > 0) {
            context.deleteCurrentSector();
        }
    }//GEN-LAST:event_btnDeleteEntityActionPerformed

    private void btnDuplicateEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDuplicateEntityActionPerformed
        if (context.getCurrentSector().getId() > 0) {
            cmbSectors.setSelectedIndex(context.duplicateCurrentSector().getId());
        }
    }//GEN-LAST:event_btnDuplicateEntityActionPerformed

    private void sliderRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderRStateChanged
        context.setColorForCurrentSectorFace(this.cmbFace.getSelectedIndex(), this.sliderR.getValue(), this.sliderG.getValue(), this.sliderB.getValue());
        updateColorPanel();
        repaint();
    }//GEN-LAST:event_sliderRStateChanged

    private void sliderGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderGStateChanged
        context.setColorForCurrentSectorFace(this.cmbFace.getSelectedIndex(), this.sliderR.getValue(), this.sliderG.getValue(), this.sliderB.getValue());
        updateColorPanel();
        repaint();
    }//GEN-LAST:event_sliderGStateChanged

    private void sliderBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderBStateChanged
        context.setColorForCurrentSectorFace(this.cmbFace.getSelectedIndex(), this.sliderR.getValue(), this.sliderG.getValue(), this.sliderB.getValue());
        updateColorPanel();
        repaint();
    }//GEN-LAST:event_sliderBStateChanged

    private void importFromObjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFromObjActionPerformed

        String filePath;

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showDialog(this, "Open");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            System.out.println(filePath);
            context.importFromObj(filePath);
        }
    }//GEN-LAST:event_importFromObjActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        if (context.isCommitNeeded()) {

            if (context.getCurrentFilename() == null) {

                String filePath;

                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.showDialog(this, "Save");
                File file = fileChooser.getSelectedFile();

                if (file != null) {

                    filePath = file.getAbsolutePath();
                    context.saveMap(filePath);
                }

            } else {
                context.commit();
            }
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed

        String filePath;

        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.showDialog(this, "Save");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            context.saveMap(filePath);
        }

    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void exportCompiledMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCompiledMapActionPerformed


        String filePath;

        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.showDialog(this, "Export");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            World.snapLevel = 2;
            ConsoleGeometryCompiler compiler = new ConsoleGeometryCompiler();
            Portalizer portalizer = new Portalizer();
            portalizer.setFileServer(compiler);
            compiler.setClient(new Portalizer());
            compiler.setArgs(new String[]{});
            World preWorld = new World(context.getWorld());
            compiler.prepareFor(preWorld);
            compiler.run();
            try {
                filePath = file.getAbsolutePath();
                context.saveMap(filePath);
                preWorld.saveToDiskAsLevel(compiler.openAsOutputStream(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }



    }//GEN-LAST:event_exportCompiledMapActionPerformed

    private void openCompiledMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCompiledMapActionPerformed
        String filePath;

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showDialog(this, "Open");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            System.out.println(filePath);
            context.loadMapLeafSectors(filePath);
        }
    }//GEN-LAST:event_openCompiledMapActionPerformed

    private void spnZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnZoomStateChanged
        repaint();
    }//GEN-LAST:event_spnZoomStateChanged

    private void btnSetDecalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDecalActionPerformed

        String filePath;

        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showDialog(this, "Open");
        File file = fileChooser.getSelectedFile();

        if (file != null) {

            filePath = file.getAbsolutePath();
            this.decalPath.setText(Utils.extractFilenameFrom(filePath));
            context.getCurrentSector().setDecalAt(this.cmbFace.getSelectedIndex(), decalPath.getText());
            contextModified();
        }
    }//GEN-LAST:event_btnSetDecalActionPerformed

    private void btnClearDecalPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearDecalPathActionPerformed
        this.decalPath.setText("-");
        context.getCurrentSector().setDecalAt(this.cmbFace.getSelectedIndex(), null);
        contextModified();
    }//GEN-LAST:event_btnClearDecalPathActionPerformed

    private void cmbFaceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFaceItemStateChanged
        updateWidgets();
    }//GEN-LAST:event_cmbFaceItemStateChanged

    private void chkDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDoorActionPerformed
        context.toggleCurrentSectorDoorAtFace(cmbFace.getSelectedIndex());
    }//GEN-LAST:event_chkDoorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LevelEditorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new LevelEditorApplication().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel XZViewPanel;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton btnCreateEntity;
    private javax.swing.JButton btnDeleteEntity;
    private javax.swing.JButton btnDuplicateEntity;
    private javax.swing.JCheckBox chkDoor;
    private javax.swing.JComboBox cmbAlternateBehaviour;
    private javax.swing.JComboBox cmbFace;
    private javax.swing.JComboBox cmbLinkedSector;
    private javax.swing.JComboBox cmbSectors;
    private javax.swing.JComboBox cmbSelectedSectorRenderingOptions;
    private javax.swing.JComboBox cmbViewMode;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JInternalFrame editFrame;
    private javax.swing.JMenu editMenu;
    private javax.swing.JDesktopPane editorDesktop;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem exportCompiledMap;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JInternalFrame frameWindow;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importFromObj;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openCompiledMap;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JSlider sliderB;
    private javax.swing.JSlider sliderG;
    private javax.swing.JSlider sliderR;
    private javax.swing.JSpinner spnZoom;
    private javax.swing.JPanel tabCursor;
    private javax.swing.JPanel tabEntity;
    private javax.swing.JPanel tabFace;
    private javax.swing.JPanel tabOptions;
    private javax.swing.JTabbedPane tabPages;
    private javax.swing.JTextField txtExtraData;
    // End of variables declaration//GEN-END:variables

    @Override
    public void contextModified() {

        shadowManager = new ShadowManager(context.getWorld());

        populateComboWithSectors(cmbLinkedSector);
        populateComboWithSectors(cmbSectors);

        updateWidgets();
        this.repaint();
    }

    private void repaintLevelPanel() {

        repaintLevelPanel(XZViewPanel.getGraphics());
    }

    private void repaintLevelPanel(Graphics g) {

        if (context.getWorld() != null) {

            views[ this.cmbViewMode.getSelectedIndex()].zoom(((Integer) spnZoom.getValue()).intValue());
            views[ this.cmbViewMode.getSelectedIndex()].draw(context);
            //shadowManager.draw( context.getCursor(), views[ this.cmbViewMode.getSelectedIndex()] );
        }
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g); //To change body of generated methods, choose Tools | Templates.
        repaintLevelPanel();
    }

    public void populateComboWithSectors(JComboBox combo) {

        combo.removeAllItems();

        String[] sectorList = context.getSectorNamesList();

        for (String s : sectorList) {
            combo.addItem(s);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        repaintLevelPanel();
    }

    private void updateWidgets() {
        try {
            if (context.getCurrentSector() != null) {

                chkDoor.setSelected(context.getCurrentSector().getDoor(cmbFace.getSelectedIndex()) != null);
                cmbLinkedSector.setSelectedIndex(context.getCurrentSector().getLink(cmbFace.getSelectedIndex()));
                br.odb.libscene.Color c0 = context.getCurrentSector().getColor(this.cmbFace.getSelectedIndex());
                sliderR.setValue(c0.getR());
                sliderG.setValue(c0.getG());
                sliderB.setValue(c0.getB());

                if (  context.getCurrentSector().getDecalAt( cmbFace.getSelectedIndex() ) != null )
                    decalPath.setText( context.getCurrentSector().getDecalAt( cmbFace.getSelectedIndex() ) );
                else
                    decalPath.setText( "-" );


            }
        } catch (Sector.InvalidSlotException ex) {
            Logger.getLogger(LevelEditorApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        spnX.setValue((int) context.getCursor().x);
        spnY.setValue((int) context.getCursor().y);
        spnZ.setValue((int) context.getCursor().z);

    }

    private void updateColorPanel() {
        br.odb.libscene.Color c0 = context.getCurrentSector().getColor(this.cmbFace.getSelectedIndex());
        java.awt.Color c = new java.awt.Color(c0.getR(), c0.getG(), c0.getB());
        colorPanel.setBackground(c);
    }
}
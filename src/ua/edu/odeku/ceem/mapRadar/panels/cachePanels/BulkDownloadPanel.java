/*
 * Copyright (C) 2013 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package ua.edu.odeku.ceem.mapRadar.panels.cachePanels;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.cache.BasicDataFileStore;
import gov.nasa.worldwind.event.BulkRetrievalEvent;
import gov.nasa.worldwind.event.BulkRetrievalListener;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.retrieve.BulkRetrievable;
import gov.nasa.worldwind.retrieve.BulkRetrievalThread;
import gov.nasa.worldwind.terrain.CompoundElevationModel;
import gov.nasa.worldwindx.examples.util.SectorSelector;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Панель с помощью которой можно управлять скачиванием кеш данных
 * User: Aleo skype: aleo72
 * Date: 06.11.13
 * Time: 21:38
 */
public class BulkDownloadPanel extends JPanel {

    protected WorldWindow wwd;
    protected Sector currentSector;
    protected ArrayList<BulkRetrievablePanel> retrievables;

    protected JButton selectButton;
    protected JLabel sectorLabel;
    protected JButton startButton;
    protected JPanel monitorPanel;
    protected BasicDataFileStore cache;

    protected SectorSelector selector;

    public BulkDownloadPanel(WorldWindow wwd) {
        this.wwd = wwd;

        // Init retievable list
        this.retrievables = new ArrayList<BulkRetrievablePanel>();
        // Layers
        for (Layer layer : this.wwd.getModel().getLayers()) {
            if (layer instanceof BulkRetrievable)
                this.retrievables.add(new BulkRetrievablePanel((BulkRetrievable) layer, cache));
        }
        // Elevation models
        CompoundElevationModel cem = (CompoundElevationModel) wwd.getModel().getGlobe().getElevationModel();
        for (ElevationModel elevationModel : cem.getElevationModels()) {
            if (elevationModel instanceof BulkRetrievable)
                this.retrievables.add(new BulkRetrievablePanel((BulkRetrievable) elevationModel, cache));
        }

        // Init sector selector
        this.selector = new SectorSelector(wwd);
        this.selector.setInteriorColor(new Color(1f, 1f, 1f, 0.1f));
        this.selector.setBorderColor(new Color(1f, 0f, 0f, 0.5f));
        this.selector.setBorderWidth(3);
        this.selector.addPropertyChangeListener(SectorSelector.SECTOR_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateSector();
            }
        });

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        this.initComponents();
    }

    protected void updateSector() {
        this.currentSector = this.selector.getSector();
        if (this.currentSector != null) {
            // Update sector description
            this.sectorLabel.setText(makeSectorDescription(this.currentSector));
            this.selectButton.setText(ResourceString.get("string_Clear_sector"));
            this.startButton.setEnabled(true);
        } else {
            // null sector
            this.sectorLabel.setText("-");
            this.selectButton.setText(ResourceString.get("string_Select_sector"));
            this.startButton.setEnabled(false);
        }
        updateRetrievablePanels(this.currentSector);
    }

    protected void updateRetrievablePanels(Sector sector) {
        for (BulkRetrievablePanel panel : this.retrievables) {
            panel.updateDescription(sector, cache);
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void selectButtonActionPerformed(ActionEvent event) {
        if (this.selector.getSector() != null) {
            this.selector.disable();
        } else {
            this.selector.enable();
        }
        updateSector();
    }

    /**
     * Clear the current selection sector and remove it from the globe.
     */
    public void clearSector() {
        if (this.selector.getSector() != null) {
            this.selector.disable();
        }
        updateSector();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void startButtonActionPerformed(ActionEvent event) {
        for (BulkRetrievablePanel panel : this.retrievables) {
            if (panel.selectCheckBox.isSelected()) {
                BulkRetrievable retrievable = panel.retrievable;
                BulkRetrievalThread thread = retrievable.makeLocal(this.currentSector, 0, this.cache,
                        new BulkRetrievalListener() {
                            public void eventOccurred(BulkRetrievalEvent event) {
                                // This is how you'd include a retrieval listener. Uncomment below to monitor downloads.
                                // Be aware that the method is not invoked on the event dispatch thread, so any interaction
                                // with AWT or Swing must be within a SwingUtilities.invokeLater() runnable.

                                //System.out.printf("%s: item %s\n",
                                //    event.getEventType().equals(BulkRetrievalEvent.RETRIEVAL_SUCCEEDED) ? "Succeeded"
                                //: event.getEventType().equals(BulkRetrievalEvent.RETRIEVAL_FAILED) ? "Failed"
                                //    : "Unknown event type", event.getItem());
                            }
                        });

                if (thread != null)
                    this.monitorPanel.add(new DownloadMonitorPanel(thread));
            }
        }
        this.getTopLevelAncestor().validate();
    }

    /**
     * Determines whether there are any active downloads running.
     *
     * @return <code>true</code> if at leat one download thread is active.
     */
    public boolean hasActiveDownloads() {
        for (Component c : this.monitorPanel.getComponents()) {
            if (c instanceof DownloadMonitorPanel)
                if (((DownloadMonitorPanel) c).thread.isAlive())
                    return true;
        }
        return false;
    }

    /**
     * Cancel all active downloads.
     */
    public void cancelActiveDownloads() {
        for (Component c : this.monitorPanel.getComponents()) {
            if (c instanceof DownloadMonitorPanel) {
                if (((DownloadMonitorPanel) c).thread.isAlive()) {
                    DownloadMonitorPanel panel = (DownloadMonitorPanel) c;
                    panel.cancelButtonActionPerformed(null);
                    try {
                        // Wait for thread to die before moving on
                        long t0 = System.currentTimeMillis();
                        while (panel.thread.isAlive() && System.currentTimeMillis() - t0 < 500) {
                            Thread.sleep(10);
                        }
                    } catch (Exception ignore) {
                    }
                }
            }
        }
    }

    /**
     * Remove inactive downloads from the monitor panel.
     */
    public void clearInactiveDownloads() {
        for (int i = this.monitorPanel.getComponentCount() - 1; i >= 0; i--) {
            Component c = this.monitorPanel.getComponents()[i];
            if (c instanceof DownloadMonitorPanel) {
                DownloadMonitorPanel panel = (DownloadMonitorPanel) c;
                if (!panel.thread.isAlive() || panel.thread.isInterrupted()) {
                    this.monitorPanel.remove(i);
                }
            }
        }
        this.monitorPanel.validate();
    }

    protected void initComponents() {
        int border = 6;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(
                new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder(ResourceString.get("string_Download"))));
        this.setToolTipText("Layer imagery bulk download.");

        final JPanel locationPanel = new JPanel(new BorderLayout(5, 5));
        JLabel locationLabel = new JLabel(" " + ResourceString.get("string_Cache") + ":");
        final JLabel locationName = new JLabel("");
        JButton locationButton = new JButton("...");
        locationPanel.add(locationLabel, BorderLayout.WEST);
        locationPanel.add(locationName, BorderLayout.CENTER);
        locationPanel.add(locationButton, BorderLayout.EAST);
        this.add(locationPanel);

        locationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setMultiSelectionEnabled(false);
                int status = fc.showOpenDialog(locationPanel);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file != null) {
                        locationName.setText(file.getPath());
                        cache = new BasicDataFileStore(file);
                        updateRetrievablePanels(selector.getSector());
                    }
                }
            }
        });

        // Select sector button
        JPanel sectorPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        sectorPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        selectButton = new JButton(ResourceString.get("string_Select_sector"));
        selectButton.setToolTipText(ResourceString.get("string_message_for_button_Select_sector"));
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                selectButtonActionPerformed(event);
            }
        });
        sectorPanel.add(selectButton);
        sectorLabel = new JLabel("-");
        sectorLabel.setPreferredSize(new Dimension(350, 16));
        sectorLabel.setHorizontalAlignment(JLabel.CENTER);
        sectorPanel.add(sectorLabel);
        this.add(sectorPanel);

        // Retrievable list combo and start button
        JPanel retrievablesPanel = new JPanel();
        retrievablesPanel.setLayout(new BoxLayout(retrievablesPanel, BoxLayout.Y_AXIS));
        retrievablesPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        // RetrievablePanel list
        for (JPanel panel : this.retrievables) {
            retrievablesPanel.add(panel);
        }
        this.add(retrievablesPanel);

        // Start button
        JPanel startPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        startPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        startButton = new JButton(ResourceString.get("string_Start_download"));
        startButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                startButtonActionPerformed(event);
            }
        });
        startPanel.add(startButton);
        this.add(startPanel);

        // Download monitor panel
        monitorPanel = new JPanel();
        monitorPanel.setLayout(new BoxLayout(monitorPanel, BoxLayout.Y_AXIS));
        monitorPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        //this.add(monitorPanel);

        // Put the monitor panel in a scroll pane.
        JPanel dummyPanel = new JPanel(new BorderLayout());
        dummyPanel.add(monitorPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(dummyPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.setPreferredSize(new Dimension(350, 200));
        this.add(scrollPane);
    }

    public static String makeSectorDescription(Sector sector) {
        return String.format("S %7.4f\u00B0 W %7.4f\u00B0 N %7.4f\u00B0 E %7.4f\u00B0",
                sector.getMinLatitude().degrees,
                sector.getMinLongitude().degrees,
                sector.getMaxLatitude().degrees,
                sector.getMaxLongitude().degrees);
    }

    public static String makeSizeDescription(long size) {
        double sizeInMegaBytes = size / 1024 / 1024;
        if (sizeInMegaBytes < 1024)
            return String.format("%,.1f MB", sizeInMegaBytes);
        else if (sizeInMegaBytes < 1024 * 1024)
            return String.format("%,.1f GB", sizeInMegaBytes / 1024);
        return String.format("%,.1f TB", sizeInMegaBytes / 1024 / 1024);
    }

}

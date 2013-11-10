package ua.edu.odeku.ceem.mapRadar.tools.cache;

import gov.nasa.worldwind.cache.BasicDataFileStore;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.retrieve.BulkRetrievable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Aleo skype: aleo72
 * Date: 06.11.13
 * Time: 22:36
 */
public class BulkRetrievablePanel extends JPanel {

    protected BulkRetrievable retrievable;
    protected JCheckBox selectCheckBox;
    protected JLabel descriptionLabel;
    protected Thread updateThread;
    protected Sector sector;

    BulkRetrievablePanel(BulkRetrievable retrievable, BasicDataFileStore cache) {
        this.retrievable = retrievable;

        this.initComponents(cache);
    }

    protected void initComponents(final BasicDataFileStore cache) {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        // Check + name
        this.selectCheckBox = new JCheckBox(this.retrievable.getName());
        this.selectCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (((JCheckBox) e.getSource()).isSelected() && sector != null)
                    updateDescription(sector, cache);
            }
        });
        this.add(this.selectCheckBox, BorderLayout.WEST);
        // Description (size...)
        this.descriptionLabel = new JLabel();
        this.add(this.descriptionLabel, BorderLayout.EAST);
    }

    public void updateDescription(final Sector sector, final BasicDataFileStore cache) {
        if (this.updateThread != null && this.updateThread.isAlive())
            return;

        this.sector = sector;
        if (!this.selectCheckBox.isSelected()) {
            doUpdateDescription(null, cache);
            return;
        }

        this.updateThread = new Thread(new Runnable() {
            public void run() {
                doUpdateDescription(sector, cache);
            }
        });
        this.updateThread.setDaemon(true);
        this.updateThread.start();
    }

    protected void doUpdateDescription(final Sector sector, BasicDataFileStore cache) {
        if (sector != null) {
            try {
                long size = retrievable.getEstimatedMissingDataSize(sector, 0, cache);
                final String formattedSize = CacheDownload.makeSizeDescription(size);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        descriptionLabel.setText(formattedSize);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        descriptionLabel.setText("-");
                    }
                });
            }
        } else
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    descriptionLabel.setText("-");
                }
            });
    }

    public String toString() {
        return this.retrievable.getName();
    }
}

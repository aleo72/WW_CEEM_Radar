package ua.edu.odeku.ceem.mapRadar.panels.cachePanels;

import gov.nasa.worldwind.retrieve.BulkRetrievalThread;
import gov.nasa.worldwind.retrieve.Progress;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * User: Aleo skype: aleo72
 * Date: 06.11.13
 * Time: 22:46
 */
public class DownloadMonitorPanel extends JPanel {

    protected BulkRetrievalThread thread;
    protected Progress progress;
    protected Timer updateTimer;

    protected JLabel descriptionLabel;
    protected JProgressBar progressBar;
    protected JButton cancelButton;

    public DownloadMonitorPanel(BulkRetrievalThread thread) {
        this.thread = thread;
        this.progress = thread.getProgress();

        this.initComponents();

        this.updateTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                updateStatus();
            }
        });
        this.updateTimer.start();
    }

    protected void updateStatus() {
        // Update description
        String text = thread.getRetrievable().getName();
        text = text.length() > 30 ? text.substring(0, 27) + "..." : text;
        text += " (" + BulkDownloadPanel.makeSizeDescription(this.progress.getCurrentSize())
                + " / " + BulkDownloadPanel.makeSizeDescription(this.progress.getTotalSize())
                + ")";
        this.descriptionLabel.setText(text);
        // Update progress bar
        int percent = 0;
        if (this.progress.getTotalCount() > 0)
            percent = (int) ((float) this.progress.getCurrentCount() / this.progress.getTotalCount() * 100f);
        this.progressBar.setValue(Math.min(percent, 100));
        // Update tooltip
        String tooltip = BulkDownloadPanel.makeSectorDescription(this.thread.getSector());
        this.descriptionLabel.setToolTipText(tooltip);
        this.progressBar.setToolTipText(makeProgressDescription());

        // Check for end of thread
        if (!this.thread.isAlive()) {
            // Thread is done
            this.cancelButton.setText(ResourceString.get("Remove"));
            this.cancelButton.setBackground(Color.GREEN);
            this.updateTimer.stop();
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void cancelButtonActionPerformed(ActionEvent event) {
        if (this.thread.isAlive()) {
            // Cancel thread
            this.thread.interrupt();
            this.cancelButton.setBackground(Color.ORANGE);
            this.cancelButton.setText(ResourceString.get("Remove"));
            this.updateTimer.stop();
        } else {
            // Remove from monitor panel
            Container top = this.getTopLevelAncestor();
            this.getParent().remove(this);
            top.validate();
        }
    }

    protected void initComponents() {
        int border = 2;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // Description label
        JPanel descriptionPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        String text = thread.getRetrievable().getName();
        text = text.length() > 40 ? text.substring(0, 37) + "..." : text;
        descriptionLabel = new JLabel(text);
        descriptionPanel.add(descriptionLabel);
        this.add(descriptionPanel);

        // Progrees and cancel button
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.X_AXIS));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(100, 16));
        progressPanel.add(progressBar);
        progressPanel.add(Box.createHorizontalStrut(8));
        cancelButton = new JButton(ResourceString.get("Cancel"));
        cancelButton.setBackground(Color.RED);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                cancelButtonActionPerformed(event);
            }
        });
        progressPanel.add(cancelButton);
        this.add(progressPanel);
    }

    protected String makeProgressDescription() {
        String text = "";
        if (this.progress.getTotalCount() > 0) {
            int percent = (int) ((double) this.progress.getCurrentCount() / this.progress.getTotalCount() * 100d);
            text = percent + "% " + ResourceString.get("of") + " ";
            text += BulkDownloadPanel.makeSizeDescription(this.progress.getTotalSize());
        }
        return text;
    }
}
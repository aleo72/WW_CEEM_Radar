package ua.edu.odeku.ceem.mapRadar.tools.importGeoName;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ua.edu.odeku.ceem.mapRadar.tools.CeemRadarTool;
import ua.edu.odeku.ceem.mapRadar.tools.ToolFrame;
import ua.edu.odeku.ceem.mapRadar.tools.importGeoName.panels.FileChooserForm;
import ua.edu.odeku.ceem.mapRadar.utils.thread.Handler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * User: Aleo skype: aleo72
 * Date: 09.11.13
 * Time: 22:24
 */
public class ImportGeoName implements CeemRadarTool {

    private JPanel panel;
    private JButton importButton;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private FileChooserForm fileChooserPanel;
    private final GeoNameImporter importer;
    private Handler handlerClose;
    private JFrame parent;

    public ImportGeoName() {

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooserPanel.getFile() != null && !importer.isAlive()) {
                    importButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    importer.stop = false;

                    importer.setFileInput(fileChooserPanel.getFile());

                    importer.start();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importButton.setEnabled(true);
                cancelButton.setEnabled(false);
                importer.stop = true;
                importer.setFileInput(null);
                handlerClose.start();
            }
        });

        importer = new GeoNameImporter(progressBar, handlerClose);
    }

    {
        handlerClose = new Handler() {
            @Override
            public void start() {
                if (parent != null)
                    ImportGeoName.this.parent.dispose();
            }
        };
    }

    @Override
    public JPanel getPanel() {
        return (JPanel) this.$$$getRootComponent$$$();
    }

    @Override
    public String getNameTool() {
        return this.getClass().getName();
    }

    public void setParent(JFrame parent) {
        this.parent = parent;
    }

    @Override
    public Handler getHandlerForJFrame(ToolFrame frame) {
        return null;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new FormLayout("left:4dlu:noGrow,fill:429px:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,bottom:p:grow"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        CellConstraints cc = new CellConstraints();
        panel.add(panel1, cc.xy(2, 5));
        importButton = new JButton();
        this.$$$loadButtonText$$$(importButton, ResourceBundle.getBundle("strings").getString("button_geoName_import"));
        panel1.add(importButton);
        cancelButton = new JButton();
        this.$$$loadButtonText$$$(cancelButton, ResourceBundle.getBundle("strings").getString("button_cancel"));
        panel1.add(cancelButton);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel.add(panel2, cc.xy(2, 3));
        progressBar = new JProgressBar();
        panel2.add(progressBar, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel.add(panel3, cc.xy(1, 3));
        fileChooserPanel = new FileChooserForm();
        panel.add(fileChooserPanel.$$$getRootComponent$$$(), cc.xy(2, 1));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}

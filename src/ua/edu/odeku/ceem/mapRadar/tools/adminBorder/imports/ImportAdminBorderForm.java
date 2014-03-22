/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ua.edu.odeku.ceem.mapRadar.tools.PanelTool;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by Aleo on 22.03.14.
 */
public class ImportAdminBorderForm implements PanelTool {

    private JPanel panel1;
    public JButton cancelButton;
    public JButton importButton;
    public JProgressBar progressBar;
    public JTextPane messagesTextPane;
    public JTextField selectedFileTextField;
    public JButton chooserButton;

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
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:noGrow,fill:d:grow", "center:d:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(panel2, cc.xyw(1, 2, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:noGrow,fill:d:grow", "center:d:noGrow"));
        panel2.add(panel3, cc.xy(1, 1));
        messagesTextPane = new JTextPane();
        messagesTextPane.setBackground(UIManager.getColor("Panel.background"));
        messagesTextPane.setContentType("text/html");
        messagesTextPane.setEditable(false);
        messagesTextPane.setEnabled(true);
        messagesTextPane.setFont(UIManager.getFont("TextPane.font"));
        messagesTextPane.setForeground(UIManager.getColor("Button.foreground"));
        messagesTextPane.setInheritsPopupMenu(false);
        messagesTextPane.setText(ResourceBundle.getBundle("messages").getString("importAdminBorder_importFileMessage"));
        panel3.add(messagesTextPane, cc.xy(2, 1, CellConstraints.FILL, CellConstraints.FILL));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:m:noGrow,left:4dlu:noGrow,fill:max(d;250px):grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panel2.add(panel4, cc.xy(1, 3));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("label").getString("importAdminBorder_importFile"));
        panel4.add(label1, cc.xy(1, 3));
        selectedFileTextField = new JTextField();
        selectedFileTextField.setEditable(false);
        selectedFileTextField.setText("...");
        panel4.add(selectedFileTextField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        chooserButton = new JButton();
        this.$$$loadButtonText$$$(chooserButton, ResourceBundle.getBundle("button").getString("chooserFile"));
        panel4.add(chooserButton, cc.xy(5, 3));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel5, cc.xy(2, 6, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        importButton = new JButton();
        this.$$$loadButtonText$$$(importButton, ResourceBundle.getBundle("button").getString("import"));
        panel5.add(importButton);
        cancelButton = new JButton();
        cancelButton.setHorizontalAlignment(11);
        cancelButton.setHorizontalTextPosition(11);
        this.$$$loadButtonText$$$(cancelButton, ResourceBundle.getBundle("button").getString("cancel"));
        panel5.add(cancelButton);
        progressBar = new JProgressBar();
        panel1.add(progressBar, cc.xy(2, 4, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
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
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
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
        return panel1;
    }

    @Override
    public JPanel rootPanel() {
        return (JPanel) this.$$$getRootComponent$$$();
    }
}

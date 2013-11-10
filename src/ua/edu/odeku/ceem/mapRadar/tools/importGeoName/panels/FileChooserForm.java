package ua.edu.odeku.ceem.mapRadar.tools.importGeoName.panels;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * User: Aleo skype: aleo72
 * Date: 09.11.13
 * Time: 22:28
 */
public class FileChooserForm {
    private JPanel panel;
    private JLabel label;
    private JTextField fileNameTextField;
    private JButton chooserFileButton;
    private JButton helpButton;

    public FileChooserForm() {
        $$$setupUI$$$();
        chooserFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(FileChooserForm.this.getClass().getName());
            }
        });


        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel, ResourceString.get("gui_message_for_import_geoName"), ResourceString.get("gui_help_title"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
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
        panel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:noGrow,left:7dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow"));
        panel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("strings").getString("gui_title_border_import_geoName")));
        label = new JLabel();
        label.setHorizontalAlignment(2);
        label.setHorizontalTextPosition(2);
        this.$$$loadLabelText$$$(label, ResourceBundle.getBundle("strings").getString("string_label_for_import_geoNames"));
        CellConstraints cc = new CellConstraints();
        panel.add(label, cc.xy(3, 1));
        fileNameTextField = new JTextField();
        fileNameTextField.setColumns(10);
        fileNameTextField.setEditable(false);
        fileNameTextField.setText("...");
        panel.add(fileNameTextField, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel.add(panel1, cc.xy(7, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        chooserFileButton = new JButton();
        chooserFileButton.setHorizontalAlignment(4);
        this.$$$loadButtonText$$$(chooserFileButton, ResourceBundle.getBundle("strings").getString("gui_button_file_chooser"));
        panel1.add(chooserFileButton);
        helpButton = new JButton();
        this.$$$loadButtonText$$$(helpButton, ResourceBundle.getBundle("strings").getString("gui_helpButton"));
        panel1.add(helpButton);
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
        return panel;
    }
}
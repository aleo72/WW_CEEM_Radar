/*
 * Copyright (C) 2013 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName;
import ua.edu.odeku.ceem.mapRadar.frames.AppCeemRadarFrame;
import ua.edu.odeku.ceem.mapRadar.tools.CeemPanel;
import ua.edu.odeku.ceem.mapRadar.tools.CeemRadarTool;
import ua.edu.odeku.ceem.mapRadar.tools.ToolFrame;
import ua.edu.odeku.ceem.mapRadar.tools.viewGeoName.dialogs.EditGeoNameDialog;
import ua.edu.odeku.ceem.mapRadar.utils.thread.Handler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

/**
 * Панель для работы с geoNameTool
 * User: Aleo skype: aleo72
 * Date: 11.11.13
 * Time: 22:17
 */
public class ViewGeoNamePanel implements CeemPanel {

    public JPanel panel1;
    public JButton refreshButton;
    public JTextField textField;
    public JTable table;
    public JComboBox<String> countryComboBox;
    public JComboBox<String> featureClassComboBox;
    public JComboBox<String> featureCodeComboBox;
    public JButton editButton;

    private EditGeoNameDialog editGeoNameDialog;


    public ViewGeoNamePanel() {
        $$$setupUI$$$();
        table.setAutoCreateRowSorter(false);
    }

    private void createUIComponents() {
        countryComboBox = new JComboBox<String>();
        featureClassComboBox = new JComboBox<String>();
        featureCodeComboBox = new JComboBox<String>();
    }

    @Override
    public JPanel getRootPanel() {
        return (JPanel) $$$getRootComponent$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:noGrow", "center:d:noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(panel2, cc.xy(3, 3));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow"));
        panel2.add(panel3, cc.xy(1, 1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("strings").getString("label_find-for-Name"));
        panel3.add(label1, cc.xy(3, 1));
        textField = new JTextField();
        textField.setColumns(10);
        panel3.add(textField, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        editButton = new JButton();
        this.$$$loadButtonText$$$(editButton, ResourceBundle.getBundle("strings").getString("button_edit"));
        panel3.add(editButton, cc.xy(1, 1));
        refreshButton = new JButton();
        this.$$$loadButtonText$$$(refreshButton, ResourceBundle.getBundle("strings").getString("button_refresh"));
        panel2.add(refreshButton, cc.xy(9, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow"));
        panel2.add(panel4, cc.xy(3, 1));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("strings").getString("label_importGeoName_country"));
        panel4.add(label2, cc.xy(1, 1));
        panel4.add(countryComboBox, cc.xy(3, 1));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,fill:d:noGrow"));
        panel2.add(panel5, cc.xy(5, 1));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("strings").getString("label_importGeoName_feature-class"));
        panel5.add(label3, cc.xy(1, 2, CellConstraints.DEFAULT, CellConstraints.CENTER));
        panel5.add(featureClassComboBox, cc.xy(3, 2));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,center:max(d;4px):noGrow"));
        panel2.add(panel6, cc.xy(7, 1));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("strings").getString("label_importGeoName_feature-code"));
        panel6.add(label4, cc.xy(1, 2));
        panel6.add(featureCodeComboBox, cc.xy(3, 2));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setAutoscrolls(true);
        scrollPane1.setDoubleBuffered(true);
        panel1.add(scrollPane1, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.FILL));
        table = new JTable();
        table.setUpdateSelectionOnSort(false);
        scrollPane1.setViewportView(table);
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
}

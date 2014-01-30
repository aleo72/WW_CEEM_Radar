/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypes;
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace.entry.AirspaceEntryMessage;
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.handlerForm.HandlerRadarEditorFrom;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * User: Aleo Bakalov
 * Date: 13.12.13
 * Time: 13:44
 */
public class RadarEditorForm {
    private JPanel panel1;
    public JSpinner altitudeSpinner;
    public JTextField nameAirspaceTextField;
    public LocationForm locationForm = new LocationForm();
    public JComboBox<RadarTypes.RadarType> typeRadarComboBox;
    public JPanel panelParm;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;

    public final HandlerRadarEditorFrom handler;

    public RadarEditorForm(AirspaceEntryMessage airspaceEntryMessage) {
        /*
        Создаем обработчик что бы можно было инициализировать некоторые элименты
         */
        handler = new HandlerRadarEditorFrom(this, airspaceEntryMessage);

        /*
        GUI
         */
        $$$setupUI$$$();

    }

    private void createUIComponents() {
//        handler.initComboBoxes();
//        handler.initSpinners();
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:max(d;100px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(panel2, cc.xy(1, 1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("label").getString("airspace_name"));
        panel2.add(label1, cc.xy(1, 2, CellConstraints.RIGHT, CellConstraints.FILL));
        panel2.add(nameAirspaceTextField, cc.xy(3, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("strings").getString("label_airspace_Altitude"));
        panel2.add(label2, cc.xy(1, 4, CellConstraints.RIGHT, CellConstraints.FILL));
        panel2.add(altitudeSpinner, cc.xy(3, 4, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("label").getString("radarManager_airspace_radatType"));
        panel2.add(label3, cc.xy(1, 6, CellConstraints.DEFAULT, CellConstraints.FILL));
        panel2.add(typeRadarComboBox, cc.xy(3, 6));
        panel1.add(locationForm.$$$getRootComponent$$$(), cc.xy(1, 5));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
        panelParm = new JPanel();
        panelParm.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        scrollPane1.setViewportView(panelParm);
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        label4.setText("Label");
        panelParm.add(label4, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        comboBox1 = new JComboBox();
        panelParm.add(comboBox1, cc.xy(3, 1));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        label5.setText("Label");
        panelParm.add(label5, cc.xy(1, 3));
        comboBox2 = new JComboBox();
        panelParm.add(comboBox2, cc.xy(3, 3));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(4);
        label6.setText("Label");
        panelParm.add(label6, cc.xy(1, 5));
        comboBox3 = new JComboBox();
        panelParm.add(comboBox3, cc.xy(3, 5));
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
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

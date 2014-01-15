/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ua.edu.odeku.ceem.mapRadar.models.Prefix_SI;
import ua.edu.odeku.ceem.mapRadar.models.radar.Radar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.util.ResourceBundle;

/**
 * User: Aleo Bakalov
 * Date: 13.12.13
 * Time: 13:44
 */
public class RadarEditorForm {
    private JPanel panel1;
    private JSpinner transmotterPowerSpinner;
    private JSpinner antenaGainSpinner;
    private JSpinner effectiveAreaSpinner;
    private JSpinner scatteringCrossSectionSpinner;
    private JSpinner minimumReceiverSensitivitySpinner;
    private JTextField coverageTextField;
    private JComboBox<Prefix_SI> transmotterPowerComboBox;
    private JComboBox<Prefix_SI> antenaGainComboBox;
    private JComboBox<Prefix_SI> effectiveAreaComboBox;
    private JComboBox<Prefix_SI> scatteringCrossSectionComboBox;
    private JComboBox<Prefix_SI> minimumReceiverSensitivityComboBox;
    private JSpinner altitudeSpinner;
    private JTextField lanTextField;
    private JTextField lonTextField;

    private Radar _radar = null;

    public RadarEditorForm() {
        $$$setupUI$$$();
        String format = "#,##0.########";
        initSpinner(antenaGainSpinner, format);
        initSpinner(effectiveAreaSpinner, format);
        initSpinner(minimumReceiverSensitivitySpinner, format);
        initSpinner(scatteringCrossSectionSpinner, format);
        initSpinner(transmotterPowerSpinner, format);

        updateCoverageTextField();
    }

    private void initSpinner(JSpinner jSpinner, String format) {
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1.0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        jSpinner.setModel(spinnerNumberModel);
        jSpinner.setEditor(new JSpinner.NumberEditor(jSpinner, format));
        jSpinner.addChangeListener(valueChangeListener);
    }

    public Radar getRadar() {
        updateRadar();
        return _radar;
    }

    private void updateRadar() {
        try {
            double gain = Double.parseDouble(antenaGainSpinner.getValue().toString())
                    * ((Prefix_SI) antenaGainComboBox.getSelectedItem()).pow();
            double effective = Double.parseDouble(effectiveAreaSpinner.getValue().toString())
                    * ((Prefix_SI) effectiveAreaComboBox.getSelectedItem()).pow();
            double minimum = Double.parseDouble(minimumReceiverSensitivitySpinner.getValue().toString())
                    * ((Prefix_SI) minimumReceiverSensitivityComboBox.getSelectedItem()).pow();
            double scattening = Double.parseDouble(scatteringCrossSectionSpinner.getValue().toString())
                    * ((Prefix_SI) scatteringCrossSectionComboBox.getSelectedItem()).pow();
            double tranmotter = Double.parseDouble(transmotterPowerSpinner.getValue().toString())
                    * ((Prefix_SI) transmotterPowerComboBox.getSelectedItem()).pow();
            int altitude = Integer.parseInt(altitudeSpinner.getValue().toString());

            if (_radar == null) {
                _radar = new Radar(tranmotter, gain, effective, scattening, minimum, altitude);
            } else {
                _radar.update(tranmotter, gain, effective, scattening, minimum, altitude);
            }

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            _radar = null;  // TODO Нужноли это или нет?
        }
    }

    private ChangeListener valueChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            updateCoverageTextField();
        }
    };

    private ItemListener comboBoxItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            updateCoverageTextField();
        }
    };

    private void updateCoverageTextField() {
        Radar radar = RadarEditorForm.this.getRadar();
        if (radar == null)
            coverageTextField.setText("");
        else
            coverageTextField.setText(String.valueOf(radar.coverage()));
    }

    private void createUIComponents() {
        transmotterPowerComboBox = new JComboBox<Prefix_SI>(Prefix_SI.array);
        transmotterPowerComboBox.setSelectedItem(Prefix_SI.ONE);
        transmotterPowerComboBox.addItemListener(comboBoxItemListener);

        antenaGainComboBox = new JComboBox<Prefix_SI>(Prefix_SI.array);
        antenaGainComboBox.setSelectedItem(Prefix_SI.ONE);
        transmotterPowerComboBox.addItemListener(comboBoxItemListener);

        effectiveAreaComboBox = new JComboBox<Prefix_SI>(Prefix_SI.array);
        effectiveAreaComboBox.setSelectedItem(Prefix_SI.ONE);
        effectiveAreaComboBox.addItemListener(comboBoxItemListener);

        scatteringCrossSectionComboBox = new JComboBox<Prefix_SI>(Prefix_SI.array);
        scatteringCrossSectionComboBox.setSelectedItem(Prefix_SI.ONE);
        scatteringCrossSectionComboBox.addItemListener(comboBoxItemListener);

        minimumReceiverSensitivityComboBox = new JComboBox<Prefix_SI>(Prefix_SI.array);
        minimumReceiverSensitivityComboBox.setSelectedItem(Prefix_SI.ONE);
        minimumReceiverSensitivityComboBox.addItemListener(comboBoxItemListener);
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;100px):grow,left:4dlu:noGrow,fill:max(d;70px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(panel2, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("strings").getString("lavel_radarManager_editor_transmitterPower"));
        panel2.add(label1, cc.xy(1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("strings").getString("label_radarManager_antenaGain"));
        panel2.add(label2, cc.xy(1, 3, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("strings").getString("label_radarManager_effectiveArea"));
        panel2.add(label3, cc.xy(1, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("strings").getString("label_radarManager_editor"));
        panel2.add(label4, cc.xy(1, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("strings").getString("label_radarManager_editor_minimumReceiverSensitivity"));
        panel2.add(label5, cc.xy(1, 9, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("strings").getString("label_airspace_radius"));
        panel2.add(label6, cc.xy(1, 11, CellConstraints.RIGHT, CellConstraints.FILL));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("strings").getString("label_airspace_Altitude"));
        panel2.add(label7, cc.xy(1, 13, CellConstraints.RIGHT, CellConstraints.FILL));
        transmotterPowerSpinner = new JSpinner();
        panel2.add(transmotterPowerSpinner, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel2.add(transmotterPowerComboBox, cc.xy(5, 1));
        antenaGainSpinner = new JSpinner();
        panel2.add(antenaGainSpinner, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel2.add(antenaGainComboBox, cc.xy(5, 3));
        effectiveAreaSpinner = new JSpinner();
        effectiveAreaSpinner.setEnabled(true);
        panel2.add(effectiveAreaSpinner, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel2.add(effectiveAreaComboBox, cc.xy(5, 5));
        scatteringCrossSectionSpinner = new JSpinner();
        panel2.add(scatteringCrossSectionSpinner, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel2.add(scatteringCrossSectionComboBox, cc.xy(5, 7));
        minimumReceiverSensitivitySpinner = new JSpinner();
        panel2.add(minimumReceiverSensitivitySpinner, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel2.add(minimumReceiverSensitivityComboBox, cc.xy(5, 9));
        coverageTextField = new JTextField();
        coverageTextField.setEditable(false);
        panel2.add(coverageTextField, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        altitudeSpinner = new JSpinner();
        panel2.add(altitudeSpinner, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,center:max(d;4px):noGrow"));
        panel1.add(panel3, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("strings").getString("label_geoName_latitude"));
        panel3.add(label8, cc.xy(1, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        lanTextField = new JTextField();
        lanTextField.setEditable(false);
        panel3.add(lanTextField, cc.xy(3, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("strings").getString("field_geoName_longitude"));
        panel3.add(label9, cc.xy(5, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        lonTextField = new JTextField();
        lonTextField.setEditable(false);
        panel3.add(lonTextField, cc.xy(7, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
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

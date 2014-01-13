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
    public JSpinner transmotterPowerSpinner;
    public JSpinner antenaGainSpinner;
    public JSpinner effectiveAreaSpinner;
    public JSpinner scatteringCrossSectionSpinner;
    public JSpinner minimumReceiverSensitivitySpinner;
    public JTextField coverageTextField;
    private JComboBox<Prefix_SI> transmotterPowerComboBox;
    private JComboBox<Prefix_SI> antenaGainComboBox;
    private JComboBox<Prefix_SI> effectiveAreaComboBox;
    private JComboBox<Prefix_SI> scatteringCrossSectionComboBox;
    private JComboBox<Prefix_SI> minimumReceiverSensitivityComboBox;

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
        Radar radar = null;
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

             radar = new Radar(tranmotter, gain, effective, scattening, minimum);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return radar;
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:max(d;60px):grow,left:4dlu:noGrow,fill:max(d;60px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("strings").getString("lavel_radarManager_editor_transmitterPower"));
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(3, 3, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("strings").getString("label_radarManager_antenaGain"));
        panel1.add(label2, cc.xy(3, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        transmotterPowerSpinner = new JSpinner();
        panel1.add(transmotterPowerSpinner, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        antenaGainSpinner = new JSpinner();
        panel1.add(antenaGainSpinner, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("strings").getString("label_radarManager_effectiveArea"));
        panel1.add(label3, cc.xy(3, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        effectiveAreaSpinner = new JSpinner();
        effectiveAreaSpinner.setEnabled(true);
        panel1.add(effectiveAreaSpinner, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("strings").getString("label_radarManager_editor"));
        panel1.add(label4, cc.xy(3, 9, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        scatteringCrossSectionSpinner = new JSpinner();
        panel1.add(scatteringCrossSectionSpinner, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("strings").getString("label_radarManager_editor_minimumReceiverSensitivity"));
        panel1.add(label5, cc.xy(3, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        minimumReceiverSensitivitySpinner = new JSpinner();
        panel1.add(minimumReceiverSensitivitySpinner, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        coverageTextField = new JTextField();
        coverageTextField.setEditable(false);
        panel1.add(coverageTextField, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        panel1.add(transmotterPowerComboBox, cc.xy(7, 3));
        panel1.add(antenaGainComboBox, cc.xy(7, 5));
        panel1.add(effectiveAreaComboBox, cc.xy(7, 7));
        panel1.add(scatteringCrossSectionComboBox, cc.xy(7, 9));
        panel1.add(minimumReceiverSensitivityComboBox, cc.xy(7, 11));
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

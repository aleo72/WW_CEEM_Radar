/*
 * Copyright (C) 2013 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.tools.CeemRadarTool;
import ua.edu.odeku.ceem.mapRadar.tools.ToolFrame;
import ua.edu.odeku.ceem.mapRadar.utils.thread.Handler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * User: Aleo skype: aleo72
 * Date: 11.11.13
 * Time: 22:17
 */
public class ViewGeoNameTool implements CeemRadarTool {

    private JFrame parent;
    private JPanel panel1;
    private JButton refreshButton;
    private JTextField textField;
    private JTable table;
    private JComboBox countryComboBox;
    private JComboBox featureClassComboBox;
    private JComboBox featureCodeComboBox;


    public ViewGeoNameTool() {
        table.setAutoCreateRowSorter(true);
        refreshTable();
        refreshCountry();
        refreshFeatureClass();
        featureClassComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshFeatureCode();
            }
        });
    }

    private void refreshTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                table.setModel(new GeoNameTableModel());
            }
        }).start();
    }

    protected void refreshCountry() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                countryComboBox.removeAllItems();
                countryComboBox.addItem("");

                Session session = DB.getSession();

                String sql =
                        " SELECT COUNTRY_CODE           " +
                                " FROM GEO_NAME                  " +
                                " WHERE COUNTRY_CODE IS NOT NULL " +
                                " GROUP BY COUNTRY_CODE          " +
                                " ORDER BY COUNTRY_CODE;         ";

                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.addScalar("COUNTRY_CODE", DB.STRING_TYPE);

                ScrollableResults results = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);

                while (results.next()) {
                    countryComboBox.addItem(results.getString(0));
                }

                results.close();
                DB.closeSession(session);
            }
        }).start();
    }

    private void refreshFeatureClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                featureClassComboBox.removeAllItems();
                featureClassComboBox.addItem("");

                Session session = DB.getSession();

                String sql = " SELECT G.FEATURE_CLASS       " +
                        " FROM GEO_NAME G                   " +
                        " WHERE G.FEATURE_CLASS IS NOT NULL " +
                        " GROUP BY G.FEATURE_CLASS          " +
                        " ORDER BY G.FEATURE_CLASS          ";
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.addScalar("FEATURE_CLASS", DB.STRING_TYPE);

                ScrollableResults results = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);

                while (results.next()) {
                    String s = results.getString(0);
                    featureClassComboBox.addItem(s);
                }

                results.close();
                DB.closeSession(session);
            }
        }).start();
    }

    private void refreshFeatureCode() {
        final String featureClass = featureClassComboBox.getSelectedItem().toString();

        if (!featureClass.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    featureCodeComboBox.removeAllItems();
                    featureCodeComboBox.addItem("");
                    Session session = DB.getSession();
                    String sql = "SELECT G.FEATURE_CODE             " +
                            "FROM GEO_NAME G                        " +
                            "WHERE G.FEATURE_CLASS = :featureClass  " +
                            "   AND G.FEATURE_CODE IS NOT NULL      " +
                            "GROUP BY G.FEATURE_CODE                " +
                            "ORDER BY G.FEATURE_CODE;               ";
                    SQLQuery sqlQuery = session.createSQLQuery(sql);
                    sqlQuery.setParameter("featureClass", featureClass);
                    sqlQuery.addScalar("FEATURE_CODE", DB.STRING_TYPE);
                    ScrollableResults results = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);
                    results.beforeFirst();
                    while (results.next()) {
                        featureCodeComboBox.addItem(results.getString(0));
                    }
                    results.close();
                    DB.closeSession(session);

                }
            }).start();
        }
    }

    @Override
    public void setParent(JFrame frame) {
        parent = frame;
    }

    @Override
    public Handler getHandlerForJFrame(final ToolFrame frame) {
        return new Handler() {
            @Override
            public void start() {
                frame.setMinimumSize(new Dimension(800, 600));
            }
        };
    }

    @Override
    public String getNameTool() {
        return this.getClass().getName();
    }

    @Override
    public JPanel getPanel() {
        return (JPanel) this.$$$getRootComponent$$$();
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
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:noGrow", "center:d:noGrow"));
        CellConstraints cc = new CellConstraints();
        panel1.add(panel2, cc.xy(3, 3));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow"));
        panel2.add(panel3, cc.xy(1, 1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("strings").getString("gui_label_find_Name"));
        panel3.add(label1, cc.xy(1, 1));
        textField = new JTextField();
        textField.setColumns(10);
        panel3.add(textField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        refreshButton = new JButton();
        this.$$$loadButtonText$$$(refreshButton, ResourceBundle.getBundle("strings").getString("gui_button_refresh"));
        panel2.add(refreshButton, cc.xy(9, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow"));
        panel2.add(panel4, cc.xy(3, 1));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("strings").getString("gui_label_country"));
        panel4.add(label2, cc.xy(1, 1));
        countryComboBox = new JComboBox();
        panel4.add(countryComboBox, cc.xy(3, 1));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,fill:d:noGrow"));
        panel2.add(panel5, cc.xy(5, 1));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("strings").getString("gui_label_feature_class"));
        panel5.add(label3, cc.xy(1, 2, CellConstraints.DEFAULT, CellConstraints.CENTER));
        featureClassComboBox = new JComboBox();
        panel5.add(featureClassComboBox, cc.xy(3, 2));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,center:max(d;4px):noGrow"));
        panel2.add(panel6, cc.xy(7, 1));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("strings").getString("gui_label_feature_code"));
        panel6.add(label4, cc.xy(1, 2));
        featureCodeComboBox = new JComboBox();
        panel6.add(featureCodeComboBox, cc.xy(3, 2));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setAutoscrolls(true);
        scrollPane1.setDoubleBuffered(true);
        panel1.add(scrollPane1, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.FILL));
        table = new JTable();
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

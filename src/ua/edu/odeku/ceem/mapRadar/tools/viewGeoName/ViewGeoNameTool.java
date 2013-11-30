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
import java.util.List;

/**
 * Панель для работы с geoNameTool
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
    private JComboBox<String> countryComboBox;
    private JComboBox<String> featureClassComboBox;
    private JComboBox<String> featureCodeComboBox;
    private JButton editButton;

    private EditGeoNameDialog editGeoNameDialog;


    public ViewGeoNameTool() {
        $$$setupUI$$$();
        table.setAutoCreateRowSorter(false);
        refreshTable();
        refreshCountry();
        refreshFeatureClass();
        featureClassComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshFeatureCode();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    GeoName geoName = ((GeoNameTableModel) table.getModel()).getListGeoName().get(table.getSelectedRow());
                    LatLon latLon = LatLon.fromDegrees(geoName.getLat(), geoName.getLon());
                    WorldWindow worldWindow = AppCeemRadarFrame.getAppCeemRadarFrame().getWwd();
                    double elevation = 0x1 << 15;
                    worldWindow.getView().goTo(new Position(latLon, elevation), elevation);
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editGeoNameDialog = new EditGeoNameDialog(((GeoNameTableModel) table.getModel()).getListGeoName().get(table.getSelectedRow()));
                editGeoNameDialog.setAlwaysOnTop(true);

                boolean isParentTop = parent.isAlwaysOnTop();
                parent.setAlwaysOnTop(false);
                editGeoNameDialog.setLocationByPlatform(true);

                editGeoNameDialog.setVisible(true);

                parent.setAlwaysOnTop(isParentTop);
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        String country, featureClass, featureCode;
        country = featureClass = featureCode = null;

        Object o = countryComboBox.getSelectedItem();
        if (o != null) {
            country = o.toString();
        }
        o = featureClassComboBox.getSelectedItem();
        if (o != null) {
            featureClass = o.toString();
        }
        o = featureCodeComboBox.getSelectedItem();
        if (o != null) {
            featureCode = o.toString();
        }

        final String finalCountry = country;
        final String finalFeatureClass = featureClass;
        final String finalFeatureCode = featureCode;
        final String finalPrefix = this.textField.getText().trim();

        new Thread(new Runnable() {
            @Override
            public void run() {
                table.setAutoCreateRowSorter(false);
                table.setModel(new GeoNameTableModel(finalPrefix, finalCountry, finalFeatureClass, finalFeatureCode));
                table.setAutoCreateRowSorter(false);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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

    private void createUIComponents() {
        countryComboBox = new JComboBox<String>();
        featureClassComboBox = new JComboBox<String>();
        featureCodeComboBox = new JComboBox<String>();
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

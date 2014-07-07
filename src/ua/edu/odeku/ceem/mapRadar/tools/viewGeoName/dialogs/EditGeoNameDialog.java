package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.db.model.GeoName;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;
import ua.edu.odeku.ceem.mapRadar.utils.geometry.LatitudeLongitudeUtils;
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class EditGeoNameDialog extends JDialog {

    private GeoName geoName;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idTextField;
    private JTextField sourceIdTextField;
    private JTextField nameTextField;
    private JTextField asciiNameTextField;
    private JTextField translateTextField;
    private JTextField featureClassTextField;
    private JComboBox<String> featureCodeComboBox;
    private JFormattedTextField lantitudeFormattedTextField;
    private JFormattedTextField longitudeFormattedTextField;
    private JTextField alternativeTextField;
    private JTextField countryTextField;
    private JButton deleteButton;

    public EditGeoNameDialog() {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserMessage.ConfirmDialog(EditGeoNameDialog.this.$$$getRootComponent$$$(),
                        ResourceString.get("title_confirmDialog_delete"),
                        ResourceString.get("message_geoName_delete-item"))
                        ) {
                    EntityManager entityManager = DB.createEntityManager();
                    entityManager.getTransaction().begin();

                    GeoName geoNameMerge = entityManager.merge(geoName);
                    entityManager.remove(geoNameMerge);

                    entityManager.getTransaction().commit();

                    entityManager.close();

                    onCancel();
                }
            }
        });
    }

    public EditGeoNameDialog(GeoName geoName) {
        this();

        this.geoName = geoName;
        this.idTextField.setText(String.valueOf(geoName.id()));
        this.sourceIdTextField.setText(String.valueOf(geoName.id()));
        this.nameTextField.setText(geoName.name());
        this.asciiNameTextField.setText(geoName.ascii());
        this.translateTextField.setText(geoName.translateName().get() != null ? geoName.translateName().get() : "");
        this.alternativeTextField.setText(geoName.alternateNames() != null ? geoName.alternateNames() : "");
        this.countryTextField.setText(geoName.countryCode());
        this.featureClassTextField.setText(String.valueOf(geoName.featureClass()));
        this.lantitudeFormattedTextField.setText(String.valueOf(geoName.lat()));
        this.longitudeFormattedTextField.setText(String.valueOf(geoName.lon()));

        initFeatureCode(geoName);

        this.pack();
        this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setMaximumSize(new Dimension(800, this.getHeight()));
    }

    private void initFeatureCode(GeoName geoName) {
        Session session = DB.createHibernateSession();

        String sql = " SELECT DISTINCT FEATURE_CODE           " +
                " FROM GEO_NAME                          " +
                " WHERE FEATURE_CLASS = :featureClass    " +
                " ORDER BY FEATURE_CODE                 ;";
        SQLQuery query = session.createSQLQuery(sql);

        query.setParameter("featureClass", geoName.featureCode());
        query.addScalar("FEATURE_CODE", DB.STRING_TYPE());

        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        results.beforeFirst();

        while (results.next()) {
            featureCodeComboBox.addItem(results.getString(0));
        }

        results.close();
        DB.closeSession(session);

        featureCodeComboBox.setSelectedItem(geoName.featureCode());
    }

    private void onOK() {
        if (saveData())
            dispose();
    }

    private void onCancel() {
        dispose();
    }

    /**
     * Метод сохраняет данные этой формы, если она не валидно то, возращает false.
     *
     * @return true если данные были сохранены
     */
    private boolean saveData() {

        if (isValidData()) {
            Session session = null;
            boolean res = false;
            try {
                session = DB.createHibernateSession();

//                GeoName geoName = (GeoName) (session.load(GeoName.class, new Integer(this.geoName.getId())));

//                geoName.setName(nameTextField.getText());
//                geoName.setAsciiname(asciiNameTextField.getText());
//                geoName.setTranslateName(translateTextField.getText());
//                geoName.setAlternatenames(alternativeTextField.getText());
//                geoName.setLat(Double.parseDouble(lantitudeFormattedTextField.getText()));
//                geoName.setLon(Double.parseDouble(longitudeFormattedTextField.getText()));
//                geoName.setFeatureCode(featureCodeComboBox.getSelectedItem().toString());

                session.flush();

                res = true;

            } catch (Exception e) {
                e.printStackTrace();
                UserMessage.error(this.$$$getRootComponent$$$(), e.getMessage());
                res = false;
            } finally {
                DB.closeSession(session);
            }
            return res;
        } else {
            return false;
        }
    }

    private boolean isValidData() {

        String data;

        data = nameTextField.getText();
        if (data.trim().isEmpty()) {
            String message = ResourceString.get("field_geoName_name") + " "
                    + ResourceString.get("message-must-not-be-empty");
            UserMessage.warning(this.$$$getRootComponent$$$(), message);
            this.nameTextField.setText(geoName.name());
            return false;
        }
        data = asciiNameTextField.getText();
        if (data.trim().isEmpty()) {
            String message = ResourceString.get("string_field") + " " +
                    ResourceString.get("field_geoName_asciiName") + " "
                    + ResourceString.get("message-must-not-be-empty") + "!";
            UserMessage.warning(this.$$$getRootComponent$$$(), message);
            this.asciiNameTextField.setText(geoName.ascii());
            return false;
        }
        data = translateTextField.getText();
        if (data.trim().isEmpty()) {
            String message = ResourceString.get("string_field") + " " +
                    ResourceString.get("field_geoName_translate") + " "
                    + ResourceString.get("message-must-not-be-empty") + "!";
            UserMessage.warning(this.$$$getRootComponent$$$(), message);
            this.translateTextField.setText(geoName.translateName().get());
            return false;
        }
        if (!LatitudeLongitudeUtils.isValidLatitude(lantitudeFormattedTextField.getText())) {
            String message = ResourceString.get("string_field") + " " +
                    ResourceString.get("field_geoName_latitude") + " "
                    + ResourceString.get("message_Do-not-satisfy-the-format-of-the-coordinates") + "!";
            UserMessage.warning(this.$$$getRootComponent$$$(), message);
            this.lantitudeFormattedTextField.setText(String.valueOf(geoName.lat()));
            return false;
        }
        if (!LatitudeLongitudeUtils.isValidLongitude(longitudeFormattedTextField.getText())) {
            String message = ResourceString.get("string_field") + " " +
                    ResourceString.get("field_geoName_longitude") + " "
                    + ResourceString.get("message_Do-not-satisfy-the-format-of-the-coordinates") + "!";
            UserMessage.warning(this.$$$getRootComponent$$$(), message);
            this.longitudeFormattedTextField.setText(String.valueOf(this.geoName.lon()));
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        EditGeoNameDialog dialog = new EditGeoNameDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        lantitudeFormattedTextField = new JFormattedTextField();
        longitudeFormattedTextField = new JFormattedTextField();

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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, ResourceBundle.getBundle("strings").getString("button_ok"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, ResourceBundle.getBundle("strings").getString("button_cancel"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        deleteButton = new JButton();
        this.$$$loadButtonText$$$(deleteButton, ResourceBundle.getBundle("strings").getString("button_delete"));
        CellConstraints cc = new CellConstraints();
        panel3.add(deleteButton, cc.xy(1, 1));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        contentPane.add(panel4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("strings").getString("label_geoName_id"));
        panel4.add(label1, cc.xy(1, 1));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("strings").getString("label_geoName_source_id"));
        panel4.add(label2, cc.xy(1, 3));
        sourceIdTextField = new JTextField();
        sourceIdTextField.setEditable(false);
        sourceIdTextField.setEnabled(true);
        panel4.add(sourceIdTextField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        idTextField = new JTextField();
        idTextField.setEditable(false);
        idTextField.setEnabled(true);
        panel4.add(idTextField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("strings").getString("label_geoName_Name"));
        panel4.add(label3, cc.xy(1, 5));
        nameTextField = new JTextField();
        panel4.add(nameTextField, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("strings").getString("label_geoName"));
        panel4.add(label4, cc.xy(1, 7));
        asciiNameTextField = new JTextField();
        panel4.add(asciiNameTextField, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("strings").getString("label_geoName_translate"));
        panel4.add(label5, cc.xy(1, 9));
        translateTextField = new JTextField();
        panel4.add(translateTextField, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        featureClassTextField = new JTextField();
        featureClassTextField.setEditable(false);
        featureClassTextField.setEnabled(true);
        panel4.add(featureClassTextField, cc.xy(3, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("strings").getString("label_geoName_featureCode"));
        panel4.add(label6, cc.xy(1, 17));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("strings").getString("label_geoName_featureClass"));
        panel4.add(label7, cc.xy(1, 15));
        panel4.add(featureCodeComboBox, cc.xy(3, 17));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,center:max(d;4px):noGrow"));
        panel4.add(panel5, cc.xy(3, 19));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("strings").getString("label_geoName_latitude"));
        panel5.add(label8, cc.xy(1, 2));
        lantitudeFormattedTextField.setColumns(8);
        panel5.add(lantitudeFormattedTextField, cc.xy(3, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("strings").getString("label_geoName_longitude"));
        panel5.add(label9, cc.xy(5, 2));
        longitudeFormattedTextField.setColumns(8);
        panel5.add(longitudeFormattedTextField, cc.xy(7, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("strings").getString("label_geoName_altName"));
        panel4.add(label10, cc.xy(1, 11));
        alternativeTextField = new JTextField();
        panel4.add(alternativeTextField, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label11 = new JLabel();
        label11.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("strings").getString("label_geoName_country:"));
        panel4.add(label11, cc.xy(1, 13));
        countryTextField = new JTextField();
        countryTextField.setEditable(false);
        panel4.add(countryTextField, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
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
        return contentPane;
    }
}

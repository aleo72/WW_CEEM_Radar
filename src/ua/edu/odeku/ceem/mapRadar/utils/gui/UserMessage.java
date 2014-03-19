package ua.edu.odeku.ceem.mapRadar.utils.gui;

import javax.swing.*;

/**
 * Класс позволяющий выкидывать сообщения пользователям
 * User: Aleo skype: aleo72
 * Date: 10.11.13
 * Time: 18:37
 */
public class UserMessage {

    public static void error(JComponent component, String message){
        JOptionPane.showMessageDialog(component, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void warning(JComponent component, String message){
        JOptionPane.showMessageDialog(component, message, "Warning!", JOptionPane.WARNING_MESSAGE);
    }

    public static void inform(JComponent component, String message){
        JOptionPane.showMessageDialog(component, message, "Information!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean ConfirmDialog(JComponent component, String title, String message){
        return JOptionPane.showConfirmDialog(
                component, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.OK_OPTION;
    }
}

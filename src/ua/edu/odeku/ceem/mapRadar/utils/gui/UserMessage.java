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
}

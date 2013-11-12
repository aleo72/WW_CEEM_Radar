package ua.edu.odeku.ceem.mapRadar.settings;

import java.util.Locale;

/**
 * User: Aleo
 * Date: 03.11.13
 * Time: 15:06
 */
public class PropertyProgram {

    /**
     * TODO Название программы должно быть получено из настроек
     */
    private final static String NAME_PROGRAM = "World Wind Ceem Radar";

    /**
     * TODO локаль должна считываться автоматически либо браться из настроек, которые установил пользователь
     */
    private final static Locale CURRENT_LOCALE = new Locale("ru", "RU");

    /**
     * TODO данная настройка так же должна быть настраеваемая пользователем, или автоматически браться по определенному критерию
     */
    private final static boolean translateGeoName = true;

    /**
     * Возращает название программы
     *
     * @return
     */
    public static String getNameProgram() {
        return NAME_PROGRAM;
    }

    public static Locale getCurrentLocale() {
        return CURRENT_LOCALE;
    }

    public static boolean isTranslateGeoName() {
        return translateGeoName;
    }
}

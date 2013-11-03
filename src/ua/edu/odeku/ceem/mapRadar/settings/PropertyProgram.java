package ua.edu.odeku.ceem.mapRadar.settings;

import java.util.Locale;

/**
 * User: Aleo
 * Date: 03.11.13
 * Time: 15:06
 */
public class PropertyProgram {

    // TODO Название программы должно быть получено из настроек
    private final static String NAME_PROGRAM = "World Wind Ceem Radar";

    private final static Locale CURRENT_LOCALE = new Locale("ru", "RU");

    /**
     * Возращает название программы
     * @return
     */
    public static String getNameProgram() {
        return NAME_PROGRAM;
    }

    public static Locale getCurrentLocale(){
        return CURRENT_LOCALE;
    }
}

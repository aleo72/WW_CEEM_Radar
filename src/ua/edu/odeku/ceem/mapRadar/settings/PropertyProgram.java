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
     * TODO флаг на начальную инициализацию базы данных в программе
     */
    public final static boolean INIT_DB = false;

    /**
     * Название как будет отображаться программа
     */
    private final static String LOOK_AND_FEEL_INFO = "Nimbus";

    /**
     * Название как будет отображаться программа
     * @return Строка названия класса отображения программы
     */
    public static String getLookAndFeelInfo() {
        return LOOK_AND_FEEL_INFO;
    }

    private final static String FILE_START_WINDOW = "resources/ww_start.png";

    /**
     * Возращает строку, где распологается этот файл с его именем
     * @return Строка, путь к файлу картинки, которая будет отображаться при старте программы
     */
    public static String getFileStartWindow() {
        return FILE_START_WINDOW;
    }

    static {
        Locale.setDefault(CURRENT_LOCALE);
    }

    /**
     * TODO данная настройка так же должна быть настраеваемая пользователем, или автоматически браться по определенному критерию
     */
    private final static boolean translateGeoName = true;

    /**
     * Возращает название программы
     *
     * @return NAME_PROGRAM
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

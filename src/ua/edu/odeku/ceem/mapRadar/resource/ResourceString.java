package ua.edu.odeku.ceem.mapRadar.resource;

import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram;

import java.util.ResourceBundle;

/**
 * Класс возращает строки из ресурсов
 * User: Aleo skype: aleo72
 * Date: 03.11.13
 * Time: 18:56
 */
public final class ResourceString {
    private static final ResourceBundle resource;
    private static final String PREFIX = "string_";

    static {
        resource = ResourceBundle.getBundle("strings", PropertyProgram.getCurrentLocale());
    }

    public static String get(String key) {
        String s = key.startsWith(PREFIX) ? key : PREFIX + key;
//        String ss = resource.containsKey(s) ? new String(resource.getString(s).getBytes("ISO-8859-1"), "UTF-8") : "None";
        return resource.containsKey(s) ? resource.getString(s) : "None";

    }

}

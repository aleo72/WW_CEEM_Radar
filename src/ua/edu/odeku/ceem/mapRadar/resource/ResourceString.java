package ua.edu.odeku.ceem.mapRadar.resource;

import ua.edu.odeku.ceem.mapRadar.settings.Property;

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
        resource = ResourceBundle.getBundle("strings", Property.CURRENT_LOCALE_LANGUAGE().toLocale());
    }

    public static String get(String key) {
        return resource.containsKey(key) ? resource.getString(key) : key;
    }

}

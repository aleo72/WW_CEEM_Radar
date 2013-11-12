package ua.edu.odeku.ceem.mapRadar.utils.translate;

import java.util.HashMap;
import java.util.Locale;

/**
 * User: ABakalov
 * Date: 12.11.13
 * Time: 15:18
 */
public final class TranslateManager {

    private static final HashMap<String, Translatable> localeTranslate = new HashMap<>();

    static {
        localeTranslate.put("UA", new UA());
    }

    public static Translatable getTranslatable(Locale locale) {
        return getTranslatable(locale.getCountry());
    }

    public static Translatable getTranslatable(String country) {
        String toUpper = country.toUpperCase();
        if (localeTranslate.containsKey(toUpper)) {
            return localeTranslate.get(toUpper);
        }
        return new Default();
    }
}

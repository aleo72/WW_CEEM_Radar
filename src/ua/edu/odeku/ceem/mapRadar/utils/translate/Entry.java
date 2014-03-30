package ua.edu.odeku.ceem.mapRadar.utils.translate;

import java.util.Map;

/**
 *
 * User: Aleo
 * Date: 12.11.13
 * Time: 15:09
 */
class Entry implements Map.Entry {

    String key;
    String value;

    Entry(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(Object value) {
        return null;
    }
}

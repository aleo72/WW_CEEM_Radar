package ua.edu.odeku.ceem.mapRadar.utils.translate;

import java.util.LinkedList;

public class UA implements Translatable {

    private static final LinkedList<Entry> ua_us = new LinkedList<Entry>();
    private static final LinkedList<Entry> us_ua = new LinkedList<Entry>();

    static {
        ua_us.add(new Entry("Є", "Ye"));   ua_us.add(new Entry("є", "ye"));
        ua_us.add(new Entry("Ї", "Yi"));   ua_us.add(new Entry("ї", "yi"));
        ua_us.add(new Entry("Ю", "Yu"));    ua_us.add(new Entry("ю", "yu"));
        ua_us.add(new Entry("Я", "Ya"));    ua_us.add(new Entry("я", "ya"));
        ua_us.add(new Entry("ые", "yye"));
        ua_us.add(new Entry("И", "Y"));    ua_us.add(new Entry("и", "y"));
        ua_us.add(new Entry("Й", "Y"));    ua_us.add(new Entry("й", "y"));
        ua_us.add(new Entry("Х", "Kh"));    ua_us.add(new Entry("х", "kh"));
        ua_us.add(new Entry("Ц", "Ts"));    ua_us.add(new Entry("ц", "ts"));
        ua_us.add(new Entry("Ч", "Ch"));    ua_us.add(new Entry("ч", "ch"));
        ua_us.add(new Entry("Ш", "Sh"));    ua_us.add(new Entry("ш", "sh"));
        ua_us.add(new Entry("Ж", "Zh"));   ua_us.add(new Entry("ж", "zh"));
        ua_us.add(new Entry("Щ", "Shch"));    ua_us.add(new Entry("щ", "shch"));
        ua_us.add(new Entry("Зг", "Zgh"));    ua_us.add(new Entry("зг", "zgh"));
        ua_us.add(new Entry("А", "A"));    ua_us.add(new Entry("а", "a"));
        ua_us.add(new Entry("Б", "B"));    ua_us.add(new Entry("б", "b"));
        ua_us.add(new Entry("В", "V"));    ua_us.add(new Entry("в", "v"));
        ua_us.add(new Entry("Г", "H"));    ua_us.add(new Entry("г", "h"));
        ua_us.add(new Entry("Ґ", "G"));    ua_us.add(new Entry("ґ", "g"));
        ua_us.add(new Entry("Д", "D"));    ua_us.add(new Entry("д", "d"));
        ua_us.add(new Entry("Е", "E"));    ua_us.add(new Entry("е", "e"));
        ua_us.add(new Entry("З", "Z"));    ua_us.add(new Entry("з", "z"));
        ua_us.add(new Entry("І", "I"));    ua_us.add(new Entry("і", "i"));
        ua_us.add(new Entry("К", "K"));    ua_us.add(new Entry("к", "k"));
        ua_us.add(new Entry("Л", "L"));    ua_us.add(new Entry("л", "l"));
        ua_us.add(new Entry("М", "M"));    ua_us.add(new Entry("м", "m"));
        ua_us.add(new Entry("Н", "N"));    ua_us.add(new Entry("н", "n"));
        ua_us.add(new Entry("О", "O"));    ua_us.add(new Entry("о", "o"));
        ua_us.add(new Entry("П", "P"));    ua_us.add(new Entry("п", "p"));
        ua_us.add(new Entry("Р", "R"));    ua_us.add(new Entry("р", "r"));
        ua_us.add(new Entry("С", "S"));    ua_us.add(new Entry("с", "s"));
        ua_us.add(new Entry("Т", "T"));    ua_us.add(new Entry("т", "t"));
        ua_us.add(new Entry("У", "U"));    ua_us.add(new Entry("у", "u"));
        ua_us.add(new Entry("Ф", "F"));    ua_us.add(new Entry("ф", "f"));
        ua_us.add(new Entry("ь", "'"));
        ua_us.add(new Entry("`", "\""));

        for (Entry entry : ua_us){
            us_ua.add(new Entry(entry.value, entry.key));
        }
    }

    /**
     * Проверяет на наличие латинских символов
     * @param string слово которое необходимо проверить
     * @return true если в переданой строке нет латинских символов
     */
    public boolean checkWorldLatin(String string){
        boolean res = true;
        for(char с : string.toCharArray()){
            if( (int) с >= 65 && (int) с <= 90)
                return false;
            if( (int) с >= 97 && (int) с <= 122)
                return false;
        }
        return res;
    }

    public static String us_to_ua(String word){
        return _to_(word, us_ua);
    }

    public static String ua_to_us(String word){
        return _to_(word, ua_us);
    }

    private static String _to_(String word, LinkedList<Entry> list){
        StringBuilder sb = new StringBuilder(word);

        boolean needSearch = true;
        while(needSearch){
            needSearch = false;
            for (Entry entry : list){
                int index = sb.indexOf(entry.getKey());
                if (index >= 0){
                    sb.replace(index, index + entry.getKey().length(), entry.getValue());
                    needSearch = true;
                    break;
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String translate(String word) {
        return us_to_ua(word);
    }
}

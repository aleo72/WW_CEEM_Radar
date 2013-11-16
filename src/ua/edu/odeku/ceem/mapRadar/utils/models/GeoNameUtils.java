/**
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.utils.models;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName;

import java.util.List;

/**
 * User: Aleo skype: aleo72
 * Date: 16.11.13
 * Time: 11:06
 */
public final class GeoNameUtils {

    /**
     * Вернет SQLQuery с задаными параметрами
     * @param session Сессия для которой будет сделан запрос
     * @param country код страны
     * @param featureClass класс объекта
     * @param featureCode код объекта
     * @return SQLQuery с задаными параметрами
     */
    public static SQLQuery getSQLQuery(Session session, String country, String featureClass, String featureCode){

        String select = "SELECT {G.*} FROM GEO_NAME G WHERE 1 = 1 ";

        // Создадим SQL Запрос
        if(country != null && !country.isEmpty()){
            select += " AND G.COUNTRY_CODE = :country ";
        }
        if(featureClass != null && !featureClass.isEmpty()){
            select += " AND G.FEATURE_CLASS = :featureClass ";
            if(featureCode != null && !featureCode.isEmpty()){
                select += " AND G.FEATURE_CODE = :featureCode ";
            }
        }
        select += ";";

        SQLQuery query = session.createSQLQuery(select);
        query.addEntity("G", GeoName.class);

        // Установим параметры
        if(country != null && !country.isEmpty()){
            query.setParameter("country", country);
        }
        if(featureClass != null && !featureClass.isEmpty()){
            query.setParameter("featureClass", featureClass);
            if(featureCode != null && !featureCode.isEmpty()){
                query.setParameter("featureCode", featureCode);
            }
        }
        return query;
    }

    /**
     * Вернет запрос для всех GeoName
     */
    public static SQLQuery getSQLQuery(Session session){
        return getSQLQuery(session, null, null, null);
    }

    /**
     * Возращает сразу списаок GeoName с указаными параметрами
     * @param country Страна, если null то не учитываеться страна
     * @param featureClass класс объекта, если null то не учитываеться
     * @param featureCode код объекта, если null то не учитываеться
     * @return список GeoName с указаными параметрами
     */
    public static List<GeoName> getList(String country, String featureClass, String featureCode){
        Session session = DB.getSession();

        SQLQuery query = getSQLQuery(session, country, featureClass, featureCode);
        List<GeoName> list = (List<GeoName>) query.list();

        DB.closeSession(session);
        return list;
    }

    /**
     * Возращает список все GeoName которые находятся в базе данных
     * @return список GeoName
     */
    public static List<GeoName> getList(){
        return getList(null, null, null);
    }

}

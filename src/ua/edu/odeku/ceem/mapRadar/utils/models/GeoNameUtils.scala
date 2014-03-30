/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.utils.models

import org.hibernate.{ScrollMode, ScrollableResults, Session, SQLQuery}
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName
import ua.edu.odeku.ceem.mapRadar.db.DB
import java.util

/**
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 15:57
 */
object GeoNameUtils {
  /**
   * Вернет SQLQuery с задаными параметрами
   * @param session Сессия для которой будет сделан запрос
   * @param country код страны
   * @param featureClass класс объекта
   * @param featureCode код объекта
   * @return SQLQuery с задаными параметрами
   */
  def SQLQuery(session: Session, country: String, featureClass: String, featureCode: String): SQLQuery = {
    var select: String = "SELECT {G.*} FROM GEO_NAME G WHERE 1 = 1 "
    if (country != null && !country.isEmpty) {
      select += " AND G.COUNTRY_CODE = :country "
    }
    if (featureClass != null && !featureClass.isEmpty) {
      select += " AND G.FEATURE_CLASS = :featureClass "
      if (featureCode != null && !featureCode.isEmpty) {
        select += " AND G.FEATURE_CODE = :featureCode "
      }
    }
    select += ";"
    val query: SQLQuery = session.createSQLQuery(select)
    query.addEntity("G", classOf[GeoName])
    if (country != null && !country.isEmpty) {
      query.setParameter("country", country)
    }
    if (featureClass != null && !featureClass.isEmpty) {
      query.setParameter("featureClass", featureClass)
      if (featureCode != null && !featureCode.isEmpty) {
        query.setParameter("featureCode", featureCode)
      }
    }
    query
  }

  /**
   * Вернет запрос для всех GeoName
   */
  def SQLQuery(session: Session): SQLQuery = {
    SQLQuery(session, null, null, null)
  }

  /**
   * Возращает сразу списаок GeoName с указаными параметрами
   * @param country Страна, если null то не учитываеться страна
   * @param featureClass класс объекта, если null то не учитываеться
   * @param featureCode код объекта, если null то не учитываеться
   * @return список GeoName с указаными параметрами
   */
  def getList(country: String, featureClass: String, featureCode: String): util.List[GeoName] = {
    val session: Session = DB.createHibernateSession()
    val query: SQLQuery = SQLQuery(session, country, featureClass, featureCode)
    val list: util.List[GeoName] = query.list.asInstanceOf[util.List[GeoName]]
    DB.closeSession(session)
    list
  }

  /**
   * Возращает список все GeoName которые находятся в базе данных
   * @return список GeoName
   */
  def getList: util.List[GeoName] = {
    getList(null, null, null)
  }

  def getListSimple(geoNameCountry: String, geoNameClass: String, geoNameCode: String): util.List[GeoName] = {
    val session: Session = DB.createHibernateSession()
    val query: SQLQuery = getSQLQuerySimple(session, geoNameCountry, geoNameClass, geoNameCode)
    val geoNames: util.ArrayList[GeoName] = new util.ArrayList[GeoName](500000)
    val results: ScrollableResults = query.scroll(ScrollMode.FORWARD_ONLY)
    while (results.next) {
      val g: GeoName = new GeoName
      g.setName(results.getString(0))
      g.setTranslateName(results.getString(1))
      g.setLat(results.getDouble(2))
      g.setLon(results.getDouble(3))
      geoNames.add(g)
    }
    results.close
    DB.closeSession(session)
    geoNames.trimToSize
    geoNames
  }

  /**
   * Вернет SQLQuery с задаными параметрами
   * @param session Сессия для которой будет сделан запрос
   * @param country код страны
   * @param featureClass класс объекта
   * @param featureCode код объекта
   * @return SQLQuery с задаными параметрами
   */
  def getSQLQuerySimple(session: Session, country: String, featureClass: String, featureCode: String): SQLQuery = {
    var select: String = "SELECT G.NAME, G.TRANSLATE, G.LAT, G.LON FROM GEO_NAME G WHERE 1 = 1 "
    if (country != null && !country.isEmpty) {
      select += " AND G.COUNTRY_CODE = :country "
    }
    if (featureClass != null && !featureClass.isEmpty) {
      select += " AND G.FEATURE_CLASS = :featureClass "
      if (featureCode != null && !featureCode.isEmpty) {
        select += " AND G.FEATURE_CODE = :featureCode "
      }
    }
    select += ";"
    val query: SQLQuery = session.createSQLQuery(select)
    query.addScalar("NAME", DB.STRING_TYPE)
    query.addScalar("TRANSLATE", DB.STRING_TYPE)
    query.addScalar("LAT", DB.DOUBLE_TYPE)
    query.addScalar("LON", DB.DOUBLE_TYPE)
    if (country != null && !country.isEmpty) {
      query.setParameter("country", country)
    }
    if (featureClass != null && !featureClass.isEmpty) {
      query.setParameter("featureClass", featureClass)
      if (featureCode != null && !featureCode.isEmpty) {
        query.setParameter("featureCode", featureCode)
      }
    }
    query
  }
}

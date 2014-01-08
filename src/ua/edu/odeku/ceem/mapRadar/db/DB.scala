/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.db

import javax.persistence.{EntityManager, Persistence}
import org.hibernate.Session

/**
 * Класс инкапсулирует работу с базой данных
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 14:38
 */
object DB {

  private lazy val _entityManagerFactory = Persistence.createEntityManagerFactory("db_h2")
  val STRING_TYPE = org.hibernate.`type`.StringType.INSTANCE
  val DOUBLE_TYPE = org.hibernate.`type`.DoubleType.INSTANCE
  val INTEGER_TYPE = org.hibernate.`type`.IntegerType.INSTANCE
  val LONG_TYPE = org.hibernate.`type`.LongType.INSTANCE

  /**
    * Создает объект EntityManager
   * @return EntityManager
   */
  def createEntityManager() : EntityManager = _entityManagerFactory.createEntityManager()

  /**
    * Создает object org.hibernate.Session
   * @return org.hibernate.Session
   */
  def createHibernateSession() : Session = createEntityManager().unwrap(classOf[Session])

  /**
    * Закрывает org.hibernate.Session переданую в качестве параметра
   * @param session
   */
  def closeSession(session : Session){
    if(session != null && session.isOpen ){
      session.close()
    }
  }

  /**
   * Метод подключается к базе данных и закрывает ее
   */
  def initDBConnection(){
    new Thread(new Runnable { def run(): Unit = DB.createEntityManager().close() }).start()
  }

}

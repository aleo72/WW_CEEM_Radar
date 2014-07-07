/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.db

import javax.persistence.{EntityManager, Persistence}
import org.hibernate.Session
import ua.edu.odeku.ceem.mapRadar.db.model.{GeoName, GeoNames}

import scala.slick.lifted.Tag

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
	def createEntityManager(): EntityManager = _entityManagerFactory.createEntityManager()

	/**
	 * Создает object org.hibernate.Session
	 * @return org.hibernate.Session
	 */
	def createHibernateSession(): Session = createEntityManager().unwrap(classOf[Session])

	/**
	 * Закрывает org.hibernate.Session переданую в качестве параметра
	 * @param session
	 */
	def closeSession(session: Session) {
		if (session != null && session.isOpen) {
			session.close()
		}
	}

	/**
	 * Метод подключается к базе данных и закрывает ее
	 */
	def initDBConnection() {
		new Thread(new Runnable {
			def run(): Unit = DB.createEntityManager().close()
		}).start()
	}

	object H2 {

		import scala.slick.driver.H2Driver.{simple => DatabaseH2}

		val pathToDB = "./CeemRadarData/database/h2/mapRadar"
		val options = Array("MVCC=true", "DB_CLOSE_ON_EXIT=FALSE")
		val driver = "org.h2.Driver"
		val user = "ceem"
		val password = "db_ceem"

		def option = options.mkString(";")

		def url = s"jdbc:h2:$pathToDB;$option"

		def urlForMemory = s"jdbc:h2:mem;$option"

		lazy val db = DatabaseH2.Database.forURL(url = url, user = user, password = password, driver = driver)

		lazy val db_memory = DatabaseH2.Database.forURL(url = urlForMemory, user = user, password = password, driver = driver)
	}

	def database = H2.db

	def database_memory = H2.db_memory
}

object Test extends App {

//	DB.database_memory withSession {
//		implicit session =>
//			GeoNames += GeoName(1,"test", "test2", "test3,test4",7234.90, 7294.94, "F", "FF", "UA", null)
//	}

}
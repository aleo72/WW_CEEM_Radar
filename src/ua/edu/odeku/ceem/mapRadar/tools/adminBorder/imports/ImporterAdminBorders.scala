/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports

import java.io.File

import ua.edu.odeku.ceem.mapRadar.db.DB
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.models._
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import ua.edu.odeku.ceem.mapRadar.utils.thread.StopProcess

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.parsing.json.JSON
//import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.{Admin1, Admin0, AdminBorder}
import scala.collection.mutable

/**
 * Объект который импортирует границы стран
 *
 * Created by Aleo on 23.03.14.
 */
object ImporterAdminBorders {

	private var countryFile: File = null
	private var provincesFile: File = null
	private var stopFlag: StopProcess = null

	val COORDINATES_JSON = "json_4326"


	def apply(countryFile: File, provincesFile: File, stopFlag: StopProcess): Boolean = {
		this.countryFile = countryFile
		this.provincesFile = provincesFile
		this.stopFlag = stopFlag
		try {
			importFromFile()
			true
		} catch {
			case ex: Throwable =>
				UserMessage.error(null, ex.getMessage)
				ex.printStackTrace()
				false
		}
	}


	private def importFromFile() {
		val sourceCountry = Source.fromFile(countryFile).getLines()
		val sourceProvinces: Iterator[String] = Source.fromFile(provincesFile).getLines()

		val headerCountry = sourceCountry.next() // Получим первую строку
		val headerMapCountry: Map[String, Int] = headerToMap(headerCountry) // Создаем Map заголовка

		val headerProvinces = sourceProvinces.next()
		val headerMapProvinces: Map[String, Int] = headerToMap(headerProvinces)
		val provincesMap = provincesToMapIsoString(sourceProvinces, headerMapProvinces)

    CountryBorders.clear
    ProvinceBorders.clear
    Polygons.clear

    DB.database withSession { implicit session =>

    }

		for (line <- sourceCountry if !stopFlag.stopProcess) {
			val array = rowToArray(line) // Преобразовали в массив
			handlerMapUnitRow(headerMapCountry, array, headerMapProvinces, provincesMap)
		}
		println("end")
	}

	private def provincesToMapIsoString(provinces: Iterator[String], headerMapProvinces: Map[String, Int]): mutable.HashMap[String, ArrayBuffer[Array[String]]] = {
		val map = new scala.collection.mutable.HashMap[String, ArrayBuffer[Array[String]]]
		for (line <- provinces) {
			val array = rowToArray(line)
			val iso = array(headerMapProvinces("iso"))
			var listProvinces = map.getOrElseUpdate(iso, new ArrayBuffer[Array[String]]())
      println(s"$iso -> ${array.mkString(";")}")
			listProvinces += array
		}
		map
	}

  def createCountryBorderPolygons(border: CountryBorder, listJsonCoordinates: List[List[List[List[Double]]]]): List[Polygon] = {
    for(p <- listJsonCoordinates) yield Polygon(None, PolygonJSON(p).toString, Some(border.id.get), None)
  }

  def createProvinceBorders(countryBorder: CountryBorder, header: Map[String, Int], option: Option[ArrayBuffer[Array[String]]]): Unit = {
    val arraySource = option.getOrElse( new ArrayBuffer[Array[String]]() )

    for (array <- arraySource) {
      val name0 = array(header("name_0"))
      val name1 = array(header("name_1"))
      val iso = array(header("iso"))

      val jsonString = array(header(COORDINATES_JSON)).replace("\"\"", "\"")
      val json: Map[String, Any] = JSON.parseFull(jsonString).get.asInstanceOf[Map[String, Any]]

      val typePolygon: String = json("type").asInstanceOf[String]
      val coordinates: List[List[List[List[Double]]]] = typePolygon match {
        case "Polygon" => List(json("coordinates").asInstanceOf[List[List[List[Double]]]])
        case "MultiPolygon" => json("coordinates").asInstanceOf[List[List[List[List[Double]]]]]
      }

      val province = ProvinceBorders <= ProvinceBorder(None, name0, name1, iso, countryBorder.id.get)

      val provinceBorderPolygons: List[Polygon] = for(p <- coordinates) yield Polygon(None,PolygonJSON(p).toString, None, Some(province.id.get))

      Polygons ++= provinceBorderPolygons
    }
  }

  /**
	 * Обработка строки данных и сохранение их в файл
	 * @param header информация о данных
	 * @param array сами данные
	 */
	private def handlerMapUnitRow(header: Map[String, Int], array: Array[String], headerProvinces: Map[String, Int], provincesMap: mutable.HashMap[String, ArrayBuffer[Array[String]]]) {
		val name = array(header("name"))
		val admin = array(header("admin"))
		val admin0a3 = array(header("adm0_a3"))

		val jsonString = array(header(COORDINATES_JSON)).replace("\"\"", "\"")

		val json: Map[String, Any] = JSON.parseFull(jsonString).get.asInstanceOf[Map[String, Any]]

		val typePolygon: String = json("type").asInstanceOf[String]
		val coordinates: List[List[List[List[Double]]]] = typePolygon match {
			case "Polygon" => List(json("coordinates").asInstanceOf[List[List[List[Double]]]])
			case "MultiPolygon" => json("coordinates").asInstanceOf[List[List[List[List[Double]]]]]
		}

    val countryBorder = CountryBorders <= CountryBorder(None, name, admin, admin0a3)

    Polygons ++= createCountryBorderPolygons(countryBorder, coordinates)

    createProvinceBorders(countryBorder, headerProvinces, provincesMap.get(admin0a3))
	}


	/**
	 * Преобразует строчку в ассоциативный массив
	 * @param header строка с заголовком таблицы
	 * @return Map[String, Int]
	 */
	def headerToMap(header: String): Map[String, Int] = {
		val res = header.split(",")
		val z = res.zip(0 until res.length)
		z.toMap
	}

	/**
	 * Конверктация строки таблицы в массив
	 * @param line строка таблицы
	 * @return массив значений из стоки
	 */
	def rowToArray(line: String): Array[String] = {
		val buffer = new ArrayBuffer[String]()

		var exitLoop = false


		var indexStart = 0
		var index = 0
		while (!exitLoop) {
			index = line.indexOf(',', Math.max(indexStart, index))

			var _index = index
			var _indexStart = indexStart

			if (index > 0) {
				val subStr = line.substring(indexStart, index)

				val findString = subStr.startsWith("\"") // Проверка это строка?

				if (!findString) {
					// Это не строка
					buffer += subStr
					indexStart = index + 1
				} else {
					// строка, необходимо найти конец строки
					if (subStr.endsWith("\"") && !subStr.endsWith("\"\"")) {
						// строка должна заканчиваться на кавычки, но не не две подряд
						buffer += subStr.substring(1, subStr.length - 1) // отрубаем кавычки в начале и в конце
						indexStart = index + 1
					} else {
						index += 1
					}
				}

			}
			if (index == -1) {
				exitLoop = true
			}
			_index = index
			_indexStart = indexStart
		}
		if (indexStart > 0) {
			val subStr = line.substring(indexStart)
			buffer += subStr
		}
		buffer.toArray
	}
}

/**
 * Класс для сериализации Polygon
 *
 * Created by Aleo on 23.03.2014.
 */
case class PolygonJSON(listCoordinates: Array[(Double, Double)]) {
  override def toString = {
    (for(coordinates <- listCoordinates) yield s"${coordinates._1}|${coordinates._2}").mkString(" ")
  }
}

object PolygonJSON {
  def apply(listCoordinates: List[List[List[Double]]]): PolygonJSON = {
    val buffer = new ArrayBuffer[(Double, Double)]
    for (list <- listCoordinates.head) {
      val latLon: (Double, Double) = (list(1), list(0))
      buffer += latLon
    }
    new PolygonJSON(buffer.toArray)
  }
}
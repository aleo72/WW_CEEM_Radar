/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports

import java.io.{FileOutputStream, ObjectOutputStream, File}
import ua.edu.odeku.ceem.mapRadar.utils.thread.StopProcess
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.util.parsing.json.{JSON, JSONObject}
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.{Admin0, AdminBorder}
import scala.collection.mutable

/**
 * Объект который импортирует границы стран
 *
 * Created by Aleo on 23.03.14.
 */
object ImportAdmin0Countries {

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

		for (line <- sourceCountry if !stopFlag.stopProcess) {
			val array = rowToArray(line) // Преобразовали в массив
			handlerMapUnitRow(headerMapCountry, array)
		}
		println("end")
	}

	private def provincesToMapIsoString(provinces: Iterator[String], headerMapProvinces: Map[String, Int]): Map[String, List[Array[String]]] = {
		val map = new scala.collection.mutable.HashMap[String, List[String]]
		for(line <- provinces) {
			val array = rowToArray(line)
			val iso = array(headerMapProvinces("iso"))
			var listProvinces = map.getOrElse(iso, List(Array[String]()))
			listProvinces = listProvinces +=
		}

	}

	/**
	 * Обработка строки данных и сохранение их в файл
	 * @param header информация о данных
	 * @param array сами данные
	 */
	private def handlerMapUnitRow(header: Map[String, Int], array: Array[String]) {
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

		saveToFile(name, admin, admin0a3, coordinates)
	}

	/**
	 * Данный метод долден сохранять в файл данные
	 * Перед сохранением он должен развернуть координаты
	 * @param name название региона который мы сохраняем
	 * @param coordinates список координат "List[List[List[List[Double]..."
	 *                    List[Double] - пара координат
	 *                    List[List[Double]... - список пар координат
	 *                    List[List[List[Double]... - Данный список как Polygon
	 *                    List[List[List[List[Double]...

	 */
	private def saveToFile(name: String, admin: String, admin0a3: String, coordinates: List[List[List[List[Double]]]]) {
		val admin0 = Admin0(name, admin, admin0a3, coordinates)
		val dirString = AdminBorder.CEEM_RADAR_DATA_FOR_ADMIN_BORDER_0
		val dir = new File(dirString)

		if (!dir.exists()) {
			dir.mkdirs()
		}

		val file = new File(dirString + admin0a3 + ".admin0")

		if (file.exists()) {
			println("delete: " + file.getName)
			file.delete()
		}

		val out = new ObjectOutputStream(new FileOutputStream(file))
		out.writeObject(admin0)
		out.close()
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

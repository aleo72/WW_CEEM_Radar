/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports

import java.io.{FileOutputStream, ObjectOutputStream, File}
import ua.edu.odeku.ceem.mapRadar.utils.thread.StopProcess
import ua.edu.odeku.ceem.mapRadar.utils.gui.UserMessage
import scala.io.Source
import scala.util.parsing.json.JSON
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.AdminBorder
import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 24.03.2014
 * Time: 15:35
 */
object ImportStatesProvinces {

	private var file: File = null
	private var stopFlag: StopProcess = null

	val NAME0 = "name_0"
	val NAME1 = "name_1"
	val ISO = "iso"
	val COORDINATES_JSON = "json_4326"


	def apply(file: File, stopFlag: StopProcess): Boolean = {
		this.file = file
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
		val source = Source.fromFile(file).getLines()

		val header = source.next() // Получим первую строку
		val headerMap: Map[String, Int] = headerToMap(header) // Создаем Map заголовка

		for (line <- source if !stopFlag.stopProcess) {
			val array = rowToArray(line) // Преобразовали в массив
			handlerStatesProvincesRow(headerMap, array)
		}

	}

	/**
	 * Обработка строки данных и сохранение их в файл
	 * @param header информация о данных
	 * @param array сами данные
	 */
	private def handlerStatesProvincesRow(header: Map[String, Int], array: Array[String]) {
		val name0 = array(header(NAME0))
		val name1 = array(header(NAME1))
		val iso = array(header(ISO))
		val hasc1 = array(header("hasc_1"))
		val shapeArea = array(header("shape_area"))

		val jsonString = array(header(COORDINATES_JSON)).replace("\"\"", "\"")

		val json: Map[String, Any] = JSON.parseFull(jsonString).get.asInstanceOf[Map[String, Any]]

		val typePolygon: String = json("type").asInstanceOf[String]
		val coordinates: List[List[List[List[Double]]]] = typePolygon match {
			case "Polygon" => List(json("coordinates").asInstanceOf[List[List[List[Double]]]])
			case "MultiPolygon" => json("coordinates").asInstanceOf[List[List[List[List[Double]]]]]
		}

		saveToFile(name0, name1, iso, hasc1, shapeArea, coordinates)
	}

	/**
	 * Данный метод долден сохранять в файл данные
	 * Перед сохранением он должен развернуть координаты
	 * @param name0 название страны который мы сохраняем
	 * @param name1 название региона страны который мы сохраняем
	 * @param iso код страны которую мы сохраняем
	 * @param coordinates список координат "List[List[List[List[Double]..."
	 *                    List[Double] - пара координат
	 *                    List[List[Double]... - список пар координат
	 *                    List[List[List[Double]... - Данный список как Polygon
	 *                    List[List[List[List[Double]...

	 */
	private def saveToFile(name0: String, name1: String, iso: String, hasc1: String, shapeArea: String, coordinates: List[List[List[List[Double]]]]) {
		val admin1 = Admin1(name0, name1, iso, coordinates)
		//		val fileString = AdminBorder.CEEM_RADAR_DATA_FOR_ADMIN_BORDER_1 + "/" + iso + "/" + name1 + ".admin0"
		val dirString = AdminBorder.CEEM_RADAR_DATA_FOR_ADMIN_BORDER_1 + "/" + iso.trim + "/"
		val dir = new File(dirString)

		if (!dir.exists()) {
			dir.mkdirs()
		}

		val file = new File(dirString + shapeArea.trim.replace('.', '_') + "_" + hasc1.trim.replace('.', '_') + ".admin1")

		if (file.exists()) {
			println("delete: " + file.getName)
			file.delete()
		}

		val out = new ObjectOutputStream(new FileOutputStream(file))
		out.writeObject(admin1)
		out.close()
	}

}

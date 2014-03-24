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

	val NAME_SORT = "name_sort"
	val COORDINATES_JSON = "json_4326"


	def apply(file: File, stopFlag: StopProcess): Boolean = {
		this.file = file
		this.stopFlag = stopFlag
		try{
			importFromFile()
			true
		} catch {
			case ex: Throwable =>
				UserMessage.error(null, ex.getMessage)
				ex.printStackTrace()
				false
		}
	}


	private def importFromFile(){
		val source = Source.fromFile(file).getLines()

		val header = source.next() // Получим первую строку
		val headerMap: Map[String, Int] = headerToMap(header) // Создаем Map заголовка

		for(line <- source if !stopFlag.stopProcess){
			val array = rowToArray(line) // Преобразовали в массив
			handlerMapUnitRow(headerMap, array)
		}

	}

	/**
	 * Обработка строки данных и сохранение их в файл
	 * @param header информация о данных
	 * @param array сами данные
	 */
	private def handlerMapUnitRow(header: Map[String, Int], array: Array[String]) {
		val name = array(header(NAME_SORT))
		val geounit = array(header("geounit"))
		val jsonString = array(header(COORDINATES_JSON)).replace("\"\"", "\"")

		val json: Map[String, Any] = JSON.parseFull(jsonString).get.asInstanceOf[Map[String, Any]]

		val typePolygon: String = json("type").asInstanceOf[String]
		val coordinates: List[List[List[List[Double]]]] = typePolygon match {
			case "Polygon" =>	List(json("coordinates").asInstanceOf[List[List[List[Double]]]])
			case "MultiPolygon" => json("coordinates").asInstanceOf[List[List[List[List[Double]]]]]
		}

		saveToFile(name, geounit, coordinates)
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
	private def saveToFile(name:String, geoUnit:String, coordinates: List[List[List[List[Double]]]]){
		val admin0 = Admin0(name, geoUnit, coordinates)
		val fileString = AdminBorder.CEEM_RADAR_DATA_FOR_ADMIN_BORDER_0 + name + ".admin0"
		val file = new File(fileString)

		if(file.exists()){
			file.delete()
		}

		val out = new ObjectOutputStream(new FileOutputStream(file))
		out.writeObject(admin0)
		out.close()
	}

}

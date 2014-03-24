/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder

import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 24.03.2014
 * Time: 15:38
 */
package object imports {

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
		while(!exitLoop){
			index = line.indexOf(',', Math.max(indexStart, index))

			var _index = index
			var _indexStart = indexStart

			if(index > 0){
				val subStr = line.substring(indexStart, index)

				val findString = subStr.startsWith("\"") // Проверка это строка?

				if(!findString){ // Это не строка
					buffer += subStr
					indexStart = index + 1
				} else { // строка, необходимо найти конец строки
					if(subStr.endsWith("\"") && !subStr.endsWith("\"\"")){ // строка должна заканчиваться на кавычки, но не не две подряд
						buffer += subStr.substring(1, subStr.length - 1) // отрубаем кавычки в начале и в конце
						indexStart = index + 1
					} else {
						index += 1
					}
				}

			}
			if(index == -1){
				exitLoop = true
			}
			_index = index
			_indexStart = indexStart
		}
		if(indexStart > 0){
			val subStr = line.substring(indexStart)
			buffer += subStr
		}
		buffer.toArray
	}
}

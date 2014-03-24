/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.adminBorder.imports

import scala.collection.mutable.ArrayBuffer

/**
 * Класс для сириализации данных
 *
 * Created by Aleo on 23.03.2014.
 */
case class Admin0(name: String, admin: String, admin0a3: String, polygons: Array[Polygon]) extends Serializable

object Admin0 {
	def apply(name: String, admin: String, admin0a3: String, polygons: List[List[List[List[Double]]]]): Admin0 = {
		val bufferPolygon = new ArrayBuffer[Polygon]
		for (polygon <- polygons) {
			bufferPolygon += Polygon(polygon)
		}
		new Admin0(name, admin, admin0a3, bufferPolygon.toArray)
	}
}

/**
 * Класс для сериализации данных по провинциям
 * @param name0
 * @param name1
 * @param iso
 * @param polygons
 */
case class Admin1(name0: String, name1: String, iso: String, polygons: Array[Polygon]) extends Serializable

object Admin1 {
	def apply(name0: String, name1: String, iso: String, polygons: List[List[List[List[Double]]]]): Admin1 = {
		val bufferPolygon = new ArrayBuffer[Polygon]
		for (polygon <- polygons) {
			bufferPolygon += Polygon(polygon)
		}
		new Admin1(name0, name1, iso, bufferPolygon.toArray)
	}
}

/**
 * Класс для сериализации Polygon
 *
 * Created by Aleo on 23.03.2014.
 */
case class Polygon(listCoordinates: Array[(Double, Double)]) extends Serializable

object Polygon {
	def apply(listCoordinates: List[List[List[Double]]]): Polygon = {
		val buffer = new ArrayBuffer[(Double, Double)]
		for (list <- listCoordinates.head) {
			val latLon: (Double, Double) = (list(1), list(0))
			buffer += latLon
		}
		new Polygon(buffer.toArray)
	}
}

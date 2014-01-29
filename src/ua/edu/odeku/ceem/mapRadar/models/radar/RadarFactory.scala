/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

/**
 * Объект фабрика для создания объектов Радаров
 *
 * Created by Aleo on 29.01.14.
 */
object RadarFactory extends Enumeration {
	type RadarNameParameter = Radar

	val MRL_2 = new Radar()
}



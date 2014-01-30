/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypes._

/**
 * Объект фабрика для создания объектов Радаров
 *
 * Created by Aleo on 29.01.14.
 */
object RadarFactory {
	def apply(radarName: RadarType): Radar = {
		radarName match {
			case MRL_2 =>
				new Radar_MRL_2
			case MRL_5_KAN_I =>
				new Radar_MRL_5_KAN_I
			case MRL_5_KAN_II =>
				new Radar_MRL_5_KAN_II
			case WSR_74C =>
				new Radar_WSR_74C
			case WSR_74S =>
				new Radar_WSR_74S
			case SIEMENS_PLESSEY_45_C =>
				new Radar_SIEMENS_PLESSEY_45_C
			case MELODI =>
				new Radar_MELODI
			case RODIN =>
				new Radar_RODIN
			case WSR_88D_S =>
				new Radar_WSR_88D_S
			// TODO Дописать....
			case _ => throw new NumberFormatException
		}
	}
}



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
			case WSR_88D_C =>
				new Radar_WSR_88D_C
			case WSR_82D_X =>
				new Radar_WSR_82D_X
			case WSR_82D_C =>
				new Radar_WSR_82D_C
			case ERICSSON_RADAR_C =>
				new Radar_ERICSSON_RADAR_C
			case ERICSSON_RADAR_S =>
				new Radar_ERICSSON_RADAR_S
			case TDWR =>
				new Radar_TDWR
			case MITSUBISHI =>
				new Radar_MITSUBISHI
			case COST_73 =>
				new Radar_COST_73
			case METEOR_500_C =>
				new Radar_METEOR_500_C
			case DWSR_8500_S =>
				new Radar_DWSR_8500_S
			case DWSR_2500_С =>
				new Radar_DWSR_2500_С
			case DWSR_2500_С_К =>
				new Radar_DWSR_2500_С_К
			case _ => throw new IllegalArgumentException
		}
	}
}



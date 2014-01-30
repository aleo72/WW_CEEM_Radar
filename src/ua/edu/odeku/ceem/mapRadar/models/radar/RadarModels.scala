/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models.radar
import RadarTypeParameters._
import scala.collection.JavaConverters._

/**
 *
 * Created by Aleo on 30.01.14.
 */
class Radar_MRL_2 extends Radar(RadarTypes.MRL_2, 'X'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(215.0),
		MINIMUM_RECEPTION_POWER     -> Array(-132.0),
		PULSE_REPETITION_FREQUENCY  -> Array(300.0),
		DURATION_PROBING_PULSE      -> Array(1.0, 2.0),
		WORKING_RADIUS              -> Array(300.0),
		ANTENNA_DIAMETER            -> Array(3.0),
		BEAM_WIDTH_AT_3_DB          -> Array(0.8)
	)
}

class Radar_MRL_5_KAN_I extends Radar(RadarTypes.MRL_5_KAN_I, 'X'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER     -> Array(-137.0),
		PULSE_REPETITION_FREQUENCY  -> Array(250.0),
		DURATION_PROBING_PULSE      -> Array(1.0, 2.0),
		WORKING_RADIUS              -> Array(300.0),
		ANTENNA_DIAMETER            -> Array(4.5),
		BEAM_WIDTH_AT_3_DB          -> Array(0.45)
	)
}

class Radar_MRL_5_KAN_II extends Radar(RadarTypes.MRL_5_KAN_II, 'S'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(800.0),
		MINIMUM_RECEPTION_POWER     -> Array(-140.0),
		PULSE_REPETITION_FREQUENCY  -> Array(250.0),
		DURATION_PROBING_PULSE      -> Array(1.0, 2.0),
		WORKING_RADIUS              -> Array(300.0),
		ANTENNA_DIAMETER            -> Array(4.5),
		BEAM_WIDTH_AT_3_DB          -> Array(1.5)
	)
}

class Radar_WSR_74C extends Radar(RadarTypes.WSR_74C, 'C'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER     -> Array(-134.0),
		PULSE_REPETITION_FREQUENCY  -> Array(256.0),
		DURATION_PROBING_PULSE      -> Array(3.0),
		WORKING_RADIUS              -> Array(400.0),
		ANTENNA_DIAMETER            -> Array(2.7),
		BEAM_WIDTH_AT_3_DB          -> Array(1.5)
	)
}

class Radar_WSR_74S extends Radar(RadarTypes.WSR_74S, 'C'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(500.0),
		MINIMUM_RECEPTION_POWER     -> Array(-134.0),
		PULSE_REPETITION_FREQUENCY  -> Array(162.0),
		DURATION_PROBING_PULSE      -> Array(1.0, 4.0),
		WORKING_RADIUS              -> Array(400.0),
		ANTENNA_DIAMETER            -> Array(3.6),
		BEAM_WIDTH_AT_3_DB          -> Array(2.0)
	)
}

class Radar_SIEMENS_PLESSEY_45_C extends Radar(RadarTypes.SIEMENS_PLESSEY_45_C, 'C'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER     -> Array(-138.0),
		PULSE_REPETITION_FREQUENCY  -> Array(300.0),
		DURATION_PROBING_PULSE      -> Array(2.0),
		WORKING_RADIUS              -> Array(256.0),
		ANTENNA_DIAMETER            -> Array(3.7),
		BEAM_WIDTH_AT_3_DB          -> Array(2.0)
	)
}

class Radar_MELODI extends Radar(RadarTypes.MELODI, 'S'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(700.0),
		MINIMUM_RECEPTION_POWER     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY  -> Array(250.0),
		DURATION_PROBING_PULSE      -> Array(2.0),
		WORKING_RADIUS              -> Array(300.0),
		ANTENNA_DIAMETER            -> Array(4.0),
		BEAM_WIDTH_AT_3_DB          -> Array(1.8)
	)
}

class Radar_RODIN extends Radar(RadarTypes.RODIN, 'C'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER     -> Array(-142.0),
		PULSE_REPETITION_FREQUENCY  -> Array(330.0),
		DURATION_PROBING_PULSE      -> Array(2.0),
		WORKING_RADIUS              -> Array(300.0),
		ANTENNA_DIAMETER            -> Array(3.0),
		BEAM_WIDTH_AT_3_DB          -> Array(1.3)
	)
}

class Radar_WSR_88D_S extends Radar(RadarTypes.WSR_88D_S, 'S'){

	override val radarParameters: Map[RadarTypeParameter, Array[Double]] = Map(
		PULSE_POWER                                 -> Array(550.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> 600.0.to(934, 1).toArray,
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> 0.8.to(0.9, 0.01).toArray,
		WORKING_RADIUS_MODE_INTENSITY               -> Array(450.0),
		WORKING_RADIUS_MODE_SPEED                   -> 120.0.to(230, 1).toArray,
		ANTENNA_DIAMETER                            -> 3.7.to(8.2, 0.5).toArray,
		BEAM_WIDTH_AT_3_DB                          -> 2.1.to(1.0, -0.1).toArray,
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> Array(120.0, 230.0),
		Z_MIN                                       -> Array(-8.0)
	)
}
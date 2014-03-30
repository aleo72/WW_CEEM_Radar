/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models.radar
import RadarTypeParameters._
import scala.collection.JavaConverters._
import scala.collection.mutable

/**
 *
 * Created by Aleo on 30.01.14.
 */
class Radar_MRL_2 extends Radar(RadarTypes.MRL_2, 'X'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
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

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(550.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> rangeDouble(600, 934),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> rangeDouble(0.8, 0.9, 0.01),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(450.0),
		WORKING_RADIUS_MODE_SPEED                   -> rangeDouble(120, 230),
		ANTENNA_DIAMETER                            -> rangeDouble(3.7, 8.2, 0.1),
		BEAM_WIDTH_AT_3_DB                          -> rangeDouble(2.1, 1.0, -0.1),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(120, 230),
		Z_MIN                                       -> Array(-8.0)
	)
}

class Radar_WSR_88D_C extends Radar(RadarTypes.WSR_88D_C, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> rangeDouble(700, 1200),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> rangeDouble(0.8, 0.9, 0.01),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(450.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> rangeDouble(1.8, 6.1, 0.1),
		BEAM_WIDTH_AT_3_DB                          -> rangeDouble(2.1, 0.7, -0.1),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-32, 32)
	)
}

class Radar_WSR_82D_X extends Radar(RadarTypes.WSR_82D_X, 'X'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(180.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> rangeDouble(1500, 2000, 10),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> rangeDouble(0.25, 0.5, 0.05),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(256.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(60.0),
		ANTENNA_DIAMETER                            -> rangeDouble(0.9, 2.4, 0.1),
		BEAM_WIDTH_AT_3_DB                          -> rangeDouble(2.5, 1.0, -0.1),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-32, 32)
	)
}

class Radar_WSR_82D_C extends Radar(RadarTypes.WSR_82D_C, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-138.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(1200.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.5),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(450.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(125.0),
		ANTENNA_DIAMETER                            -> Array(4.3),
		BEAM_WIDTH_AT_3_DB                          -> Array(1.0),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48)
	)
}

class Radar_ERICSSON_RADAR_C extends Radar(RadarTypes.ERICSSON_RADAR_C, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(900.0, 1200.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.5),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(300.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(125.0),
		ANTENNA_DIAMETER                            -> Array(4.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(0.8),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48)
	)
}

class Radar_ERICSSON_RADAR_S extends Radar(RadarTypes.ERICSSON_RADAR_S, 'S'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(900.0, 1200.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.5),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(480.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> Array(4.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(1.6),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48)
	)
}

class Radar_TDWR extends Radar(RadarTypes.TDWR, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(1000.0, 1900.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> rangeDouble(0.8, 0.9, 0.01),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(480.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> Array(6.0),
		BEAM_WIDTH_AT_3_DB                          -> Array(0.55),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-40, 40)
	)
}

class Radar_MITSUBISHI extends Radar(RadarTypes.MITSUBISHI, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-136.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(280.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(1120.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.52),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(400.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(128.0),
		ANTENNA_DIAMETER                            -> Array(3.0),
		BEAM_WIDTH_AT_3_DB                          -> rangeDouble(1.4, 1.5, 0.01),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-16, 16)
	)
}

class Radar_COST_73 extends Radar(RadarTypes.COST_73, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-144.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(900.0, 1200),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.5),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> Array(4.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(0.9),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48),
		RECEIVER_DYNAMIC_RANGE                      -> rangeDouble(85, 100),
		FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN  -> Array(-27.0)
	)
}

class Radar_METEOR_500_C extends Radar(RadarTypes.METEOR_500_C, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-138.0, -142.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(1200.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.83),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(500.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(125.0),
		ANTENNA_DIAMETER                            -> Array(4.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(1.0),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48),
		RECEIVER_DYNAMIC_RANGE                      -> Array(80.0),
		FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN  -> Array(-28.0, -30, 0.2)
	)
}

class Radar_DWSR_8500_S extends Radar(RadarTypes.DWSR_8500_S, 'S'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(850.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-143.0, -140.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(623.0, 700.0, 934.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.8),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(480.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> Array(8.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(1.0),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-75, 75, 5),
		RECEIVER_DYNAMIC_RANGE                      -> Array(90.0),
		FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN  -> Array(-25.0, 0, 0.2)
	)
}

class Radar_DWSR_2500_С extends Radar(RadarTypes.DWSR_2500_С, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-143.0, -140.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(786.0, 885.0, 1180.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.8),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(480.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> Array(4.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(1.0),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48, 2),
		RECEIVER_DYNAMIC_RANGE                      -> Array(90.0),
		FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN  -> Array(-25.0, 0, 0.2)
	)
}
class Radar_DWSR_2500_С_К extends Radar(RadarTypes.DWSR_2500_С_К, 'C'){

	override val radarParameters: mutable.LinkedHashMap[RadarTypeParameter, Array[Double]] = mutable.LinkedHashMap(
		PULSE_POWER                                 -> Array(250.0),
		MINIMUM_RECEPTION_POWER                     -> Array(-143.0, -140.0),
		PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   -> Array(250.0),
		PULSE_REPETITION_FREQUENCY_MODE_SPEED       -> Array(786.0, 885.0, 1180.0),
		DURATION_PROBING_PULSE_MODE_INTENSITY       -> Array(2.0),
		DURATION_PROBING_PULSE_MODE_SPEED           -> Array(0.8),
		WORKING_RADIUS_MODE_INTENSITY               -> Array(480.0),
		WORKING_RADIUS_MODE_SPEED                   -> Array(120.0),
		ANTENNA_DIAMETER                            -> Array(4.2),
		BEAM_WIDTH_AT_3_DB                          -> Array(1.0),
		UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    -> rangeDouble(-48, 48, 2),
		RECEIVER_DYNAMIC_RANGE                      -> Array(90.0),
		FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN  -> Array(-25.0, 0, 0.2)
	)
}
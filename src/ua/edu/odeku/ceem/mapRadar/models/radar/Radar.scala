/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

import java.util.ResourceBundle
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypeParameters.RadarTypeParameter
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypes.RadarType
import scala.collection.mutable
import gov.nasa.worldwind.geom.LatLon

/**
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 13:41
 */
abstract class Radar(val radarName: RadarType, val FREQUENCY_BAND: Char) {

	val radarParameters : mutable.LinkedHashMap[RadarTypeParameter, Array[Double]]
	lazy val setRadarParameters : mutable.HashMap[RadarTypeParameter, Double] = initParameter()
	var _altitude: Int = 10
	var latLon: LatLon = null

	/**
	 * Метод возращает радус радара
	 * По умолчанию вернет значения RadarTypeParameter.WORKING_RADIUS
	 * или RadarTypeParameter.WORKING_RADIUS_MODE_THE_INTENSITY_OF_SPEED
	 * @return
	 */
	def radius: Double = {
		// TODO WORKING_RADIUS_MODE_INTENSITY и WORKING_RADIUS_MODE_SPEED
		val radiusInKm = if(setRadarParameters.keySet.exists(RadarTypeParameters.WORKING_RADIUS == _)) {
			setRadarParameters(RadarTypeParameters.WORKING_RADIUS)
		} else if(setRadarParameters.keySet.exists(RadarTypeParameters.WORKING_RADIUS_MODE_INTENSITY == _)){
			setRadarParameters(RadarTypeParameters.WORKING_RADIUS_MODE_INTENSITY)
		} else if(setRadarParameters.keySet.exists(RadarTypeParameters.WORKING_RADIUS_MODE_SPEED == _)){
			setRadarParameters(RadarTypeParameters.WORKING_RADIUS_MODE_SPEED)
		} else {
			throw new NumberFormatException
		}
		radiusInKm * 1000
	}
	def initParameter(): mutable.HashMap[RadarTypeParameter, Double] = {
		var map = new mutable.HashMap[RadarTypeParameter, Double]()
		for(parm <- radarParameters){
			map += (parm._1-> parm._2.head)
		}
		map
	}

	def altitude = _altitude

	def altitude_=(value: Int) : Unit = {
		if(value >= 0)
			_altitude = value
		else
			throw new IllegalArgumentException
	}
}

object RadarTypeParameters extends Enumeration {

	case class RadarTypeParameter(value: String) extends Val(value) {
		val descriptions: String = loadRadarTypeParameter(value)

		implicit def valueToRadarTypeParameter(value: Value) = value.asInstanceOf[RadarTypeParameter]

		private def loadRadarTypeParameter(name: String): String = {
			ResourceBundle.getBundle("RadarTypeParameter").getString(name)
		}
	}

	val FREQUENCY_BAND                              = RadarTypeParameter("FREQUENCY_BAND")
	val PULSE_POWER                                 = RadarTypeParameter("PULSE_POWER")
	val MINIMUM_RECEPTION_POWER                     = RadarTypeParameter("MINIMUM_RECEPTION_POWER")
	val PULSE_REPETITION_FREQUENCY                  = RadarTypeParameter("PULSE_REPETITION_FREQUENCY")
	val DURATION_PROBING_PULSE                      = RadarTypeParameter("DURATION_PROBING_PULSE")
	val WORKING_RADIUS                              = RadarTypeParameter("WORKING_RADIUS")
	val ANTENNA_DIAMETER                            = RadarTypeParameter("ANTENNA_DIAMETER")
	val BEAM_WIDTH_AT_3_DB                          = RadarTypeParameter("BEAM_WIDTH_AT_3_DB")
	val PULSE_REPETITION_FREQUENCY_MODE_INTENSITY   = RadarTypeParameter("PULSE_REPETITION_FREQUENCY_MODE_INTENSITY")
	val PULSE_REPETITION_FREQUENCY_MODE_SPEED       = RadarTypeParameter("PULSE_REPETITION_FREQUENCY_MODE_SPEED")
	val DURATION_PROBING_PULSE_MODE_INTENSITY       = RadarTypeParameter("DURATION_PROBING_PULSE_MODE_INTENSITY")
	val DURATION_PROBING_PULSE_MODE_SPEED           = RadarTypeParameter("DURATION_PROBING_PULSE_MODE_SPEED")
	val WORKING_RADIUS_MODE_SPEED                   = RadarTypeParameter("WORKING_RADIUS_MODE_SPEED")
	val WORKING_RADIUS_MODE_INTENSITY               = RadarTypeParameter("WORKING_RADIUS_MODE_INTENSITY")
	val BEAM_WIDTH_AT_HALF_POWER                    = RadarTypeParameter("BEAM_WIDTH_AT_HALF_POWER")
	val UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED    = RadarTypeParameter("UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED")
	val Z_MIN                                       = RadarTypeParameter("Z_MIN")
	val RECEIVER_DYNAMIC_RANGE                      = RadarTypeParameter("RECEIVER_DYNAMIC_RANGE")
	val FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN  = RadarTypeParameter("FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN")
}

object RadarTypes extends Enumeration {

	case class RadarType(name:String) extends Val(name)

	val MRL_2                   = RadarType("МРЛ-2")
	val MRL_5_KAN_I             = RadarType("МРЛ-5 Кан.I")
	val MRL_5_KAN_II            = RadarType("МРЛ-5 Кан.II")
	val WSR_74C                 = RadarType("WSR-74C")
	val WSR_74S                 = RadarType("WSR-74S")
	val SIEMENS_PLESSEY_45_C    = RadarType("Siemens Plessey 45 C")
	val MELODI                  = RadarType("Melodi")
	val RODIN                   = RadarType("Rodin")
	val WSR_88D_S               = RadarType("WSR-88D (EEC. USA) S")
	val WSR_88D_C               = RadarType("WSR-88D (EEC. USA) C")
	val WSR_82D_X               = RadarType("WSR-82D (EEC, USA) X")
	val WSR_82D_C               = RadarType("WSR-82D (EEC, USA) C")
	val ERICSSON_RADAR_C        = RadarType("Ericsson Radar (Electronics AB, Sweden) C")
	val ERICSSON_RADAR_S        = RadarType("Ericsson Radar (Electronics AB, Sweden) S")
	val TDWR                    = RadarType("TDWR  (EEC, USA)")
	val MITSUBISHI              = RadarType("Mitsubishi (Japan)")
	val COST_73                 = RadarType("COST-73 на ДМРЛ малої потужності (1989 р.)")
	val METEOR_500_C            = RadarType("Meteor 500 С (Gematronic,Germany)")
	val DWSR_8500_S             = RadarType("DWSR 8500 S (Enterprise Electronic Corporation, USA)")
	val DWSR_2500_С             = RadarType("DWSR 2500 С (Enterprise Electronic Corporation, USA)")
	val DWSR_2500_С_К           = RadarType("DWSR 2500 С/К (Enterprise Electronic Corporation, USA)")

	implicit def valueToRadarType(value: Value): RadarType = value.asInstanceOf[RadarType]
}
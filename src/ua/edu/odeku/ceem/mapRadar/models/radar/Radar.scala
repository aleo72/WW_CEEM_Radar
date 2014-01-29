/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

import java.util.ResourceBundle
import ua.edu.odeku.ceem.mapRadar.models.radar.RadarTypeParameter.RadarTypeParameter

/**
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 13:41
 *
 * @param transmitterPower мощность передатчика;
 * @param antennaGain коэффициент направленного действия антенны;
 * @param effectiveArea эффективная площадь антенны
 * @param scatteringCrossSection эффективная площадь рассеяния цели
 * @param minimumReceiverSensitivity минимальная чувствительность приёмника.
 * @param altitude высота над землей
 */
class Radar(var transmitterPower: Double,
            var antennaGain: Double,
            var effectiveArea: Double,
            var scatteringCrossSection: Double,
            var minimumReceiverSensitivity: Double,
            var altitude: Int) {

	def this() {
		this(1.0, 1.0, 1.0, 1.0, 1.0, 1)
	}

	/**
	 * <p>D max = sqrt(sqrt( Pt * Gt * Ar * q / Pr.min * (4П)^2^ ))</p>
	 *
	 * <a href="http://ru.wikipedia.org/wiki/%D0%9E%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D0%BE%D0%B5_%D1%83%D1%80%D0%B0%D0%B2%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5_%D1%80%D0%B0%D0%B4%D0%B8%D0%BE%D0%BB%D0%BE%D0%BA%D0%B0%D1%86%D0%B8%D0%B8">wiki</a>
	 * @return Дальность действия радиолокатора с пассивным ответом
	 */
	def coverage: Double = {
		math.pow(
			(transmitterPower * antennaGain * effectiveArea * scatteringCrossSection)
				/ ((4 * math.Pi) * (4 * math.Pi) * minimumReceiverSensitivity)
			, 0.25
		) / 2
	}

	/**
	 * Метод обновляет все переменные
	 * @param transmitterPower мощность передатчика;
	 * @param antennaGain коэффициент направленного действия антенны;
	 * @param effectiveArea эффективная площадь антенны
	 * @param scatteringCrossSection эффективная площадь рассеяния цели
	 * @param minimumReceiverSensitivity минимальная чувствительность приёмника.
	 * @param altitude высота над землей
	 */
	def update(transmitterPower: Double,
	           antennaGain: Double,
	           effectiveArea: Double,
	           scatteringCrossSection: Double,
	           minimumReceiverSensitivity: Double,
	           altitude: Int): Radar = {
		this.transmitterPower = transmitterPower
		this.antennaGain = antennaGain
		this.effectiveArea = effectiveArea
		this.scatteringCrossSection = scatteringCrossSection
		this.minimumReceiverSensitivity = minimumReceiverSensitivity
		this.altitude = altitude

		this
	}

	def isValid = coverage > 0
}

class RadarParameter[T] private (val radarTypeParameter: RadarTypeParameter, private val _values: Array[T]) {

	/**
	 * Вернет кодовое название параметра радара
	 * @return название параметра
	 */
	def name = radarTypeParameter._1

	/**
	 * Вернет описание параметра радара
	 * @return описание параметра
	 */
	def description = radarTypeParameter._2

	/**
	 * Вернет возможные значения данного параметра
	 * @return возможные значения данного параметра
	 */
	def values = _values.clone()
}

object RadarTypeParameter extends Enumeration {
	type RadarTypeParameter = (String, String)

	val FREQUENCY_BAND = loadRadarTypeParameter("FREQUENCY_BAND")
	val	PULSE_POWER = loadRadarTypeParameter("PULSE_POWER")
	val MINIMUM_RECEPTION_POWER = loadRadarTypeParameter("MINIMUM_RECEPTION_POWER")
	val PULSE_REPETITION_FREQUENCY = loadRadarTypeParameter("PULSE_REPETITION_FREQUENCY")
	val DURATION_PROBING_PULSE = loadRadarTypeParameter("DURATION_PROBING_PULSE")
	val WORKING_RADIUS = loadRadarTypeParameter("WORKING_RADIUS")
	val ANTENNA_DIAMETER = loadRadarTypeParameter("ANTENNA_DIAMETER")
	val BEAM_WIDTH_AT_3_DB = loadRadarTypeParameter("BEAM_WIDTH_AT_3_DB")
	val PULSE_REPETITION_FREQUENCY_MODE_THE_INTENSITY_OF_SPEED = loadRadarTypeParameter("PULSE_REPETITION_FREQUENCY_MODE_THE_INTENSITY_OF_SPEED")
	val DURATION_PROBING_PULSE_MODE_THE_INTENSITY_OF_SPEED = loadRadarTypeParameter("DURATION_PROBING_PULSE_MODE_THE_INTENSITY_OF_SPEED")
	val WORKING_RADIUS_MODE_THE_INTENSITY_OF_SPEED = loadRadarTypeParameter("WORKING_RADIUS_MODE_THE_INTENSITY_OF_SPEED")
	val BEAM_WIDTH_AT_HALF_POWER = loadRadarTypeParameter("BEAM_WIDTH_AT_HALF_POWER")
	val UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED = loadRadarTypeParameter("UNIQUELY_DETERMINED_BY_THE_DOPPLER_SPEED")
	val Z_MIN = loadRadarTypeParameter("Z_MIN")
	val RECEIVER_DYNAMIC_RANGE = loadRadarTypeParameter("RECEIVER_DYNAMIC_RANGE")
	val FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN = loadRadarTypeParameter("FIRST_SIDE_LOBE_LEVEL_RELATIVE_TO_THE_MAIN")

	private def loadRadarTypeParameter(name: String) : (String, String) = {
		(name, ResourceBundle.getBundle("RadarTypeParameter").getString(name))
	}
}
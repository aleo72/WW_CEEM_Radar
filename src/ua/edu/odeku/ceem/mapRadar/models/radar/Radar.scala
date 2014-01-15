/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

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

	/**
	 * <p>D max = sqrt(sqrt( Pt * Gt * Ar * q / Pr.min * (4П)^2^ ))</p>
	 *
	 * <a href="http://ru.wikipedia.org/wiki/%D0%9E%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D0%BE%D0%B5_%D1%83%D1%80%D0%B0%D0%B2%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5_%D1%80%D0%B0%D0%B4%D0%B8%D0%BE%D0%BB%D0%BE%D0%BA%D0%B0%D1%86%D0%B8%D0%B8">wiki</a>
	 * @return Дальность действия радиолокатора с пассивным ответом
	 */
	def coverage = {
		math.pow(
			(transmitterPower * antennaGain * effectiveArea * scatteringCrossSection)
				/ ((4 * math.Pi) * (4 * math.Pi) * minimumReceiverSensitivity)
			, 0.25
		)
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
			   altitude: Int) {
		this.transmitterPower = transmitterPower
		this.antennaGain = antennaGain
		this.effectiveArea = effectiveArea
		this.scatteringCrossSection = scatteringCrossSection
		this.minimumReceiverSensitivity = this.minimumReceiverSensitivity
		this.altitude = altitude
	}

	def isValid = coverage > 0
}

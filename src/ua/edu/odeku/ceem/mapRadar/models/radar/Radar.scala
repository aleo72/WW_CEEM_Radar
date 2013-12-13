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
 */
class Radar( var transmitterPower : Double,
             var antennaGain : Double,
             var effectiveArea : Double,
             var scatteringCrossSection : Double,
             var minimumReceiverSensitivity : Double) {

  /**
   * @return Дальность действия радиолокатора с пассивным ответом
   */
  def coverage = {
    math.pow(
      (transmitterPower * antennaGain * effectiveArea * scatteringCrossSection)
        / ((4 * math.Pi) * (4 * math.Pi) * minimumReceiverSensitivity)
      ,0.25
    )
  }

}

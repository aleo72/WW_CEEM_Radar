/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.models

import gov.nasa.worldwind.geom.LatLon
import ua.edu.odeku.ceem.mapRadar.settings.Settings

/**
 * Модель данных которая моделирует радар
 *
 * Created by Aleo on 27.07.2014.
 * Конструктор со стандартной размерностью велечин
 * @param durationPulse - Длительность импульса в сек
 * @param wavelength - Длина волны в м
 * @param antennaDiameter - Диаметр антены
 * @param pulsePower - Мощность в Вт = 10 &#94; 6 Вт
 * @param reflectivityMeteoGoals - Отражаемость метеоцелей 1/м
 * @param attenuation - Затухание
 * @param radius - Радиус в м
 * @param altitude - Высота установки радара в м
 * @param gainFactor - Коэффициент усиления
 */
case class Radar(var durationPulse: Double,
                 var wavelength: Double,
                 var antennaDiameter: Double,
                 var pulsePower: Double,
                 var reflectivityMeteoGoals: Double,
                 var attenuation: Double,
                 var radius: Double,
                 var altitude: Double,
                 var latLon: LatLon = LatLon.ZERO,
                 private var _name: Option[String] = None,
                 var gainFactor: Double = GAIN_FACTOR_DEFAULT) {

  def name = _name.getOrElse(s"Radar ${Radar.radarCounter}")

  def name_=(value: String): Unit = _name = Some(value)

  def power(length: Double) = {
    pulsePower / length // TODO
  }

  def radiusOnElevation(elevation: Double) : Double = Radar.radiusOnElevation(this, elevation)
}

object Radar {
  val EMPTY_RADAR = Radar(0, 0, 0, 0, 0, 0, 0, 0)

  private def radarCounter: Long = Settings.Program.Tools.Radar.counterRadar

  /**
   * Метод высчитывает радус изолиний на нужной высоте
   *
   * @param radius - реальный радиус который имеет сфера
   * @param elevation - высота для которой высчитывается радиус отображения
   * @return - радиус плоскости на данной высоте сферы
   */
  def radiusOnElevation(radius: Double, elevation: Double) : Double = {
    Math.sqrt(radius * radius - elevation * elevation)
  }

  def radiusOnElevation(radar: Radar, elevation: Double) = radiusOnElevation(radar.radius, elevation)
}


/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.settings

import java.io.File
import java.util.Locale

import ua.com.serious_panda.settings.Property

/**
 * Обїект який маніпулює конфігурацієй
 *
 * Created by aleo on 02.08.14.
 */
object Settings {

  implicit val file = new File("CeemRadarDataConfigs/program.properties")
  val nameResourceBundle = "label_properties"

  object Program {

    val programNameProperty = new Property[String]("program.name", "World Wind Ceem Radar", Some("Назва програми"), nameResourceBundle, "program.name")
    programNameProperty.editable = false

    def name = programNameProperty.value

    val localeProperty = new Property[Locale]("program.locale", new Locale("ru", "RU"), Some("Локаль прогрими"), nameResourceBundle, "program.locale")

    def locale = localeProperty.value

    val debugProperty = new Property[Boolean]("program.debug", true, Some("Отладка програми"), nameResourceBundle, "program.debug")

    def debug = debugProperty.value

    object Start {
      val fileStartWindowProperty = new Property[String]("program.start.window", "resources/ww_start.png", Some("Зображеня яке показане при старте програми"), nameResourceBundle, "program.start.window")

      def fileStartWindow = fileStartWindowProperty.value
    }

    object Style {
      val lookAndFeelInfoProperty = new Property[String]("program.style.look_and_feel_info", "Nimbus", Some("Візуальний стиль"), nameResourceBundle, "program.style.look_and_feel_info")

      def lookAndFeelInfo = lookAndFeelInfoProperty.value
    }

    object Data {
      val ceemRadarDataProperty = new Property[String]("program.data.folder", "CeemRadarData/", Some("Папка де зберігається данні програми"), nameResourceBundle, "program.data.folder")
      ceemRadarDataProperty.editable = false

      def folder = ceemRadarDataProperty.value
    }

    object Config {
      val ceemRadarConfigProperty = new Property[String]("program.config.folder", "CeemRadarDataConfigs/", Some("Папка з конфігурацією"), nameResourceBundle, "program.config.folder")
      ceemRadarConfigProperty.editable = false

      def folder = ceemRadarConfigProperty.value
    }

    object Tools {

      object GeoNames {
        val translateGeoNameProperty = new Property[Boolean]("program.tools.geoNames.translateGeoName", false, Some("Транслировання GeoNames"), nameResourceBundle, "program.tools.geoNames.translateGeoName")

        def translateGeoName = translateGeoNameProperty.value
      }

      object Radar {
        private val prefix = "program.tools.radar"
        val maxAltitudeForRadarProperty = new Property[Long](s"$prefix.altitude.max", 100000L, Some("Максимальна висота установка радара"), nameResourceBundle, s"$prefix.altitude.max")

        def maxAltitude = maxAltitudeForRadarProperty.value

        val minAltitudeForRadarProperty = new Property[Long](s"$prefix.altitude.min", 0L, Some("Мінімальна висота установка радара"), nameResourceBundle, s"$prefix.altitude.min")

        def minAltitude = minAltitudeForRadarProperty.value

        val defaultAltitudeForRadarProperty = new Property[Long](s"$prefix.altitude.default", 10L, Some("Висота установка радара за замовчуванням"), nameResourceBundle, s"$prefix.altitude.default")

        def defaultAltitude = defaultAltitudeForRadarProperty.value

        val stepAltitudeForRadarProperty = new Property[Long](s"$prefix.altitude.step", 5L, Some("Крок зміни установки висоти радара"), nameResourceBundle, s"$prefix.altitude.step")

        def stepAltitude = stepAltitudeForRadarProperty.value

        val counterRadarProperty = new Property[Long](s"$prefix.counter", 1L, Some("Лічильник радарів"), nameResourceBundle, s"$prefix.counter")
        counterRadarProperty.editable = false

        def counterRadar = counterRadarProperty.inc

        val viewToRadarHeightProperty = new Property[Long](s"$prefix.viewToRadarHeight", 100000L, Some("Висота на яку переміщається "), nameResourceBundle, s"$prefix.viewToRadarHeight")

        def viewToRadarHeight = viewToRadarHeightProperty.value

        val pulsePowerProperty = new Property[Double](s"$prefix.pulsePower", Math.pow(10, 6) , Some("Сила умпульсу"), nameResourceBundle, s"$prefix.pulsePower")

        def pulsePower = pulsePowerProperty.value

        val waveLengthProperty = new Property[Double](s"$prefix.waveLength", 0.8 / 100, Some("Довжина хвилі"), nameResourceBundle, s"$prefix.waveLength")

        def waveLength = waveLengthProperty.value

        val antennaDiameterProperty = new Property[Double](s"$prefix.antenna.diameter", 0.0263 , Some("Діаметер антени"), nameResourceBundle, s"$prefix.antenna.diameter")

        def antennaDiameter = antennaDiameterProperty.value

        val reflectivityMeteoGoalsProperty = new Property[Double](s"$prefix.reflectivity.Meteo.Goals", 1.0, Some("Отражаемості метео цілей"), nameResourceBundle, s"$prefix.reflectivity.Meteo.Goals")

        def reflectivityMeteoGoals = reflectivityMeteoGoalsProperty.value

        val attenuationProperty = new Property[Double](s"$prefix.attenuationFactor", 0.8 / 100, Some("затухання"), nameResourceBundle, s"$prefix.attenuation")

        def attenuation = attenuationProperty.value

        val radiusProperty = new Property[Double](s"$prefix.radius", 1.0, Some("Радіус"), nameResourceBundle, s"$prefix.radius")

        def radius = radiusProperty.value

        val grainFactorProperty = new Property[Double](s"$prefix.grainFactor", Math.pow(10, 4), Some("Коєфіциент підсилення"), nameResourceBundle, s"$prefix.grainFactor")

        def grainFactor = grainFactorProperty.value

        val durationPulseProperty = new Property[Double](s"$prefix.durationPulse", 1.0, Some("Тривалість умпульсу"), nameResourceBundle, s"$prefix.durationPulse")
        val durationPulse = durationPulseProperty.value

        object Location {
          val decimalFormatProperty = new Property[String](s"$prefix.location.decimalFormat", "#.000000", Some("Формат відображення координат"), nameResourceBundle, s"$prefix.location.decimalFormat")
          val decimalFormat = decimalFormatProperty.value
        }

        object Isolines {
          private val prefix = s"${Settings.Program.Tools.Radar.prefix}.isolines"

          object SanitaryStandards {
            private val prefix = s"${Settings.Program.Tools.Radar.Isolines.prefix}.sanitaryStandards"
            val radiationPowerProperty = new Property[Double](s"$prefix.radiation_power", 1000, Some("Санітарні норми випромінювання"), nameResourceBundle, s"$prefix.radiation_power")
            val radiationPower = radiationPowerProperty.value
          }

        }

      }

    }

  }

}


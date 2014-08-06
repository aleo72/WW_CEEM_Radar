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

  private implicit val file = new File("CeemRadarDataConfigs/program.properties")
  private val nameResourceBundle = "label_properties"

  object Program {

    private val _programName = new Property[String]("program.name", "World Wind Ceem Radar", Some("Назва програми"), nameResourceBundle, "program.name")
    _programName.editable = false
    def name = _programName.value

    private val _locale = new Property[Locale]("program.locale", new Locale("ru", "RU"), Some("Локаль прогрими"), nameResourceBundle, "program.locale")
    def locale = _locale.value
    def locale_=(newLocale: Locale): Unit = _locale.value = newLocale

    private val _debug = new Property[Boolean]("program.debug", true, Some("Отладка програми"), nameResourceBundle, "program.debug")
    def debug = _debug.value

    object Start {
      private val _fileStartWindow = new Property[String]("program.start.window", "resources/ww_start.png", Some("Зображеня яке показане при старте програми"), nameResourceBundle, "program.start.window")
      def fileStartWindow = _fileStartWindow.value
    }

    object Style {
      private val _lookAndFeelInfo = new Property[String]("program.style.look_and_feel_info", "Nimbus", Some("Візуальний стиль"), nameResourceBundle, "program.style.look_and_feel_info")
      def lookAndFeelInfo = _lookAndFeelInfo.value
    }

    object Data {
      private val _ceemRadarData = new Property[String]("program.data.folder", "CeemRadarData/", Some("Папка де зберігається данні програми"), nameResourceBundle, "program.data.folder")
      _ceemRadarData.editable = false
      def folder = _ceemRadarData.value
    }

    object Config {
      private val ceemRadarConfig = new Property[String]("program.config.folder", "CeemRadarDataConfigs/", Some("Папка з конфігурацією"), nameResourceBundle, "program.config.folder")
      ceemRadarConfig.editable = false
      def folder = ceemRadarConfig.value
    }

    object Tools {
      object GeoNames {
        private val _translateGeoName = new Property[Boolean]("program.tools.geoNames.translateGeoName", false, Some("Транслировання GeoNames"), nameResourceBundle, "program.tools.geoNames.translateGeoName")
        def translateGeoName = _translateGeoName.value
      }

      object Radar {
        private val maxAltitudeForRadar = new Property[Long]("program.tools.radar.altitude.max", 100000L, Some("Максимальна висота установка радара"), nameResourceBundle, "program.tools.radar.altitude.max")
        def maxAltitude = maxAltitudeForRadar.value
        def maxAltitude_=(value: Long): Unit = maxAltitudeForRadar.value = value

        private val minAltitudeForRadar= new Property[Long]("program.tools.radar.altitude.min", 0L, Some("Мінімальна висота установка радара"), nameResourceBundle, "program.tools.radar.altitude.min")
        def minAltitude = minAltitudeForRadar.value
        def minAltitude_=(value: Long): Unit = minAltitudeForRadar.value = value

        private val defaultAltitudeForRadar = new Property[Long]("program.tools.radar.altitude.default", 10L, Some("Висота установка радара за замовчуванням"), nameResourceBundle, "program.tools.radar.altitude.default")
        def defaultAltitude = defaultAltitudeForRadar.value
        def defaultAltitude_=(value: Long): Unit = defaultAltitudeForRadar.value = value

        private val stepAltitudeForRadar = new Property[Long]("program.tools.radar.altitude.step", 5L, Some("Крок зміни установки висоти радара"), nameResourceBundle, "program.tools.radar.altitude.step")
        def stepAltitude = stepAltitudeForRadar.value
        def stepAltitude_=(value: Long): Unit = stepAltitudeForRadar.value = value

        private val _counterRadar = new Property[Long]("program.tools.radar.counter", 1L, Some("Лічильник радарів"), nameResourceBundle, "program.tools.radar.counter")
        _counterRadar.editable = false
        def counterRadar = _counterRadar.inc

        private val _viewToRadarHeight = new Property[Long]("program.tools.radar.viewToRadarHeight", 100000L, Some("Висота на яку переміщається "), nameResourceBundle, "program.tools.radar.viewToRadarHeight")
        def viewToRadarHeight = _viewToRadarHeight.value
        def viewToRadarHeight_=(value: Long): Unit = _viewToRadarHeight.value = value

      }
    }

  }

}


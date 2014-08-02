/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.settings

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.Locale

import ua.edu.odeku.ceem.mapRadar.properties.PropertiesUtils._
import ua.edu.odeku.ceem.mapRadar.properties.{BooleanProperties, LocaleProperties, LongProperties, StringProperties}

/**
 * Обїект який маніпулює конфігурацієй
 *
 * Created by aleo on 02.08.14.
 */
object Settings {

  private val file = new File("CeemRadarDataConfigs/program.properties")
  file.createNewFile()
  private implicit val in = new FileInputStream(file)
  private implicit val out = new FileOutputStream(file)


  object Program {

    private val _programName: StringProperties = ("program.name", "World Wind Ceem Radar")
    private val _locale: LocaleProperties = ("program.locale", new Locale("ru", "RU"))
    private val _debug: BooleanProperties = ("program.debug", true)

    def name = _programName.value

    def locale = _locale.value

    def locale_=(newLocale: Locale): Unit = _locale.value = newLocale

    def debug = _debug.value

    object Start {
      private val _fileStartWindow: StringProperties = ("program.start.window", "resources/ww_start.png")

      def fileStartWindow = _fileStartWindow.value
    }

    object Style {
      private val _lookAndFeelInfo: StringProperties = ("program.style.look_and_feel_info", "Nimbus")

      def lookAndFeelInfo = _lookAndFeelInfo.value
    }

    object Data {
      private val _ceemRadarData: StringProperties = ("program.data.folder", "CeemRadarData/")

      def folder = _ceemRadarData.value
    }

    object Config {
      private val ceemRadarConfig: StringProperties = ("program.config.folder", "CeemRadarDataConfigs/")

      def folder = ceemRadarConfig.value
    }

    object Tools {
      object GeoNames {
        private val _translateGeoName: BooleanProperties = ("program.tools.geoNames.translateGeoName", false)

        def translateGeoName = _translateGeoName.value
      }

      object Radar {
        private val maxAltitudeForRadar: LongProperties = ("program.tools.radar.altitude.max", 100000L)
        private val minAltitudeForRadar: LongProperties = ("program.tools.radar.altitude.min", 0L)
        private val defaultAltitudeForRadar: LongProperties = ("program.tools.radar.altitude.default", 10L)
        private val stepAltitudeForRadar: LongProperties = ("program.tools.radar.altitude.step", 5L)
        private val _counterRadar: LongProperties = ("program.tools.radar.counter", 1L)
        private val _viewToRadarHeight: LongProperties = ("program.tools.radar.viewToRadarHeight", 100000L)

        def maxAltitude = maxAltitudeForRadar.value
        def maxAltitude_=(value: Long): Unit = maxAltitudeForRadar.value = value

        def minAltitude = minAltitudeForRadar.value
        def minAltitude_=(value: Long): Unit = minAltitudeForRadar.value = value

        def defaultAltitude = defaultAltitudeForRadar.value
        def defaultAltitude_=(value: Long): Unit = defaultAltitudeForRadar.value = value

        def stepAltitude = stepAltitudeForRadar.value
        def stepAltitude_=(value: Long): Unit = stepAltitudeForRadar.value = value

        def counterRadar = _counterRadar.inc

        def viewToRadarHeight = _viewToRadarHeight.value

        def viewToRadarHeight_=(value: Long): Unit = _viewToRadarHeight.value = value
      }
    }

  }

}


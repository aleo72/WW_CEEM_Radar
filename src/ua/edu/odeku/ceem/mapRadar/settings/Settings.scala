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
    def locale_=(newLocale: Locale): Unit = localeProperty.value = newLocale

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
        val maxAltitudeForRadarProperty = new Property[Long]("program.tools.radar.altitude.max", 100000L, Some("Максимальна висота установка радара"), nameResourceBundle, "program.tools.radar.altitude.max")
        def maxAltitude = maxAltitudeForRadarProperty.value
        def maxAltitude_=(value: Long): Unit = maxAltitudeForRadarProperty.value = value

        val minAltitudeForRadarProperty = new Property[Long]("program.tools.radar.altitude.min", 0L, Some("Мінімальна висота установка радара"), nameResourceBundle, "program.tools.radar.altitude.min")
        def minAltitude = minAltitudeForRadarProperty.value
        def minAltitude_=(value: Long): Unit = minAltitudeForRadarProperty.value = value

        val defaultAltitudeForRadarProperty = new Property[Long]("program.tools.radar.altitude.default", 10L, Some("Висота установка радара за замовчуванням"), nameResourceBundle, "program.tools.radar.altitude.default")
        def defaultAltitude = defaultAltitudeForRadarProperty.value
        def defaultAltitude_=(value: Long): Unit = defaultAltitudeForRadarProperty.value = value

        val stepAltitudeForRadarProperty = new Property[Long]("program.tools.radar.altitude.step", 5L, Some("Крок зміни установки висоти радара"), nameResourceBundle, "program.tools.radar.altitude.step")
        def stepAltitude = stepAltitudeForRadarProperty.value
        def stepAltitude_=(value: Long): Unit = stepAltitudeForRadarProperty.value = value

        val counterRadarProperty = new Property[Long]("program.tools.radar.counter", 1L, Some("Лічильник радарів"), nameResourceBundle, "program.tools.radar.counter")
        counterRadarProperty.editable = false
        def counterRadar = counterRadarProperty.inc

        val viewToRadarHeightProperty = new Property[Long]("program.tools.radar.viewToRadarHeight", 100000L, Some("Висота на яку переміщається "), nameResourceBundle, "program.tools.radar.viewToRadarHeight")
        def viewToRadarHeight = viewToRadarHeightProperty.value
        def viewToRadarHeight_=(value: Long): Unit = viewToRadarHeightProperty.value = value

      }
    }

  }

}


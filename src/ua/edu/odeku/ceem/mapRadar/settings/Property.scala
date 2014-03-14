/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.settings

import java.util.Locale
import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 03.03.14
 * Time: 11:50
 */
object Property {

	val values = new ArrayBuffer[PropertyValue]()

	case class PropertyValue( name: String, startValue: String, changesValue: Boolean = true){
		var _value = startValue

		values += this

		def value = _value

		def value_=(v: String): Unit = {
			if(changesValue){
				_value = v
			}
		}

		def toBoolean = value.toBoolean

		def toInt = value.toInt

		def toDouble = value.toDouble

		def toLocale = Locale.forLanguageTag(value)

		override def toString = value
	}

	/**
	 * Название программы
	 */
	val NAME_PROGRAM = PropertyValue("NAME_PROGRAM", "World Wind Ceem Radar", changesValue = false)

	/**
	 * Локаль программы
	 */
	val CURRENT_LOCALE_LANGUAGE = PropertyValue("CURRENT_LOCALE_LANGUAGE", Locale.getDefault.toLanguageTag)

	/**
	 * Инициализация базы данных
	 */
	val INIT_DB = PropertyValue("INIT_DB", true.toString )

	/**
	 * Визуальный стиль
	 */
	val LOOK_AND_FEEL_INFO = PropertyValue("LOOK_AND_FEEL_INFO", "Nimbus", changesValue = false)

	/**
	 * Файл с изображением стартовой картинки
	 */
	val FILE_START_WINDOW = PropertyValue("FILE_START_WINDOW", "resources/ww_start.png", changesValue = false)

	/**
	 * Нужно ли пытаться перевести название GeoNames
	 */
	val TRANSLATE_GEO_NAME = PropertyValue("TRANSLATE_GEO_NAME", true.toString)

	/**
	 * Максимальная высота установки радара над уровнем моря
	 */
	val MAX_ALTITUDE_FOR_RADAR = PropertyValue("MAX_ALTITUDE_FOR_RADAR", 100000.toString)

	/**
	 * Минимальная высота установки радара над уровнем моря
	 */
	val MIN_ALTITUDE_FOR_RADAR = PropertyValue("MIN_ALTITUDE_FOR_RADAR", (-10).toString)

	/**
	 * По умолчанию высота установки радара над уровнем моря
	 */
	val DEFAULT_ALTITUDE_FOR_RADAR = PropertyValue("DEFAULT_ALTITUDE_FOR_RADAR", 10.toString)

	/**
	 * Шаг увеличения высоты установки радара для JSpinner
	 */
	val STEP_ALTITUDE_FOR_RADAR = PropertyValue("STEP_ALTITUDE_FOR_RADAR", 5.toString)
	/**
	 * Значение высоты которую необходимо дополнительно установить над куполом радара, при переходе к нему
	 */
	val ALTITUDE_FOR_GO_TO_AIRSPACE = PropertyValue("ALTITUDE_FOR_GO_TO_AIRSPACE", 100000.toString)

	PropertyManager.load()
	PropertyManager.store()
}

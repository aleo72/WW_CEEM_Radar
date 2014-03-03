/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.settings

import java.util.Locale

/**
 * User: Aleo Bakalov
 * Date: 03.03.14
 * Time: 11:50
 */
object Property extends Enumeration {

	case class PropertyValue[ValueType]( name: String,
										 var value: ValueType,
										 private val string2Value: (String) => ValueType,
										 private val value2String: (ValueType) => String
										) extends Val(name) {
		def valueToString() = value2String.apply(value)

		def stringToValue(str: String) = {
			value = string2Value.apply(str)
			value
		}
	}

	implicit def valueToPropertyValue(value: Value): PropertyValue = value.asInstanceOf[PropertyValue]

	private val stringToString: (String) => String = str => str
	private val stringToBoolean: (String) => Boolean = str => str.toBoolean
	private val stringToInt: (String) => Int = str => str.toInt
	private val stringToLocale: (String) => Locale = str => new Locale(str)

	private val booleanToString: (Boolean) => String = v => v.toString
	private val intToString: (Int) => String = v => v.toString
	private val localeToString: (Locale) => String = v => v.getLanguage

	/**
	 * Название программы
	 */
	val NAME_PROGRAM = PropertyValue[String]("NAME_PROGRAM", "World Wind Ceem Radar", stringToString, stringToString)

	/**
	 * Локаль программы
	 */
	val CURRENT_LOCALE = PropertyValue[Locale]("CURRENT_LOCALE", Locale.getDefault, stringToLocale, localeToString)

	/**
	 * Инициализация базы данных
	 */
	val INIT_DB = PropertyValue[Boolean]("INIT_DB", true, stringToBoolean, booleanToString)

	/**
	 * Визуальный стиль
	 */
	val LOOK_AND_FEEL_INFO = PropertyValue[String]("LOOK_AND_FEEL_INFO", "Nimbus", stringToString, stringToString)

	/**
	 * Файл с изображением стартовой картинки
	 */
	val FILE_START_WINDOW = PropertyValue[String]("FILE_START_WINDOW", "resources/ww_start.png", stringToString, stringToString)

	/**
	 * Нужно ли пытаться перевести название GeoNames
	 */
	val TRANSLATE_GEO_NAME = PropertyValue[Boolean]("TRANSLATE_GEO_NAME", true, stringToBoolean, booleanToString)

	/**
	 * Максимальная высота установки радара над уровнем моря
	 */
	val MAX_ALTITUDE_FOR_RADAR = PropertyValue[Int]("MAX_ALTITUDE_FOR_RADAR", 100000, stringToInt, intToString)

	/**
	 * Минимальная высота установки радара над уровнем моря
	 */
	val MIN_ALTITUDE_FOR_RADAR = PropertyValue[Int]("MIN_ALTITUDE_FOR_RADAR", -10, stringToInt, intToString)

	/**
	 * По умолчанию высота установки радара над уровнем моря
	 */
	val DEFAULT_ALTITUDE_FOR_RADAR = PropertyValue[Int]("DEFAULT_ALTITUDE_FOR_RADAR", 10, stringToInt, intToString)

	/**
	 * Шаг увеличения высоты установки радара для JSpinner
	 */
	val STEP_ALTITUDE_FOR_RADAR = PropertyValue[Int]("STEP_ALTITUDE_FOR_RADAR", 5, stringToInt, intToString)
	/**
	 * Значение высоты которую необходимо дополнительно установить над куполом радара, при переходе к нему
	 */
	val ALTITUDE_FOR_GO_TO_AIRSPACE = PropertyValue[Int](ALTITUDE_FOR_GO_TO_AIRSPACE, 100000, stringToInt, intToString)
}

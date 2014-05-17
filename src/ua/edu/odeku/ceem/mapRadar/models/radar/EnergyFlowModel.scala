/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

/**
 * Класс описывает можель потока энергии.
 *
 * Присутствуют два режима:
 * 1) точечных целей.
 * 2) распределенных целей.
 *
 * Created by Алексей on 17.05.2014.
 */
class EnergyFlowModel {

}

object EnergyFlowModel {

	/**
	 * Скорость света
	 */
	val CONSTANT_C = 299792458

	/**
	 * Коэфициент усиления
	 */
	val CONSTANT_G = 10000

	/**
	 * Мощность P
	 */
	val CONSTANT_P = 1000000 // TODO описать

	/**
	 * Варианты длин волны
	 */
	val RANGE_WAVELENGTH = (0.8, 3.0, 6.0, 10.0)

}
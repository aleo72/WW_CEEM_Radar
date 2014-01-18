/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models

/**
 *
 * Created by Aleo on 18.01.14.
 */
case class DoubleWhisPrefix_SI(value: Double) {

	if (value.isInfinite || value.isNaN || value.isInfinity) throw new NumberFormatException

	val (_v, _prefix) = DoubleWhisPrefix_SI.split(value)

	override def toString = {
		_v + " * " + _prefix.toString
	}
}

object DoubleWhisPrefix_SI {

	/**
	 * Метод раздивает значение, с учетом значущих цифр и префикса
	 * @param value число подлежащее разбиению
	 */
	def split(value: Double): (Double, Prefix_SI) = {
		val str = value.toString
		if (str.contains("E")){
			splitWithMantissa(value)
		} else {
			splitWithoutMantissa(value)
		}
	}

	/**
	 * Делим число в котором существует матисса
	 * @param value число для разбиения
	 */
	def splitWithMantissa(value: Double) = {
		val arr = value.toString.split("E")
		var v = arr(0).toDouble
		var d = arr(1).toInt

		val nearestPrefixSI = Prefix_SI.nearestExistValue(d)
		d -= nearestPrefixSI.degree

		v *= Math.pow(10, d)

		(v, nearestPrefixSI)
	}

	/**
	 * Делим число, которые без матиссы
	 * @param value число для разбиения
	 */
	def splitWithoutMantissa(value: Double) = {
//		if (value == 0.0)
//			(value, Prefix_SI.ONE)
//		else {
//			if (value > 1 || value < -1){
//
//			} else {
//
//			}
//		}
		// TODO Улучшить алгоритм
		(value, Prefix_SI.ONE)
	}
}

/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils

/**
 * User: Aleo Bakalov
 * Date: 09.07.2014
 * Time: 15:42
 */
object CeemUtilsFunctions {

	def notNullAndEmpty(string: String) = if (string != null && !string.trim.isEmpty) true else false

	implicit def stringToBoolean(string: String) = notNullAndEmpty(string)

	implicit class StringPower(string: String) {

		def unary_~  = stringToBoolean(string)

		def unary_? : Boolean = if (string != null && !string.trim.isEmpty) true else false

		def ? = if (string != null && !string.trim.isEmpty) true else false

		def DEBUG = println(string)

	}

  implicit class NumberPower(value: Double) {
    def ** (i: Int) = Math.pow(value, i)
  }




}

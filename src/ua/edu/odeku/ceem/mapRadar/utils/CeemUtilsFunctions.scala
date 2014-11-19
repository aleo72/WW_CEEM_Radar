/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils

import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 09.07.2014
 * Time: 15:42
 */
object CeemUtilsFunctions {

  def notNullAndEmpty(string: String) = if (string != null && !string.trim.isEmpty) true else false

  implicit def stringToBoolean(string: String) = notNullAndEmpty(string)

  implicit def charToBoolean(char: Char) = if (char != null) true else false

  implicit class StringPower(string: String) {

    def unary_~ = stringToBoolean(string)

    def unary_? : Boolean = if (string != null && !string.trim.isEmpty) true else false

    def ? = if (string != null && !string.trim.isEmpty) true else false

    def DEBUG = println(string)

  }

  implicit class NumberPower(value: Double) {
    def **(i: Int) = Math.pow(value, i)
  }

  def flatArray(multyArray: Array[Array[Double]]): Array[Double] = {
    val buf = new ArrayBuffer[Double]
    for (array <- multyArray) {
      buf ++= array
    }
    buf.toArray
  }


}

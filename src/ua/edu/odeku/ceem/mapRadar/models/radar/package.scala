/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.models

/**
 *
 * Created by Aleo on 31.01.14.
 */
package object radar {

	def rangeDouble(from: Double, to: Double, step: Double = 1) : Array[Double] = {
		(for( i <- from.to(to, step) ) yield Math.round(i * 100) / 100.0).toArray
	}
}

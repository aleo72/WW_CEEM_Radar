/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import java.awt.Color
import java.util

import gov.nasa.worldwind.util.{BufferWrapper, WWMath}

/**
 * Created by aleo on 07.09.14.
 */
case class GridPointAttributes(value: Double, color: Color)

object GridPointAttributesFactory {
  def apply(buffer: BufferWrapper, minValue: Double, maxValue: Double, minHue: Double, maxHue: Double): java.lang.Iterable[GridPointAttributes] = {
    new util.ArrayList[GridPointAttributes]() {
      {
        for (i <- 0 until buffer.length()) {
          add(createGridPointAttributes(buffer.getDouble(i), minValue, maxValue, minHue, maxHue))
        }
      }
    }
  }

  def createGridPointAttributes(value: Double, minValue: Double, maxValue: Double, minHue: Double, maxHue: Double): GridPointAttributes = {
    val hueFactor = WWMath.computeInterpolationFactor(value, minValue, maxValue)
    val color = Color.getHSBColor(WWMath.mixSmooth(hueFactor, minHue, maxHue).toFloat, 1f, 1f)
    val opacity: Double = WWMath.computeInterpolationFactor(value, minValue, minValue + (maxValue - minValue) * 0.1)
    val rgbaColor: Color = new Color(color.getRed, color.getGreen, color.getBlue, (255 * opacity).toInt)
    GridPointAttributes(value, rgbaColor)
  }
}



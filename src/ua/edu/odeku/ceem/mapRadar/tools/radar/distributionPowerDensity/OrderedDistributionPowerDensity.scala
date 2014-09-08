/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import java.awt.Point

import gov.nasa.worldwind.render.{DrawContext, OrderedRenderable}

/**
 * Created by aleo on 07.09.14.
 */
class OrderedDistributionPowerDensity(val distributionPowerDensity: DistributionPowerDensity, val eyeDistance: Double) extends OrderedRenderable {

  override def getDistanceFromEye: Double = eyeDistance

  override def pick(dc: DrawContext, pickPoint: Point): Unit = {
    this.distributionPowerDensity.pickSupport.beginPicking(dc)
    try{
      this.render(dc)
    } finally {
      this.distributionPowerDensity.pickSupport.endPicking(dc)
      this.distributionPowerDensity.pickSupport.resolvePick(dc, dc.getPickPoint, this.distributionPowerDensity.clientLayer)
    }
  }

  override def render(dc: DrawContext): Unit = {
    this.distributionPowerDensity.drawOrderedRenderable(dc)
  }
}

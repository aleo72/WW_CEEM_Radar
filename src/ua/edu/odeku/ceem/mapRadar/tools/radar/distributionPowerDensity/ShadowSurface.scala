/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import javax.media.opengl.{GL, GL2}

import gov.nasa.worldwind.render.DrawContext

/**
 * Created by Aleo on 14.09.2014.
 */
class ShadowSurface(d: DistributionPowerDensity) extends DistributionPowerDensityObject(d) {
  override protected def buildPickRepresentation(dc: DrawContext): Unit = {
    // The analytic surface's shadow is not drawn during picking. Suppress any attempt to create a pick
    // representation for the shadow to eliminate unnecessary overhead.
    //    super.buildPickRepresentation(dc)
  }

  override def bind(dc: DrawContext): Unit = {
    val gl = dc.getGL.getGL2
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, this.distributionPowerDensity.renderInfo.geographicVertexBuffer)
    gl.glColorPointer(4, GL.GL_UNSIGNED_BYTE, 0, this.distributionPowerDensity.renderInfo.shadowColorBuffer)
  }

  override protected def drawOutline(dc: DrawContext): Unit = {
    if (!dc.isPickingMode) {
      // Convert the floating point opacity from the range [0, 1] to the unsigned byte range [0, 255].
      val alpha = (255 * this.distributionPowerDensity.attributes.outlineOpacity * this.distributionPowerDensity.attributes.shadowOpacity + 0.5).toByte
      val gl: GL2 = dc.getGL.getGL2
      gl.glColor4ub(0.toByte, 0.toByte, 0.toByte, alpha)
    }
    super.drawOutline(dc)
  }
}

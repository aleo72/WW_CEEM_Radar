/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import java.util
import javax.media.opengl.fixedfunc.{GLPointerFunc, GLMatrixFunc}
import javax.media.opengl.{GL, GL2}

import gov.nasa.worldwind.geom.{Matrix, Sector}
import gov.nasa.worldwind.render.{DrawContext, AbstractSurfaceObject}
import gov.nasa.worldwind.util.{OGLUtil, SurfaceTileDrawContext}

/**
 * Created by aleo on 07.09.14.
 */
class DistributionPowerDensityObject(val distributionPowerDensity: DistributionPowerDensity) extends AbstractSurfaceObject {


  def markAsModified(): Unit = {
    super.updateModifiedTime()
    super.clearCaches()
  }

  override protected def drawGeographic(dc: DrawContext, sdc: SurfaceTileDrawContext): Unit = {
    this.beginDrawing(dc)
    try{
      this.doDrawGeographic(dc, sdc)
    } finally {
      this.endDrawing(dc)
    }
  }

  override def getSectors(dc: DrawContext): util.List[Sector] = {
    assert(dc == null , "draw context is null")
    util.Arrays.asList(this.distributionPowerDensity.sector)
  }
  protected def beginDrawing(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT | GL2.GL_CURRENT_BIT | GL.GL_DEPTH_BUFFER_BIT | GL2.GL_LINE_BIT)
    gl.glPushClientAttrib(GL2.GL_CLIENT_VERTEX_ARRAY_BIT)
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
    gl.glPushMatrix()
    gl.glEnable(GL.GL_BLEND)
    OGLUtil.applyBlending(gl, false)
    gl.glDisable(GL.GL_DEPTH_TEST)
    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY)
  }

  def endDrawing(context: DrawContext): Unit = {
    val gl = context.getGL.getGL2
    gl.glPopMatrix()
    gl.glPopClientAttrib()
    gl.glPopAttrib()
  }

  protected def doDrawGeographic(dc: DrawContext, sdc: SurfaceTileDrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    val modelview: Matrix = sdc.getModelviewMatrix(this.distributionPowerDensity.referencePos)
    gl.glMultMatrixd(modelview.toArray(new Array[Double](16), 0, false), 0)
    this.bind(dc)
    if (this.distributionPowerDensity.attributes.drawInterior) this.drawInterior(dc)
    if (this.distributionPowerDensity.attributes.drawOutline) this.drawOutline(dc)
  }

  def bind(dc: DrawContext) : Unit = Unit

  protected def drawInterior(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    if (!dc.isPickingMode) {
      gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY)
    }
    this.distributionPowerDensity.renderInfo.drawInterior(dc)
  }

  protected def drawOutline(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    if (!dc.isPickingMode) {
      gl.glEnable(GL.GL_LINE_SMOOTH)
      gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY)
    }
    gl.glLineWidth(this.distributionPowerDensity.attributes.outlineWidth.asInstanceOf[Float])
    this.distributionPowerDensity.renderInfo.drawOutline(dc)
  }
}

/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import java.nio.{ByteBuffer, FloatBuffer, IntBuffer}
import javax.media.opengl.{GL, GL2}

import com.jogamp.common.nio.Buffers
import gov.nasa.worldwind.render.DrawContext
import gov.nasa.worldwind.util.WWMath

/**
 * Created by aleo on 07.09.14.
 */
class RenderInfo(val gridWidth: Int, val gridHeight: Int) {

  val numVertices: Int = gridWidth * gridHeight

  val interiorIndexBuffer: IntBuffer = WWMath.computeIndicesForGridInterior(gridWidth, gridHeight)
  val outlineIndexBuffer: IntBuffer = WWMath.computeIndicesForGridOutline(gridWidth, gridHeight)
  val cartesianVertexBuffer: FloatBuffer = Buffers.newDirectFloatBuffer(3 * numVertices)
  val cartesianNormalBuffer: FloatBuffer = Buffers.newDirectFloatBuffer(3 * numVertices)
  val geographicVertexBuffer: FloatBuffer = Buffers.newDirectFloatBuffer(3 * numVertices)
  val colorBuffer: ByteBuffer = Buffers.newDirectByteBuffer(4 * numVertices)
  val shadowColorBuffer: ByteBuffer = Buffers.newDirectByteBuffer(4 * numVertices)

  def drawInterior(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    gl.glDrawElements(GL.GL_TRIANGLE_STRIP, this.interiorIndexBuffer.remaining, GL.GL_UNSIGNED_INT, this.interiorIndexBuffer)
  }

  def drawOutline(dc: DrawContext) {
    val gl: GL2 = dc.getGL.getGL2
    gl.glDrawElements(GL.GL_LINE_LOOP, this.outlineIndexBuffer.remaining, GL.GL_UNSIGNED_INT, this.outlineIndexBuffer)
  }
}

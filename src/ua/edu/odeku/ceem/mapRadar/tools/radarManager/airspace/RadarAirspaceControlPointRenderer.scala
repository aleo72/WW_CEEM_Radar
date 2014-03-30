/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.render.airspaces.editor.{AirspaceControlPoint, AirspaceControlPointRenderer}
import gov.nasa.worldwind.render.DrawContext
import java.lang.Iterable
import java.awt.Point
import gov.nasa.worldwind.layers.Layer

/**
 * Простая реализация контроллера
 *
 * Created by Aleo on 14.01.14.
 */
class RadarAirspaceControlPointRenderer extends AirspaceControlPointRenderer {

	def render(dc: DrawContext, controlPoints: Iterable[_ <: AirspaceControlPoint]): Unit = {

	}

	def pick(dc: DrawContext, controlPoints: Iterable[_ <: AirspaceControlPoint], pickPoint: Point, layer: Layer): Unit = {

	}
}

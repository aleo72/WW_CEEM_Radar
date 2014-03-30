/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.ActionListeners.airspaceActionListeners

import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditEvent, AirspaceEditListener}
import javax.swing.text.JTextComponent
import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.LocationForm
import gov.nasa.worldwind.render.airspaces.SphereAirspace
import gov.nasa.worldwind.geom.LatLon

/**
 * Слушатель изменяет значения JTextComponent в зависимости от перемещения Airspace
 *
 * User: Aleo Bakalov
 * Date: 28.01.14
 * Time: 16:24
 */
class AirspaceChangeLocationOnFormListener(val lat: JTextComponent, val lon: JTextComponent) extends AirspaceEditListener {

	def this(location: LocationForm) {
		this(location.latTextField, location.lonTextField)
	}

	def updateFields(location: LatLon) {
		lat.setText(location.getLatitude.getDegrees.toString)
		lon.setText(location.getLongitude.getDegrees.toString)
	}

	def airspaceMoved(e: AirspaceEditEvent): Unit = {
		e.getAirspace match {
			case airspace: SphereAirspace =>
				updateFields(airspace.getLocation)
		}
	}

	def airspaceResized(e: AirspaceEditEvent): Unit = {}

	def controlPointAdded(e: AirspaceEditEvent): Unit = {}

	def controlPointRemoved(e: AirspaceEditEvent): Unit = {}

	def controlPointChanged(e: AirspaceEditEvent): Unit = {}
}

/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.WWObjectImpl
import gov.nasa.worldwind.render.airspaces.{AirspaceAttributes, Airspace}
import gov.nasa.worldwind.render.airspaces.editor.AirspaceEditor
import gov.nasa.worldwind.avlist.AVKey

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:04
 */
class AirspaceEntry(val airspace: Airspace, val editor: AirspaceEditor) extends WWObjectImpl {

  val attributes: AirspaceAttributes = airspace.getAttributes
  private var _editing: Boolean = false
  private var _selected: Boolean = false
  var _intersecting: Boolean = false

  def editing = _editing

  def editing_=(value : Boolean) : Unit = {
    this._editing = value
    this.updateAttributes()
  }

  def selected = _selected

  def selected_=(value : Boolean) : Unit = {
    this._selected = value
    this.updateAttributes()
  }

  def intersecting = _intersecting

  def intersecting_=(value: Boolean) : Unit = {
    this._intersecting = value
    this.updateAttributes()
  }

  def name : String = this.getStringValue(AVKey.DISPLAY_NAME)

  def name_=(value : String) : Unit = this.setValue(AVKey.DISPLAY_NAME, value)

  def updateAttributes() {
    if (this.selected && this.intersecting ){
      this.airspace.setAttributes(getSelectionAndIntersectionAttributes)
    }
    else if (this.selected) {
      this.airspace.setAttributes(getSelectionAttributes)
    }
    else if (this.intersecting) {
      this.airspace.setAttributes(getIntersectionAttributes)
    }
    else {
      this.airspace.setAttributes(this.attributes)
    }
  }

  override def toString = this.name

  override def getValue(key : String) : AnyRef = {
    val value = super.getValue(key)
    if (value == null) this.airspace.getValue(key) else value
  }

  override def setValue(key : String, value : AnyRef) : AnyRef = {
    if (AVKey.DISPLAY_NAME == key) {
      this.airspace.setValue(key, value)
    }
    else {
      super.setValue(key, value)
    }
  }
}

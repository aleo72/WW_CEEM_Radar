/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.distributionPowerDensity

import gov.nasa.worldwind.render.Material

/**
 * Created by aleo on 07.09.14.
 */
class DistributionPowerDensityAttributes(val material: Material, val opacity: Double) {
  assert(material == null, "material is null")

  var drawInterior = true
  var drawOutline = true
  var drawShadow = true
  private var _interiorMaterial = material
  private var _outlineMaterial = Material.WHITE
  private var _interiorOpacity: Double = opacity
  private var _outlineOpacity: Double = 1d
  private var _shadowOpacity: Double = 1d
  private var _outlineWidth: Double = 1d

  def this() {
    this(Material.GRAY, 1d)
  }

  def this(attr: DistributionPowerDensityAttributes) {
    this()
    this.drawInterior = attr.drawInterior
    this.drawOutline = attr.drawOutline
    this.drawShadow = attr.drawShadow
    this.interiorMaterial = attr.interiorMaterial
    this.outlineMaterial = attr.outlineMaterial
    this.interiorOpacity = attr.interiorOpacity
    this.outlineOpacity = attr.outlineOpacity
    this.shadowOpacity = attr.shadowOpacity
    this.outlineWidth = attr.outlineWidth
  }

  def copy() = new DistributionPowerDensityAttributes(this)

  def interiorMaterial = _interiorMaterial

  def interiorMaterial_=(value: Material): Unit = {
    assert(value == null, "material is null")
    _interiorMaterial = value
  }

  def outlineMaterial = _outlineMaterial

  def outlineMaterial_=(value: Material) : Unit = {
    assert(value == null , "material is null")
    _outlineMaterial = value
  }

  def interiorOpacity = _interiorOpacity

  def interiorOpacity_=(value: Double): Unit = {
    assert(value < 0 || value > 1)
    _interiorOpacity = value
  }

  def outlineOpacity = _outlineOpacity

  def outlineOpacity_=(value: Double): Unit = {
    assert(value < 0 || value > 1)
    _outlineOpacity = value
  }

  def shadowOpacity = _shadowOpacity

  def shadowOpacity_=(value: Double): Unit = _shadowOpacity = value

  def outlineWidth = _outlineWidth

  def outlineWidth_=(value: Double): Unit = _outlineWidth = value
}

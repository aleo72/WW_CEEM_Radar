/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.sector

import gov.nasa.worldwind.geom.{Position, Sector}
import gov.nasa.worldwind.render._
import gov.nasa.worldwind.avlist.AVKey
import java.awt.Color
import gov.nasa.worldwind.util.Logging
import gov.nasa.worldwind.pick.{PickedObject, PickedObjectList}

/**
 * User: Aleo Bakalov
 * Date: 08.02.14
 * Time: 12:54
 */
protected class RegionShape(var __sector: Sector) extends SurfaceSector(__sector) {

	var resizeable = false
	var startPosition: Position = null
	var endPosition: Position = null
	private var borderShape: SurfaceSector = null

	// The edges of the region shape should be constant lines of latitude and longitude.
	this.border = new SurfaceSector(_sector_)
	this.setPathType(AVKey.LINEAR)
	this.border.setPathType(AVKey.LINEAR)

	// Setup default interior rendering attributes. Note that the interior rendering attributes are
	// configured so only the SurfaceSector's interior is rendered.
	val interiorAttrs: ShapeAttributes = new BasicShapeAttributes
	interiorAttrs.setDrawOutline(false)
	interiorAttrs.setInteriorMaterial(new Material(Color.WHITE))
	interiorAttrs.setInteriorOpacity(0.1)
	this.setAttributes(interiorAttrs)
	this.setHighlightAttributes(interiorAttrs)

	val borderAttrs = new BasicShapeAttributes
	borderAttrs.setDrawInterior(false)
	borderAttrs.setOutlineMaterial(new Material(Color.RED))
	borderAttrs.setOutlineOpacity(0.7)
	borderAttrs.setOutlineWidth(30)
	this.border.setAttributes(borderAttrs)
	this.border.setHighlightAttributes(borderAttrs)

	def interiorColor: Color = this.getAttributes.getInteriorMaterial.getDiffuse

	def interiorColor_=(value: Color): Unit = {
		val attr = this.getAttributes
		attr.setInteriorMaterial(new Material(value))
		this.setAttributes(attr)
	}

	def borderColor: Color = this.border.getAttributes.getOutlineMaterial.getDiffuse

	def borderColor_=(value: Color): Unit = {
		val attr = this.border.getAttributes
		attr.setOutlineMaterial(new Material(value))
		border.setAttributes(attr)
	}

	def interiorOpacity: Double = this.getAttributes.getInteriorOpacity

	def interiorOpacity_=(opacity: Double): Unit = {
		val attr: ShapeAttributes = this.getAttributes
		attr.setInteriorOpacity(opacity)
		this.setAttributes(attr)
	}

	def borderOpacity: Double = this.border.getAttributes.getOutlineOpacity

	def borderOpacity_=(opacity: Double): Unit = {
		val attr: ShapeAttributes = this.border.getAttributes
		attr.setOutlineOpacity(opacity)
		this.border.setAttributes(attr)
	}

	def borderWidth: Double = this.border.getAttributes.getOutlineWidth

	def borderWidth_=(width: Double): Unit = {
		val attr: ShapeAttributes = this.border.getAttributes
		attr.setOutlineWidth(width)
		this.border.setAttributes(attr)
	}

	def _sector_ : Sector = this.__sector

	def _sector__=(value: Sector): Unit = {
		super.setSector(value)
		border.setSector(value)
	}

	def border_=(value: SurfaceSector): Unit = {
		if (value == null) {
			val message = Logging.getMessage("nullValue.Shape")
			Logging.logger().severe(message)
			throw new IllegalArgumentException
		}
		this.borderShape = value
	}

	def border: SurfaceSector = borderShape

	def hashSelection: Boolean = startPosition != null && endPosition != null

	def clear() {
		startPosition = null
		endPosition = null
		_sector_ = Sector.EMPTY_SECTOR
	}

	override def preRender(dc: DrawContext) {
		// This is called twice: once during normal rendering, then again during ordered surface rendering. During
		// normal renering we pre-render both the interior and border shapes. During ordered surface rendering, both
		// shapes are already added to the DrawContext and both will be individually processed. Therefore we just
		// call our superclass behavior
		if (dc.isOrderedRenderingMode) {
			super.preRender(dc)
		} else {
			this.doPreRender(dc)
		}
	}

	override def render(dc: DrawContext) {
		if (dc.isPickingMode && this.resizeable)
			return
		// This is called twice: once during normal rendering, then again during ordered surface rendering. During
		// normal renering we render both the interior and border shapes. During ordered surface rendering, both
		// shapes are already added to the DrawContext and both will be individually processed. Therefore we just
		// call our superclass behavior
		if(dc.isOrderedRenderingMode){
			super.render(dc)
			return
		}

		if(!this.resizeable){
			if(this.hashSelection){
				this.doRender(dc)
			}
			return
		}

		val pos: PickedObjectList = dc.getPickedObjects
		val terrainObject: PickedObject = if (pos!=null) pos.getTerrainObject else null

		if(terrainObject == null)
			return
		if(this.startPosition != null){
			val end = terrainObject.getPosition
			if(this.startPosition != end){
				this.endPosition = end
				this._sector_ = Sector.boundingSector(this.startPosition, this.endPosition)
				this.doRender(dc)
			}
		} else {
			this.startPosition = pos.getTerrainObject.getPosition
		}
	}

	def doPreRender(dc: DrawContext) {
		this.doPreRenderInterior(dc)
		this.doPreRenderBorder(dc)
	}

	def doPreRenderInterior(context: DrawContext) {
		super.preRender(context)
	}

	def doPreRenderBorder(context: DrawContext) {
		this.border.preRender(context)
	}

	def doRender(dc: DrawContext) {
		this.doRenderInterior(dc)
		this.doRenderBorder(dc)
	}

	def doRenderInterior(dc: DrawContext) {
		super.render(dc)
	}

	def doRenderBorder(dc: DrawContext) {
		this.border.render(dc)
	}
}

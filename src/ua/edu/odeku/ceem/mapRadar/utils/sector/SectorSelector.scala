/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.sector

import gov.nasa.worldwind.{View, Movable, WorldWindow, WWObjectImpl}
import gov.nasa.worldwind.event._
import java.awt.event.{InputEvent, MouseEvent, MouseMotionListener, MouseListener}
import gov.nasa.worldwind.layers.RenderableLayer
import gov.nasa.worldwind.render._
import gov.nasa.worldwind.geom._
import gov.nasa.worldwind.util.Logging
import gov.nasa.worldwind.avlist.AVKey
import java.awt.{Component, Cursor, Color}
import gov.nasa.worldwind.pick.PickedObjectList
import gov.nasa.worldwind.globes.EllipsoidalGlobe

/**
 * Provides an interactive region selector. To use, construct and call enable/disable. Register a property listener to
 * receive changes to the sector as they occur, or just wait until the user is done and then query the result via
 * {@link #getSector()}.
 *
 * Created by Aleo on 02.02.14.
 */
class SectorSelector(val wwd: WorldWindow, val shape: RegionShape = new RegionShape(Sector.EMPTY_SECTOR), val layer: RenderableLayer = new RenderableLayer) extends WWObjectImpl with SelectListener with MouseListener with MouseMotionListener with RenderingListener {

	if (wwd == null) {
		Logging.logger().log(java.util.logging.Level.SEVERE, Logging.getMessage("nullValue.WorldWindow"))
		throw new IllegalArgumentException
	}

	if (shape == null) {
		Logging.logger().log(java.util.logging.Level.SEVERE, Logging.getMessage("nullValue.Shape"))
		throw new IllegalArgumentException
	}

	if (layer == null) {
		Logging.logger().log(java.util.logging.Level.SEVERE, Logging.getMessage("nullValue.Layer"))
		throw new IllegalArgumentException
	}

	val SECTOR_PROPERTY = "gov.nasa.worldwind.SectorSelector"

	val NONE = 0
	val MOVING = 1
	val SIZING = 2

	val NORTH = 1
	val SOUTH = 2
	val EAST = 4
	val WEST = 8

	val NORTHWEST = NORTH + WEST
	val NORTHEAST = NORTH + EAST
	val SOUTHWEST = SOUTH + WEST
	val SOUTHEAST = SOUTH + EAST

	this.layer.asInstanceOf[RenderableLayer].addRenderable(this.shape)

	var edgeFactor = 0.1
	var armed = false
	private var operatiron = NONE
	var side = NONE
	var previousPosition: Position = null
	var previousSector: Sector = null

	def enable() {
		this.shape.startPosition = null

		val layers = this.wwd.getModel.getLayers

		if (!layers.contains(this.layer)) layers.add(this.layer)

		if (!this.layer.isEnabled) this.layer.setEnabled(true)

		this.armed = true

		this.wwd.addRenderingListener(this)
		this.wwd.addSelectListener(this)
		this.wwd.getInputHandler.addMouseListener(this)
		this.wwd.getInputHandler.addMouseMotionListener(this)

		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR))
	}

	def disable() {
		this.wwd.removeRenderingListener(this)
		this.wwd.removeSelectListener(this)
		this.wwd.getInputHandler.removeMouseListener(this)
		this.wwd.getInputHandler.removeMouseMotionListener(this)

		this.wwd.getModel.getLayers.remove(this.layer)
		this.shape.clear()
	}

	def setCursor(cursor: Cursor) = {
		this.wwd.asInstanceOf[Component].setCursor(if (cursor != null) cursor else Cursor.getDefaultCursor)
	}

	def sector: Sector = if (this.shape.hashSelection) this.shape.sector else null

	def interiorColor = this.shape.interiorColor

	def interiorColor_=(value: Color): Unit = this.shape.interiorColor = value

	def borderColor = this.shape.borderColor

	def borderColor_=(color: Color): Unit = this.shape.borderColor

	def interiorOpacity = this.shape.interiorOpacity

	def interiorOpacity_=(opacity: Double): Unit = this.shape.interiorOpacity = opacity

	def borderOpacity = this.shape.borderOpacity

	def borderOpacity_=(opacity: Double): Unit = this.shape.borderOpacity = opacity

	def borderWidth = this.shape.borderWidth

	def borderWidth_=(width: Double): Unit = this.shape.borderWidth = width

	def notifySectorChanged() {
		if (this.shape.hashSelection && this.sector != null && this.sector != previousSector) {
			this.firePropertyChange(SECTOR_PROPERTY, this.previousSector, this.shape.sector)
			this.previousSector = this.sector
		}
	}

	override def selected(event: SelectEvent): Unit = {
		if (event == null) {
			val msg: String = Logging.getMessage("nullValue.EventIsNull")
			Logging.logger.log(java.util.logging.Level.FINE, msg)
			throw new IllegalArgumentException(msg)
		}
		if (this.operatiron == NONE && event.getTopObject != null && !(event.getTopPickedObject.getParentLayer == this.layer)) {
			this.setCursor(null)
			return
		}
		if (event.getEventAction == SelectEvent.LEFT_PRESS)
			this.previousPosition = this.wwd.getCurrentPosition
		else if (event.getEventAction == SelectEvent.DRAG) {
			val dragEvent = event.asInstanceOf[DragSelectEvent]
			val topObject = dragEvent.getTopObject
			if (topObject == null)
				return
			val dragObject = this.shape
			if (this.operatiron == SIZING) {
				val newSector = this.resizeShape(dragObject, this.side)
				if (newSector != null)
					dragObject.setSector(newSector)
				event.consume()
			} else {
				this.side = this.determineAdjustmentSide(dragObject, this.edgeFactor)
				if (this.side == NONE || this.operatiron == MOVING) {
					this.operatiron = MOVING
					this.dragWholeShape(dragEvent, dragObject)
				} else {
					val newSector = this.resizeShape(dragObject, this.side)
					if (newSector != null) {
						dragObject.sector = newSector
					}
					this.operatiron = SIZING
				}
				event.consume()
			}
			this.previousPosition = this.wwd.getCurrentPosition
			this.notifySectorChanged()
		} else if (event.getEventAction == SelectEvent.DRAG_END) {
			this.operatiron = NONE
			this.previousPosition = null
		} else if (event.getEventAction == SelectEvent.ROLLOVER && this.operatiron == NONE) {
			if (!this.wwd.isInstanceOf[Component]) {
				return
			}
			if (event.getTopObject == null || event.getTopPickedObject.isTerrain) {
				this.setCursor(null)
				return
			}
			if (!event.getTopObject.isInstanceOf[Movable]) {
				return
			}
			this.setCursor(this.determineAdjustmentSide(event.getTopObject.asInstanceOf[Movable], this.edgeFactor))
		}
	}

	def determineAdjustmentSide(dragObject: Movable, factor: Double): Int = {
		dragObject match {
			case quar: SurfaceSector =>
				val s = quar.getSector
				val p = this.wwd.getCurrentPosition
				if (p == null) {
					NONE
				} else {
					import Math.abs
					val dN: Double = abs(s.getMaxLatitude.subtract(p.getLatitude).degrees)
					val dS: Double = abs(s.getMinLatitude.subtract(p.getLatitude).degrees)
					val dW: Double = abs(s.getMinLongitude.subtract(p.getLongitude).degrees)
					val dE: Double = abs(s.getMaxLongitude.subtract(p.getLongitude).degrees)
					val sLat: Double = factor * s.getDeltaLatDegrees
					val sLon: Double = factor * s.getDeltaLonDegrees

					if (dN < sLat && dW < sLon)
						NORTHWEST
					else if (dN < sLat && dE < sLon)
						NORTHEAST
					else if (dS < sLat && dW < sLon)
						SOUTHWEST
					else if (dS < sLat && dE < sLon)
						SOUTHEAST
					else if (dN < sLat)
						NORTH
					else if (dS < sLat)
						SOUTH
					else if (dW < sLon)
						WEST
					else if (dE < sLon)
						EAST
					else
						NONE
				}
			case _ =>
				NONE
		}
	}

	def setCursor(sideName: Int) {
		var cursor: Cursor = null
		sideName match {
			case NONE =>
				cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
			case NORTH =>
				cursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)
			case SOUTH =>
				cursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)
			case EAST =>
				cursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)
			case WEST =>
				cursor = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)
			case NORTHWEST =>
				cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)
			case NORTHEAST =>
				cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)
			case SOUTHWEST =>
				cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)
			case SOUTHEAST =>
				cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)
		}
		this.setCursor(cursor)
	}

	def resizeShape(dragObject: Movable, side: Int): Sector = {
		dragObject match {
			case quad: SurfaceSector =>
				val s = quad.getSector
				val p = this.wwd.getCurrentPosition
				if (p == null || this.previousPosition == null) {
					null
				} else {
					val dLat: Angle = p.getLatitude.subtract(this.previousPosition.getLatitude)
					val dLon: Angle = p.getLongitude.subtract(this.previousPosition.getLongitude)
					var newMinLat: Angle = s.getMinLatitude
					var newMinLon: Angle = s.getMinLongitude
					var newMaxLat: Angle = s.getMaxLatitude
					var newMaxLon: Angle = s.getMaxLongitude

					if (side == NORTH) {
						newMaxLat = s.getMaxLatitude.add(dLat)
					}
					else if (side == SOUTH) {
						newMinLat = s.getMinLatitude.add(dLat)
					}
					else if (side == EAST) {
						newMaxLon = s.getMaxLongitude.add(dLon)
					}
					else if (side == WEST) {
						newMinLon = s.getMinLongitude.add(dLon)
					}
					else if (side == NORTHWEST) {
						newMaxLat = s.getMaxLatitude.add(dLat)
						newMinLon = s.getMinLongitude.add(dLon)
					}
					else if (side == NORTHEAST) {
						newMaxLat = s.getMaxLatitude.add(dLat)
						newMaxLon = s.getMaxLongitude.add(dLon)
					}
					else if (side == SOUTHWEST) {
						newMinLat = s.getMinLatitude.add(dLat)
						newMinLon = s.getMinLongitude.add(dLon)
					}
					else if (side == SOUTHEAST) {
						newMinLat = s.getMinLatitude.add(dLat)
						newMaxLon = s.getMaxLongitude.add(dLon)
					}
					new Sector(newMinLat, newMaxLat, newMinLon, newMaxLon)
				}
			case _ =>
				null
		}
	}

	def dragWholeShape(dragEvent: DragSelectEvent, dragObject: Movable) {
		val view: View = this.wwd.getView
		val globe: EllipsoidalGlobe = this.wwd.getModel.getGlobe.asInstanceOf[EllipsoidalGlobe]
		val refPos: Position = dragObject.getReferencePosition
		if (refPos == null) return
		val refPoint: Vec4 = globe.computePointFromPosition(refPos)
		val screenRefPoint: Vec4 = view.project(refPoint)
		val dx: Int = dragEvent.getPickPoint.x - dragEvent.getPreviousPickPoint.x
		val dy: Int = dragEvent.getPickPoint.y - dragEvent.getPreviousPickPoint.y
		val x: Double = screenRefPoint.x + dx
		val y: Double = dragEvent.getMouseEvent.getComponent.getSize.height - screenRefPoint.y + dy - 1
		val ray: Line = view.computeRayFromScreenPoint(x, y)
		val inters = globe.intersect(ray, refPos.getElevation)
		if (inters != null) {
			val p: Position = globe.computePositionFromPoint(inters(0).getIntersectionPoint)
			dragObject.moveTo(p)
		}
	}

	override def stageChanged(event: RenderingEvent): Unit = {
		if (event.getStage != RenderingEvent.AFTER_BUFFER_SWAP) {
			this.notifySectorChanged()
		}
	}

	override def mouseExited(e: MouseEvent): Unit = {

	}

	override def mouseEntered(e: MouseEvent): Unit = {

	}

	override def mouseReleased(e: MouseEvent): Unit = {
		if (MouseEvent.BUTTON1 == e.getButton) {
			if (this.shape.resizeable)
				this.setCursor(null)

			this.shape.resizeable = false

			e.consume()

			this.firePropertyChange(SECTOR_PROPERTY, this.previousSector, null)
		}
	}

	override def mousePressed(e: MouseEvent): Unit = {
		if (InputEvent.BUTTON1_DOWN_MASK == e.getModifiersEx && this.armed) {
			this.shape.resizeable = true
			this.shape.startPosition = null
			this.armed = false

			e.consume()
		}
	}

	override def mouseClicked(e: MouseEvent): Unit = {

	}

	override def mouseMoved(e: MouseEvent): Unit = {

	}

	override def mouseDragged(e: MouseEvent): Unit = {
		if (InputEvent.BUTTON1_DOWN_MASK == e.getModifiersEx && this.shape.resizeable) {
			e.consume()
		}
	}
}

private class RegionShape(var _sector: Sector) extends SurfaceSector(_sector) {

	var resizeable = false
	var startPosition: Position = null
	var endPosition: Position = null
	private var borderShape: SurfaceSector = null

	// The edges of the region shape should be constant lines of latitude and longitude.
	this.border = new SurfaceSector(sector)
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
	borderAttrs.setOutlineWidth(3)
	this.border.setAttributes(borderAttrs)
	this.border.setHighlightAttributes(borderAttrs)

	def interiorColor = this.getAttributes.getInteriorMaterial.getDiffuse

	def interiorColor_=(value: Color): Unit = {
		val attr = this.getAttributes
		attr.setInteriorMaterial(new Material(value))
		this.setAttributes(attr)
	}

	def borderColor = this.border.getAttributes.getOutlineMaterial.getDiffuse

	def borderColor_=(value: Color): Unit = {
		val attr = this.border.getAttributes
		attr.setOutlineMaterial(new Material(value))
		border.setAttributes(attr)
	}

	def interiorOpacity = this.getAttributes.getInteriorOpacity

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

	def sector = this._sector

	def sector_=(value: Sector): Unit = {
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

	def border = borderShape

	def hashSelection: Boolean = startPosition != null && endPosition != null

	def clear() {
		startPosition = null
		endPosition = null
		sector = Sector.EMPTY_SECTOR
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
		if (!dc.isOrderedRenderingMode) {
			if (this.resizeable) {
				val pos: PickedObjectList = dc.getPickedObjects
				val terrainObject = if (pos != null) pos.getTerrainObject else null

				if (terrainObject != null) {
					if (this.startPosition != null) {
						val end = terrainObject.getPosition
						if (this.startPosition != end) {
							this.endPosition = end
							this.sector = Sector.boundingSector(this.startPosition, this.endPosition)
							this.doRender(dc)
						}
					} else {
						this.startPosition = pos.getTerrainObject.getPosition
					}
				}
			} else {
				if (this.hashSelection) {
					this.doRender(dc)
				}
			}
		} else {
			super.render(dc)
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
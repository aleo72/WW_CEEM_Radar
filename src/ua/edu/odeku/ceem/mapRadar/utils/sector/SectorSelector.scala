/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.sector

import gov.nasa.worldwind.{View, Movable, WorldWindow, WWObjectImpl}
import gov.nasa.worldwind.event._
import java.awt.event.{InputEvent, MouseEvent, MouseMotionListener, MouseListener}
import gov.nasa.worldwind.layers.{LayerList, RenderableLayer}
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
class SectorSelector(
						val wwd: WorldWindow,
						val shape: RegionShape = new RegionShape(Sector.EMPTY_SECTOR),
						val layer: RenderableLayer = new RenderableLayer
						) extends WWObjectImpl
with SelectListener
with MouseListener
with MouseMotionListener
with RenderingListener {

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

	this.layer.asInstanceOf[RenderableLayer].addRenderable(this.shape)

	var edgeFactor = 0.1
	var armed = false
	private var operatiron = SectorSelector.NONE
	var side = SectorSelector.NONE
	var previousPosition: Position = null
	var previousSector: Sector = null

	def enable() {
		this.shape.startPosition = null

		val layers: LayerList = this.wwd.getModel.getLayers

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

	def sector: Sector = if (this.shape.hashSelection) this.shape._sector_ else null

	def interiorColor: Color = this.shape.interiorColor

	def interiorColor_=(value: Color): Unit = this.shape.interiorColor = value

	def borderColor: Color = this.shape.borderColor

	def borderColor_=(color: Color): Unit = this.shape.borderColor

	def interiorOpacity: Double = this.shape.interiorOpacity

	def interiorOpacity_=(opacity: Double): Unit = this.shape.interiorOpacity = opacity

	def borderOpacity: Double = this.shape.borderOpacity

	def borderOpacity_=(opacity: Double): Unit = this.shape.borderOpacity = opacity

	def borderWidth: Double = this.shape.borderWidth

	def borderWidth_=(width: Double): Unit = this.shape.borderWidth = width

	def notifySectorChanged() {
		if (this.shape.hashSelection && this.sector != null && this.sector != previousSector) {
			this.firePropertyChange(SectorSelector.SECTOR_PROPERTY, this.previousSector, this.shape._sector_)
			this.previousSector = this.sector
		}
	}

	override def selected(event: SelectEvent): Unit = {
		if (event == null) {
			val msg: String = Logging.getMessage("nullValue.EventIsNull")
			Logging.logger.log(java.util.logging.Level.FINE, msg)
			throw new IllegalArgumentException(msg)
		}
		if (this.operatiron == SectorSelector.NONE && event.getTopObject != null && !(event.getTopPickedObject.getParentLayer == this.layer)) {
			this.setCursor(null)
			return
		}
		if (event.getEventAction == SelectEvent.LEFT_PRESS)
			this.previousPosition = this.wwd.getCurrentPosition
		else if (event.getEventAction == SelectEvent.DRAG) {
			val dragEvent: DragSelectEvent = event.asInstanceOf[DragSelectEvent]
			val topObject: AnyRef = dragEvent.getTopObject
			if (topObject == null)
				return
			val dragObject: RegionShape = this.shape
			if (this.operatiron == SectorSelector.SIZING) {
				val newSector: Sector = this.resizeShape(dragObject, this.side)
				if (newSector != null)
					dragObject._sector_ = newSector
				event.consume()
			} else {
				this.side = this.determineAdjustmentSide(dragObject, this.edgeFactor)
				if (this.side == SectorSelector.NONE || this.operatiron == SectorSelector.MOVING) {
					this.operatiron = SectorSelector.MOVING
					this.dragWholeShape(dragEvent, dragObject)
				} else {
					val newSector: Sector = this.resizeShape(dragObject, this.side)
					if (newSector != null) {
						dragObject._sector_ = newSector
					}
					this.operatiron = SectorSelector.SIZING
				}
				event.consume()
			}
			this.previousPosition = this.wwd.getCurrentPosition
			this.notifySectorChanged()
		} else if (event.getEventAction == SelectEvent.DRAG_END) {
			this.operatiron = SectorSelector.NONE
			this.previousPosition = null
		} else if (event.getEventAction == SelectEvent.ROLLOVER && this.operatiron == SectorSelector.NONE) {
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
					SectorSelector.NONE
				} else {
					import Math.abs
					val dN: Double = abs(s.getMaxLatitude.subtract(p.getLatitude).degrees)
					val dS: Double = abs(s.getMinLatitude.subtract(p.getLatitude).degrees)
					val dW: Double = abs(s.getMinLongitude.subtract(p.getLongitude).degrees)
					val dE: Double = abs(s.getMaxLongitude.subtract(p.getLongitude).degrees)
					val sLat: Double = factor * s.getDeltaLatDegrees
					val sLon: Double = factor * s.getDeltaLonDegrees

					if (dN < sLat && dW < sLon)
						SectorSelector.NORTHWEST
					else if (dN < sLat && dE < sLon)
						SectorSelector.NORTHEAST
					else if (dS < sLat && dW < sLon)
						SectorSelector.SOUTHWEST
					else if (dS < sLat && dE < sLon)
						SectorSelector.SOUTHEAST
					else if (dN < sLat)
						SectorSelector.NORTH
					else if (dS < sLat)
						SectorSelector.SOUTH
					else if (dW < sLon)
						SectorSelector.WEST
					else if (dE < sLon)
						SectorSelector.EAST
					else
						SectorSelector.NONE
				}
			case _ =>
				SectorSelector.NONE
		}
	}

	def setCursor(sideName: Int) {
		var cursor: Cursor = null
		sideName match {
			case SectorSelector.NONE =>
				cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
			case SectorSelector.NORTH =>
				cursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)
			case SectorSelector.SOUTH =>
				cursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)
			case SectorSelector.EAST =>
				cursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)
			case SectorSelector.WEST =>
				cursor = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)
			case SectorSelector.NORTHWEST =>
				cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)
			case SectorSelector.NORTHEAST =>
				cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)
			case SectorSelector.SOUTHWEST =>
				cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)
			case SectorSelector.SOUTHEAST =>
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

					if (side == SectorSelector.NORTH) {
						newMaxLat = s.getMaxLatitude.add(dLat)
					}
					else if (side == SectorSelector.SOUTH) {
						newMinLat = s.getMinLatitude.add(dLat)
					}
					else if (side == SectorSelector.EAST) {
						newMaxLon = s.getMaxLongitude.add(dLon)
					}
					else if (side == SectorSelector.WEST) {
						newMinLon = s.getMinLongitude.add(dLon)
					}
					else if (side == SectorSelector.NORTHWEST) {
						newMaxLat = s.getMaxLatitude.add(dLat)
						newMinLon = s.getMinLongitude.add(dLon)
					}
					else if (side == SectorSelector.NORTHEAST) {
						newMaxLat = s.getMaxLatitude.add(dLat)
						newMaxLon = s.getMaxLongitude.add(dLon)
					}
					else if (side == SectorSelector.SOUTHWEST) {
						newMinLat = s.getMinLatitude.add(dLat)
						newMinLon = s.getMinLongitude.add(dLon)
					}
					else if (side == SectorSelector.SOUTHEAST) {
						newMinLat = s.getMinLatitude.add(dLat)
						newMaxLon = s.getMaxLongitude.add(dLon)
					}
					new Sector(newMinLat, newMaxLat, newMinLon, newMaxLon)
				}
			case _ =>
				null
		}
	}

	def dragWholeShape(dragEvent: DragSelectEvent, dragObject: RegionShape) {
		val view: View = this.wwd.getView
		val globe: EllipsoidalGlobe = this.wwd.getModel.getGlobe.asInstanceOf[EllipsoidalGlobe]

		val refPos: Position = dragObject.getReferencePosition
		if (refPos == null)
			return

		val refPoint: Vec4 = globe.computePointFromPosition(refPos)
		val screenRefPoint: Vec4 = view.project(refPoint)

		val dx: Int = dragEvent.getPickPoint.x - dragEvent.getPreviousPickPoint.x
		val dy: Int = dragEvent.getPickPoint.y - dragEvent.getPreviousPickPoint.y

		val x: Double = screenRefPoint.x + dx
		val y: Double = dragEvent.getMouseEvent.getComponent.getSize.height - screenRefPoint.y + dy - 1
		val ray: Line = view.computeRayFromScreenPoint(x, y)
		val inters: Array[Intersection] = globe.intersect(ray, refPos.getElevation)

		if (inters != null) {
			val p: Position = globe.computePositionFromPoint(inters(0).getIntersectionPoint)
			dragObject.moveTo(p)
			dragObject.border.moveTo(p)
		}
	}

	override def stageChanged(event: RenderingEvent): Unit = {
		if (event.getStage == RenderingEvent.AFTER_BUFFER_SWAP) {
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

			this.firePropertyChange(SectorSelector.SECTOR_PROPERTY, this.previousSector, null)
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

object SectorSelector {
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
}
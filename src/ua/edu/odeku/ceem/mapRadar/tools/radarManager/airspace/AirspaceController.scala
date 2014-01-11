/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import gov.nasa.worldwind.WWObjectImpl
import java.awt.event.{MouseEvent, ActionEvent, MouseListener, ActionListener}
import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditor, AirspaceEditorController, AirspaceEditEvent, AirspaceEditListener}
import ua.edu.odeku.ceem.mapRadar.frames.AppCeemRadarFrame
import javax.swing.JFileChooser
import gov.nasa.worldwind.pick.PickedObjectList
import gov.nasa.worldwind.render.airspaces.Airspace
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils
import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:07
 */
class AirspaceController(private var _model: AirspaceBuilderModel, private var _view: AirspaceManagerView) extends WWObjectImpl with ActionListener with MouseListener with AirspaceEditListener {

	protected var _selectedEntry: AirspaceEntry = _

	val appCeemRadarFrame: AppCeemRadarFrame = AppCeemRadarFrame.getAppCeemRadarFrame
	val editorController: AirspaceEditorController = new AirspaceEditorController(appCeemRadarFrame.getWwd)
	appCeemRadarFrame.getWwd.getInputHandler.addMouseListener(this)

	private var _enabled = true
	private var _enableEdit = true
	private var _resizeNewShapes = false

	private var fileChooser: JFileChooser = _

	def model: AirspaceBuilderModel = _model

	def model_=(value: AirspaceBuilderModel): Unit = this._model = value

	def view: AirspaceManagerView = _view

	def view_=(value: AirspaceManagerView): Unit = this._view = value

	def enabled: Boolean = _enabled

	def enabled_=(value: Boolean): Unit = {
		this._enabled = value
		view.panel.setEnabled(value)
		appCeemRadarFrame.setEnabled(value)
	}

	def enableEdit: Boolean = _enableEdit

	def enableEdit_=(value: Boolean): Unit = {
		this._enableEdit = value
		this.handleEnableEdit(value)
		this.firePropertyChange(ENABLE_EDIT, null, value)
	}

	def resizeNewShapesToViewport = this._resizeNewShapes

	def resizeNewShapesToViewport_=(value: Boolean): Unit = {
		this._resizeNewShapes = value
		this.firePropertyChange(SIZE_NEW_SHAPES_TO_VIEWPORT, null, value)
	}

	def selectedEntry: AirspaceEntry = _selectedEntry

	def selectedEntry_=(value: AirspaceEntry): Unit = _selectedEntry = value

	def selectionEditing = this.selectedEntry != null && this.selectedEntry.editing

	def selectionEditing_=(editing: Boolean): Unit = {
		if (this.selectedEntry == null) {
			throw new IllegalStateException()
		}

		if (this.selectedEntry.editing == editing) {
			throw new IllegalStateException()
		}

		this.selectedEntry.editing = editing

		val editor: AirspaceEditor = this.selectedEntry.editor
		editor.setArmed(editing)

		if (editing) {
			this.editorController.setEditor(editor)
			VisibleUtils.insertBeforeCompass(appCeemRadarFrame.getWwd, editor)
		} else {
			this.editorController.setEditor(null)
			this.appCeemRadarFrame.getWwd.getModel.getLayers.remove(editor)
		}
		val index: Int = this.model.getIndexForEntry(this.selectedEntry)
		this.model.fireTableRowsUpdated(index, index)
	}

	def airspaceMoved(e: AirspaceEditEvent): Unit = ???

	def airspaceResized(e: AirspaceEditEvent): Unit = ???

	def controlPointAdded(e: AirspaceEditEvent): Unit = ???

	def controlPointRemoved(e: AirspaceEditEvent): Unit = ???

	def controlPointChanged(e: AirspaceEditEvent): Unit = ???

	def mouseClicked(e: MouseEvent): Unit = {

	}

	def mousePressed(e: MouseEvent): Unit = ???

	def mouseReleased(e: MouseEvent): Unit = ???

	def mouseEntered(e: MouseEvent): Unit = ???

	def mouseExited(e: MouseEvent): Unit = ???

	def actionPerformed(e: ActionEvent): Unit = ???

	def handleSelect() {
		val pickedObjects: PickedObjectList = this.appCeemRadarFrame.getWwd.getObjectsAtCurrentPosition
		val topObject: AnyRef = pickedObjects.getTopObject
		if (topObject.isInstanceOf[Airspace]) {
			val pickedEntry = this.entryFor(topObject.asInstanceOf[Airspace])
			if (pickedEntry != null) {
				if (this.selectedEntry != pickedEntry) {
					this.selectEntry(pickedEntry, true)
				}
			}
		}
	}

	def handleEnableEdit(enable: Boolean) {
		if (this.selectedEntry != null && this.selectionEditing != enable) {
			this.selectionEditing = enabled
		}
	}

	def updateShapeIntersection() {
		val selected: AirspaceEntry = this.selectedEntry
		if (selected != null) {
			var hasIntersection = false
			for (entry: AirspaceEntry <- this.model.entries) {
				if (entry != selected) {
					val intersecting = areShapesIntersecting(entry.airspace, selected.airspace)
					hasIntersection = if (intersecting) true else false
					entry.intersecting = intersecting
				}
			}
			selected.intersecting = hasIntersection
		}
	}

	def createNewEntry(factory: AirspaceFactory) {
		val airspace: Airspace = factory.createAirspace(appCeemRadarFrame.getWwd, this.resizeNewShapesToViewport)
		val editor: AirspaceEditor = factory.createEditor(airspace)
		val entry: AirspaceEntry = new AirspaceEntry(airspace, editor)

		this.addEntry(entry)

		this.selectEntry(entry, true)
	}

	def removeEntries(entries: Iterable[_ <: AirspaceEntry]) {
		if (entries != null) {
			for (entry: AirspaceEntry <- entries) {
				this.removeEntry(entry)
			}
		}
	}

	def addEntry(entry: AirspaceEntry) {
		entry.editor.addEditListener(this)
		this.model.addEntry(entry)
		this.updateShapeIntersection()

		appCeemRadarFrame.getAirspaceLayer.addAirspace(entry.airspace)
		appCeemRadarFrame.getWwd.redraw()
	}

	def removeEntry(entry: AirspaceEntry) {
		entry.editor.removeEditListener(this)
		if (this.selectedEntry == entry) {
			this.selectEntry(null, true)
		}
		this.model.removeEntry(entry)
		this.updateShapeIntersection()

		appCeemRadarFrame.getAirspaceLayer.removeAirspace(entry.airspace)
		appCeemRadarFrame.getWwd.redraw()
	}

	def selectEntry(entry: AirspaceEntry, updateView: Boolean) {
		this.selectedEntry = entry
		if (updateView) {
			if (entry != null) {
				val index: Int = this.model.getIndexForEntry(entry)
				this.view.selectedIndices_=(Array(index))
			} else {
				this.view.selectedIndices_=(Array())
			}
		}
		if (this.enableEdit) {
			if (this.selectedEntry != null && !this.selectionEditing) {
				this.selectionEditing = true
			}
		}
		this.updateShapeIntersection()
		this.appCeemRadarFrame.getWwd.redraw()
	}

	def viewSelectionChanged() {
		val indices: Array[Int] = this.view.selectedIndices
		if (indices != null) {
			for (entry: AirspaceEntry <- this.entriesFor(indices)) {
				this.selectEntry(entry, updateView = false)
			}
		}
		appCeemRadarFrame.getWwd.redraw()
	}

	def selectedEntries: Array[AirspaceEntry] = {
		val indices = view.selectedIndices
		if (indices != null) {
			this.entriesFor(indices)
		} else {
			Array()
		}
	}

	def entriesFor(indices: Array[Int]): Array[AirspaceEntry] = {
		val entries = new ArrayBuffer[AirspaceEntry](indices.length)
		for (i: Int <- 0 to indices.length) {
			entries += this.model.getEntry(indices(i))
		}
		entries.toArray
	}

	def entryFor(airspace: Airspace): AirspaceEntry = {
		for (entry: AirspaceEntry <- model.entries) {
			if (entry.airspace == airspace) {
				return entry
			}
		}
		null
	}
}

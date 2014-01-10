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
			val pickedEntry = this.getEntryFor(topObject.asInstanceOf[Airspace])
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
		if(selected != null) {
			var hasIntersection = false
			for (entry : AirspaceEntry <- this.model.entries){
				if(entry != selected){
					val intersecting = areShapesIntersecting(entry.airspace, selected.airspace)
					hasIntersection = if(intersecting) true else false
					entry.intersecting = intersecting
				}
			}
			selected.intersecting = hasIntersection
		}
	}

	def createNewEntry
}

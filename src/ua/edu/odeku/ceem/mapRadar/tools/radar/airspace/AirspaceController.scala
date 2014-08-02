/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radar.airspace

import gov.nasa.worldwind.WWObjectImpl
import java.awt.event.{MouseEvent, ActionEvent, MouseListener, ActionListener}
import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditorController, AirspaceEditEvent, AirspaceEditListener}
import javax.swing.{AbstractButton, JFileChooser}
import gov.nasa.worldwind.pick.PickedObjectList
import gov.nasa.worldwind.render.airspaces.Airspace
import ua.edu.odeku.ceem.mapRadar.utils.gui.VisibleUtils
import scala.collection.mutable.ArrayBuffer
import ua.edu.odeku.ceem.mapRadar.tools.radar.RadarManagerTool
import ua.edu.odeku.ceem.mapRadar.tools.radar.airspace.entry.AirspaceEntry
import gov.nasa.worldwind.geom.Position
import ua.edu.odeku.ceem.mapRadar.settings.PropertyProgram
import ua.edu.odeku.ceem.mapRadar.AppCeemRadarFrame

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:07
 */
class AirspaceController(private val ceemTool: RadarManagerTool) extends WWObjectImpl with ActionListener with MouseListener with AirspaceEditListener {

	protected var _selectedEntry: AirspaceEntry = _
	private var _model: AirspaceBuilderModel = _
	private var _view: AirspaceManagerView = _

	val editorController: AirspaceEditorController = new AirspaceEditorController(AppCeemRadarFrame.wwd)
	AppCeemRadarFrame.wwd.getInputHandler.addMouseListener(this)

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
		AppCeemRadarFrame.setEnabled(value)
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

	def selectedEntry_=(value: AirspaceEntry): Unit = {
		if (_selectedEntry != null) {
			if (_selectedEntry != value && _selectedEntry.editing) {
				this.selectionEditing = false
			}
			this._selectedEntry.selected = false
		}

		_selectedEntry = value

		if (this._selectedEntry != null) {
			this._selectedEntry.selected = true
		}
	}

	def selectionEditing = this.selectedEntry != null && this.selectedEntry.editing

	def selectionEditing_=(editing: Boolean): Unit = {
		if (this.selectedEntry == null) {
			throw new IllegalStateException()
		}

		if (this.selectedEntry.editing == editing) {
			throw new IllegalStateException()
		}

		this.selectedEntry.editing = editing

		this.selectedEntry.setArmedForAirspaceEditor(editing)

		if (editing) {
//			selectedEntry.registerEditorForEditController(this.editorController)
			this.editorController.setEditor(selectedEntry.editor)
			VisibleUtils.insertBeforeCompass(AppCeemRadarFrame.wwd, selectedEntry.editor)
		} else {
			this.editorController.setEditor(null)
			AppCeemRadarFrame.wwd.getModel.getLayers.remove(selectedEntry.editor)
		}
		val index: Int = this.model.getIndexForEntry(this.selectedEntry)
		this.model.fireTableRowsUpdated(index, index)
	}

	def airspaceMoved(e: AirspaceEditEvent): Unit = {
		this.updateShapeIntersection()
	}

	def airspaceResized(e: AirspaceEditEvent): Unit = {
		this.updateShapeIntersection()
	}

	def controlPointAdded(e: AirspaceEditEvent): Unit = {

	}

	def controlPointRemoved(e: AirspaceEditEvent): Unit = {

	}

	def controlPointChanged(e: AirspaceEditEvent): Unit = {

	}

	def mouseClicked(e: MouseEvent): Unit = {
		if(e.getButton == MouseEvent.BUTTON1 && e.getClickCount == 2){
			editSelectedEntry()
		}
	}

	def mousePressed(e: MouseEvent): Unit = {
		if (e == null || e.isConsumed) return
		if (this.enabled) {
			if (e.getButton == MouseEvent.BUTTON1) {
				this.handleSelect()
			}
		}

	}

	def mouseReleased(e: MouseEvent): Unit = {

	}

	def mouseEntered(e: MouseEvent): Unit = {

	}

	def mouseExited(e: MouseEvent): Unit = {

	}

	def actionPerformed(e: ActionEvent): Unit = {
		if (this.enabled) {
			e.getActionCommand match {
				case NEW_AIRSPACE =>
					this.createNewEntry()
				case CLEAR_SELECTION =>
					this.selectEntry(null, updateView = true)
				case SIZE_NEW_SHAPES_TO_VIEWPORT =>
					e.getSource match {
						case button: AbstractButton =>
							this.resizeNewShapesToViewport = button.isSelected
						case _ =>
					}
				case ENABLE_EDIT =>
					e.getSource match {
						case button: AbstractButton =>
							this.enableEdit = button.isSelected
						case _ =>
					}
				case REMOVE_SELECTED =>
					this.removeEntries(this.selectedEntries)
				case SELECTION_CHANGED =>
					this.viewSelectionChanged()
				case GO_TO_SELECTION_AIRSPACE =>
					this.goToSelectionAirspace()
				case _ =>
			}
		}
	}

	def handleSelect() {
		val pickedObjects: PickedObjectList = AppCeemRadarFrame.wwd.getObjectsAtCurrentPosition
		val topObject: AnyRef = pickedObjects.getTopObject
		topObject match {
			case airspace: Airspace =>
				val pickedEntry = this.entryFor(airspace)
				if (pickedEntry != null) {
					if (this.selectedEntry != pickedEntry) {
						this.selectEntry(pickedEntry, updateView = true)
					}
				}
			case _ =>
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

	private def editSelectedEntry(){
		AirspaceEntry.edit(selectedEntry, AppCeemRadarFrame.wwd, selectedEntry_=)
	}
	
	/**
	 * Метод дает сигнал на открытие формы для создания нового Airspace
	 */
	private def createNewEntry() {
		AirspaceEntry(AppCeemRadarFrame.wwd, createNewEntry)
	}

	def createNewEntry(entry: AirspaceEntry) {
		this.addEntry(entry)
		this.selectEntry(entry, updateView = true)
	}

	def removeEntries(entries: Iterable[_ <: AirspaceEntry]) {
		if (entries != null) {
			for (entry: AirspaceEntry <- entries) {
				this.removeEntry(entry)
			}
		}
	}

	def addEntry(entry: AirspaceEntry) {
		entry.addAirspaceEditorListener(this)
		this.model.addEntry(entry)
		this.updateShapeIntersection()

		entry.addAirspaceToAirspaceLayer(ceemTool.airspaceLayer)
		AppCeemRadarFrame.wwd.redraw()
	}

	def removeEntry(entry: AirspaceEntry) {
		entry.removeEditListener(this)
		if (this.selectedEntry == entry) {
			this.selectEntry(null, updateView = true)
		}
		this.model.removeEntry(entry)
		this.updateShapeIntersection()

		entry.removeAirspaceFromAirspaceLayer(ceemTool.airspaceLayer)
		entry.remove()
		AppCeemRadarFrame.wwd.redraw()
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
		AppCeemRadarFrame.wwd.redraw()
	}

	def viewSelectionChanged() {
		val indices: Array[Int] = this.view.selectedIndices
		if (indices != null) {
			for (entry: AirspaceEntry <- this.entriesFor(indices)) {
				this.selectEntry(entry, updateView = false)
			}
		}
		AppCeemRadarFrame.wwd.redraw()
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
		if (indices.length > 0) {
			for (i: Int <- 0 until indices.length) {
				entries += this.model.getEntry(indices(i))
			}
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

	def goToSelectionAirspace(){
		val latLon = selectedEntry.airspace.location
		val elevation = selectedEntry.radar.altitude + 2 * selectedEntry.radar.radius + PropertyProgram.getAltitudeForGoToAirspace
		val position = new Position(latLon, elevation )
		AppCeemRadarFrame.wwd.getView.goTo(position, elevation)
	}
}

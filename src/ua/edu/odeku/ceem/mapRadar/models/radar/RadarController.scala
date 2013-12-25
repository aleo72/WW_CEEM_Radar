/*
 * Odessa State environmental University
 * Copyright (C) 2013
 */

package ua.edu.odeku.ceem.mapRadar.models.radar

import gov.nasa.worldwind.WWObjectImpl
import java.awt.event.{MouseEvent, ActionEvent, MouseListener, ActionListener}
import gov.nasa.worldwind.render.airspaces.editor.{AirspaceEditor, AirspaceEditorController, AirspaceEditEvent, AirspaceEditListener}
import ua.edu.odeku.ceem.mapRadar.frames.AppCeemRadarFrame

/**
 * User: Aleo Bakalov
 * Date: 23.12.13
 * Time: 15:57
 */
class RadarController(protected val appCeemRadarFrame : AppCeemRadarFrame) extends WWObjectImpl with ActionListener with MouseListener with AirspaceEditListener {

  protected val editorController = new AirspaceEditorController(appCeemRadarFrame.getWwd)
  appCeemRadarFrame.getWwd.getInputHandler.addMouseListener(this)

  protected var _enabled: Boolean = true
  protected var _enableEdit: Boolean = true
  protected var _resizeNewShapes: Boolean = false

  protected var _model: AirspaceBuilderModel = null
  protected var _view: AirspaceBuilderPanel = null
  protected var _selectedEntry: AirspaceEntry = null

  def model = this._model
  def model_= (value : AirspaceBuilderModel) : Unit = _model = value

  def view = this._view
  def view_= (value : AirspaceBuilderPanel) : Unit = _view = value

  def isEnabled = this._enabled
  def enables_= (value: Boolean) : Unit = {
    _enabled = value
    view.enabled = _enabled
    appCeemRadarFrame.setEnabled(_enabled)
  }

  def isEnableEdit = this._enableEdit
  def enabledEdit_= (value : Boolean) : Unit ={
    _enableEdit = value
    this.handleEnabledEdit(false)
    this.firePropertyChange(RadarController.ENABLE_EDIT, null, _enabled)
  }

  protected def isSelectionEditing: Boolean = {
    this.selectedEntry != null && this.selectedEntry.isEditing
  }

  def selectedEntry = _selectedEntry
  def selectedEntry_= (value: AirspaceEntry) : Unit = _selectedEntry = value

  def selectEntry(entry : AirspaceEntry, updateView : Boolean ){
    selectedEntry = entry
    if(updateView){

      if(entry != null){
        val index = this.model.getIndexForEntry(entry)
        view.selectedIndices(Array[Int](index))
      } else {
        view.selectedIndices(Array[Int](0))
      }

      if(isEnableEdit){
        if(selectedEntry != null && !isSelectionEditing){
          this setSelectionEditing true
        }
      }

      this.updateShapeIntersection()
      appCeemRadarFrame.getWwd.redraw()
    }
  }

  protected def setSelectionEditing(editing: Boolean) {
    if (this.selectedEntry == null) {
      throw new IllegalStateException
    }

    if (this.selectedEntry.isEditing == editing) {
      throw new IllegalStateException
    }

    this.selectedEntry.setEditing(editing)
    val editor: AirspaceEditor = this.selectedEntry.getEditor
    editor.setArmed(editing)

    if (editing) {
      this.editorController.setEditor(editor)
      insertBeforePlacenames(appCeemRadarFrame.getWwd, editor)

    } else {
      this.editorController.setEditor(null)
      appCeemRadarFrame.getWwd.getModel.getLayers.remove(editor)
    }

    val index: Int = model.getIndexForEntry(this.selectedEntry)
    model.fireTableRowsUpdated(index, index)
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

  }

  def mousePressed(e: MouseEvent): Unit = ???

  def mouseReleased(e: MouseEvent): Unit = {

  }

  def mouseEntered(e: MouseEvent): Unit = {

  }

  def mouseExited(e: MouseEvent): Unit = {

  }

  def actionPerformed(e: ActionEvent): Unit = ???

  protected def handleEnableEdit(enable: Boolean) {
    if (this.selectedEntry == null)
      return
    if (this.isSelectionEditing != enable)
      this.setSelectionEditing(enable)
  }
}

object RadarController {
  val ENABLE_EDIT = "RadarController.EnableEdit"
}
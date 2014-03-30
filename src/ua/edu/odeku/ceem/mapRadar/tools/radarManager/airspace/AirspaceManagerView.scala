/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.radarManager.airspace

import ua.edu.odeku.ceem.mapRadar.tools.radarManager.panel.AirspacePanel
import javax.swing.{ListSelectionModel, JPanel}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}
import java.awt.event.{MouseEvent, MouseAdapter, ActionEvent}
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import ua.edu.odeku.ceem.mapRadar.tools.CeemPanel

/**
 * User: Aleo Bakalov
 * Date: 08.01.14
 * Time: 11:09
 */
class AirspaceManagerView(val model: AirspaceBuilderModel, val controller: AirspaceController) extends CeemPanel {

	val form: AirspacePanel = new AirspacePanel
	val panel: JPanel = form.getRootPanel
	var ignoreSelectEvents: Boolean = false

	this.initComponents()

	def selectedIndices: Array[Int] = this.form.table.getSelectedRows

	def selectedIndices_=(indices: Array[Int]): Unit = {
		this.ignoreSelectEvents = true

		if (indices != null && indices.length != 0) {
			for (index: Int <- indices) {
				this.form.table.setRowSelectionInterval(index, index)
			}
		} else {
			this.form.table.clearSelection()
		}

		this.ignoreSelectEvents = false
	}

	//	def selectedFactory: AirspaceFactory = {
	//		SphereAirspaceFactory.obj
	//	}

	def initComponents() {

//		form.checkBoxResizeNewShapes.setActionCommand(SIZE_NEW_SHAPES_TO_VIEWPORT)
//		form.checkBoxResizeNewShapes.addActionListener(controller)
//		form.checkBoxResizeNewShapes.setSelected(controller.resizeNewShapesToViewport)

//		form.checkBoxEnableEdit.setActionCommand(ENABLE_EDIT)
//		form.checkBoxEnableEdit.addActionListener(controller)
//		form.checkBoxEnableEdit.setSelected(controller.enableEdit)

		form.table.setModel(model)
		form.table.setColumnSelectionAllowed(false)
		form.table.setRowSelectionAllowed(true)
		form.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
		form.table.getSelectionModel.addListSelectionListener(new ListSelectionListener {
			def valueChanged(e: ListSelectionEvent): Unit = {
				if (!ignoreSelectEvents) {
					controller.actionPerformed(new ActionEvent(e.getSource, -1, SELECTION_CHANGED))
				}
			}
		})

		form.table.addMouseListener(new MouseAdapter {

			override def mouseClicked(e: MouseEvent): Unit = {
				if(e.getClickCount == 2 && e.getButton == MouseEvent.BUTTON1){
					controller.actionPerformed(new ActionEvent(e.getSource, -1, GO_TO_SELECTION_AIRSPACE))
				}
			}
		})

		// init buttonCreate
		form.buttonCreate.setActionCommand(NEW_AIRSPACE)
		form.buttonCreate.addActionListener(controller)
		form.buttonCreate.setToolTipText("Create a new shape centered in the viewport")

		form.buttonClearSelection.setActionCommand(CLEAR_SELECTION)
		form.buttonClearSelection.addActionListener(controller)

		form.buttonRemove.setActionCommand(REMOVE_SELECTED)
		form.buttonRemove.addActionListener(controller)

		controller.addPropertyChangeListener(new PropertyChangeListener {
			def propertyChange(evt: PropertyChangeEvent): Unit = {
				evt.getPropertyName match {
					case SIZE_NEW_SHAPES_TO_VIEWPORT =>
//						form.checkBoxResizeNewShapes.setSelected(controller.resizeNewShapesToViewport)
					case ENABLE_EDIT =>
//						form.checkBoxEnableEdit.setSelected(controller.enableEdit)
					case _ =>
				}
			}
		})
	}

	override def getRootPanel = panel
}

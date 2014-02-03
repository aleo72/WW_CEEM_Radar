/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.cacheManager.handler

import ua.edu.odeku.ceem.mapRadar.tools.cacheManager.panel.CacheDownloaderForm
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Sector
import scala.collection.mutable.ArrayBuffer
import javax.swing._
import gov.nasa.worldwind.retrieve.{BulkRetrievalThread, BulkRetrievable}
import gov.nasa.worldwind.cache.BasicDataFileStore
import gov.nasa.worldwind.layers.Layer
import gov.nasa.worldwind.terrain.CompoundElevationModel
import gov.nasa.worldwind.globes.ElevationModel
import java.awt.{Component, Color}
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.util.ResourceBundle
import java.awt.event.ActionEvent
import gov.nasa.worldwind.event.{BulkRetrievalEvent, BulkRetrievalListener}
import ua.edu.odeku.ceem.mapRadar.utils.sector.{SectorUtils, SectorSelector}
import gov.nasa.worldwindx.examples.BulkDownloadPanel

/**
 * Класс обработчик собитий для формы CacheDownloaderForm
 * Created by Aleo on 02.02.14.
 */
class CacheDownloaderHandler(val form: CacheDownloaderForm, val wwd: WorldWindow) {

	var currentSector: Sector = null
	val retrievables: ArrayBuffer[BulkRetrievablePanel] = initRetrievables(wwd)
	var cache: BasicDataFileStore = null
	var selector: SectorSelector = initSelector(wwd)

	JPopupMenu.setDefaultLightWeightPopupEnabled(false)
	ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false)


	initComponents()

	def initRetrievables(wwd: WorldWindow): ArrayBuffer[BulkRetrievablePanel] = {
		val buff = new ArrayBuffer[BulkRetrievablePanel]()
		for (layer: Layer <- wwd.getModel.getLayers if layer.isInstanceOf[BulkRetrievable])
			buff += new BulkRetrievablePanel(layer.asInstanceOf[BulkRetrievable])

		val cem: CompoundElevationModel = wwd.getModel.getGlobe.getElevationModel.asInstanceOf[CompoundElevationModel]
		for (elevationModel: ElevationModel <- cem.getElevationModels if elevationModel.asInstanceOf[BulkRetrievable])
			buff += new BulkRetrievablePanel(elevationModel.asInstanceOf[BulkRetrievable])

		buff
	}

	def initSelector(wwd: WorldWindow): SectorSelector = {
		val selector = new SectorSelector(wwd)
		selector.interiorColor = new Color(1f, 1f, 1f, 0.1f)
		selector.borderColor = new Color(1f, 0f, 0f, 0.5f)
		selector.borderWidth = 3
		selector.addPropertyChangeListener(selector.SECTOR_PROPERTY, new PropertyChangeListener {
			override def propertyChange(evt: PropertyChangeEvent): Unit = {
				updateSector()
			}
		})
		selector
	}

	def updateSector() {
		this.currentSector = this.selector.sector
		if (this.currentSector != null) {
			this.form.sectorSelectedTextField.setText(makeSectorDescription(this.currentSector))
			this.form.selectButton.setText(ResourceBundle.getBundle("button").getString("clear-sector"))
			this.form.startButton.setEnabled(true)
		} else {
			this.form.sectorSelectedTextField.setText("")
			this.form.selectButton.setText(ResourceBundle.getBundle("button").getString("select-sector"))
		}
		updateRetrievablePanels(this.currentSector)
	}

	def updateRetrievablePanels(sector: Sector) {
		this.retrievables.foreach(
			(panel: BulkRetrievablePanel) => panel.updateDescription(sector)
		)
	}

	def selectButtonActionPerformed(event: ActionEvent) {
		if (this.selector.sector != null) {
			this.selector.disable()
		} else {
			this.selector.enable()
		}
		updateSector()
	}

	/** Clear the current selection sector and remove it from the globe. */
	def clearSector() {
		if(this.selector.sector != null){
			this.selector.disable()
		}
		updateSector()
	}

	def startButtonActionPerformed(event: ActionEvent){
		for(panel: BulkRetrievablePanel <- this.retrievables if panel.selectCheckBox.isSelected){
			val retrievable = panel.retrievable
			val thread = retrievable.makeLocal(this.currentSector, 0, this.cache, new BulkRetrievalListener {
				override def eventOccurred(event: BulkRetrievalEvent): Unit = {

				}
			})
			if (thread != null){
				this.form.monitorPanel.add(new DownloadMonitorPanel(thread))
			}
		}
		this.asInstanceOf[JComponent].getTopLevelAncestor.validate()
	}

	def hasActiveDownloads: Boolean = {
		var flag = false
		for(c: Component <- this.form.monitorPanel.getComponents
		    if !flag && c.isInstanceOf[DownloadMonitorPanel] && c.asInstanceOf[DownloadMonitorPanel].thread.isAlive){
			flag = true
		}
		flag
	}

	def cancelActiveDownloads(){
		for(c:Component <- this.form.monitorPanel.getComponents
		    if c.isInstanceOf[DownloadMonitorPanel] && c.asInstanceOf[DownloadMonitorPanel].thread.isAlive){
			val panel = c.asInstanceOf[DownloadMonitorPanel]
			panel.cancelButtonActionPerformed(null)
			try{
				val time = System.currentTimeMillis()
				while (panel.thread.isAlive && System.currentTimeMillis() - time < 500)
					Thread.sleep(10)

			} catch {
				case _: Throwable =>
			}
		}
	}

	def clearInactiveDownloads(){
		val count: Int = this.form.monitorPanel.getComponentCount - 1
		for( i <- count.to(0, -1) ){
			val c: Component = this.form.monitorPanel.getComponent(i)
			c match {
				case panel:DownloadMonitorPanel =>
					if(!panel.thread.isAlive || panel.thread.isInterrupted){
						this.form.monitorPanel.remove(i)
					}
			}
		}
		this.form.monitorPanel.validate()
	}

	def initComponents() = {

	}

	protected class BulkRetrievablePanel(val retrievable: BulkRetrievable) extends JPanel {

		val selectCheckBox: JCheckBox = new JCheckBox()
		val descriptionLabel: JLabel = null
		var updateThread: Thread = null
		var sector: Sector = null

		def initComponents() {
			// TODO ---!!!!!!!!!!!!!!!!!!!!
		}

		def updateDescription(sector: Sector) {
			if (this.updateThread != null && this.updateThread.isAlive)
				return

			this.sector = sector
			if(!this.selectCheckBox.isSelected){
				doUpdateDescription(null)
			} else {
				this.updateThread = new Thread(new Runnable {
					override def run(): Unit = {
						doUpdateDescription(sector)
					}
				})
				this.updateThread.setDaemon(true)
				this.updateThread.start()
			}
		}

		def doUpdateDescription(sector: Sector){
			if(sector != null){
				try{
					val size = retrievable.getEstimatedMissingDataSize(sector, 0, cache)
					val formattedSize = SectorUtils.makeSizeDescription(size)
					SwingUtilities.invokeLater(new Runnable {
						override def run(): Unit = {
							descriptionLabel.setText(formattedSize)
						}
					})
				} catch {
					case _: Throwable =>
						SwingUtilities.invokeLater(new Runnable {
							override def run(): Unit = {
								descriptionLabel.setText("-")
							}
						})
				}
			} else {
				SwingUtilities.invokeLater(new Runnable {
					override def run(): Unit = {
						descriptionLabel.setText("-")
					}
				})
			}
		}

		override def toString = this.retrievable.getName
	}
}

class DownloadMonitorPanel(val thread: BulkRetrievalThread) extends JPanel{

}
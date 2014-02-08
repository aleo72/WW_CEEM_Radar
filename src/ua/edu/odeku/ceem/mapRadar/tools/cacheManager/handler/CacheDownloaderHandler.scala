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
import gov.nasa.worldwind.retrieve.{Progress, BulkRetrievalThread, BulkRetrievable}
import gov.nasa.worldwind.cache.BasicDataFileStore
import gov.nasa.worldwind.layers.Layer
import gov.nasa.worldwind.terrain.CompoundElevationModel
import gov.nasa.worldwind.globes.ElevationModel
import java.awt._
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.util.ResourceBundle
import java.awt.event.{ActionListener, ActionEvent}
import gov.nasa.worldwind.event.{BulkRetrievalEvent, BulkRetrievalListener}
import ua.edu.odeku.ceem.mapRadar.utils.sector.{SectorUtils, SectorSelector}

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

	def initComponents() {

		this.form.selectButton.setText(ResourceBundle.getBundle("button").getString("select-sector"))

		this.form.locationButton.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				val fc = new JFileChooser()
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
				fc.setMultiSelectionEnabled(false)
				val status = fc.showOpenDialog(form.locationButton.getParent)

				if (status == JFileChooser.APPROVE_OPTION) {
					val file = fc.getSelectedFile
					if (file != null) {
						form.locationTextField.setText(file.getPath)
						cache = new BasicDataFileStore(file)
						updateRetrievablePanels(selector.sector)
					}
				}
			}
		})

		this.form.selectButton.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				selectButtonActionPerformed(e)
			}
		})

		form.layersPanel.setLayout(new BoxLayout(this.form.layersPanel, BoxLayout.Y_AXIS))
		form.layersPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2))
		this.retrievables.foreach(this.form.layersPanel.add(_))


		this.form.startButton.setEnabled(false)
		this.form.startButton.addActionListener(new ActionListener {
			override def actionPerformed(e: ActionEvent): Unit = {
				startButtonActionPerformed(e)
			}
		})

		this.form.monitorPanel.setLayout(new BoxLayout(this.form.monitorPanel, BoxLayout.Y_AXIS))
		this.form.monitorPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2))

	}

	def initRetrievables(wwd: WorldWindow): ArrayBuffer[BulkRetrievablePanel] = {
		val buff = new ArrayBuffer[BulkRetrievablePanel]()
		//		wwd.getModel.getLayers
		for (i: Int <- 0 until wwd.getModel.getLayers.size()) {
			val layer: Layer = wwd.getModel.getLayers.get(i)
			if (layer.isInstanceOf[BulkRetrievable])
				buff += new BulkRetrievablePanel(layer.asInstanceOf[BulkRetrievable])
		}

		val cem: CompoundElevationModel = wwd.getModel.getGlobe.getElevationModel.asInstanceOf[CompoundElevationModel]

		for (i: Int <- 0 until cem.getElevationModels.size()) {
			val elevationModel: ElevationModel = cem.getElevationModels.get(i)
			if (elevationModel.isInstanceOf[BulkRetrievable])
				buff += new BulkRetrievablePanel(elevationModel.asInstanceOf[BulkRetrievable])
		}

		buff
	}

	def initSelector(wwd: WorldWindow): SectorSelector = {
		val selector = new SectorSelector(wwd)
		selector.interiorColor = new Color(1f, 1f, 1f, 0.1f)
		selector.borderColor = new Color(1f, 0f, 0f, 0.5f)
		selector.borderWidth = 3
		selector.addPropertyChangeListener(SectorSelector.SECTOR_PROPERTY, new PropertyChangeListener {
			override def propertyChange(evt: PropertyChangeEvent): Unit = {
				updateSector()
			}
		})
		selector
	}

	def updateSector() {
		this.currentSector = this.selector.sector
		if (this.currentSector != null) {
			this.form.sectorSelectedTextField.setText(SectorUtils.makeSectorDescription(this.currentSector))
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
		if (this.selector.sector != null) {
			this.selector.disable()
		}
		updateSector()
	}

	def startButtonActionPerformed(event: ActionEvent) {
		for (panel: BulkRetrievablePanel <- this.retrievables if panel.selectCheckBox.isSelected) {
			val retrievable = panel.retrievable
			val thread = retrievable.makeLocal(this.currentSector, 0, this.cache, new BulkRetrievalListener {
				override def eventOccurred(event: BulkRetrievalEvent): Unit = {

				}
			})
			if (thread != null) {
				this.form.monitorPanel.add(new DownloadMonitorPanel(thread))
			}
		}
		this.asInstanceOf[JComponent].getTopLevelAncestor.validate()
	}

	def hasActiveDownloads: Boolean = {
		var flag = false
		for (c: Component <- this.form.monitorPanel.getComponents
		     if !flag && c.isInstanceOf[DownloadMonitorPanel] && c.asInstanceOf[DownloadMonitorPanel].thread.isAlive) {
			flag = true
		}
		flag
	}

	def cancelActiveDownloads() {
		for (c: Component <- this.form.monitorPanel.getComponents
		     if c.isInstanceOf[DownloadMonitorPanel] && c.asInstanceOf[DownloadMonitorPanel].thread.isAlive) {
			val panel = c.asInstanceOf[DownloadMonitorPanel]
			panel.cancelButtonActionPerformed(null)
			try {
				val time = System.currentTimeMillis()
				while (panel.thread.isAlive && System.currentTimeMillis() - time < 500)
					Thread.sleep(10)

			} catch {
				case _: Throwable =>
			}
		}
	}

	def clearInactiveDownloads() {
		val count: Int = this.form.monitorPanel.getComponentCount - 1
		for (i <- count.to(0, -1)) {
			val c: Component = this.form.monitorPanel.getComponent(i)
			c match {
				case panel: DownloadMonitorPanel =>
					if (!panel.thread.isAlive || panel.thread.isInterrupted) {
						this.form.monitorPanel.remove(i)
					}
			}
		}
		this.form.monitorPanel.validate()
	}

	protected class BulkRetrievablePanel(val retrievable: BulkRetrievable) extends JPanel {

		var selectCheckBox: JCheckBox = new JCheckBox()
		var descriptionLabel: JLabel = null
		var updateThread: Thread = null
		var sector: Sector = null

		initComponents()

		def initComponents() {
			this.setLayout(new BorderLayout())
			this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))

			this.selectCheckBox = new JCheckBox(this.retrievable.getName)
			this.selectCheckBox.addActionListener(new ActionListener {
				override def actionPerformed(e: ActionEvent): Unit = {
					if (e.getSource.asInstanceOf[JCheckBox].isSelected && sector != null) {
						updateDescription(sector)
					}
				}
			})

			this.add(this.selectCheckBox, BorderLayout.WEST)

			this.descriptionLabel = new JLabel()
			this.add(this.descriptionLabel, BorderLayout.EAST)
		}

		def updateDescription(sector: Sector) {
			if (this.updateThread != null && this.updateThread.isAlive)
				return

			this.sector = sector
			if (!this.selectCheckBox.isSelected) {
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

		def doUpdateDescription(sector: Sector) {
			if (sector != null) {
				try {
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

class DownloadMonitorPanel(val thread: BulkRetrievalThread) extends JPanel {

	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
	this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4))

	val progress: Progress = thread.getProgress

	val descriptionPanel = new JPanel(new GridLayout(0, 1, 0, 0))
	descriptionPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2))

	val descriptionLabel: JLabel = new JLabel(if (this.thread.getRetrievable.getName.length > 27) this.thread.getRetrievable.getName.substring(0, 40) + "..." else this.thread.getRetrievable.getName)
	descriptionPanel.add(descriptionLabel)

	this.add(descriptionPanel)

	val progressPanel: JPanel = new JPanel(new BoxLayout(progressPanel, BoxLayout.X_AXIS))
	progressPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2))

	val progressBar: JProgressBar = new JProgressBar(0, 100)
	progressBar.setPreferredSize(new Dimension(100, 16))
	progressPanel.add(progressBar)
	progressPanel.add(Box.createHorizontalStrut(8))

	val cancelButton: JButton = new JButton(ResourceBundle.getBundle("button").getString("cancel"))
	cancelButton.setBackground(Color.RED)
	cancelButton.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			cancelButtonActionPerformed(e)
		}
	})

	progressPanel.add(cancelButton)
	this.add(progressPanel)

	val updateTimer: Timer = new Timer(1000, new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			updateStatus()
		}
	})

	this.updateTimer.start()

	def updateStatus() {
		var text = if (this.thread.getRetrievable.getName.length > 27) this.thread.getRetrievable.getName.substring(0, 27) + "..." else this.thread.getRetrievable.getName
		text += " (" + SectorUtils.makeSizeDescription(this.progress.getCurrentSize) + " / " + SectorUtils.makeSizeDescription(this.progress.getTotalSize) + ")"
		this.descriptionLabel.setText(text)
		val percent: Double = if (this.progress.getTotalCount > 0) this.progress.getCurrentCount / this.progress.getTotalCount else 0
		this.progressBar.setValue(Math.min(percent.toInt, 100))

		descriptionLabel.setToolTipText(SectorUtils.makeSectorDescription(this.thread.getSector))
		this.progressBar.setToolTipText(makeProgressDescription())

		if (!this.thread.isAlive) {
			this.cancelButton.setText(ResourceBundle.getBundle("button").getString("remove"))
			this.cancelButton.setBackground(Color.GREEN)
			this.updateTimer.stop()
		}
	}

	def cancelButtonActionPerformed(event: ActionEvent) {
		if (this.thread.isAlive) {
			this.thread.interrupt()
			this.cancelButton.setBackground(Color.ORANGE)
			this.cancelButton.setText(ResourceBundle.getBundle("button").getString("remove"))
			this.updateTimer.stop()
		} else {
			val top = this.getTopLevelAncestor
			this.getParent.remove(this)
			top.validate()
		}
	}

	def makeProgressDescription(): String = {
		if (this.progress.getTotalCount > 0) {
			val percent = this.progress.getCurrentSize / this.progress.getTotalSize * 100.0
			percent.toInt + "% (" + SectorUtils.makeSizeDescription(this.progress.getTotalSize) + ")"
		} else {
			""
		}
	}
}
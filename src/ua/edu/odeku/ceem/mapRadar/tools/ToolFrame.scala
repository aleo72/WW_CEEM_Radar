/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools

import javax.swing.JFrame
import java.awt.{Dimension, BorderLayout}
import java.awt.event.{WindowEvent, WindowAdapter}

/**
 * Общее окно для всех Tool компонентов программы
 *
 * User: Aleo Bakalov
 * Date: 10.12.13
 * Time: 16:28
 */
class ToolFrame(val ceemRadarTool: CeemRadarTool, val titleToolFrame: String) extends JFrame() {

	def this(toolName: String, titleToolFrame: String){
		this(Class.forName(toolName).newInstance().asInstanceOf[CeemRadarTool] , titleToolFrame)
	}

	ceemRadarTool.setParent(this)

	protected val startFunction: (ToolFrame) => Unit = ceemRadarTool.startFunction
	protected val endFunction: (ToolFrame) => Unit = ceemRadarTool.endFunction

	this.setTitle(titleToolFrame)

	this.setLocationByPlatform(true)

	initToolFrameSettings()

	protected def initToolFrameSettings() {
		//		this.setAlwaysOnTop(true)
		this.setLayout(new BorderLayout)
		this.add(ceemRadarTool.rootPanel, BorderLayout.CENTER)
		this.pack()
		this.setMinimumSize(new Dimension(this.getWidth, this.getHeight))
		if (startFunction != null) {
			startFunction.apply(this)
		}
	}

	this.addWindowListener(new WindowAdapter {
		override def windowClosing(e: WindowEvent) {
			if (endFunction != null) {
				endFunction.apply(ToolFrame.this)
			}
		}
	})

}

/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.utils.configsNonApps

import java.io.{FileInputStream, ObjectInputStream, File}
import scala.xml.{Attribute, XML}
import ua.edu.odeku.ceem.mapRadar.tools.adminBorder.Admin0

/**
 * User: Aleo Bakalov
 * Date: 26.03.2014
 * Time: 12:19
 */
object ManageAdminBorderXML extends App {

	val fileConfig = new File("CeemRadarDataConfigs/adminBorderConfig/manageAdminBorder.xml")
	val dirWithAdmin0 = new File("CeemRadarData/admin_border/admin_country")
	val emptySeqOfNone = Seq().asInstanceOf[Seq[scala.xml.Node]]

	var xml = XML.loadFile(fileConfig)

	val tagAdmin0 = <admin0 iso="" viewCountryBorder="false"/>

	def initWithAdmin0(){
		val files = dirWithAdmin0.listFiles()
		
		for(file <- files){
			val input = new ObjectInputStream(new FileInputStream(file))
			val ob = input.readObject()
			ob match {
				case admin: Admin0 =>
					xml = xml.copy(child = xml.child ++ admin0(admin) )
			}
			println(xml)
		}
	}

	def admin0(admin: Admin0) = {
		tagAdmin0 % Attribute(null, "iso", admin.admin0a3, scala.xml.Null )
	}



	xml = xml.copy(child = emptySeqOfNone) // Очищаем
	initWithAdmin0()

	XML.save(fileConfig.getAbsolutePath, xml)
}

/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.settings

import java.util.{Calendar, Properties}
import java.io.{FileInputStream, FileOutputStream}

/**
 * User: Aleo Bakalov
 * Date: 03.03.14
 * Time: 11:47
 */
object PropertyManager {

	val fileProperty = "CeemRadarConfig.properties"
	val properties = new Properties()

	def save(property: Property.PropertyValue){
		properties.setProperty(property.name, property.value.toString)
	}

	def saveAll(){
		for(property <- Property.values){
			properties.setProperty(property.name, property.value.toString)
		}
	}

	def save() = saveAll()

	def load(){
		var input: FileInputStream = null
		try{
			input = createFileInputStream(fileProperty)
			properties.load(input)

			for(property <- Property.values){
				val value = properties.getProperty(property.name, property.value.toString)
				property.value = value
			}
		} catch {
			case ex: Exception =>
				if(input != null){
					input.close()
				}
				ex.printStackTrace()
		}
	}

	def createFileOutputStream(fileName: String = fileProperty) = {
		new FileOutputStream(fileName)
	}

	def createFileInputStream(fileName: String = fileProperty) = {
		new FileInputStream(fileName)
	}

	def store(){
		var output: FileOutputStream = null
		try{
			output = createFileOutputStream(fileName = fileProperty)
			properties.store(output, null)
			output.flush()
			output.close()
		} catch {
			case ex: Exception =>
				if(output != null){
					output.close()
				}
				ex.printStackTrace()
		}
	}
}

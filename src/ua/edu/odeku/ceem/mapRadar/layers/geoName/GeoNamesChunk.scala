/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName

import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.render.{GeographicText, UserFacingText, DrawContext}
import ua.edu.odeku.ceem.mapRadar.db.model.GeoName
import ua.edu.odeku.ceem.mapRadar.utils.CeemUtilsFunctions.stringToBoolean

import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 10.07.2014
 * Time: 13:35
 */
class GeoNamesChunk(val geoNames: GeoNames) {

	val geoNameList = ua.edu.odeku.ceem.mapRadar.db.model.GeoNames.list(null, geoNames.country, geoNames.geoClass, geoNames.geoCode)

	def iterable(dc: DrawContext) = {
		val buff = new ArrayBuffer[GeographicText]()
		for(geoName <- geoNameList){
			val nameGeo = GeoNamesChunk.mkText(geoName)
			val pos = GeoNamesChunk.mkPosition(geoName)
			val text = new UserFacingText(nameGeo, pos)
			text.setFont(this.geoNames.font)
			text.setColor(this.geoNames.color)
			text.setBackgroundColor(this.geoNames.backgroundColor)
			text.setVisible(GeoNameLayer.isNameVisible(dc,this.geoNames, pos))
			text.setPriority(this.geoNames.maxDisplayDistance)
			buff += text
		}
		buff.toList
	}
}

object GeoNamesChunk {
	def mkText(geo: GeoName) = if (geo.translateName.isDefined) geo.name + '(' + geo.translateName + ')' else geo.name

	def mkPosition(geo: GeoName) = Position.fromDegrees(geo.lat, geo.lon, 0)
	
	

}

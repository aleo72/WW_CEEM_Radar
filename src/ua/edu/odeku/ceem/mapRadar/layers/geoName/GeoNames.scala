/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName

import java.awt.{Color, Font}
import java.io.File

import gov.nasa.worldwind.geom.{Angle, LatLon, Sector}
import gov.nasa.worldwind.util.{AbsentResourceList, Tile, Logging}

/**
 * User: Aleo Bakalov
 * Date: 10.07.2014
 * Time: 12:12
 */
class GeoNames(val country: String, val geoClass: String, val geoCode: String, val sector: Sector, val tileDelta: LatLon, val font: Font) {

	private val absentTiles = new AbsentResourceList(GeoNames.MAX_ABSENT_TILE_TRIES, GeoNames.MIN_ABSENT_TILE_CHECK_INTERVAL)
	var enable = true
	var color = Color.WHITE
	private var _backgroundColor: Color = null
	var minDisplayDistance = Double.MinValue
	var maxDisplayDistance = Double.MaxValue

	validate()

	var numColumns = this.numColumnsInLevel
	val typeGeoNames = country + "_" + geoClass + "_" + geoCode
	val classCode = geoClass + "_" + geoCode

	def validate() {
		var msg: String = ""
		if (this.geoClass == null) {
			msg += Logging.getMessage("nullValue.GeoNameClassIsNull") + ", "
		}
		if (this.geoCode == null) {
			msg += Logging.getMessage("nullValue.GeoNameCodeIsNull") + ", "
		}
		if (this.tileDelta == null) {
			msg += Logging.getMessage("nullValue.TileDeltaIsNull") + ", "
		}
		if (this.font == null) {
			msg += Logging.getMessage("nullValue.FontIsNull") + ", "
		}
		if (!msg.isEmpty) {
			Logging.logger().severe(msg)
			throw new IllegalArgumentException(msg)
		}
	}

	def numColumnsInLevel = {
		val firstCol = Tile.computeColumn(this.tileDelta.getLongitude, GeoNames.TILING_SECTOR.getMinLongitude, Angle.NEG180)
		val lastCol = Tile.computeColumn(this.tileDelta.getLongitude, GeoNames.TILING_SECTOR.getMaxLongitude.subtract(this.tileDelta.getLongitude), Angle.NEG180)
		lastCol - firstCol + 1
	}

	final def copy(): GeoNames = new GeoNames(country, geoClass, geoCode, sector, tileDelta, font)

	def backgroundColor = {
		if (_backgroundColor == null) {
			this._backgroundColor = GeoNames.suggestBackgroundColor(this.color)
		}
		_backgroundColor
	}

	def backgroundColor_=(value: Color): Unit = _backgroundColor = value

	override def equals(o: Any): Boolean = {
		if (this == o) return true
		if (o == null || this.getClass != o.getClass) return false
		val other: GeoNames = o.asInstanceOf[GeoNames]
		if (this.typeGeoNames != other.typeGeoNames) {
			false
		} else {
			true
		}
	}

	def fromDB = ua.edu.odeku.ceem.mapRadar.db.model.GeoNames.list(null, country, geoClass, geoCode)

	def markResourceAbsent(tileNumber: Long): Unit = this.synchronized {
		this.absentTiles.markResourceAbsent(tileNumber)
	}

	def isResourceAbsent(resourceNumber: Long): Boolean = this.synchronized {
		this.absentTiles.isResourceAbsent(resourceNumber)
	}

	final def unmarkResourceAbsent(tileNumber: Long): Unit = this.synchronized {
		this.absentTiles.unmarkResourceAbsent(tileNumber)
	}

	def createFileCachePathFromTile(row: Int, column: Int): String = {
		val message: String = Logging.getMessage("GeoNames.RowOrColumnOutOfRange", row, column)
		Logging.logger.severe(message)
		throw new IllegalArgumentException(message)
	}

	def tileNumber(row: Int, column: Int) = row * numColumns + column
}

object GeoNames {
	val TILING_SECTOR = Sector.FULL_SPHERE
	val MAX_ABSENT_TILE_TRIES = 2
	val MIN_ABSENT_TILE_CHECK_INTERVAL = 10000

	def suggestBackgroundColor(foreground: Color): Color = {
		val compArray: Array[Float] = new Array[Float](4)
		Color.RGBtoHSB(foreground.getRed, foreground.getGreen, foreground.getBlue, compArray)
		val colorValue: Int = if (compArray(2) < 0.5f) 255 else 0
		val alphaValue: Int = foreground.getAlpha
		new Color(colorValue, colorValue, colorValue, alphaValue)
	}
}

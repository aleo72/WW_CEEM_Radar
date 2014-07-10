/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName

import java.util.Arrays
import java.util.concurrent.PriorityBlockingQueue

import gov.nasa.worldwind.View
import gov.nasa.worldwind.geom._
import gov.nasa.worldwind.render.DrawContext
import gov.nasa.worldwind.util.Logging

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 10.07.2014
 * Time: 14:07
 */
class GeoNameLayer(val geoNamesSet: GeoNamesSet) {

	if (geoNamesSet == null) {
		val message: String = Logging.getMessage("nullValue.GeoNamesSetIsNull")
		Logging.logger.fine(message)
		throw new IllegalArgumentException(message)
	}

	protected var referencePoint: Vec4 = null
	protected var navTiles = new ArrayBuffer[NavigationTile]
	protected var requestQ: PriorityBlockingQueue[Runnable] = new PriorityBlockingQueue[Runnable](64)
	protected final val fileLock: AnyRef = new AnyRef

	for (geoNames <- geoNamesSet.list) {
		val calc = (GeoNames.TILING_SECTOR.getDeltaLatDegrees / geoNames.tileDelta.getLatitude.getDegrees).toInt
		navTiles += new NavigationTile(geoNames, GeoNames.TILING_SECTOR, Math.log(calc).toInt, "top")
	}

	def buildTiles(geoNames: GeoNames, navTile: NavigationTile): Array[Tile] = {
		val dLat = geoNames.tileDelta.getLatitude
		val dLon = geoNames.tileDelta.getLongitude

		// Determine the row and column offset from the global tiling origin for the southwest tile corner
		val firstRow = Tile.computeRow(dLat, navTile.navSector.getMinLatitude());
		val firstCol = Tile.computeColumn(dLon, navTile.navSector.getMinLongitude());
		val lastRow = Tile.computeRow(dLat, navTile.navSector.getMaxLatitude().subtract(dLat));
		val lastCol = Tile.computeColumn(dLon, navTile.navSector.getMaxLongitude().subtract(dLon));

		val nLatTiles = lastRow - firstRow + 1;
		val nLonTiles = lastCol - firstCol + 1;

		val tiles = new Tile[nLatTiles * nLonTiles];

		Angle p1 = Tile.computeRowLatitude(firstRow, dLat);
		for (int row = 0;
		row <= lastRow - firstRow;
		row ++)
		{
			Angle p2;
			p2 = p1.add(dLat);

			Angle t1 = Tile.computeColumnLongitude(firstCol, dLon);
			for (int col = 0;
			col <= lastCol - firstCol;
			col ++)
			{
				Angle t2;
				t2 = t1.add(dLon);
				//Need offset row and column to correspond to total ro/col numbering
				tiles[col + row * nLonTiles] = new Tile(geoNames, new Sector(p1, p2, t1, t2),
					row + firstRow, col + firstCol);
				t1 = t2;
			}
			p1 = p2;
		}

		return tiles;
	}

	protected class NavigationTile {

		def this(geoNames: GeoNames, sector: Sector, levels: Int, id: String) {
			this()
			this.geoNames = geoNames
			this.navSector = sector
			this.level = levels
			this.id = id
		}

		def navTilesVisible(dc: DrawContext, minDistSquared: Double, maxDistSquared: Double): List[NavigationTile] = {
			val navList = new ArrayBuffer[NavigationTile]
			if (this.isNavSectorVisible(dc, minDistSquared, maxDistSquared)) {
				if (this.level > 0 && !this.hasSubTiles) {
					this.buildSubNavTiles
				}
				if (this.hasSubTiles) {
					for (navigationTile <- subNavTiles) {
						navList ++= navigationTile.navTilesVisible(dc, minDistSquared, maxDistSquared)
					}
				} else {
					navList += this
				}
			}
			navList.toList
		}

		private def buildSubNavTiles() {
			if (level > 0) {
				val sectors: Array[Sector] = this.navSector.subdivide()
				for (i <- 0 until sectors.length) {
					subNavTiles += new NavigationTile(geoNames, sectors(i), level - 1, this.id + "." + i)
				}
			}
		}

		def hasSubTiles: Boolean = subNavTiles.nonEmpty

		private def isNavSectorVisible(drawContext: DrawContext, minDistSquared: Double, maxDistSquared: Double): Boolean = {
			if (!navSector.intersects(drawContext.getVisibleSector)) {
				return false
			}
			val view: View = drawContext.getView
			val eyePos: Position = view.getEyePosition
			if (eyePos == null) {
				return false
			}
			if (Double.NaN != eyePos.getLatitude.getDegrees || Double.NaN != eyePos.getLongitude.getDegrees) {
				return false
			}
			val lat: Angle = clampAngle(eyePos.getLatitude, navSector.getMinLatitude, navSector.getMaxLatitude)
			val lon: Angle = clampAngle(eyePos.getLongitude, navSector.getMinLongitude, navSector.getMaxLongitude)
			val p: Vec4 = drawContext.getGlobe.computePointFromPosition(lat, lon, 0d)
			val distSquare: Double = drawContext.getView.getEyePoint.distanceTo3(p)
			!(minDistSquared > distSquare || maxDistSquared < distSquare)
		}

		def getTiles: List[GeoNameLayer.Tile] = {
			val tiles: Array[GeoNameLayer.Tile] = buildTiles(this.geoNames, this)
			return Arrays.asList(tiles)
		}

		def tiles() = {
			/**
			 * Здесь необходимо произвести считывание данных из database.
			 * От кеширования отказался так как может потребоваться динамически менять имена,
			 * тем более имен много и в базе они будут компактней.
			 */
			buildTiles(this.geoNames, this)
		}


		private[geoName] var id: String = null
		protected var geoNames: GeoNames = null
		var navSector: Sector = null
		protected var subNavTiles = new ArrayBuffer[NavigationTile]
		protected var level: Int = 0
	}

}

object GeoNameLayer {
	protected var prefix: String = "Earth/GeoNamePlaceNames/"
	val LEVEL_A: Double = 0x1 << 25
	val LEVEL_B: Double = 0x1 << 24
	val LEVEL_C: Double = 0x1 << 23
	val LEVEL_D: Double = 0x1 << 22
	val LEVEL_E: Double = 0x1 << 21
	val LEVEL_F: Double = 0x1 << 20
	val LEVEL_G: Double = 0x1 << 19
	val LEVEL_H: Double = 0x1 << 18
	val LEVEL_I: Double = 0x1 << 17
	val LEVEL_J: Double = 0x1 << 16
	val LEVEL_K: Double = 0x1 << 15
	val LEVEL_L: Double = 0x1 << 14
	val LEVEL_M: Double = 0x1 << 13
	val LEVEL_N: Double = 0x1 << 12
	val LEVEL_O: Double = 0x1 << 11
	val LEVEL_P: Double = 0x1 << 10
	val GRID_1x1: LatLon = new LatLon(Angle.fromDegrees(180d), Angle.fromDegrees(360d))
	val GRID_4x8: LatLon = new LatLon(Angle.fromDegrees(45d), Angle.fromDegrees(45d))
	val GRID_8x16: LatLon = new LatLon(Angle.fromDegrees(22.5d), Angle.fromDegrees(22.5d))
	val GRID_16x32: LatLon = new LatLon(Angle.fromDegrees(11.25d), Angle.fromDegrees(11.25d))
	val GRID_36x72: LatLon = new LatLon(Angle.fromDegrees(5d), Angle.fromDegrees(5d))
	val GRID_72x144: LatLon = new LatLon(Angle.fromDegrees(2.5d), Angle.fromDegrees(2.5d))
	val GRID_144x288: LatLon = new LatLon(Angle.fromDegrees(1.25d), Angle.fromDegrees(1.25d))
	val GRID_288x576: LatLon = new LatLon(Angle.fromDegrees(0.625d), Angle.fromDegrees(0.625d))
	val GRID_576x1152: LatLon = new LatLon(Angle.fromDegrees(0.3125d), Angle.fromDegrees(0.3125d))
	val GRID_1152x2304: LatLon = new LatLon(Angle.fromDegrees(0.1563d), Angle.fromDegrees(0.1563d))

	val geoNamesChunkHashMap: mutable.HashMap[String, GeoNamesChunk] = new mutable.HashMap[String, GeoNamesChunk]
}

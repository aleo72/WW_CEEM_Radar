/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.layers.geoName

import java.awt.geom.Rectangle2D
import java.util.concurrent.PriorityBlockingQueue
import java.util.logging.Level

import gov.nasa.worldwind.cache.Cacheable
import gov.nasa.worldwind.geom._
import gov.nasa.worldwind.render.{DrawContext, GeographicText, UserFacingText}
import gov.nasa.worldwind.util.Logging
import gov.nasa.worldwind.{View, WorldWind}
import ua.edu.odeku.ceem.mapRadar.db.model.GeoName
import ua.edu.odeku.ceem.mapRadar.utils.CeemUtilsFunctions.NumberPower

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
    val firstRow = Tile.computeRow(dLat, navTile.navSector.getMinLatitude)
    val firstCol = Tile.computeColumn(dLon, navTile.navSector.getMinLongitude)
    val lastRow = Tile.computeRow(dLat, navTile.navSector.getMaxLatitude.subtract(dLat))
    val lastCol = Tile.computeColumn(dLon, navTile.navSector.getMaxLongitude.subtract(dLon))

    val nLatTiles = lastRow - firstRow + 1
    val nLonTiles = lastCol - firstCol + 1

    val tiles = new Array[Tile](nLatTiles * nLonTiles)

    var p1 = Tile.computeRowLatitude(firstRow, dLat)
    for (row <- 0 to (lastRow - firstRow)) {
      val p2 = p1.add(dLat)

      var t1 = Tile.computeColumnLongitude(firstCol, dLon)
      for (col <- 0 to (lastCol - firstCol)) {
        val t2 = t1.add(dLon)
        //Need offset row and column to correspond to total ro/col numbering
        tiles(col + row * nLonTiles) = new Tile(geoNames, new Sector(p1, p2, t1, t2),
          row + firstRow, col + firstCol)
        t1 = t2
      }
      p1 = p2
    }
    tiles
  }

  def drawOrRequestTile(context: DrawContext, tile: Tile, minDistSquared: Double, maxDistSquared: Double) = {
    if (GeoNameLayer.isTileVisible(context, tile, minDistSquared, maxDistSquared)) {
      val chunk = GeoNameLayer.geoNamesChunkHashMap.getOrElseUpdate(tile.geoNames.typeGeoNames, new GeoNamesChunk(tile.geoNames))

      import scala.collection.JavaConversions.asJavaIterable
      context.getDeclutteringTextRenderer.render(context, chunk.iterable(context).toIterable)
    }
  }

  def doRender(dc: DrawContext): Unit = {
    referencePoint = this.computeReferencePoint(dc)
    var index = -1
    for (geoNames: GeoNames <- geoNamesSet.list) {
      index += 1
      if (GeoNameLayer.isGeoNamesVisible(dc, geoNames)) {
        val minDistSquared = geoNames.minDisplayDistance ** 2
        val maxDistSquared = geoNames.maxDisplayDistance ** 2

        if (GeoNameLayer.isSectorVisible(dc, geoNames.sector, minDistSquared, maxDistSquared)) {
          val navigationTile = this.navTiles(index)
          val list = navigationTile.navTilesVisible(dc, minDistSquared, maxDistSquared)
          for (navTile <- list;
               tile <- navTile.tiles()) {
            try {
              drawOrRequestTile(dc, tile, minDistSquared, maxDistSquared)
            }
            catch {
              case e: Exception => Logging.logger.log(Level.FINE, Logging.getMessage("layers.GeoNameLayer.ExceptionRenderingTile"))
            }
          }
        }
      }
    }
    sendRequests()
    this.requestQ.clear()
  }

  def sendRequests() {
    var task: Runnable = this.requestQ.poll
    while (task != null) {
      if (!WorldWind.getTaskService.isFull) {
        WorldWind.getTaskService.addTask(task)
      }
      task = this.requestQ.poll
    }
  }

  def computeReferencePoint(drawContext: DrawContext): Vec4 = {
    if (drawContext.getViewportCenterPosition != null) {
      return drawContext.getGlobe.computePointFromPosition(drawContext.getViewportCenterPosition)
    }
    val viewport: Rectangle2D = drawContext.getView.getViewport
    val x = (viewport.getWidth / 2).toInt
    for( y <- (viewport.getHeight / 2).toInt.to(0, -1)){
      val pos = drawContext.getView.computePositionFromScreenPoint(x, y)
      if(pos != null){
        return drawContext.getGlobe.computePointFromPosition(pos.getLatitude, pos.getLongitude, 0.0)
      }
    }
    null
  }


  class NavigationTile {

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
          this.buildSubNavTiles()
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

    def buildSubNavTiles() {
      if (level > 0) {
        val sectors: Array[Sector] = this.navSector.subdivide()
        for (i <- 0 until sectors.length) {
          subNavTiles += new NavigationTile(geoNames, sectors(i), level - 1, this.id + "." + i)
        }
      }
    }

    def hasSubTiles: Boolean = subNavTiles.nonEmpty

    def isNavSectorVisible(drawContext: DrawContext, minDistSquared: Double, maxDistSquared: Double): Boolean = {
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
      val lat: Angle = GeoNameLayer.clampAngle(eyePos.getLatitude, navSector.getMinLatitude, navSector.getMaxLatitude)
      val lon: Angle = GeoNameLayer.clampAngle(eyePos.getLongitude, navSector.getMinLongitude, navSector.getMaxLongitude)
      val p: Vec4 = drawContext.getGlobe.computePointFromPosition(lat, lon, 0d)
      val distSquare: Double = drawContext.getView.getEyePoint.distanceTo3(p)
      !(minDistSquared > distSquare || maxDistSquared < distSquare)
    }

    def tiles() = {
      /**
       * Здесь необходимо произвести считывание данных из database.
       * От кеширования отказался так как может потребоваться динамически менять имена,
       * тем более имен много и в базе они будут компактней.
       */
      buildTiles(this.geoNames, this)
    }


    var id: String = null
    protected var geoNames: GeoNames = null
    var navSector: Sector = null
    protected var subNavTiles = new ArrayBuffer[NavigationTile]
    protected var level: Int = 0
  }

  object Tile {
    def computeRow(delta: Angle, latitude: Angle): Int = {
      if (delta == null || latitude == null) {
        val msg: String = Logging.getMessage("nullValue.AngleIsNull")
        Logging.logger.severe(msg)
        throw new IllegalArgumentException(msg)
      }
      ((latitude.getDegrees + 90d) / delta.getDegrees).toInt
    }

    def computeColumn(delta: Angle, longitude: Angle): Int = {
      if (delta == null || longitude == null) {
        val msg: String = Logging.getMessage("nullValue.AngleIsNull")
        Logging.logger.severe(msg)
        throw new IllegalArgumentException(msg)
      }
      ((longitude.getDegrees + 180d) / delta.getDegrees).toInt
    }

    def computeRowLatitude(row: Int, delta: Angle): Angle = {
      if (delta == null) {
        val msg: String = Logging.getMessage("nullValue.AngleIsNull")
        Logging.logger.severe(msg)
        throw new IllegalArgumentException(msg)
      }
      Angle.fromDegrees(-90d + delta.getDegrees * row)
    }

    def computeColumnLongitude(column: Int, delta: Angle): Angle = {
      if (delta == null) {
        val msg: String = Logging.getMessage("nullValue.AngleIsNull")
        Logging.logger.severe(msg)
        throw new IllegalArgumentException(msg)
      }
      Angle.fromDegrees(-180 + delta.getDegrees * column)
    }
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

  def isNameVisible(dc: DrawContext, geoNames: GeoNames, position: Position): Boolean = {
    val elevation = dc.getVerticalExaggeration * position.getElevation
    val namePoint = dc.getGlobe.computePointFromPosition(position.getLatitude, position.getLongitude, elevation)
    val eyeVec = dc.getView.getEyePoint
    val dist = eyeVec.distanceTo3(namePoint)
    dist >= geoNames.minDisplayDistance && dist <= geoNames.maxDisplayDistance
  }

  def clampAngle(a: Angle, min: Angle, max: Angle): Angle = {
    val degrees: Double = a.degrees
    val minDegrees: Double = min.degrees
    val maxDegrees: Double = max.degrees
    Angle.fromDegrees(if (degrees < minDegrees) minDegrees else if (degrees > maxDegrees) maxDegrees else degrees)
  }

  def isTileVisible(drawContext: DrawContext, tile: Tile, minDistSquared: Double, maxDistSquared: Double) = {
    if (!tile.sector.intersects(drawContext.getVisibleSector)) {
      false
    }

    val view: View = drawContext.getView
    val eyePos: Position = view.getEyePosition
    if (eyePos == null) {
      false
    }
    val lat: Angle = clampAngle(eyePos.getLatitude, tile.sector.getMinLatitude, tile.sector.getMaxLatitude)
    val lon: Angle = clampAngle(eyePos.getLongitude, tile.sector.getMinLongitude, tile.sector.getMaxLongitude)
    val p: Vec4 = drawContext.getGlobe.computePointFromPosition(lat, lon, 0d)
    val distSquared: Double = drawContext.getView.getEyePoint.distanceToSquared3(p)
    !(minDistSquared > distSquared || maxDistSquared < distSquared)
  }

  def isGeoNamesVisible(drawContext: DrawContext, geoNames: GeoNames): Boolean = {
    geoNames.enable && (drawContext.getVisibleSector != null) && geoNames.sector.intersects(drawContext.getVisibleSector)
  }

  def isSectorVisible(drawContext: DrawContext, sector: Sector, minDistSquared: Double, maxDistSquared: Double): Boolean = {
    val eyePos: Position = drawContext.getView.getEyePosition
    if (eyePos == null) {
      false
    }
    val lat: Angle = clampAngle(eyePos.getLatitude, sector.getMinLatitude, sector.getMaxLatitude)
    val lon: Angle = clampAngle(eyePos.getLongitude, sector.getMinLongitude, sector.getMaxLongitude)
    val p: Vec4 = drawContext.getGlobe.computePointFromPosition(lat, lon, 0d)
    val distSquared: Double = drawContext.getView.getEyePoint.distanceToSquared3(p)
    !(minDistSquared > distSquared || maxDistSquared < distSquared)
  }
}

class Tile(val geoNames: GeoNames, val sector: Sector, val row: Int, val column: Int) extends Cacheable {

  var _fileCachePath: String = null
  var dataChunk: GeoNamesChunk = null
  var priority: Double = .0

  def makeIterable(dc: DrawContext): Iterable[GeographicText] = {
    val maxDisplayDistance = this.geoNames.maxDisplayDistance
    val listGeoName: List[GeoName] = geoNames.fromDB
    for (geoName <- listGeoName) yield {
      val str = geoName.name
      val position: Position = Position.fromDegrees(geoName.lat, geoName.lon, 0d)
      val text: GeographicText = new UserFacingText(str, position)
      text.setFont(this.geoNames.font)
      text.setColor(this.geoNames.color)
      text.setBackgroundColor(this.geoNames.backgroundColor)
      text.setVisible(GeoNameLayer.isNameVisible(dc, this.geoNames, position))
      text.setPriority(maxDisplayDistance)
      text
    }
  }

  def isTileInMemoryWithData: Boolean = {
    val t = WorldWind.getMemoryCache(classOf[Tile].getName).getObject(fileCachePath).asInstanceOf[Tile]
    !(t == null || t.dataChunk == null)
  }

  def fileCachePath: String = {
    if (this._fileCachePath == null) {
      this._fileCachePath = this.geoNames.createFileCachePathFromTile(this.row, this.column)
    }
    _fileCachePath
  }

  def getSizeInBytes: Long = {
    var result: Long = 32
    result += sector.getSizeInBytes
    if (this.fileCachePath != null) result += this.fileCachePath.length
    result
  }
}
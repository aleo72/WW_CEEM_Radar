/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.tools.geoName.layer

import gov.nasa.worldwind.WWObjectImpl
import gov.nasa.worldwind.avlist.AVList
import gov.nasa.worldwind.util.Logging

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * User: Aleo Bakalov
 * Date: 10.07.2014
 * Time: 11:01
 */
class GeoNamesSet extends WWObjectImpl {

  private val geoNamesList = new mutable.HashSet[GeoNames]()
  var expiryTime = 0L

  def ++=(list: List[GeoNames], replace: Boolean = true) = list.foreach(this.+=(_, replace))

  def +=(geoNames: GeoNames, replace: Boolean = true) {
    if (geoNames != null) {
      if (geoNamesList.exists(_.equals(geoNames))) {
        if (replace) {
          geoNamesList += geoNames
        }
      } else {
        geoNamesList += geoNames
      }
    } else {
      val message = Logging.getMessage("nullValue.GeoNamesIsNull")
      Logging.logger().severe(message)
      throw new IllegalArgumentException(message)
    }
  }

  override def copy() = {
    val copy = new GeoNamesSet
    copy.setValues(this.asInstanceOf[AVList])

    copy.geoNamesList ++= this.geoNamesList
    copy.expiryTime = this.expiryTime
    copy
  }

  final def size = this.geoNamesList.size

  def list = this.geoNamesList.toList

  def get(classCode: String) = {
    geoNamesList.find(_.classCode == classCode)
  }
}

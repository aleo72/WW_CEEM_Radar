/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.properties

import java.io.{FileInputStream, FileOutputStream}
import java.util.Locale

/**
 * Программная оболочка для настроек
 * Created by aleo on 02.08.14.
 */

trait PropertiesTrait[T] {

  val key: String

  val defaultValue: T

  def store(properties: java.util.Properties, comment: String = "")(implicit out: FileOutputStream): Unit = {
    properties.store(out, comment)
  }

  def properties(implicit in: FileInputStream): java.util.Properties = {
    val p = new java.util.Properties()
    p.load(in)
    p
  }

  def putAndStore(string: String)(implicit in: FileInputStream, out: FileOutputStream) = {
    val prop = properties
    prop.setProperty(key, string)
    store(prop)
  }

  protected def stringToObject(valueString: String): T

  protected def objectToString(value: T): String

  def value(implicit in: FileInputStream): T = stringToObject(properties.getProperty(key, objectToString(defaultValue)))

  def value_=(obj: T)(implicit in: FileInputStream, out: FileOutputStream): Unit = putAndStore(objectToString(obj))

}

object PropertiesUtils {
  implicit def tupleToProperties(tuple: (String, String)) = StringProperties(tuple._1, tuple._2)

  implicit def tupleToProperties(tuple: (String, Locale)) = LocaleProperties(tuple._1, tuple._2)

  implicit def tupleToProperties(tuple: (String, Boolean)) = BooleanProperties(tuple._1, tuple._2)

  implicit def tupleToProperties(tuple: (String, Long)) = LongProperties(tuple._1, tuple._2)
}

case class StringProperties(key: String, defaultValue: String) extends PropertiesTrait[String] {

  override protected def stringToObject(valueString: String): String = valueString

  override protected def objectToString(value: String): String = value

}

case class BooleanProperties(key: String, defaultValue: Boolean) extends PropertiesTrait[Boolean] {

  override protected def stringToObject(valueString: String): Boolean = valueString.toBoolean

  override protected def objectToString(value: Boolean): String = value.toString

}

case class LocaleProperties(key: String, defaultValue: Locale) extends PropertiesTrait[Locale] {

  override protected def stringToObject(valueString: String): Locale = {
    val a = valueString.split("-")
    new Locale(a(0), a(1))
  }

  override protected def objectToString(value: Locale): String = s"${value.getLanguage}-${value.getCountry}"
}

case class LongProperties(key: String, defaultValue: Long) extends PropertiesTrait[Long] {

  override protected def stringToObject(valueString: String): Long = valueString.toLong

  override protected def objectToString(value: Long): String = value.toString

  def inc(implicit in: FileInputStream, out: FileOutputStream) = {
    val v = this.value
    this.value = v + 1
    v
  }

}

object Test extends App {

  import java.io.{File, FileInputStream, FileOutputStream}

  val file = new File("test.properties")
  file.createNewFile()
  file.getAbsolutePath

  implicit val in = new FileInputStream(file)

  implicit val out = new FileOutputStream(file)

  val test = StringProperties("testString", "testvalue")

  var v = test.value

  test.value = "none"

  println(test.value)

}
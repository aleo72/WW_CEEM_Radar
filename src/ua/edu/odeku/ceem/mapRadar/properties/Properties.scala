/*
 * Odessa State environmental University
 * Copyright (C) 2014
 */

package ua.edu.odeku.ceem.mapRadar.properties

/**
 * Программная оболочка для настроек
 * Created by aleo on 02.08.14.
 */

trait PropertiesTrait[T] {

  /**
   * Ключ за яким буде здерігатися настройка
   */
  val key: String

  /**
   * Значення за замовчуванням
   */
  val defaultValue: T

  /**
   * Настройки які були останнього разу зчитані
   */
  private var _properties: java.util.Properties = null

  private var lastModified = 0L

  /**
   * Метод збереження настройки
   * @param properties настройки для збереження
   * @param comment коментарій для збереження
   * @param file файл куди потрібно зберегти
   */
  def store(properties: java.util.Properties, comment: String = "")(implicit file: java.io.File) {
    var out: java.io.FileOutputStream = null
    try {
      out = new java.io.FileOutputStream(file)
      properties.store(out, comment)
    } finally {
      if (out != null) {
        out.close()
      }
    }
  }

  /**
   * Метод віддає настройки які є у файлі
   * @param file файл в якому зберігається настройки
   * @return настройки
   */
  def properties(implicit file: java.io.File): java.util.Properties = {
    if (!file.exists()) {
      file.createNewFile()
      lastModified = 0L
    }

    if (_properties == null || file.lastModified() > lastModified) {
      var in: java.io.FileInputStream = null
      try {
        in = new java.io.FileInputStream(file)
        _properties = new java.util.Properties()
        _properties.load(in)
      } finally {
        if (in != null) {
          in.close()
        }
      }
    }
    _properties
  }

  /**
   * Проміжний метод збереження настройки
   * @param string значення яке необхідно зберегти
   * @param file файл у який необхідно зберігти настройки
   */
  def putAndStore(string: String)(implicit file: java.io.File) {
    val prop = properties
    prop.setProperty(key, string)
    store(prop)
  }

  /**
   * Метод який перетворює строку у об’єкт
   * @param valueString строка яку необхідно перетворити у об’єкт
   * @return об’єкт
   */
  protected def stringToObject(valueString: String): T

  /**
   * Метод який перетворює об’єкт у строку
   * @param value об’єкт який необхідно перетворити у строку
   * @return об’єкт перетворений у строку
   */
  protected def objectToString(value: T): String

  /**
   * Метод верне збережений об’єкт
   * @param file файл з якого потрідбно зчитати
   * @return Об’єкт який зчитали з файлу
   */
  def value(implicit file: java.io.File): T = stringToObject(properties.getProperty(key, objectToString(defaultValue)))

  /**
   * Метод збереження об’єкта
   * @param obj об’єкт який необхідно зберігти
   * @param file файл у який необхідно зберігти
   * @return Unit
   */
  def value_=(obj: T)(implicit file: java.io.File): Unit = putAndStore(objectToString(obj))

}

/**
 * Об’єкт у якому зберігаються методі перетворення у об’єкти настроєк
 */
object PropertiesUtils {
  implicit def tupleToProperties(tuple: (String, String)) = StringProperties(tuple._1, tuple._2)

  implicit def tupleToProperties(tuple: (String, java.util.Locale)) = LocaleProperties(tuple._1, tuple._2)

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

case class LocaleProperties(key: String, defaultValue: java.util.Locale) extends PropertiesTrait[java.util.Locale] {

  override protected def stringToObject(valueString: String): java.util.Locale = {
    val a = valueString.split("-")
    new java.util.Locale(a(0), a(1))
  }

  override protected def objectToString(value: java.util.Locale): String = s"${value.getLanguage}-${value.getCountry}"
}

case class LongProperties(key: String, defaultValue: Long) extends PropertiesTrait[Long] {

  override protected def stringToObject(valueString: String): Long = valueString.toLong

  override protected def objectToString(value: Long): String = value.toString

  def inc(implicit file: java.io.File) = {
    val v = this.value
    this.value = v + 1
    v
  }

}

object Test extends App {

  import java.io.File

  implicit val file = new File("test.properties")
  file.createNewFile()
  file.getAbsolutePath

  val test = StringProperties("testString", "testvalue")

  var v = test.value

  test.value = "none"

  println(test.value)

}
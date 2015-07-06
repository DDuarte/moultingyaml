package net.jcazevedo.moultingyaml

import com.github.nscala_time.time.Imports._

sealed abstract class YamlValue {
  def convertTo[A](implicit reader: YamlReader[A]): A = reader.read(this)
}

case class YamlObject(fields: Map[YamlValue, YamlValue]) extends YamlValue

case class YamlArray(elements: Vector[YamlValue]) extends YamlValue

case class YamlSet(set: Set[YamlValue]) extends YamlValue

case class YamlString(value: String) extends YamlValue

case class YamlNumber[A: Numeric](value: A) extends YamlValue

case class YamlDate(date: DateTime) extends YamlValue

case class YamlBoolean(boolean: Boolean) extends YamlValue

case object YamlNull extends YamlValue

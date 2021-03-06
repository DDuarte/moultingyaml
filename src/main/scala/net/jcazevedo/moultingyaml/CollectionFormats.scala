package net.jcazevedo.moultingyaml

import scala.reflect.ClassTag

trait CollectionFormats {

  implicit def listFormat[A: YF] = new YF[List[A]] {
    def write(list: List[A]) = YamlArray(list.map(_.toYaml).toVector)
    def read(value: YamlValue): List[A] = value match {
      case YamlArray(elements) =>
        elements.map(_.convertTo[A])(collection.breakOut)
      case x =>
        deserializationError("Expected List as YamlArray, but got " + x)
    }
  }

  implicit def arrayFormat[A: YF: ClassTag] = new YF[Array[A]] {
    def write(array: Array[A]) = YamlArray(array.map(_.toYaml).toVector)
    def read(value: YamlValue) = value match {
      case YamlArray(elements) => elements.map(_.convertTo[A]).toArray
      case x =>
        deserializationError("Expected Array as YamlArray, but got " + x)
    }
  }

  implicit def setFormat[A: YF] = new YF[Set[A]] {
    def write(set: Set[A]) = YamlSet(set.map(_.toYaml))
    def read(value: YamlValue): Set[A] = value match {
      case YamlSet(elements) => elements.map(_.convertTo[A])
      case x =>
        deserializationError("Expected Set as YamlSet, but got " + x)
    }
  }

  implicit def mapFormat[K: YF, V: YF] = new YF[Map[K, V]] {
    def write(m: Map[K, V]) =
      YamlObject(m.map { case (k, v) => k.toYaml -> v.toYaml })

    def read(value: YamlValue) = value match {
      case x: YamlObject =>
        x.fields.map { case (k, v) => k.convertTo[K] -> v.convertTo[V] }
      case x =>
        deserializationError("Expected Map as YamlObject, but got " + x)
    }
  }

  import collection.{ immutable => imm }

  implicit def immIterableFormat[T: YF] =
    viaSeq[imm.Iterable[T], T](seq => imm.Iterable(seq: _*))

  implicit def immSeqFormat[T: YF] =
    viaSeq[imm.Seq[T], T](seq => imm.Seq(seq: _*))

  implicit def immIndexedSeqFormat[T: YF] =
    viaSeq[imm.IndexedSeq[T], T](seq => imm.IndexedSeq(seq: _*))

  implicit def immLinearSeqFormat[T: YF] =
    viaSeq[imm.LinearSeq[T], T](seq => imm.LinearSeq(seq: _*))

  implicit def vectorFormat[T: YF] =
    viaSeq[Vector[T], T](seq => Vector(seq: _*))

  import collection._

  implicit def iterableFormat[T: YF] =
    viaSeq[Iterable[T], T](seq => Iterable(seq: _*))

  implicit def seqFormat[T: YF] =
    viaSeq[Seq[T], T](seq => Seq(seq: _*))

  implicit def indexedSeqFormat[T: YF] =
    viaSeq[IndexedSeq[T], T](seq => IndexedSeq(seq: _*))

  implicit def linearSeqFormat[T: YF] =
    viaSeq[LinearSeq[T], T](seq => LinearSeq(seq: _*))

  def viaSeq[I <: Iterable[T], T: YF](f: imm.Seq[T] => I): YF[I] = new YF[I] {
    def write(iterable: I) = YamlArray(iterable.map(_.toYaml).toVector)
    def read(value: YamlValue) = value match {
      case YamlArray(elements) => f(elements.map(_.convertTo[T]))
      case x =>
        deserializationError("Expected Collection as YamlArray, but got " + x)
    }
  }
}

name := "moultingyaml"

organization := "net.jcazevedo"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.yaml"   %  "snakeyaml"   % "1.15",
  "org.specs2" %% "specs2-core" % "2.4.17" % "test")

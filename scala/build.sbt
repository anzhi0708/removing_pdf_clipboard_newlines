val scala3Version = "3.2.2"

// fork := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "pdf_copypaste.scala",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
	javaOptions ++= Seq(
  		"-Xms32m",
  		"-Xmx50m"
	)
  )

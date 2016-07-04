import sbt.datatype._

name := "example"

datatypeCodecNamespace in generateDatatypes in Compile := Some("com.example")

datatypeFormatsForType in generateDatatypes in Compile := { tpe =>
  val substitutions = Map("java.io.File" -> "com.example.FileFormat")
  val arrayFormat = if (tpe.repeated) List("sbt.datatype.ArrayFormat") else Nil
  CodecCodeGen.removeTypeParameters(tpe) match {
    case TpeRef(name, _, _) if substitutions contains name => substitutions(name) :: arrayFormat
    case                                                 _ => ((datatypeFormatsForType in generateDatatypes in Compile).value)(tpe) ++ arrayFormat
  }
}

libraryDependencies += "com.eed3si9n" %% "sjson-new-scalajson" % "0.4.0"

enablePlugins(DatatypePlugin)
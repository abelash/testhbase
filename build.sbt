name := "testhbasespark"

version := "0.3"

scalacOptions ++= Seq("-deprecation")

resolvers += Resolver.sonatypeRepo("releases")
//lazy val scriptClasspath = Seq("*")


//scalaVersion := "2.12.9"
scalaVersion := "2.11.12"

val junitVersion = "4.10"
val sparkVersion = "2.3.3"
val hbaseVersion = "2.2.0"
val hadoopVersion = "3.2.1"

libraryDependencies += "junit" % "junit" % junitVersion % Test
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

//libraryDependencies += "org.apache.hbase" % "hbase-client" % hbaseVersion
//libraryDependencies += "org.apache.hbase" % "hbase-common" % hbaseVersion
//libraryDependencies += "org.apache.hbase" % "hbase" % hbaseVersion
//libraryDependencies += "org.apache.hadoop" % "hadoop-common" % hadoopVersion

// include the common dir
//commonSourcePackages += "common"
//mainClass in (Compile, run) := Some("examples.SparkPiBe")
//mainClass in (Compile, packageBin) := Some("examples.SparkPiBe")

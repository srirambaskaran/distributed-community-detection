name := "distributed-community-detection"
version := "0.1"
scalaVersion := "2.11.11"
mainClass in assembly := Some("org.insight.cd.shortestpaths.APSP")
assemblyJarName in assembly := "apsp.jar"


val sparkVersion = "2.3.0"
val spark = "org.apache.spark"


libraryDependencies ++= Seq(
    spark %% "spark-core" % sparkVersion % "provided",
    spark %% "spark-graphx" % sparkVersion % "provided",
    "mysql" % "mysql-connector-java" % "6.0.6"
)


assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("org.slf4j",  xs @ _*) => MergeStrategy.first
    case _ => MergeStrategy.first
}






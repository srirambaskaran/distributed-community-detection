name := "distributed-community-detection"
version := "0.1"
scalaVersion := "2.11.11"
mainClass in assembly := Some("org.insight.cd.shortestpaths.APSP")
assemblyJarName in assembly := "apsp.jar"


val sparkVersion = "2.3.0"
val spark = "org.apache.spark"


resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
libraryDependencies ++= Seq(
    spark %% "spark-core" % sparkVersion % "provided",
    spark %% "spark-graphx" % sparkVersion % "provided",
    ("neo4j-contrib" % "neo4j-spark-connector" % "2.1.0-M4")
        .exclude("org.slf4j", "slf4j-api")
        .exclude("graphframes","graphframes")
        .exclude("com.typesafe.scala-logging","scala-logging-slf4j")
)

assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("org.slf4j",  xs @ _*) => MergeStrategy.first
    case _ => MergeStrategy.first
}






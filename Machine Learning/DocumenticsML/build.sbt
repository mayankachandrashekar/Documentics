name := "DocumenticsML"

version := "1.0"

scalaVersion := "2.11.8"


scalacOptions ++= Seq(
  "-optimize",
  "-unchecked",
  "-deprecation"
)

classpathTypes += "maven-plugin"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
  "org.apache.spark" %% "spark-streaming" % "1.6.0",
  "org.apache.spark" %% "spark-mllib" % "1.6.0",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.0" classifier "models",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.0",
  "com.github.scopt" % "scopt_2.10" % "3.4.0",
  "org.apache.opennlp" % "opennlp-tools" % "1.6.0",
  "org.apache.opennlp" % "opennlp-uima" % "1.6.0",
    "org.apache.opennlp" % "opennlp-maxent" % "3.0.3",
  "org.apache.lucene" % "lucene-core" % "6.0.0",
  "org.apache.lucene" % "lucene-queryparser" % "6.0.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "6.0.0"
)

resolvers ++= Seq(
  "Akka Repository" at "http://repo.akka.io/releases/",
  "scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "JavaCV maven repo" at "http://maven2.javacv.googlecode.com/git/",
  "JavaCPP maven repo" at "http://maven2.javacpp.googlecode.com/git/",
  Resolver.sonatypeRepo("public")
)

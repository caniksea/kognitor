import com.typesafe.sbt.packager.docker._
import sbt.Resolver

name := "kognitor"
 
version := "1.0" 

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, DebianPlugin, JavaAppPackaging, SystemdPlugin)

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

val PhantomVersion = "2.33.0"
val PlayFrameWorkVersion = "2.7.0"
val catsVersion = "1.6.0"
val circeVersion = "0.10.0"
val TwitterChillVersion = "0.9.3"
val MoshiVersion ="1.8.0"

maintainer := "Arinze Anikue "
packageSummary in Linux := "CPUT "
packageDescription := "RESEARCH API "

dockerCommands := Seq(
  Cmd("FROM", "azul/zulu-openjdk-alpine:11"),
  Cmd("RUN", "apk --no-cache add bash"),
  Cmd("MAINTAINER", maintainer.value),
  Cmd("WORKDIR", "/opt/docker"),
  Cmd("ADD", "/opt /opt"),
  ExecCmd("RUN", "chown", "-R", "daemon:daemon", "."),
  Cmd("USER", "daemon"),
  Cmd("WORKDIR", "/opt/docker"),
  ExecCmd("ENTRYPOINT", "bin/kognitor"),
  Cmd("VOLUME", "/opt/docker"),
  Cmd("EXPOSE", "9000"),
  Cmd("EXPOSE", "9999"),
  Cmd("EXPOSE", "8888")
)

javaOptions in Universal ++= Seq(
  // JVM memory tuning
  "-J-Xms4g",
  "-J-Xmx4g",
  "-J-Xmn2g"
)



libraryDependencies ++= Seq("io.circe" %% "circe-core", "io.circe" %% "circe-generic", "io.circe" %% "circe-parser"
).map(_ % circeVersion)




libraryDependencies += guice
libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" % "scalatestplus-play_2.12" % "3.1.2" % "test"
libraryDependencies += "org.scalactic" % "scalactic_2.12" % "3.0.5"
// https://mvnrepository.com/artifact/com.twitter/chill-akka
libraryDependencies += "com.twitter" % "chill_2.12" % TwitterChillVersion
libraryDependencies += "com.twitter" % "chill-akka_2.12" %  TwitterChillVersion

libraryDependencies += "org.typelevel" % "cats-core_2.12" % catsVersion
libraryDependencies += "org.typelevel" %% "cats-effect" % "0.10.1"


// https://mvnrepository.com/artifact/com.cra.figaro/figaro
libraryDependencies += "com.cra.figaro" %% "figaro" % "5.0.0.0"


libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
libraryDependencies += "com.typesafe.play" % "play-json_2.12" % "2.7.1"



libraryDependencies += "com.github.romix.akka" % "akka-kryo-serialization_2.12" % "0.5.2"
libraryDependencies += "com.esotericsoftware" % "kryo" % "4.0.1"

libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "3.10.0"
libraryDependencies += "com.squareup.moshi" % "moshi" % "1.6.0"
libraryDependencies += "com.squareup.moshi" % "moshi-adapters" % "1.6.0"



libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.7"
libraryDependencies += "commons-io" % "commons-io" % "2.6"




libraryDependencies += "com.typesafe.play" % "play-iteratees_2.12" % "2.6.1"
libraryDependencies += "com.typesafe.play" % "play-iteratees-reactive-streams_2.12" % "2.6.1"

libraryDependencies += "com.outworkers" % "phantom-dsl_2.12" % PhantomVersion
libraryDependencies += "com.outworkers" % "phantom-connectors_2.12" % PhantomVersion
libraryDependencies += "com.outworkers" % "phantom-streams_2.12" % PhantomVersion
libraryDependencies += "com.outworkers" % "phantom-jdk8_2.12" % PhantomVersion


libraryDependencies += "com.typesafe.play" % "play-akka-http-server_2.12" % PlayFrameWorkVersion
libraryDependencies += "com.typesafe.play" % "play-guice_2.12" % PlayFrameWorkVersion
libraryDependencies += "com.typesafe.play" % "play-ws_2.12" % PlayFrameWorkVersion

// https://mvnrepository.com/artifact/com.softwaremill.sttp/core
libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.5.2"
// https://mvnrepository.com/artifact/com.softwaremill.sttp/circe
libraryDependencies += "com.softwaremill.sttp" %% "circe" % "1.5.2"
// https://mvnrepository.com/artifact/com.softwaremill.sttp/akka-http-backend
libraryDependencies += "com.softwaremill.sttp" %% "async-http-client-backend-future" % "1.5.2"
// https://mvnrepository.com/artifact/com.softwaremill.sttp/json4s
libraryDependencies += "com.softwaremill.sttp" %% "play-json" % "1.5.2"
// https://mvnrepository.com/artifact/org.json4s/json4s-native


resolvers ++= Seq(
  "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "softprops-maven" at "http://dl.bintray.com/content/softprops/maven",
  "Brando Repository" at "http://chrisdinn.github.io/releases/",
  "Sonatype repo" at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging" at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
  "Twitter Repository" at "http://maven.twttr.com",
  "Websudos releases" at "https://dl.bintray.com/websudos/oss-releases/",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("public")
)



      
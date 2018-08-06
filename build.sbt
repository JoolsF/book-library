lazy val akkaHttpVersion = "10.1.3"
lazy val akkaVersion    = "2.5.14"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.joolsf",
      scalaVersion    := "2.12.6"
    )),
    name := "book-library",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
//      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,
      "org.mockito"       % "mockito-core"          % "1.8.5"         % Test,
      "io.circe"          %% "circe-core"           % "0.9.3",
      "io.circe"          %% "circe-generic"        % "0.9.3",
      "io.circe"          %% "circe-parser"         % "0.9.3",
      "de.heikoseeberger" %% "akka-http-circe" % "1.21.0"
    )
  )

lazy val appName = "api-stop-autodeploy-test"

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / majorVersion := 1

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val microservice = Project(appName, file("."))
    .enablePlugins(PlayScala, SbtDistributablesPlugin)
    .disablePlugins(JUnitXmlReportPlugin)
    .settings(CodeCoverageSettings.settings: _*)
    .settings(
      libraryDependencies ++= AppDependencies(),
      Test / parallelExecution := false,
      Test / fork:= false,
      retrieveManaged := true
    )
    .settings(
      Compile / unmanagedResourceDirectories  += baseDirectory.value / "resources"
    )
    .settings(
      scalacOptions ++= Seq(
        // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
        // suppress warnings in generated routes files
        "-Wconf:src=routes/.*:s"
      )
    )

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  // Coverage does not need compile !
  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "coverage" :: "run-all-tests" :: "coverageOff" :: "coverageAggregate" :: state }
)

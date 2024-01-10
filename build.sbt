import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._

lazy val appName = "api-stop-autodeploy-test"

scalaVersion := "2.13.12"

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
    .enablePlugins(PlayScala, SbtDistributablesPlugin)
    .disablePlugins(JUnitXmlReportPlugin)
    .settings(playSettings: _*)
    .settings(scalaSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(CodeCoverageSettings.settings: _*)
    .settings(
      name := appName,
      majorVersion := 1,
      libraryDependencies ++= AppDependencies(),
      Test / parallelExecution := false,
      Test / fork:= false,
      retrieveManaged := true,
      routesGenerator := InjectedRoutesGenerator
    )
    .settings(addTestReportOption(Test, "test-reports"))
    .settings(
      Compile / unmanagedResourceDirectories  += baseDirectory.value / "resources"
    )
    .settings(
      scalacOptions ++= Seq(
        "-Wconf:cat=unused&src=views/.*\\.scala:s",
        "-Wconf:cat=unused&src=.*RoutesPrefix\\.scala:s",
        "-Wconf:cat=unused&src=.*Routes\\.scala:s",
        "-Wconf:cat=unused&src=.*ReverseRoutes\\.scala:s"
      )
    )

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  // Coverage does not need compile !
  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "coverage" :: "run-all-tests" :: "coverageOff" :: "coverageAggregate" :: state }
)

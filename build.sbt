import _root_.play.core.PlayVersion
import _root_.play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._

lazy val appName = "api-stop-autodeploy-test"

lazy val plugins: Seq[Plugins] = Seq(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)

lazy val playSettings: Seq[Setting[_]] = Seq.empty
lazy val microservice = (project in file("."))
    .enablePlugins(plugins: _*)
    .settings(playSettings: _*)
    .settings(scalaSettings: _*)
    .settings(publishingSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      name := appName,
      majorVersion := 1,
      scalaVersion := "2.12.12",
      libraryDependencies ++= appDependencies,
      Test / parallelExecution := false,
      Test / fork:= false,
      retrieveManaged := true,
      routesGenerator := InjectedRoutesGenerator
    )
    .settings(Test / testOptions := Seq(Tests.Filter(unitFilter)),
      addTestReportOption(Test, "test-reports")
    )
    .settings(
      Compile / unmanagedResourceDirectories  += baseDirectory.value / "resources"
    )
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    // .settings(ivyScala := ivyScala.value map {
    //   _.copy(overrideScalaVersion = true)
    // })

lazy val appDependencies: Seq[ModuleID] = compile ++ test

lazy val hmrcBootstrapPlay28Version = "7.12.0"
lazy val scalaJVersion = "2.4.1"
lazy val mockitoVersion = "1.10.19"
lazy val wireMockVersion = "2.21.0"


lazy val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "bootstrap-backend-play-28" % hmrcBootstrapPlay28Version
)

lazy val test = Seq(
  "uk.gov.hmrc" %% "bootstrap-test-play-28" % hmrcBootstrapPlay28Version % Test,
  "org.pegdown" % "pegdown" % "1.6.0" % "test",
  "org.webjars" %% "webjars-play" % "2.8.8" % "test",
  "org.mockito" %% "mockito-scala-scalatest" % "1.16.46" % Test,
  "org.scalaj" %% "scalaj-http" % scalaJVersion % "test",
  "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % "test",
  "info.cukes" %% "cucumber-scala" % "1.2.5" % "test",
  "info.cukes" % "cucumber-junit" % "1.2.5" % "test"
)

def unitFilter(name: String): Boolean = name startsWith "unit"

// Coverage configuration
coverageMinimum := 60
coverageFailOnMinimum := true
coverageExcludedPackages := "<empty>;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo"

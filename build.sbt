import _root_.play.core.PlayVersion
import _root_.play.sbt.PlayImport._
import play.routes.compiler.StaticRoutesGenerator
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._

lazy val appName = "api-stop-autodeploy-test"

lazy val plugins: Seq[Plugins] = Seq(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)

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
      targetJvm := "jvm-1.8",
      scalaVersion := "2.11.11",
      libraryDependencies ++= appDependencies,
      parallelExecution in Test := false,
      fork in Test := false,
      retrieveManaged := true,
      routesGenerator := StaticRoutesGenerator
    )
    .settings(testOptions in Test := Seq(Tests.Filter(unitFilter)),
      addTestReportOption(Test, "test-reports")
    )
    .settings(
      unmanagedResourceDirectories in Compile += baseDirectory.value / "resources"
    )
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    .settings(
      resolvers += Resolver.bintrayRepo("hmrc", "releases"),
      resolvers += Resolver.jcenterRepo)
    .settings(ivyScala := ivyScala.value map {
      _.copy(overrideScalaVersion = true)
    })

lazy val appDependencies: Seq[ModuleID] = compile ++ test


lazy val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "microservice-bootstrap" % "6.18.0"
)

lazy val test = Seq(
  "uk.gov.hmrc" %% "hmrctest" % "3.0.0" % "test",
  "org.scalaj" %% "scalaj-http" % "2.4.0" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test",
  "com.typesafe.play" %% "play-test" % PlayVersion.current % "test",
  "com.github.tomakehurst" % "wiremock" % "1.58" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "info.cukes" %% "cucumber-scala" % "1.2.5" % "test",
  "info.cukes" % "cucumber-junit" % "1.2.5" % "test"
)

def unitFilter(name: String): Boolean = name startsWith "unit"

// Coverage configuration
coverageMinimum := 25
coverageFailOnMinimum := true
coverageExcludedPackages := "<empty>;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo"

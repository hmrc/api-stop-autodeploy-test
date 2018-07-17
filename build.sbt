import _root_.play.core.PlayVersion
import _root_.play.sbt.PlayImport._
import play.routes.compiler.StaticRoutesGenerator
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._

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
  "uk.gov.hmrc" %% "microservice-bootstrap" % "5.14.0",
  "uk.gov.hmrc" %% "play-health" % "2.1.0",
  "uk.gov.hmrc" %% "play-config" % "4.3.0",
  "uk.gov.hmrc" %% "logback-json-logger" % "3.1.0"
)

lazy val test = Seq(
  "uk.gov.hmrc" %% "hmrctest" % "2.4.0" % "test",
  "org.scalaj" %% "scalaj-http" % "1.1.6" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test",
  "com.typesafe.play" %% "play-test" % PlayVersion.current % "test",
  "com.github.tomakehurst" % "wiremock" % "1.58" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "info.cukes" %% "cucumber-scala" % "1.2.5" % "test",
  "info.cukes" % "cucumber-junit" % "1.2.5" % "test"
)

def unitFilter(name: String): Boolean = name startsWith "unit"

def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
  tests map {
    test => Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
  }

// Coverage configuration
coverageMinimum := 20
coverageFailOnMinimum := true
coverageExcludedPackages := "<empty>;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo"

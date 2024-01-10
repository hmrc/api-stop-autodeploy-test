import sbt._

object AppDependencies {

  def apply(): Seq[ModuleID] = compile ++ test

  lazy val bootstrapVersion = "7.15.0"

  lazy val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % bootstrapVersion
  )

  lazy val test = Seq(
    "uk.gov.hmrc"           %% "bootstrap-test-play-28"  % bootstrapVersion,
    "org.pegdown"            % "pegdown"                 % "1.6.0",
    "org.webjars"           %% "webjars-play"            % "2.8.8",
    "org.mockito"           %% "mockito-scala-scalatest" % "1.17.29",
    "org.scalatest"         %% "scalatest"               % "3.2.17",
    "com.vladsch.flexmark"   % "flexmark-all"            % "0.62.2",
    "org.scalaj"            %% "scalaj-http"             % "2.4.2",
    "com.github.tomakehurst" % "wiremock-jre8"           % "2.21.0",
    "io.cucumber"           %% "cucumber-scala"          % "5.7.0",
    "io.cucumber"            % "cucumber-junit"          % "5.7.0"
  ).map(_ % "test")
}

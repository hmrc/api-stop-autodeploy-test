import sbt._

object AppDependencies {

  def apply(): Seq[ModuleID] = compile ++ test

  lazy val bootstrapVersion = "9.0.0"

  lazy val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % bootstrapVersion
  )

  lazy val test = Seq(
    "uk.gov.hmrc"           %% "bootstrap-test-play-30"  % bootstrapVersion,
    "org.pegdown"            % "pegdown"                 % "1.6.0",
    "org.mockito"           %% "mockito-scala-scalatest" % "1.17.29"
  ).map(_ % "test")
}

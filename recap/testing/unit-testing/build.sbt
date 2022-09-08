scalaVersion := "3.0.0"

libraryDependencies += "org.scalameta" %% "munit" % "0.7.26" % Test
testFrameworks += new TestFramework("munit.Framework")

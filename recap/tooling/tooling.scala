package area

val facade = 5 * 3

package price

val paint = 3.5
val facade = area.facade * paint

// Good practice is to put source files in subdirectoris
// mirroring the packages stucture

// For an object 'Hello' in a package 'effective.example'
// it would be 'src/main/scala/effective/example/Hello.scala'

// All members from packages 'scala', 'java.lang' and object 'scala.Predef'
// are imported by automatically

// An sbt project is a directory with 2 files:
//   project/build.properties
//   build.sbt

// By default sbt comples files in 'src/main/scala/...'

// In sbt:

// 'compile' to compile
// If you modify some file, sbt will recompile only this file
// and all files depending on it, but not more.

// 'console' to open REPL
// But this repl can refer to your source files objects.

scala-yaml
==========

A small Scala library that can parse very basic [YAML](http://yaml.org)
documents that might meet fundamental needs for YAML support.


Installation
------------

### SBT ###

Add the following line to your `build.sbt` file

~~~ scala
libraryDependencies += "de.vorb" % "yaml" % "0.0.+"
~~~


Usage
-----

Here is an example.

~~~ scala
import de.vorb.util.parsing.yaml.YAML

val yamlDocuments =
  """|---
     |title: Sample document
     |created: 2013-07-09
     |tags: [ english, dev, scala ]
     |---
     |title: Sample document 2
     |created: 2013-07-10
     |tags:
     |  - english
     |  - scala
     |  - parsing
     |...
     |""".stripMargin

val results = YAML.parseAll(YAML.documents, yamlDocuments)

println(results.get == List(Map(
  "title" -> "Sample document",
  "created" -> "2013-07-09",
  "tags" -> List("english", "dev", "scala")
), Map(
  "title" -> "Sample document 2",
  "created" -> "2013-07-10",
  "tags" -> List("english", "scala", "parsing")
))) // will print "true"
~~~

There's also `YAML.document` which parses single documents only (not separated
by `---`).


License
-------

Copyright © 2013 Paul Vorbach

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the “Software”), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

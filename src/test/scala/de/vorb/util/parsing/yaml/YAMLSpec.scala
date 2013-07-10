package de.vorb.util.parsing.yaml

import org.scalatest.FunSpec

class YAMLSpec extends FunSpec {

  val single =
    """|title: Sample document
       |created: 2013-07-09
       |tags: [ english, dev, scala ]""".stripMargin

  val multiple =
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

  describe("YAML.document") {
    it("should parse complex single YAML documents") {
      assert(YAML.parseAll(YAML.document, single).get ===
        Map(
          "title" -> "Sample document",
          "created" -> "2013-07-09",
          "tags" -> ("english" :: "dev" :: "scala" :: Nil)
        )
      )
    }
  }

  describe("YAML.documents") {
    it("should parse sequences of complex YAML documents") {
      assert(YAML.parseAll(YAML.documents, multiple).get ===
        List(Map(
          "title" -> "Sample document",
          "created" -> "2013-07-09",
          "tags" -> ("english" :: "dev" :: "scala" :: Nil)
        ), Map(
          "title" -> "Sample document 2",
          "created" -> "2013-07-10",
          "tags" -> ("english" :: "scala" :: "parsing" :: Nil)
        ))
      )
    }
  }
}

package de.vorb.util.parsing.yaml

import scala.util.parsing.combinator._

object YAML extends RegexParsers {
  override def skipWhitespace = false

  def `\n` = """ *(\r?\n)+""".r
  def `:` = """: *(\r?\n)?""".r
  def `,` = """ *, +""".r
  def `[` = """\[ *""".r
  def `]` = """ *+\]""".r
  def `{` = """ *\{""".r
  def `}` = """ *\}""".r
  def listIndicator = """- *(\r?\n)?""".r
  def unquotedString = """[^,:\]]+""".r
  def mapKey = """[^-:\r\n\}\{\[]+""".r

  def indentation(indent: Int): Parser[Int] =
    ("^ {" + indent + ",}").r ^^ { _.length }

  def map(indent: Int): Parser[Map[String, Any]] =
    (indentedMap(indent) | inlineMap) ^^ { _.toMap }

  def inlineMap: Parser[Map[String, Any]] =
    `{` ~> repsep(inlineMapping, `,`) <~ `}` ^^ { _.toMap }
  def inlineMapping: Parser[(String, Any)] =
    mapKey ~ `:` ~ (inlineList | inlineMap | scalar("""[^,\r\n\}]+""")) ^^
      { case key ~ _ ~ value => (key, value) }

  def indentedMap(indent: Int): Parser[List[(String, Any)]] =
    rep1sep(indentedMapping(indent), `\n`)
  def indentedMapping(indent: Int): Parser[(String, Any)] =
    indentation(indent) ~> mapKey ~ `:` ~
      (list(0) | map(indent + 1) | scalar("""[^\r\n]+""")) ^^
      { case key ~ _ ~ value => (key, value) }

  def list(indent: Int): Parser[List[Any]] =
    (inlineList | indentedList(indent)) ^^ { List() ++ _ }

  def inlineList: Parser[List[Any]] =
    `[` ~> repsep(nestedListData(0), `,`) <~ `]`
  def indentedList(indent: Int): Parser[List[Any]] =
    rep1sep(indentation(indent) ~ listIndicator ~> nestedListData(indent), `\n`)
  def nestedListData(indent: Int): Parser[Any] =
    (list(indent + 1) | map(indent) | scalar("""[^,\r\n\]]+"""))

  def scalar(regexString: String): Parser[String] =
    regex(regexString.r) ^^ { case value => value.trim }

  def documentSeparator: Parser[Any] = "---" <~ `\n`
  def documentTerminator: Parser[Any] = "..." <~ opt(`\n`)

  def document: Parser[Any] = opt(`\n`) ~> (list(0) | map(0)) <~ opt(`\n`)

  def documents: Parser[List[Any]] =
    rep(documentSeparator ~> document) <~ opt(documentTerminator)
}

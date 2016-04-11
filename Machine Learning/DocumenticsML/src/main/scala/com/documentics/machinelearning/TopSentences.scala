package com.documentics.machinelearning

import java.io.{FileInputStream, InputStream}

import opennlp.tools.tokenize.{Tokenizer, TokenizerME, TokenizerModel}
import org.apache.spark.SparkContext
import org.json4s.jackson.JsonMethods._

/**
  * Created by Mayanka on 10-Apr-16.
  */
object TopSentences {

  def topSentences(line: String, tfidf: String, sc: SparkContext) {
    val modelIn: InputStream = new FileInputStream("models\\en-token.bin")
    val modelTokenizer: TokenizerModel = new TokenizerModel(modelIn)
    modelIn.close
    val tokenizer: Tokenizer = new TokenizerME(modelTokenizer)
    val tfidfJson = parse(tfidf)
    val stopWords = sc.broadcast(loadStopWords("/stopwords.txt")).value

  }
/*

  def stopWords(line:String): Set[String] ={
    val d=scala.io.Source.fromURL(getClass.getResource("data/stopwords.txt")).getLines().toSet
  }
*/

  def loadStopWords(path: String): Set[String] =
    scala.io.Source.fromURL(getClass.getResource(path))
      .getLines().toSet

  def isOnlyLetters(str: String): Boolean = {
    // While loop for high performance
    var i = 0
    while (i < str.length) {
      if (!Character.isLetter(str.charAt(i))) {
        return false
      }
      i += 1
    }
    true
  }

}

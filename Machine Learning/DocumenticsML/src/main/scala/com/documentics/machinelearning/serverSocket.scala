package com.documentics.machinelearning

import java.io.{FileInputStream, InputStream, PrintStream}
import java.net._

import com.documentics.machinelearning.namedentity.MainNamedEntity
import opennlp.tools.namefind.TokenNameFinderModel
import opennlp.tools.tokenize.{Tokenizer, TokenizerME, TokenizerModel}
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.JsonDSL._
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, JValue}

import scala.io._

/**
  * Created by Mayanka on 09-Apr-16.
  */
object serverSocket {
  def main(args: Array[String]) {

    System.setProperty("hadoop.home.dir", "F:\\winutils")
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark-Machine_Learning-Text-1").set("spark.driver.memory", "3g").set("spark.executor.memory", "3g")
    val sc = new SparkContext(sparkConf)
    val server = new ServerSocket(9999)
    var flag = false
    val modelIn: InputStream = new FileInputStream("models\\en-token.bin")
    val modelTokenizer: TokenizerModel = new TokenizerModel(modelIn)
    modelIn.close
    val tokenizer: Tokenizer = new TokenizerME(modelTokenizer)
    var is: InputStream = new FileInputStream("models/en-university.bin")
    val modelUniversity: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close
    is = new FileInputStream("models/en-ner-person.bin")
    val modelName: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close
    is = new FileInputStream("models/en-ner-organization.bin")
    val modelOrganisation: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close
    is = new FileInputStream("models/en-ner-time.bin")
    val modelTime: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close
    is = new FileInputStream("models/en-ner-date.bin")
    val modelDate: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close
    is = new FileInputStream("models/en-ner-location.bin")
    val modelLocation: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close
    is = new FileInputStream("models/en-ner-money.bin")
    val modelMoney: TokenNameFinderModel = new TokenNameFinderModel(is)
    is.close


    var output = ""
    while (true) {
      val s = server.accept()
      if (flag) {
        val out = new PrintStream(s.getOutputStream())
        println(output)
        out.println(output)
        out.flush()
        flag = false
        s.close()
      }
      else {
        val in = new BufferedSource(s.getInputStream()).getLines()
        val input = in.mkString(" ")
        if (input.contains("ANALYZE")) {
          val splitInput = input.split("ANALYZE :")
          val output1 = tfIdf.fmain(splitInput(1), sc)
          val output2 = /*sentiment.fmain(splitInput(1))*/ "none"
          val line = splitInput(1)
          val nameOutput: String = MainNamedEntity.findEntity(line, tokenizer, modelName)
          // val nameUniversity: String = MainNamedEntity.findEntity(line, tokenizer, modelUniversity)
          val Organisation: String = MainNamedEntity.findEntity(line, tokenizer, modelOrganisation)
          val time: String = MainNamedEntity.findEntity(line, tokenizer, modelTime)
          val date: String = MainNamedEntity.findEntity(line, tokenizer, modelDate)
          val location: String = MainNamedEntity.findEntity(line, tokenizer, modelLocation)
          val money: String = MainNamedEntity.findEntity(line, tokenizer, modelMoney)
          val topSentences=tfIdf.TopSentences(line,sc)
          val tfJa = parse(output1)

          val jv: JValue =
            ("analysis" ->
              ("tfidf" -> tfJa) ~
                ("sentiment" -> output2) ~
                //("university entities" -> parse(nameUniversity)) ~
                ("nameEntities" -> parse(nameOutput)) ~
                ("organisationEntities" -> parse(Organisation)) ~
                ("timeEntities" -> parse(time)) ~
                ("dateEntities" -> parse(date)) ~
                ("locationEntities" -> parse(location)) ~
                ("moneyEntities" -> parse(money))~
                  ("topSentences" -> topSentences))
          val jss = Json(DefaultFormats).write(jv)



          output = jss
          flag = true
          s.close()
        }
      }
    }
  }

}

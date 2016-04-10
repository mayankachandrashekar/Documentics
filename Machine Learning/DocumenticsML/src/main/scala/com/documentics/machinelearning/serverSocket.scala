package com.documentics.machinelearning

import java.io.PrintStream
import java.net._

import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.JsonDSL._
import org.json4s.jackson.Json
import org.json4s.{DefaultFormats, JValue}
import org.json4s.jackson.JsonMethods._
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
          val output2 = sentiment.fmain(splitInput(1))
          val ja=parse(output1)
          val jv: JValue =
            ("analysis" ->
              ("tf-idf" -> ja)~
                ("sentiment" -> output2) )
          val jss = Json(DefaultFormats).write(jv)

         

          output = jss
          flag = true
          s.close()
        }
      }
    }
  }

}

package com.documentics.machinelearning

import java.io.PrintStream
import java.net._

import org.apache.spark.{SparkConf, SparkContext}

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

    var output=""
    while (true) {
      val s = server.accept()
      if(flag)
        {
          val out = new PrintStream(s.getOutputStream())
          println(output)
          out.println(output)
          out.flush()
          flag=false
          s.close()
        }
      else {
        val in = new BufferedSource(s.getInputStream()).getLines()
        val input = in.mkString(" ")
        if (input.contains("TF-IDF")) {
          val splitInput = input.split("TF-IDF :")
          output = tfIdf.fmain(splitInput(1), sc)
          // val output="Hello!!!"
         flag=true
          s.close()
        }
      }

    }
  }

}

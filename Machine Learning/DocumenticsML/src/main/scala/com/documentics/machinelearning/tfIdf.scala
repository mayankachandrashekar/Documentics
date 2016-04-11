package com.documentics.machinelearning

import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.{HashMap, ListMap}


object tfIdf {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "F:\\winutils")
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark-Machine_Learning-Text-1").set("spark.driver.memory", "3g").set("spark.executor.memory", "3g")
    val sc = new SparkContext(sparkConf)
    val args = "Computer science is the1111111 the1111111 the1111111 the1111111 the1111111 scientific and practical approach to computation and its applications. \nIt is the systematic study of the feasibility, structure, expression, and mechanization of the methodical procedures (or algorithms) that underlie the acquisition, representation, processing, storage, communication of, and access to information. \nAn alternate, more succinct definition of computer science is the study of automating algorithmic processes that scale. \nA computer scientist specializes in the theory of computation and the design of computational systems.[1]\n\nIts fields can be divided into a variety of theoretical and practical disciplines. \nSome fields, such as computational complexity theory (which explores the fundamental properties of computational and intractable problems), are highly abstract, while fields such as computer graphics emphasize real-world visual applications. \nStill other fields focus on challenges in implementing computation. \nFor example, programming language theory considers various approaches to the description of computation, while the study of computer programming itself investigates various aspects of the use of programming language and complex systems. \nHuman�computer interaction considers the challenges in making computers and computations useful, usable, and universally accessible to humans."
    val result = tfIdf.TopSentences(args, sc)
    print(result)
  }

  def fmain(args: String, sc: SparkContext): String = {

    val line=args.replaceAll("[^a-zA-Z\\s:]","")
    val stopWords = sc.broadcast(loadStopWords("/stopwords.txt"))
    val data = sc.parallelize(line.split(" "))
    val datanew = data.filter(f=>{
      val flag = true
      stopWords.value.foreach(s=>{
        if(s==f.toLowerCase)
          false
      })
      flag
    })
    val documents: RDD[Seq[String]] = /*sc.textFile("data/sample.txt")*/ data.map(_.split(" ").toSeq)

    val d: RDD[String] = /*sc.textFile("data/sample.txt")*/ data.flatMap(_.split(" "))
    documents.foreach(f => println(f))
    val hashingTF = new HashingTF()
    val tf = hashingTF.transform(documents)
    //tf.cache()
    tf.foreach(v => println(v))

    // tf.saveAsTextFile("outputtf")
    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)

    tfidf.foreach(vv => println(vv))

    var map2 = HashMap[Int, Double]()
    var ResultMap = HashMap[String, Double]()
    tfidf.collect.foreach(f => {

      f.foreachActive((i, v) => {
        map2 += i -> v
      })
    })
    val doc = d.collect().map(f => {
      val index = hashingTF.indexOf(f)
      val value = map2.get(index)
      ResultMap += f -> value.get
      (f, index, value)
    })
    doc.foreach(f => {
      println(f)
    })


    val sorted = ListMap(ResultMap.toSeq.sortWith(_._2 > _._2): _*)
    println(sorted)


    val ss = sorted.take(10)

    val output = new StringBuilder()

    output.append("[")
    sorted.take(20).foreach(item => output.append("{\"word\":\""+item._1+"\"" + ",\"value\":"+item._2+"},"))
    val original = output.toString()
    val editedResult = original.substring(0, original.toString().length() - 1) + "]"

    editedResult.toString()

//  implicit val formats = org.json4s.DefaultFormats
//
//    val output=Serialization.write(ss)
//
    editedResult.toString
  }

  def TopSentences(args: String, sc: SparkContext): String = {

    val line=args.replaceAll("[^a-zA-Z\\s:]","")
    val stopWords = sc.broadcast(loadStopWords("/stopwords.txt"))
    val data = sc.parallelize(line.split(" "))
    val datanew = data.filter(f=>{
      val flag = true
      stopWords.value.foreach(s=>{
        if(s==f.toLowerCase)
          false
      })
      flag
    })
    val documents: RDD[Seq[String]] = /*sc.textFile("data/sample.txt")*/ data.map(_.split(" ").toSeq)

    val d: RDD[String] = /*sc.textFile("data/sample.txt")*/ data.flatMap(_.split(" "))
    documents.foreach(f => println(f))
    val hashingTF = new HashingTF()
    val tf = hashingTF.transform(documents)
    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)
    var map2 = HashMap[Int, Double]()
    var ResultMap = HashMap[String, Double]()
    tfidf.collect.foreach(f => {

      f.foreachActive((i, v) => {
        map2 += i -> v
      })
    })
    val doc = d.collect().map(f => {
      val index = hashingTF.indexOf(f)
      val value = map2.get(index)
      ResultMap += f -> value.get
      (f, index, value)
    })

    val sorted = ListMap(ResultMap.toSeq.sortWith(_._2 > _._2): _*)

    val ss = sorted.take(10)
    val impWords=ss.keySet.toArray
    println(impWords)
    val imW=sc.broadcast(impWords)
    val sent = sc.parallelize(args.split("\n"))

    println(sent.take(1).mkString(" "))
    val result=sent.filter(f=>{
      var flag = false
      imW.value.foreach(w=>{
        if(f.toLowerCase.contains(w.toLowerCase())) {
          flag = true
          println(f)
          println(w)
        }
      })
      flag
    })

    val summary=result.collect().mkString(".")
    println(result.collect())
    println(summary)

    summary
  }

  def loadStopWords(path: String): Set[String] =
    scala.io.Source.fromURL(getClass.getResource(path))
      .getLines().toSet
}
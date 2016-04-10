package com.documentics.machinelearning

import org.json4s.DefaultFormats
import org.json4s.jackson.Json

/**
  * Created by Mayanka on 09-Apr-16.
  */
object sentiment {

  def main(args: Array[String]) {
    val args = "Computer science is the scientific and practical approach to computation and its applications. \nIt is the systematic study of the feasibility, structure, expression, and mechanization of the methodical procedures (or algorithms) that underlie the acquisition, representation, processing, storage, communication of, and access to information. \nAn alternate, more succinct definition of computer science is the study of automating algorithmic processes that scale. \nA computer scientist specializes in the theory of computation and the design of computational systems.[1]\n\nIts fields can be divided into a variety of theoretical and practical disciplines. \nSome fields, such as computational complexity theory (which explores the fundamental properties of computational and intractable problems), are highly abstract, while fields such as computer graphics emphasize real-world visual applications. \nStill other fields focus on challenges in implementing computation. \nFor example, programming language theory considers various approaches to the description of computation, while the study of computer programming itself investigates various aspects of the use of programming language and complex systems. \nHuman�computer interaction considers the challenges in making computers and computations useful, usable, and universally accessible to humans."
    val result = fmain(args)
    print(result)
  }

  def fmain(args: String): String = {

    val sentimentAnalyzer: SentimentAnalyzer = new SentimentAnalyzer
    val tweetWithSentiment: TweetWithSentiment = sentimentAnalyzer.findSentiment(args)

    return tweetWithSentiment.getCssClass

  }

}

package com.documentics.machinelearning

import java.net._
import java.io._
import scala.io._

/**
  * Created by Mayanka on 09-Apr-16.
  */
object clientSocket {
  def main(args: Array[String]) {
   val s = new Socket(InetAddress.getByName("Mayanka-PC"), 9999)
    lazy val in = new BufferedSource(s.getInputStream()).getLines()
    val out = new PrintStream(s.getOutputStream())

    out.println("TF-IDF :Computer science is the scientific and practical approach to computation and its applications. \nIt is the systematic study of the feasibility, structure, expression, and mechanization of the methodical procedures (or algorithms) that underlie the acquisition, representation, processing, storage, communication of, and access to information. \nAn alternate, more succinct definition of computer science is the study of automating algorithmic processes that scale. \nA computer scientist specializes in the theory of computation and the design of computational systems.[1]\n\nIts fields can be divided into a variety of theoretical and practical disciplines. \nSome fields, such as computational complexity theory (which explores the fundamental properties of computational and intractable problems), are highly abstract, while fields such as computer graphics emphasize real-world visual applications. \nStill other fields focus on challenges in implementing computation. \nFor example, programming language theory considers various approaches to the description of computation, while the study of computer programming itself investigates various aspects of the use of programming language and complex systems. \nHuman�computer interaction considers the challenges in making computers and computations useful, usable, and universally accessible to humans.")
    out.close()
//    println("Received: " + in.next())
//
//    s.close()
  }

}

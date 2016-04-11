package com.documentics.machinelearning.namedentity;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Extracts noun phrases from a sentence. To create sentences using OpenNLP use
 * the SentenceDetector classes.
 */
public class StemmingandNGramPartition {

  static final int N = 2;

  public static void main(String[] args) {

    try {
    	String sentence0 = "Tom is playing basketball with other kids";
    	System.out.println("Before Processing:");
    	System.out.println(sentence0);
    	System.out.println("==============================================================");
    	System.out.println("After Processing:");
    	
    	String answer  = stem(sentence0);

    	System.out.println(answer);
    	
    	System.out.println();
    	
    	
    	
      System.out.println("Before NGram:");
      TokenizerModel tm = new TokenizerModel(new FileInputStream("en-token.bin"));
      TokenizerME wordBreaker = new TokenizerME(tm);
      POSModel pm = new POSModel(new FileInputStream("en-pos-maxent.bin"));
      POSTaggerME posme = new POSTaggerME(pm);
      InputStream modelIn = new FileInputStream("en-chunker.bin");
      ChunkerModel chunkerModel = new ChunkerModel(modelIn);
      ChunkerME chunkerME = new ChunkerME(chunkerModel);
      //this is your sentence
      String sentence = "Barack Hussein Obama II is the 44th and current President of the United States, and the first African American to hold the office.";
      System.out.println(sentence);
      System.out.println("==============================================================");
  	System.out.println("After Processing:");
      //words is the tokenized sentence
      String[] words = wordBreaker.tokenize(sentence);
      //posTags are the parts of speech of every word in the sentence (The chunker needs this info of course)
      String[] posTags = posme.tag(words);
      //chunks are the start end "spans" indices to the chunks in the words array
      Span[] chunks = chunkerME.chunkAsSpans(words, posTags);
      //chunkStrings are the actual chunks
      String[] chunkStrings = Span.spansToStrings(chunks, words);
      for (int i = 0; i < chunks.length; i++) {
        if (chunks[i].getType().equals("NP")) {
          System.out.println("NP: \n\t" + chunkStrings[i]);
          String[] split = chunkStrings[i].split(" ");

          List<String> ngrams = ngram(Arrays.asList(split), N, " ");
          System.out.println("ngrams:");
          for (String gram : ngrams) {
            System.out.println("\t" + gram);
          }

        }
      }


    } catch (IOException e) {
    }
  }

  public static List<String> ngram(List<String> input, int n, String separator) {
    if (input.size() <= n) {
      return input;
    }
    List<String> outGrams = new ArrayList<String>();
    for (int i = 0; i < input.size() - (n - 2); i++) {
      String gram = "";
      if ((i + n) <= input.size()) {
        for (int x = i; x < (n + i); x++) {
          gram += input.get(x) + separator;
        }
        gram = gram.substring(0, gram.lastIndexOf(separator));
        outGrams.add(gram);
      }
    }
    return outGrams;
  }
  
  
  public static String stem(String string) throws IOException {
	    TokenStream tokenizer = new StandardTokenizer();
	    tokenizer = new StandardFilter( tokenizer);
	    tokenizer = new LowerCaseFilter(tokenizer);
	    tokenizer = new PorterStemFilter(tokenizer);

	    CharTermAttribute token = tokenizer.getAttribute(CharTermAttribute.class);

	    tokenizer.reset();

	    StringBuilder stringBuilder = new StringBuilder();

	    while(tokenizer.incrementToken()) {
	        if(stringBuilder.length() > 0 ) {
	            stringBuilder.append(" ");
	        }

	        stringBuilder.append(token.toString());
	    }

	    tokenizer.end();
	    tokenizer.close();

	    return stringBuilder.toString();
	}
}
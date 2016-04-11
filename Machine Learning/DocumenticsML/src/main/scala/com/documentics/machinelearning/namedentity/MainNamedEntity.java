package com.documentics.machinelearning.namedentity;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.codehaus.jettison.json.JSONArray;
import scala.collection.mutable.StringBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mayanka on 10-Apr-16.
 */
public class MainNamedEntity {
    public static void main(String[] args) {
        try {
            String line = "The school has its roots in the Lincoln and Lee University movement first put forth by the Methodist Church and its Bishop Ernest Lynn Waldorf in the 1920s. The proposed university (which was to honor Abraham Lincoln and Robert E. Lee) was to be built on the Missouri–Kansas border at 75th and State Line Road, where the Battle of Westport (the largest battle west of the Mississippi River during the American Civil War) took place. The centerpiece of the school was to be a National Memorial marking the tomb of an unknown Union soldier and unknown Confederate soldier. Proponents of the school said it would be a location \"where North met South and East met West.\" The Methodist interest reflected the church's important role in the development of the Kansas City area through the Shawnee Methodist Mission which was the second capital of Kansas.\n" +
                    "\n" +
                    "As the Methodists started having problems piecing together the necessary property, other civic leaders including J.C. Nichols began pushing to create a cultural center on either side of Brush Creek, just east of the Country Club Plaza. According to this plan the Nelson-Atkins Museum of Art and Kansas City Art Institute would be built north of Brush Creek around the estate of The Kansas City Star publisher William Rockhill Nelson and a private nonsectarian University of Kansas City (initially proposed as a junior college) would be built south of the creek. In addition, a hospital would be constructed around the estate of Kansas City Journal-Post publisher Walter S. Dickey. The hospital was never built.\n" +
                    "\n" +
                    "In 1930, after the Methodists had brought the Kansas City Dental School into their fold, the two plans were merged. The new school was to be called \"Lincoln and Lee, the University of Kansas City.\"[6] and plans were underway to develop it into a four-year school.\n" +
                    "\n" +
                    "The university was built on a 40-acre (16.19 ha) plot, southeast of the Nelson mansion. William Volker had purchased and donated this land for the University of Kansas City. The original Volker purchase did not include the Dickey mansion itself. Dickey died unexpectedly in 1931 and Volker acquired it to be the first building.";
            InputStream modelIn = new FileInputStream("models\\en-token.bin");
            TokenizerModel modelTokenizer = new TokenizerModel(modelIn);
            modelIn.close();
            Tokenizer tokenizer = new TokenizerME(modelTokenizer);
            InputStream is = new FileInputStream("models/en-university.bin");
            TokenNameFinderModel modelUniversity = new TokenNameFinderModel(is);
            is.close();
            is = new FileInputStream("models/en-ner-person.bin");
            TokenNameFinderModel modelName = new TokenNameFinderModel(is);
            is.close();
            is = new FileInputStream("models/en-ner-organization.bin");
            TokenNameFinderModel modelOrganisation = new TokenNameFinderModel(is);
            is.close();
            is = new FileInputStream("models/en-ner-time.bin");
            TokenNameFinderModel modelTime= new TokenNameFinderModel(is);
            is.close();
            is = new FileInputStream("models/en-ner-date.bin");
            TokenNameFinderModel modelDate = new TokenNameFinderModel(is);
            is.close();
            is = new FileInputStream("models/en-ner-location.bin");
            TokenNameFinderModel modelLocation = new TokenNameFinderModel(is);
            is.close();
            is = new FileInputStream("models/en-ner-money.bin");
            TokenNameFinderModel modelMoney = new TokenNameFinderModel(is);
            is.close();

            String nameOutput = findEntity(line, tokenizer, modelName);
            String nameUniversity = findEntity(line, tokenizer, modelUniversity);
            String Organisation=findEntity(line,tokenizer,modelOrganisation);
            String time=findEntity(line,tokenizer,modelTime);
            String date=findEntity(line,tokenizer,modelDate);
            String location=findEntity(line,tokenizer,modelLocation);
            String money=findEntity(line,tokenizer,modelMoney);

            System.out.println(nameOutput+"\n"+nameUniversity+"\n"+Organisation+"\n"+time+"\n"+date+"\n"+location+"\n"+money);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findEntity(String dataString, Tokenizer tokenizer, TokenNameFinderModel modelUniversity) throws IOException {

        String sentence[] = tokenizer.tokenize(dataString);


        NameFinderME nameFinder = new NameFinderME(modelUniversity);
        Span nameSpans[] = nameFinder.find(sentence);
        String[] result = new String[nameSpans.length];
        int j = 0;
        for (Span s : nameSpans) {
            StringBuilder currentString = new StringBuilder();
            for (int i = s.getStart(); i < s.getEnd(); i++) {
                currentString.append(sentence[i]+" ");
            }
            result[j] = currentString.toString();
            System.out.println(s.toString());
            j++;
        }
        List<String> list = new ArrayList<String>(Arrays.asList(result));
        JSONArray jsnobject = new JSONArray(list);

        return jsnobject.toString();

    }


}
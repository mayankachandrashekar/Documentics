package com.documentics.machinelearning.namedentity;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.featuregen.*;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * Created by Mayanka on 10-Apr-16.
 */
public class EntityTrainer {

    public static void main(String[] args) {
        try {
            Charset charset = Charset.forName("UTF-8");
            ObjectStream<String> lineStream =
                    new PlainTextByLineStream(new FileInputStream("data\\university.txt"), charset);
            ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

            TokenNameFinderModel model;
            AdaptiveFeatureGenerator featureGenerator = new CachedFeatureGenerator(
                    new AdaptiveFeatureGenerator[]{
                            new WindowFeatureGenerator(new TokenFeatureGenerator(), 2, 2),
                            new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), 2, 2),
                            new OutcomePriorFeatureGenerator(),
                            new PreviousMapFeatureGenerator(),
                            new BigramNameFeatureGenerator(),
                            new SentenceFeatureGenerator(false, false)
                    });

            try {
                model = NameFinderME.train("en", "university", sampleStream, TrainingParameters.defaultParams(),
                        featureGenerator, Collections.<String, Object>emptyMap());
            } finally {
                sampleStream.close();
            }
            BufferedOutputStream modelOut;


                modelOut = new BufferedOutputStream(new FileOutputStream("models\\en-university.bin"));
                model.serialize(modelOut);

                    modelOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

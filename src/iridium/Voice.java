package iridium;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

public class Voice {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("en-us");
        configuration.setDictionaryPath("cmudict-en-us.dict");
        configuration.setLanguageModelPath("en-us.lm.bin");
        configuration.setGrammarPath("grammar.gram");
        configuration.setGrammarName("grammmar");
        configuration.setUseGrammar(true);

        //StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

        //InputStream stream = new FileInputStream(new File("test.wav"));

        //recognizer.startRecognition(stream);
        SpeechResult result;

        recognizer.startRecognition(true);
        while (true) {

            while ((result = recognizer.getResult()) != null) {
                System.out.format("Hypothesis: %s\n", result.getHypothesis());
                if (result.getHypothesis().equals("quit"))
                    System.exit(0);
            }
        }
        //recognizer.stopRecognition();
    }
}
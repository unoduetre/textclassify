package fuzzy;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import text.Text;
import text.WordList;
import categories.StringCategory;

public class WordTypeParser {
  public static Map<String, FuzzySet> parse(List<Text> data, String wordType, File directory) throws Exception {
    Set<String> labels = new HashSet<String>();
    Map<String, Map<String, Double>> measurements = new TreeMap<String, Map<String, Double>>();
    WordList keywords = new WordList(new File("data/wordtypes/" + wordType + ".txt"));
    
    for(Text text: data) {
      labels.add(((StringCategory)text.getCategory()).getString());
    }
    
    for(String word: keywords) {
      measurements.put(word, new TreeMap<String, Double>());
      for(String label: labels) {
        measurements.get(word).put(label, 0.0);
      }
    }
    
    for(Text text: data) {
      String label = ((StringCategory)text.getCategory()).getString();
      for(String word: keywords) {
        double oldVal = measurements.get(word).get(label);
        double newVal = oldVal + text.getWordCount(word);
        measurements.get(word).put(label, newVal);
      }
    }
    
    for(int i = 0; i < keywords.size(); ++i) {
      String word = keywords.get(i);
      double sum = 0.0;
      for(String label: labels) {
        sum += measurements.get(word).get(label);
      }
      if(sum != 0.0) {
        for(String label: labels) {
          double oldVal = measurements.get(word).get(label);
          double newVal = oldVal / sum;
          measurements.get(word).put(label, newVal);
        }
      } else {
        measurements.remove(word);
        keywords.remove(i--);
      }
    }
    
    Map<String, FuzzySet> results = new HashMap<String, FuzzySet>();
    
    for(String label: labels) {
      FuzzySet fs = new FuzzySet();
      for(String word: keywords) {
        fs.put(word, measurements.get(word).get(label));
      }
      fs.setFile(new File(directory.getPath() + "/" + label + "_" + wordType));
      results.put(label, fs);
    }
    
    return results;
  }
}

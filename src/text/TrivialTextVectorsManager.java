package text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import metrics.MetricClassifiable;

public class TrivialTextVectorsManager {
  
  private List<String> keywords = null;
  private List<MetricClassifiable> vectors = null;
  
  public TrivialTextVectorsManager(List<Text> samples, List<String> iKeywords) {
    if(iKeywords == null) { // brak zadanej z góry lista słów kluczowych => wszystkie słowa
      Set<String> keywordsSet = new HashSet<String>();
      for(Text text: samples) {
        keywordsSet.addAll(text.getWords());
      }
      keywords = new ArrayList<String>(keywordsSet.size());
      keywords.addAll(keywordsSet);
    } else { // zadana z góry lista słów kluczowych
      keywords = iKeywords;
    }
    vectors = new ArrayList<MetricClassifiable>(samples.size());
    for(Text text: samples) {
      vectors.add(new TrivialTextVector(text, keywords));
    }
  }
  
  public List<MetricClassifiable> getVectors() {
    return vectors;
  }
  
  public MetricClassifiable getVectorForNewSample(Text text) {
    return new TrivialTextVector(text, keywords);
  }
}
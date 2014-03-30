package extraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import knn.Classifiable;

import text.Text;

public class TrivialTextVectorsManager implements VectorManager {
  
  private List<String> keywords = null;
  private List<Classifiable> vectors = null;
  
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
    vectors = new ArrayList<Classifiable>(samples.size());
    for(Text text: samples) {
      vectors.add(new TrivialTextVector(text, keywords));
    }
  }
  
  @Override
  public List<Classifiable> getVectors() {
    return vectors;
  }
  
  @Override
  public Classifiable getVectorForNewSample(Text text) {
    Classifiable ret = new TrivialTextVector(text, keywords);
    ret.setCategory(null);
    return ret;
  }
}

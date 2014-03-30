package extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import knn.Classifiable;

import fuzzy.FuzzySet;
import text.Text;

public class FuzzyVectorsManager implements VectorManager {

  private List<FuzzySet> fuzzySets = null;
  private List<Classifiable> vectors = null;
  
  public FuzzyVectorsManager(List<Text> samples, Map<String, Map<String, FuzzySet>> iFuzzySets) {
    fuzzySets = new ArrayList<FuzzySet>();
    for(Map<String, FuzzySet> fse: iFuzzySets.values()) {
      for(FuzzySet fs: fse.values()) {
        fuzzySets.add(fs);
      }
    }
    vectors = new ArrayList<Classifiable>(samples.size());
    for(Text text: samples) {
      vectors.add(new FuzzyVector(text, fuzzySets));
    }
  }
  
  @Override
  public List<Classifiable> getVectors() {
    return vectors;
  }

  @Override
  public Classifiable getVectorForNewSample(Text text) {
    Classifiable ret = new FuzzyVector(text, fuzzySets);
    ret.setCategory(null);
    return ret;
  }

}

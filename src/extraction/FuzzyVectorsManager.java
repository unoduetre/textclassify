package extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fuzzy.FuzzySet;

import metrics.MetricClassifiable;
import text.Text;

public class FuzzyVectorsManager implements VectorManager {

  private List<FuzzySet> fuzzySets = null;
  private List<MetricClassifiable> vectors = null;
  
  public FuzzyVectorsManager(List<Text> samples, Map<String, Map<String, FuzzySet>> iFuzzySets) {
    fuzzySets = new ArrayList<FuzzySet>();
    for(Map<String, FuzzySet> fse: iFuzzySets.values()) {
      for(FuzzySet fs: fse.values()) {
        fuzzySets.add(fs);
      }
    }
    vectors = new ArrayList<MetricClassifiable>(samples.size());
    for(Text text: samples) {
      vectors.add(new FuzzyVector(text, fuzzySets));
    }
  }
  
  @Override
  public List<MetricClassifiable> getVectors() {
    return vectors;
  }

  @Override
  public MetricClassifiable getVectorForNewSample(Text text) {
    // TODO Auto-generated method stub
    return null;
  }

}

package extraction;

import java.util.ArrayList;
import java.util.List;

import categories.StringCategory;

import text.Text;
import fuzzy.FuzzySet;

import knn.Category;
import metrics.MetricClassifiable;

public class FuzzyVector implements MetricClassifiable {

  Category category;
  List<Float> data;

  public FuzzyVector(Text text, List<FuzzySet> fuzzySets) {
    data = new ArrayList<Float>(fuzzySets.size());
    category = text.getCategory();
    double sqsum = 0.0;
    for(FuzzySet fs: fuzzySets) {
      double c = calculate(text, fs);
      data.add((float) c);
      sqsum += c * c;
    }
    if(sqsum != 0.0) {
      double mul = Math.pow(sqsum, -0.5);
      for(int i = 0; i < data.size(); ++i) {
        data.set(i, (float) (data.get(i) * mul));
      }
    }
  }

  private double calculate(Text text, FuzzySet fs) {
    double num = 0.0, den = 0.0;
    for(String word: text.getText()) {
      if(fs.containsKey(word)) num += fs.get(word);
      den += 1.0;
    }
    if(den == 0.0) {
      return 0.0;
    } else {
      return num / den;
    }
  }
  
  @Override
  public Category getCategory() {
    return category;
  }

  @Override
  public void setCategory(Category category) {
    StringCategory sc = (StringCategory) category;
    if(sc != null) {
      this.category = sc;
    }
  }

  @Override
  public List<Float> getVector() {
    return data;
  }
}

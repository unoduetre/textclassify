package text;

import java.util.ArrayList;
import java.util.List;

import knn.Category;
import categories.StringCategory;

import metrics.MetricClassifiable;

public class TrivialTextVector implements MetricClassifiable {

  StringCategory category;
  List<Double> data;
  
  public TrivialTextVector(Text text, List<String> keywords) {
    data = new ArrayList<Double>(keywords.size());
    double sqsum = 0.0;
    for(String word: keywords) {
      double c = (double) text.getWordCount(word);
      data.add(c);
      sqsum += c * c;
    }
    double mul = Math.pow(sqsum, -0.5);
    for(int i = 0; i < data.size(); ++i) {
      data.set(i, data.get(i) * mul);
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
  public List<Double> getVector() {
    return data;
  }
}

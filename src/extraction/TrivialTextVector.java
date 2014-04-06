package extraction;

import java.util.ArrayList;
import java.util.List;

import text.Text;

import knn.Category;
import categories.StringCategory;

import metrics.MetricClassifiable;

public class TrivialTextVector implements MetricClassifiable {

  Category category;
  List<Float> data;
  
  public TrivialTextVector(Text text, List<String> keywords) {
    data = new ArrayList<Float>(keywords.size());
    category = text.getCategory();
    double sqsum = 0.0;
    for(String word: keywords) {
      float c = (float) text.getWordCount(word);
      data.add(c);
      sqsum += c * c;
    }
    if(sqsum != 0.0) {
      double mul = Math.pow(sqsum, -0.5);
      for(int i = 0; i < data.size(); ++i) {
        data.set(i, (float) (data.get(i) * mul));
      }
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

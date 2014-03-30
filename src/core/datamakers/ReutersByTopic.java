package core.datamakers;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import text.Text;

import core.DataMaker;

import knn.Category;
import categories.StringCategory;

public class ReutersByTopic extends DataMaker {

  @Override
  public void createDataSets(List<Text> texts) throws Exception {
    List<File> files = Arrays.asList(new File("data/xml").listFiles());
    Collections.sort(files);
    
    parseTexts(
        texts,
        files,
        Arrays.asList("LEWIS","REUTERS"),
        Arrays.asList("LEWIS","REUTERS","TOPICS","D"),
        Arrays.asList("LEWIS","REUTERS","TEXT","BODY")
    );
    
    int i = 0;
    while(i < texts.size()) {
      boolean aboutGrain = false, aboutInterest = false;
      for(Category category: texts.get(i).getCategories()) {
        if(((StringCategory)category).getString().equals(new StringCategory("grain"))) aboutGrain = true;
        if(((StringCategory)category).getString().equals(new StringCategory("interest"))) aboutInterest = true;
      }
      if(aboutGrain ^ aboutInterest) {
        if(aboutGrain) texts.get(i).setCategories(Arrays.asList(((Category)new StringCategory("grain"))));
        if(aboutInterest) texts.get(i).setCategories(Arrays.asList((Category)new StringCategory("interest")));
        ++i;
      } else {
        texts.remove(i);
      }
    }
  }

}

package core.datamakers;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import text.Text;

import core.DataMaker;

public class ReutersByTopic extends DataMaker {

  @Override
  public void createDataSets(List<Text> texts) throws Exception {
    System.out.println("Mode: Reuters, topics [grain|interest] are labels.");
    
    System.out.println("Parsing texts...");
    
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
      for(String category: texts.get(i).getCategories()) {
        if(category.equals("grain")) aboutGrain = true;
        if(category.equals("interest")) aboutInterest = true;
      }
      if(aboutGrain ^ aboutInterest) {
        if(aboutGrain) texts.get(i).setCategories(Arrays.asList("grain"));
        if(aboutInterest) texts.get(i).setCategories(Arrays.asList("interest"));
        ++i;
      } else {
        texts.remove(i);
      }
    }
  }

}

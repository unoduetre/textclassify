package core.datamakers;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import categories.StringCategory;

import text.Text;

import core.DataMaker;

public class ReutersByCountry extends DataMaker {

  @Override
  public void createDataSets(List<Text> texts) throws Exception {
    Set<String> validCategories = new HashSet<String>(Arrays.asList("canada", "japan", "france", "uk", "usa", "west-germany"));
    
    List<File> files = Arrays.asList(new File("data/xml").listFiles());
    Collections.sort(files);
    
    parseTexts(
        texts,
        files,
        Arrays.asList("LEWIS","REUTERS"),
        Arrays.asList("LEWIS","REUTERS","PLACES","D"),
        Arrays.asList("LEWIS","REUTERS","TEXT","BODY")
    );
    
    int i = 0;
    while(i < texts.size()) {
      if(texts.get(i).getCategories().size() != 1
          || (! validCategories.contains(((StringCategory) texts.get(i).getCategory()).getString()))) {
        texts.remove(i);
      } else {
        ++i;
      }
    }
  }

}

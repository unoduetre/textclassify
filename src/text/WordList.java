package text;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class WordList extends LinkedList<String> {

  /**
   * 
   */
  private static final long serialVersionUID = 8354254196055228864L;

  public WordList(List<File> files) throws Exception {
    super();
    Set<String> ss = new HashSet<String>();
    for(File file: files) {
      Scanner scanner = new Scanner(file);
      while(scanner.hasNext()) {
        String s = scanner.next();
        int c = 0;
        for(String word: s.split("\\-")) {
          String fw = Text.fixWord(word);
          if(fw != null) {
            ss.add(fw);
          }
          c += 1;
        }
        if(c > 1) {
          String fw = Text.fixWord(s);
          if(fw != null) {
            ss.add(fw);
          }
        }
      }
      scanner.close();
    }
    addAll(ss);
  }
}

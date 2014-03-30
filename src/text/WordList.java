package text;

import java.io.File;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class WordList extends LinkedList<String> {

  /**
   * 
   */
  private static final long serialVersionUID = 8354254196055228864L;

  public WordList(File file) throws Exception {
    super();
    Set<String> ss = new HashSet<String>();
    Scanner scanner = new Scanner(file);
    while(scanner.hasNext()) {
      String s = scanner.next();
      for(String word: s.split("\\-")) {
        String fw = Text.fixWord(word);
        if(fw != null) {
          ss.add(Text.fixWord(word));
        }
      }
    }
    scanner.close();
    addAll(ss);
  }
}

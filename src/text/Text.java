package text;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Text
{
  List<String> categories = new ArrayList<String>();
  List<String> text = new ArrayList<String>();
  Map<String, Integer> histogram = new TreeMap<String, Integer>();
  
  public void addCategory(String aCategory)
  {
    categories.add(aCategory);
  }

  public static String fixWord(String iWord) {
    String tmp = iWord.toLowerCase();
    tmp = tmp.replaceAll("&[#\\w]*;", "");
    tmp = Normalizer.normalize(tmp, Normalizer.Form.NFD);
    tmp = tmp.replaceAll("[^a-z]", "");
    tmp = tmp.replaceAll("((i?e)?s|(i?e)?d|ing)$", "");
    return tmp.length() < 2 ? null : tmp;
  }
  
  public int getWordCount(String iWord) {
    return histogram.containsKey(iWord) ? histogram.get(iWord) : 0;
  }
  
  public void setText(String aText)
  {
    text = new ArrayList<String>();
    Scanner scanner = new Scanner(aText);
    while(scanner.hasNext())
    {
      String word = fixWord(scanner.next());
      if(word != null)
      {
        text.add(word);
      }
    }
    scanner.close();
    
    if (text.size() != 0 && text.get(text.size() - 1).equals("reuter")) {
      text.remove(text.size() - 1);
    }
    
    for(String word: text) {
      histogram.put(word, getWordCount(word) + 1);
    }
  }

  public Integer numberOfCategories()
  {
    return categories.size();
  }

  public List<String> getCategories()
  {
    return categories;
  }

  public void setCategories(List<String> newCategories)
  {
    categories = newCategories;
  }


  public String getCategory()
  {
    if(categories.size() > 0)
    {
      return categories.get(0);
    }
    else
    {
      return null;
    }
  }

  public List<String> getText()
  {
    return text;
  }

  @Override
  public String toString()
  {
    Boolean first = true;
    StringBuilder builder = new StringBuilder();

    for(String word : text)
    {
      if(first)
      {
        first = false;
      }
      else
      {
        builder.append(" ");
      }
      builder.append(word);
    }
    return String.valueOf(builder);
  }
}

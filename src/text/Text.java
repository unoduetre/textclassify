package text;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Text
{
  List<String> categories = new ArrayList<String>();
  List<String> text = new ArrayList<String>();

  public void addCategory(String aCategory)
  {
    categories.add(aCategory);
  }

  public void setText(String aText)
  {
    text = new ArrayList<String>();
    Scanner scanner = new Scanner(aText);
    while(scanner.hasNext())
    {
      text.add(scanner.next());
    }
    scanner.close();
  }

  public Integer numberOfCategories()
  {
    return categories.size();
  }

  public List<String> getCategories()
  {
    return categories;
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

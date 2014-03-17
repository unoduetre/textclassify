package text;

import java.util.LinkedList;

public class Text
{
  LinkedList<String> categories = new LinkedList<String>();
  String text = null;

  public void addCategory(String aCategory)
  {
    categories.addLast(aCategory);
  }

  public void setText(String aText)
  {
    text = aText;
  }

  public Integer numberOfCategories()
  {
    return categories.size();
  }

  public LinkedList<String> getCategories()
  {
    return categories;
  }

  public String getCategory()
  {
    if(categories.size() > 0)
    {
      return categories.getLast();
    }
    else
    {
      return null;
    }
  }

  public String getText()
  {
    return text;
  }

  @Override
  public String toString()
  {
    Boolean first = true;
    StringBuilder builder = new StringBuilder();

    builder.append("------------------------------------------------------\n");

    builder.append("<TEXT>\n<CATEGORIES>\n");
    for(String category : categories)
    {
      if(!first)
      {
        builder.append(" ");
      }
      builder.append(category);
      first = false;
    }
    builder.append("\n</CATEGORIES>\n<VALUE>\n");
    builder.append(text);
    builder.append("\n</VALUE>\n</TEXT>\n");
    return String.valueOf(builder);
  }
}

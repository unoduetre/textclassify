package categories;

import knn.Category;

public class StringCategory implements Category
{
  String string = null;

  public StringCategory(String aString)
  {
    string = aString;
  }

  @Override
  public boolean equals(Category category)
  {
    if(category instanceof StringCategory)
    {
      return string.equals(((StringCategory)category).getString());
    }
    else
    {
      return false;
    }
  }

  public String getString()
  {
    return string;
  }
}

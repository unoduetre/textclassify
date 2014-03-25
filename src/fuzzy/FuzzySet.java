package fuzzy;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.Scanner;

public class FuzzySet extends TreeMap<String,Float>
{
  /**
   * 
   */
  private static final long serialVersionUID = -5237469610440917069L;
  File file = null;

  public FuzzySet(File aFile) throws Exception
  {
    super();
    file = aFile;
    Scanner scanner = null;
    try
    {
      scanner = new Scanner(file);
      while(scanner.hasNext())
      {
        put(scanner.next(), scanner.nextFloat());
      }
    }
    finally
    {
      if(scanner != null)
      {
        scanner.close();
      }
    }
  }

  public void save() throws Exception
  {
    PrintWriter writer = null;
    try
    {
      writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
      for(String word : keySet())
      {
        writer.printf("%1$s %2$f%n", word, get(word));
      }

    }
    finally
    {
      if(writer != null)
      {
        writer.close();
      }
    }
  }
}

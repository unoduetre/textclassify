package main;

import java.io.FileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.net.URL;
import java.net.URLConnection;
import java.net.JarURLConnection;


import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import xml.XMLHandler;
import text.Text;
import fuzzy.FuzzySet;

public class Main
{

  static List<Text> texts = new LinkedList<Text>();
  static Map<String, Map<String, FuzzySet>> fuzzySets = new HashMap<String, Map<String, FuzzySet>>();
  /*
   * W powyższym każdy zbiór rozmyty oznaczony jest przez 2 napisy. Pierwszy oznacza kategorię której dotyczy dany zbiór.
   * Drugi oznacza już konkretny zbiór. Na przykład dla krajów pierwszy łańcuch to canada, a drugi to geography.
   * Zbiory rozmyte powinny znajdować się w plikach o nazwach postaci kategoria_typ, czyli w powyższym wypadku: canada_geography
   */

  public static void main(String[] args)
  {
    try
    {
      parseFuzzySets(fuzzySets, new File("data/fuzzy/countries"));

      for(String fuzzySetCategory : fuzzySets.keySet())
      {
        for(String fuzzySetType : fuzzySets.get(fuzzySetCategory).keySet())
        {
          FuzzySet fuzzySet = fuzzySets.get(fuzzySetCategory).get(fuzzySetType);
          System.out.println(fuzzySetCategory + "_" + fuzzySetType + " = " + String.valueOf(fuzzySet));
          fuzzySet.put("keyword1",new Float(0.0));
          fuzzySet.put("keyword2",new Float(0.0));
          fuzzySet.save();
        }
      }

      parseTexts(
          texts, 
          Arrays.asList(new File("data/xml").listFiles()),
          Arrays.asList("LEWIS","REUTERS"),
          Arrays.asList("LEWIS","REUTERS","PLACES","D"),
          Arrays.asList("LEWIS","REUTERS","TEXT","BODY")
      );

      for(Text text : texts)
      {
        System.out.print(text);
      }
      
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private static void parseTexts(List<Text> texts, List<File> files, List<String> rootNode, List<String> categoryNode, List<String> textNode) throws Exception
  {
    XMLReader reader = XMLReaderFactory.createXMLReader();
    XMLHandler handler = new XMLHandler(texts, rootNode, categoryNode, textNode);
    reader.setContentHandler(handler);
    reader.setErrorHandler(handler);

    for(File file : files)
    {
      FileReader fileReader = null;
      try
      {
        fileReader = new FileReader(file);
        reader.parse(new InputSource(fileReader));
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if(fileReader != null)
        {
          fileReader.close();
        }
      }
    }
  }

  private static void parseFuzzySets(Map<String, Map<String, FuzzySet>> fuzzySets, File directory) throws Exception
  {
    for(File file : directory.listFiles())
    {
      try
      {
        String[] name = file.getName().split("_");
        Map<String, FuzzySet> fuzzySetsInCategory = fuzzySets.get(name[0]);
        if(fuzzySetsInCategory == null)
        {
          fuzzySetsInCategory = new HashMap<String, FuzzySet>();
          fuzzySets.put(name[0],fuzzySetsInCategory);
        }
        fuzzySetsInCategory.put(name[1],new FuzzySet(file));
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}

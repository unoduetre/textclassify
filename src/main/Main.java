package main;

import java.io.FileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.HashMap;
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

  static LinkedList<Text> texts = new LinkedList<Text>();
  static HashMap<String, FuzzySet> fuzzySets = new HashMap<String, FuzzySet>();
  static LinkedList<String> ordering = new LinkedList<String>();

  public static void main(String[] args)
  {
    try
    {
      parseFuzzySets();

      for(String name : ordering)
      {
        System.out.println(name+" := "+fuzzySets.get(name));
      }

      //parseTexts(args);

      /*
      for(Text text : texts)
      {
        System.out.print(text);
      }
      */
      
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private static void parseTexts(String[] args) throws Exception
  {
    XMLReader reader = XMLReaderFactory.createXMLReader();
    XMLHandler handler = new XMLHandler(texts);
    reader.setContentHandler(handler);
    reader.setErrorHandler(handler);

    for(String arg : args)
    {
      FileReader fileReader = null;
      try
      {
        fileReader = new FileReader(arg);
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

  private static void parseFuzzySets() throws Exception
  {
    URLConnection connection = ClassLoader.getSystemResource("resources/fuzzy").openConnection();

    if(connection instanceof JarURLConnection)
    {
      JarURLConnection jarConnection = (JarURLConnection)connection;
      JarFile jar = jarConnection.getJarFile();
      JarEntry root = jarConnection.getJarEntry();
      Enumeration<JarEntry> entries = jar.entries();
      while(entries.hasMoreElements())
      {
        InputStream stream = null;
        try
        {
          JarEntry entry = entries.nextElement();
          if(!entry.isDirectory() && entry.getName().startsWith(root.getName()))
          {
            stream = jar.getInputStream(entry);
            String name = (new File(entry.getName())).getName();
            ordering.addLast(name);
            fuzzySets.put(name,new FuzzySet(stream));
          }
        }
        catch(Exception e)
        {
          continue;
        }
        finally
        {
          if(stream != null)
          {
            stream.close();
          }
        }
      }
    }
    else
    {
      File root = new File(connection.getURL().getPath());
      for(File file : root.listFiles())
      {
        FileInputStream stream = null;
        try
        {
          stream = new FileInputStream(file);
          String name = file.getName();
          ordering.addLast(name);
          fuzzySets.put(name,new FuzzySet(stream));
        }
        catch(Exception e)
        {
          continue;
        }
        finally
        {
          if(stream != null)
          {
            stream.close();
          }
        }
      }
    }
  }
}

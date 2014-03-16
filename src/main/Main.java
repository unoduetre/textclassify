package main;

import java.io.FileReader;
import java.io.BufferedReader;

import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import xml.XMLHandler;

public class Main
{
  public static void main(String[] args)
  {
    try
    {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      XMLHandler handler = new XMLHandler();
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
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}

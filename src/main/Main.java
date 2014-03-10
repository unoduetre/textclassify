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
        BufferedReader bufferedReader = null;
        try
        {
          bufferedReader = new BufferedReader(new FileReader(arg));
          reader.parse(new InputSource(bufferedReader));
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
          if(bufferedReader != null)
          {
            bufferedReader.close();
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

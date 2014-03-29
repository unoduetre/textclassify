package core;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import text.Text;
import xml.XMLHandler;

public abstract class DataMaker {
  public abstract void createDataSets(List<Text> texts) throws Exception;
  
  public void parseTexts(List<Text> texts, List<File> files, List<String> rootNode, List<String> categoryNode, List<String> textNode) throws Exception
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
    
    // Tekst bez słów nie niesie zbyt wiele informacji...
    int i = 0;
    while(i < texts.size()) {
      if(texts.get(i).getText().size() == 0) {
        texts.remove(i);
      } else {
        ++i;
      }
    }
  }
}

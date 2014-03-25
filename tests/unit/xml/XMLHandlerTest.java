package xml;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import text.Text;

public class XMLHandlerTest
{
  @Test
  public void noDnoBody() throws Exception
  {
    List<Text> texts = new LinkedList<Text>();
    XMLReader reader = XMLReaderFactory.createXMLReader();
    XMLHandler handler = new XMLHandler(
        texts,
        Arrays.asList("LEWIS","REUTERS"),
        Arrays.asList("LEWIS","REUTERS","PLACES","D"),
        Arrays.asList("LEWIS","REUTERS","TEXT","BODY")
    );
    reader.setContentHandler(handler);
    reader.setErrorHandler(handler);
    reader.parse(new InputSource(new FileReader(new File("data/tests/test.xml"))));
    assertEquals(texts.get(0).numberOfCategories(),new Integer(0));
    assertEquals(texts.get(0).toString(),"");
  }
}

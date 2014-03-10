package xml;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

public class XMLHandler extends DefaultHandler
{
  @Override
  public void startDocument()
  {
    System.out.println("Początek dokumentu");
  }

  @Override
  public void endDocument()
  {
    System.out.println("Koniec dokumentu");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
  {
    //System.out.println("<"+localName+">");
  }

  @Override
  public void endElement(String uri, String localName, String qName) 
  {
    //System.out.println("</"+localName+">");
  }

  @Override
  public void characters(char[] ch, int start, int length) 
  {
    System.out.println(ch);
  }

}

package xml;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import text.Text;

public class XMLHandler extends DefaultHandler
{

  LinkedList<StringBuilder> elementContentStack = null;
  LinkedList<String> elementStack = null;
  LinkedList<Text> texts = null;
  Text currentText = null;

  public XMLHandler(LinkedList<Text> someTexts)
  {
    texts = someTexts;
  }

  @Override
  public void startDocument()
  {
    elementContentStack = new LinkedList<StringBuilder>();
    elementStack = new LinkedList<String>();
  }

  @Override
  public void endDocument()
  {
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
  {
    elementStack.addLast(localName);
    elementContentStack.addLast(new StringBuilder());
    if(path("LEWIS","REUTERS"))
    {
      currentText = new Text();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) 
  {
    if(path("LEWIS","REUTERS","PLACES","D"))
    {
      currentText.addCategory(String.valueOf(elementContentStack.getLast()));
    }
    if(path("LEWIS","REUTERS","TEXT","BODY"))
    {
      currentText.setText(String.valueOf(elementContentStack.getLast()));
    }
    if(path("LEWIS","REUTERS"))
    {
      texts.addLast(currentText);
    }
    elementStack.removeLast();
    elementContentStack.removeLast();
  }

  @Override
  public void characters(char[] ch, int start, int length) 
  {
    elementContentStack.getLast().append(ch,start,length);
  }

  private Boolean path(String... positions)
  {
    if(elementStack.size() != positions.length)
    {
      return false;
    }
    ListIterator<String> iterator = elementStack.listIterator();
    while(iterator.hasNext())
    {
      Integer index = iterator.nextIndex();
      if(!iterator.next().equals(positions[index]))
      {
        return false;
      }
    }
    return true;
  }
}

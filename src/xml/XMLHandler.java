package xml;

import java.util.LinkedList;
import java.util.List;
import java.util.Deque;
import java.util.Iterator;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import text.Text;

public class XMLHandler extends DefaultHandler
{

  List<String> rootPath = null;
  List<String> categoryPath = null;
  List<String> textPath = null;
  Deque<StringBuilder> elementContentStack = null;
  Deque<String> elementStack = null;
  List<Text> texts = null;
  Text currentText = null;

  public XMLHandler(List<Text> someTexts, List<String> aRootPath, List<String> aCategoryPath, List<String> aTextPath)
  {
    texts = someTexts;
    rootPath = aRootPath;
    categoryPath = aCategoryPath;
    textPath = aTextPath;
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
    if(path(rootPath))
    {
      currentText = new Text();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) 
  {
    if(path(categoryPath))
    {
      currentText.addCategory(String.valueOf(elementContentStack.getLast()));
    }
    if(path(textPath))
    {
      currentText.setText(String.valueOf(elementContentStack.getLast()));
    }
    if(path(rootPath))
    {
      texts.add(currentText);
    }
    elementStack.removeLast();
    elementContentStack.removeLast();
  }

  @Override
  public void characters(char[] ch, int start, int length) 
  {
    elementContentStack.getLast().append(ch,start,length);
  }

  private Boolean path(List<String> positions)
  {
    if(elementStack.size() != positions.size())
    {
      return false;
    }
    Iterator<String> elementStackIterator = elementStack.iterator();
    Iterator<String> positionsIterator = positions.iterator();
    while(elementStackIterator.hasNext())
    {
      if(!elementStackIterator.next().equals(positionsIterator.next()))
      {
        return false;
      }
    }
    return true;
  }
}

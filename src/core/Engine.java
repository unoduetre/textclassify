package core;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import text.Text;
import xml.XMLHandler;
import fuzzy.FuzzySet;

public class Engine {
  private List<Text> texts = new LinkedList<Text>();
  private Map<String, Map<String, FuzzySet>> fuzzySets = new HashMap<String, Map<String, FuzzySet>>();
  
  private List<Text> trainingSet = new LinkedList<Text>();
  private List<Text> testSet = new LinkedList<Text>();
  
  /*
   * W powyższym każdy zbiór rozmyty oznaczony jest przez 2 napisy. Pierwszy oznacza kategorię której dotyczy dany zbiór.
   * Drugi oznacza już konkretny zbiór. Na przykład dla krajów pierwszy łańcuch to canada, a drugi to geography.
   * Zbiory rozmyte powinny znajdować się w plikach o nazwach postaci kategoria_typ, czyli w powyższym wypadku: canada_geography
   */
  
  public void pickTrainingAndTestSets(double trainingSetSize, double testSetSize, boolean randomizeSets, boolean allowOverlap) {
    trainingSet = new LinkedList<Text>();
    testSet = new LinkedList<Text>();
    
    if(randomizeSets) {
      if(allowOverlap) {
        assert trainingSetSize <= 1.0 && testSetSize <= 1.0;
        
        {
          List<Integer> availableIDs = new LinkedList<Integer>();
          for(int j = 0; j < texts.size(); ++j) availableIDs.add(j);
          for(int k = 0; k < trainingSetSize * texts.size(); ++k) {
            int r = (int) (Math.random() * availableIDs.size());
            trainingSet.add(texts.get(availableIDs.get(r)));
            availableIDs.remove(r);
          }
        }
        
        {
          List<Integer> availableIDs = new LinkedList<Integer>();
          for(int j = 0; j < texts.size(); ++j) availableIDs.add(j);
          for(int k = 0; k < testSetSize * texts.size(); ++k) {
            int r = (int) (Math.random() * availableIDs.size());
            testSet.add(texts.get(availableIDs.get(r)));
            availableIDs.remove(r);
          }
        }
      } else {
        assert trainingSetSize + testSetSize <= 1.0;
        List<Integer> availableIDs = new LinkedList<Integer>();
        for(int j = 0; j < texts.size(); ++j) availableIDs.add(j);
        
        for(int k = 0; k < trainingSetSize * texts.size(); ++k) {
          int r = (int) (Math.random() * availableIDs.size());
          trainingSet.add(texts.get(availableIDs.get(r)));
          availableIDs.remove(r);
        }
        
        for(int k = 0; k < testSetSize * texts.size(); ++k) {
          int r = (int) (Math.random() * availableIDs.size());
          testSet.add(texts.get(availableIDs.get(r)));
          availableIDs.remove(r);
        }
      }
    } else {
      double sumOfSetSizes = trainingSetSize + testSetSize;
      assert sumOfSetSizes <= 1.0;
      for(int j = 0; j < sumOfSetSizes * texts.size(); ++j) {
        if(j < trainingSetSize * texts.size()) {
          trainingSet.add(texts.get(j));
        } else {
          testSet.add(texts.get(j));
        }
      }
    }
  }
  
  public void createDataSetsFromReutersByCountry() throws Exception {
    Set<String> validCategories = new HashSet<String>(Arrays.asList("canada", "japan", "france", "uk", "usa", "west-germany"));
    
    System.out.println("Mode: Reuters, countries [canada|japan|france|uk|usa|west-germany] are labels.");
    
    System.out.println("Parsing texts...");
    
    List<File> files = Arrays.asList(new File("data/xml").listFiles());
    Collections.sort(files);
    
    parseTexts(
        files,
        Arrays.asList("LEWIS","REUTERS"),
        Arrays.asList("LEWIS","REUTERS","PLACES","D"),
        Arrays.asList("LEWIS","REUTERS","TEXT","BODY")
    );
    
    int i = 0;
    while(i < texts.size()) {
      if(texts.get(i).getCategories().size() != 1
          || (! validCategories.contains(texts.get(i).getCategory()))) {
        texts.remove(i);
      } else {
        ++i;
      }
    }
  }
  
  public void createDataSetsFromReutersByTopic() throws Exception {
    System.out.println("Mode: Reuters, topics [grain|interest] are labels.");
    
    System.out.println("Parsing texts...");
    
    List<File> files = Arrays.asList(new File("data/xml").listFiles());
    Collections.sort(files);
    
    parseTexts(
        files,
        Arrays.asList("LEWIS","REUTERS"),
        Arrays.asList("LEWIS","REUTERS","TOPICS","D"),
        Arrays.asList("LEWIS","REUTERS","TEXT","BODY")
    );
    
    int i = 0;
    while(i < texts.size()) {
      boolean aboutGrain = false, aboutInterest = false;
      for(String category: texts.get(i).getCategories()) {
        if(category.equals("grain")) aboutGrain = true;
        if(category.equals("interest")) aboutInterest = true;
      }
      if(aboutGrain ^ aboutInterest) {
        if(aboutGrain) texts.get(i).setCategories(Arrays.asList("grain"));
        if(aboutInterest) texts.get(i).setCategories(Arrays.asList("interest"));
        ++i;
      } else {
        texts.remove(i);
      }
    }
  }
  
  public void createDataSetsFromCustomSamples() throws Exception {
    System.out.println("Mode: Custom texts, authors [Byron|Shakespeare] are labels.");
    
    System.out.println("Parsing texts...");
    
    List<File> files = Arrays.asList(new File("data/xml-custom").listFiles());
    Collections.sort(files);
    
    parseTexts(
        files,
        Arrays.asList("CUSTOMSAMPLES","SAMPLE"),
        Arrays.asList("CUSTOMSAMPLES","SAMPLE","AUTHOR"),
        Arrays.asList("CUSTOMSAMPLES","SAMPLE","CONTENT")
    );
  }
  
  public void parseTexts(List<File> files, List<String> rootNode, List<String> categoryNode, List<String> textNode) throws Exception
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

  public void parseFuzzySets(File directory) throws Exception
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

  public List<Text> getTexts() {
    return texts;
  }

  public Map<String, Map<String, FuzzySet>> getFuzzySets() {
    return fuzzySets;
  }
}
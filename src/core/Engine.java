package core;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import text.Text;
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
      if(allowOverlap) {
        assert trainingSetSize <= 1.0 && testSetSize <= 1.0;
      } else {
        assert trainingSetSize + testSetSize <= 1.0;
      }
      for(int j = 0; j < trainingSetSize * texts.size(); ++j) {
        trainingSet.add(texts.get(j));
      }
      for(int j = (int) ((1.0 - testSetSize) * texts.size()); j < texts.size(); ++j) {
        testSet.add(texts.get(j));
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

  public void clearTexts() {
    texts.clear();
  }
  
  public void clearTrainingAndTestSets() {
    trainingSet = new LinkedList<Text>();
    testSet = new LinkedList<Text>();
  }
  
  public Map<String, Map<String, FuzzySet>> getFuzzySets() {
    return fuzzySets;
  }

  public List<Text> getTrainingSet() {
    return trainingSet;
  }

  public List<Text> getTestSet() {
    return testSet;
  }
}

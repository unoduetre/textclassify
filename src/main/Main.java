package main;

import java.io.File;

import core.Engine;
import text.Text;
import fuzzy.FuzzySet;

public class Main
{

  static Engine engine;

  public static void main(String[] args)
  {
    try
    {
      engine = new Engine();
      
      engine.parseFuzzySets(new File("data/fuzzy/countries"));

      for(String fuzzySetCategory : engine.getFuzzySets().keySet())
      {
        for(String fuzzySetType : engine.getFuzzySets().get(fuzzySetCategory).keySet())
        {
          FuzzySet fuzzySet = engine.getFuzzySets().get(fuzzySetCategory).get(fuzzySetType);
          // System.out.println(fuzzySetCategory + "_" + fuzzySetType + " = " + String.valueOf(fuzzySet));
          fuzzySet.put("keyword1",new Float(0.0));
          fuzzySet.put("keyword2",new Float(0.0));
          // System.out.println(fuzzySetCategory + "_" + fuzzySetType + " = " + String.valueOf(fuzzySet));
          fuzzySet.save();
        }
      }

      // engine.createDataSetsFromReutersByCountry();
      // engine.createDataSetsFromReutersByTopic();
      engine.createDataSetsFromCustomSamples();
      
      System.out.println("Picking training set and test set...");
      engine.pickTrainingAndTestSets(0.6, 0.4, false, false);
      
      for(Text text : engine.getTexts())
      {
        System.out.println("--------------------------------------------------------");
        System.out.println(String.valueOf(text.getCategories()));
        System.out.println(String.valueOf(text));
      }
      
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}

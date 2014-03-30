package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import core.Engine;
// import core.datamakers.ReutersByTopic;
// import core.datamakers.ReutersByCountry;
import core.datamakers.CustomSamples;
import core.datamakers.ReutersByCountry;
import text.Text;
import text.WordList;
import fuzzy.FuzzySet;
import fuzzy.WordTypeParser;

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
          System.out.println(fuzzySetCategory + "_" + fuzzySetType + " = " + String.valueOf(fuzzySet));
        }
      }

      (new ReutersByCountry()).createDataSets(engine.getTexts());
      // (new ReutersByTopic()).createDataSets(engine.getTexts());
      // (new CustomSamples()).createDataSets(engine.getTexts());
      
      System.out.println("Picking training set and test set...");
      engine.pickTrainingAndTestSets(0.6, 0.4, false, false);
      
      /* for(Text text : engine.getTexts())
      {
        System.out.println("--------------------------------------------------------");
        System.out.println(String.valueOf(text.getCategories()));
        System.out.println(String.valueOf(text));
      } */
      
      engine.createNewFuzzySets(new File("data/fuzzy/countries"), Arrays.asList("exchanges", "orgs", "people", "places", "topics"));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}

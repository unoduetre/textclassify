package comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;

public class NGramKNNComparatorTest
{
  private static NGramKNNComparator comparator = null;
  @BeforeClass
  public static void setUpClass()
  {
    comparator = new NGramKNNComparator();
  }

  @AfterClass
  public static void tearDownClass()
  {
  }

  @Before
  public void setUp()
  {
  }

  @After
  public void tearDown()
  {
  }

  @Test
  public void identicalWords()
  {
    Double wynik = comparator.calculateNGramSimilarityForWords(2,"abcd","abcd");
    assertTrue(wynik <= 1);
    assertTrue(wynik > 0.9);
  }

  @Test
  public void differentWords()
  {
    Double wynik = comparator.calculateNGramSimilarityForWords(2,"abcd","efgh");
    assertTrue(wynik >= 0);
    assertTrue(wynik < 0.1);
  }

  @Test
  public void similarWords()
  {
    Double wynik = comparator.calculateNGramSimilarityForWords(2,"abc","abd");
    assertTrue(wynik > 0.4);
    assertTrue(wynik < 0.6);
  }

  @Test
  public void identicalSentences()
  {
    Double wynik = comparator.calculateNGramSimilarityForStrings(2,new HashSet<String>(Arrays.asList("zielony","balonik")),new HashSet<String>(Arrays.asList("zielony","balonik")));
    assertTrue(wynik <= 1);
    assertTrue(wynik > 0.9);
  }

  @Test
  public void differentSentences()
  {
    Double wynik = comparator.calculateNGramSimilarityForStrings(2,new HashSet<String>(Arrays.asList("zielony","balonik")),new HashSet<String>(Arrays.asList("xxxxx","ccccc")));
    assertTrue(wynik >= 0);
    assertTrue(wynik < 0.1);
  }

  @Test
  public void similarSentences1()
  {
    Double wynik = comparator.calculateNGramSimilarityForStrings(2,new HashSet<String>(Arrays.asList("ab","xx")),new HashSet<String>(Arrays.asList("ab","yy")));
    assertTrue(wynik > 0.4);
    assertTrue(wynik < 0.6);
  }

  @Test
  public void similarSentences2()
  {
    Double wynik = comparator.calculateNGramSimilarityForStrings(2,new HashSet<String>(Arrays.asList("abc","xx")),new HashSet<String>(Arrays.asList("abd","yy")));
    assertTrue(wynik > 0.2);
    assertTrue(wynik < 0.3);
  }
}

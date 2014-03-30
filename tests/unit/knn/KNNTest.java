package knn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;

import categories.StringCategory;
import metrics.MetricClassifiable;
import comparators.MetricKNNComparator;
import metrics.EuclidMetric;
import metrics.TaxicabMetric;
import metrics.ChebyshevMetric;
import knn.Category;

public class KNNTest
{
  List<Classifiable> training = null;
  List<Classifiable> test = null;

  ClassifiableObject object = null;

  class ClassifiableObject implements MetricClassifiable
  {
    List<Double> vector = null;
    StringCategory category = null;

    public ClassifiableObject(List<Double> aVector)
    {
      vector = aVector;
    }

    public ClassifiableObject(List<Double> aVector, StringCategory aCategory)
    {
      this(aVector);
      category = aCategory;
    }

    public List<Double> getVector()
    {
      return vector;
    }

    public Category getCategory()
    {
      return category;
    }

    public void setCategory(Category aCategory)
    {
      category = (StringCategory)aCategory;
    }

    public String toString()
    {
      return String.valueOf(vector)+":"+String.valueOf(category.getString());
    }
  }

  @BeforeClass
  public static void setUpClass()
  {
  }

  @AfterClass
  public static void tearDownClass()
  {
  }

  @Before
  public void setUp()
  {
    training = new ArrayList<Classifiable>();
    test = new ArrayList<Classifiable>();

    object = new ClassifiableObject(Arrays.asList(new Double(0.0), new Double(0.0)));

    training.add(new ClassifiableObject(Arrays.asList(new Double(0.1), new Double(0.2)), new StringCategory("aaa")));
    training.add(new ClassifiableObject(Arrays.asList(new Double(0.5), new Double(0.5)), new StringCategory("aaa")));
    training.add(new ClassifiableObject(Arrays.asList(new Double(0.1), new Double(0.1)), new StringCategory("ccc")));
    training.add(new ClassifiableObject(Arrays.asList(new Double(1.0), new Double(1.0)), new StringCategory("ccc")));
    training.add(new ClassifiableObject(Arrays.asList(new Double(2.0), new Double(2.0)), new StringCategory("ccc")));

    test.add(object);
  }

  @After
  public void tearDown()
  {
  }

  @Test
  public void euclidKnn()
  {
    KNN knn = new KNN(3);

    knn.train(training);

    knn.classify(test,new MetricKNNComparator(new EuclidMetric()));
    
    assertTrue(((StringCategory)object.getCategory()).getString().equals("aaa"));
  }

  @Test
  public void taxicabKnn()
  {
    KNN knn = new KNN(3);

    knn.train(training);

    knn.classify(test,new MetricKNNComparator(new TaxicabMetric()));
    
    assertTrue(((StringCategory)object.getCategory()).getString().equals("aaa"));
  }

  @Test
  public void chebyshevKnn()
  {
    KNN knn = new KNN(3);

    knn.train(training);

    knn.classify(test,new MetricKNNComparator(new ChebyshevMetric()));
    
    assertTrue(((StringCategory)object.getCategory()).getString().equals("aaa"));
  }

}

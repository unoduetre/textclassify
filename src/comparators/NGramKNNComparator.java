package comparators;

import java.util.Set;

import knn.KNNComparator;
import knn.Classifiable;
import text.Text;

public class NGramKNNComparator implements KNNComparator
{
  private Text reference = null;

  public Classifiable getReference()
  {
    return reference;
  }

  public void setReference(Classifiable aReference)
  {
    reference = (Text)aReference;
  }

  public int compare(Classifiable text1, Classifiable text2)
  {
    Double similarity1 = calculateNGramSimilarityForStrings(3, ((Text)reference).getWords(), ((Text)text1).getWords());
    Double similarity2 = calculateNGramSimilarityForStrings(3, ((Text)reference).getWords(), ((Text)text2).getWords());

    if(similarity1 < similarity2)
    {
      return -1;
    }
    else if(similarity1 > similarity2)
    {
      return 1;
    }
    else
    {
      return 0;
    }
  }

  public Double calculateNGramSimilarityForStrings(Integer n, Set<String> words1, Set<String> words2)
  {
    Double sum = 0.0;
    for(String word1 : words1)
    {
      Double max = 0.0;
      for(String word2 : words2)
      {
        Double current = calculateNGramSimilarityForWords(n, word1, word2);
        max = current > max ? current : max;
      }
      sum += max;
    }

    return sum / words1.size();
  }

  public Double calculateNGramSimilarityForWords(Integer n, String word1, String word2)
  {
    if(word1.length() < n || word2.length() < n)
    {
      return 0.0;
    }

    Integer count = 0;

    for(Integer i = 0; i <= word1.length()-n; ++i)
    {
      String ngram1 = word1.substring(i,i+n);
      if(word2.contains(ngram1))
      {
        ++count;
      }
    }

    return (double)count / ((double)word1.length()-n+1);
  }
}

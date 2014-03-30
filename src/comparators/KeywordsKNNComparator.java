package comparators;

import java.util.Set;
import java.util.HashSet;

import knn.KNNComparator;
import knn.Classifiable;
import text.Text;

public class KeywordsKNNComparator implements KNNComparator
{
  private Text reference = null;
  Set<String> keywords = null;

  public KeywordsKNNComparator(Set<String> someKeywords)
  {
    keywords = someKeywords;
  }

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
    Set<String> intersection1 = new HashSet<String>(keywords);
    intersection1.retainAll(((Text)reference).getWords());
    intersection1.retainAll(((Text)text1).getWords());
    Set<String> antintersection1 = new HashSet<String>(keywords);
    antintersection1.removeAll(((Text)reference).getWords());
    antintersection1.removeAll(((Text)text1).getWords());

    Double similarity1 = (double)(intersection1.size() + antintersection1.size()) / (double)keywords.size();

    Set<String> intersection2 = new HashSet<String>(keywords);
    intersection2.retainAll(((Text)reference).getWords());
    intersection2.retainAll(((Text)text2).getWords());
    Set<String> antintersection2 = new HashSet<String>(keywords);
    antintersection2.removeAll(((Text)reference).getWords());
    antintersection2.removeAll(((Text)text2).getWords());

    Double similarity2 = (double)(intersection2.size() + antintersection2.size()) / (double)keywords.size();

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
}

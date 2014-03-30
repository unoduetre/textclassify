package comparators;

import java.util.Set;
import java.util.HashSet;

import knn.KNNComparator;
import knn.Classifiable;
import text.Text;

public class JaccardKNNComparator implements KNNComparator
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
    Set<String> ref1 = new HashSet<String>(((Text)reference).getWords());
    Set<String> ref2 = new HashSet<String>(ref1);
    Set<String> ref3 = new HashSet<String>(ref1);
    Set<String> ref4 = new HashSet<String>(ref1);

    ref1.retainAll(((Text)text1).getWords());
    ref2.addAll(((Text)text1).getWords());

    ref3.retainAll(((Text)text2).getWords());
    ref4.addAll(((Text)text2).getWords());

    Double similarity1 = (double)ref1.size() / (double)ref2.size();
    Double similarity2 = (double)ref3.size() / (double)ref4.size();

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

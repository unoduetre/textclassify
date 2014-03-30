package knn;

import java.util.Comparator;

public interface KNNComparator extends Comparator<Classifiable>
{
  void setReference(Classifiable object);
  Classifiable getReference();
}

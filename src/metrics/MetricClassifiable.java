package metrics;

import java.util.List;

import knn.Classifiable;

public interface MetricClassifiable extends Classifiable
{
  List<Float> getVector();
}

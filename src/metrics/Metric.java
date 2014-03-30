package metrics;

import java.util.List;

import knn.Classifiable;

abstract public class Metric
{
  abstract public int compare(List<Double> reference, List<Double> vector1, List<Double> vector2);
}

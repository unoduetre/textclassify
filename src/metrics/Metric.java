package metrics;

import java.util.List;

abstract public class Metric
{
  abstract public int compare(List<Float> reference, List<Float> vector1, List<Float> vector2);
}

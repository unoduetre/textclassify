package metrics;

import java.util.List;

public class ChebyshevMetric extends Metric
{
  public int compare(List<Float> reference, List<Float> vector1, List<Float> vector2)
  {
    Float max1 = 0.0f;
    Float max2 = 0.0f;
    Float temp = null;
    for(Integer i = 0; i < reference.size(); ++i)
    {
      temp = Math.abs(reference.get(i)-vector1.get(i));
      max1 = temp > max1 ? temp : max1;
      temp = Math.abs(reference.get(i)-vector2.get(i));
      max2 = temp > max2 ? temp : max2;
    }

    if(max1 < max2)
    {
      return 1;
    }
    else if(max1 > max2)
    {
      return -1;
    }
    else
    {
      return 0;
    }
  }
}

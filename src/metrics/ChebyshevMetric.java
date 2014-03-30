package metrics;

import java.util.List;

public class ChebyshevMetric extends Metric
{
  public int compare(List<Double> reference, List<Double> vector1, List<Double> vector2)
  {
    Double max1 = 0.0;
    Double max2 = 0.0;
    Double temp = null;
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

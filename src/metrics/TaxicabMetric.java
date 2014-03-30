package metrics;

import java.util.List;

public class TaxicabMetric extends Metric
{
  public int compare(List<Double> reference, List<Double> vector1, List<Double> vector2)
  {
    Double sum1 = 0.0;
    Double sum2 = 0.0;
    for(Integer i = 0; i < reference.size(); ++i)
    {
      sum1+= Math.abs(reference.get(i)-vector1.get(i));
      sum2+= Math.abs(reference.get(i)-vector2.get(i));
    }

    if(sum1 < sum2)
    {
      return -1;
    }
    else if(sum1 > sum2)
    {
      return 1;
    }
    else
    {
      return 0;
    }
  }
}

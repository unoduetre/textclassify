package metrics;

import java.util.List;

public class TaxicabMetric extends Metric
{
  public int compare(List<Float> reference, List<Float> vector1, List<Float> vector2)
  {
    Float sum1 = 0.0f;
    Float sum2 = 0.0f;
    for(Integer i = 0; i < reference.size(); ++i)
    {
      sum1+= Math.abs(reference.get(i)-vector1.get(i));
      sum2+= Math.abs(reference.get(i)-vector2.get(i));
    }

    if(sum1 < sum2)
    {
      return 1;
    }
    else if(sum1 > sum2)
    {
      return -1;
    }
    else
    {
      return 0;
    }
  }
}

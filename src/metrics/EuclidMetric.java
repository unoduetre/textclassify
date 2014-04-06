package metrics;

import java.util.List;

public class EuclidMetric extends Metric
{
  public int compare(List<Float> reference, List<Float> vector1, List<Float> vector2)
  {
    Float sum1 = 0.0f;
    Float sum2 = 0.0f;
    Float temp = 0.0f;
    for(Integer i = 0; i < reference.size(); ++i)
    {
      temp = reference.get(i)-vector1.get(i);
      sum1+= temp*temp;
      temp = reference.get(i)-vector2.get(i);
      sum2+= temp*temp;
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

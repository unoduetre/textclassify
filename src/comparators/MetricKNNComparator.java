package comparators;

import metrics.Metric;
import metrics.MetricClassifiable;
import knn.KNNComparator;
import knn.Classifiable;

public class MetricKNNComparator implements KNNComparator
{
  MetricClassifiable reference = null;
  Metric metric = null;

  public MetricClassifiable getReference()
  {
    return reference;
  }

  public void setReference(Classifiable aReference)
  {
    reference = (MetricClassifiable)aReference;
  }

  public MetricKNNComparator(Metric aMetric)
  {
    metric = aMetric;
  }

  public int compare(Classifiable object1, Classifiable object2)
  {
    return metric.compare(reference.getVector(), ((MetricClassifiable)object1).getVector(), ((MetricClassifiable)object2).getVector());
  }
}

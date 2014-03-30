package extraction;

import java.util.List;

import metrics.MetricClassifiable;
import text.Text;

public interface VectorManager {
  public List<MetricClassifiable> getVectors();
  
  public MetricClassifiable getVectorForNewSample(Text text);
}

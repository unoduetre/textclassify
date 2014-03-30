package extraction;

import java.util.List;

import knn.Classifiable;

import text.Text;

public interface VectorManager {
  public List<Classifiable> getVectors();
  
  public Classifiable getVectorForNewSample(Text text);
}

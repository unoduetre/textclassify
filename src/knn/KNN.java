package knn;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.PriorityQueue;

public class KNN
{
  private List<Classifiable> votes = null;
  private Integer k = null;

  public KNN(Integer aK)
  {
    k = aK;
  }

  public void train(List<Classifiable> someVotes)
  {
    votes = someVotes;
  }

  public void classify(List<Classifiable> objects, KNNComparator comparator)
  {
    for(Classifiable object : objects)
    {
      comparator.setReference(object);
      PriorityQueue<Classifiable> queue = new PriorityQueue<Classifiable>(votes.size(), comparator);
      for(Classifiable vote : votes)
      {
        queue.add(vote);
        while(queue.size() > k)
        {
          queue.poll();
        }
      }

      // System.out.println(queue);

      Map<Category, Integer> votingTable = new TreeMap<Category, Integer>();
      for(Classifiable vote : queue)
      {
        Category category = vote.getCategory();
        if(votingTable.get(category) == null)
        {
          votingTable.put(category,1);
        }
        else
        {
          votingTable.put(category,votingTable.get(category)+1);
        }
      }

      // System.out.println(votingTable);

      Integer maximalCount = 0;
      Category maximalCategory = null;
      for(Map.Entry<Category, Integer> entry : votingTable.entrySet())
      {
        if(entry.getValue() > maximalCount)
        {
          maximalCount = entry.getValue();
          maximalCategory = entry.getKey();
        }
      }

      object.setCategory(maximalCategory);
    }
  }
}

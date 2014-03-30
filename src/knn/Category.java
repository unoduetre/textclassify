package knn;

public interface Category extends Comparable<Category>
{
  int compareTo(Category aCategory);  
  boolean equals(Category category);
}

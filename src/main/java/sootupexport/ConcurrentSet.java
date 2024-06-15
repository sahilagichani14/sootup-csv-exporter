package sootupexport;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentSet<E> implements Set<E> {
  private final ConcurrentMap<E, Boolean> map;

  // Constructor
  public ConcurrentSet() {
    this.map = new ConcurrentHashMap<>();
  }

  // Adds the specified element to this set if it is not already present
  @Override
  public boolean add(E e) {
    return map.putIfAbsent(e, Boolean.TRUE) == null;
  }

  // Removes the specified element from this set if it is present
  @Override
  public boolean remove(Object o) {
    return map.remove(o) != null;
  }

  // Returns true if this set contains the specified element
  @Override
  public boolean contains(Object o) {
    return map.containsKey(o);
  }

  // Returns the number of elements in this set
  @Override
  public int size() {
    return map.size();
  }

  // Returns true if this set contains no elements
  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  // Removes all of the elements from this set
  @Override
  public void clear() {
    map.clear();
  }

  // Returns an iterator over the elements in this set
  @Override
  public Iterator<E> iterator() {
    return map.keySet().iterator();
  }

  // Returns an array containing all of the elements in this set
  @Override
  public Object[] toArray() {
    return map.keySet().toArray();
  }

  // Returns an array containing all of the elements in this set; the runtime type of the returned
  // array is that of the specified array
  @Override
  public <T> T[] toArray(T[] a) {
    return map.keySet().toArray(a);
  }

  // Returns true if this set contains all of the elements of the specified collection
  @Override
  public boolean containsAll(Collection<?> c) {
    return map.keySet().containsAll(c);
  }

  // Adds all of the elements in the specified collection to this set if they're not already present
  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean modified = false;
    for (E e : c) {
      if (add(e)) {
        modified = true;
      }
    }
    return modified;
  }

  // Retains only the elements in this set that are contained in the specified collection
  @Override
  public boolean retainAll(Collection<?> c) {
    boolean modified = false;
    for (E e : map.keySet()) {
      if (!c.contains(e)) {
        remove(e);
        modified = true;
      }
    }
    return modified;
  }

  // Removes from this set all of its elements that are contained in the specified collection
  @Override
  public boolean removeAll(Collection<?> c) {
    boolean modified = false;
    for (Object e : c) {
      if (remove(e)) {
        modified = true;
      }
    }
    return modified;
  }

  // Returns true if this set is equal to the specified object
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Set)) {
      return false;
    }
    Set<?> that = (Set<?>) o;
    return this.size() == that.size() && this.containsAll(that);
  }

  // Returns the hash code value for this set
  @Override
  public int hashCode() {
    return map.keySet().hashCode();
  }
}

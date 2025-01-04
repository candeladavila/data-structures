package org.uma.ed.datastructures.dictionary;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.uma.ed.datastructures.searchtree.AVL;
import org.uma.ed.datastructures.searchtree.SearchTree;
import org.uma.ed.datastructures.set.AVLSet;

/**
 * Dictionaries (finite maps) associating different keys to values implemented as AVL trees sorted by keys. Notice that
 * the order of keys is provided by the corresponding {@code Comparator}.
 *
 * @param <K> Type of keys.
 * @param <V> Types of values.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class AVLDictionary<K, V> extends AbstractSortedDictionary<K, V> implements SortedDictionary<K, V> {
  private final Comparator<K> comparator;
  private final SearchTree<Entry<K, V>> avlTree;

  private AVLDictionary(Comparator<K> comparator, SearchTree<Entry<K, V>> avlTree) {
    this.comparator = comparator;
    this.avlTree = avlTree;
  }

  /**
   * Creates an empty AVLDictionary. keys are sorted using provided Comparator.
   * <p> Time complexity: O(1)
   *
   * @param comparator Comparator defining order of keys in this sorted dictionary.
   */
  public AVLDictionary(Comparator<K> comparator) {
    this(comparator, new AVL<>(Entry.onKeyComparator(comparator)));
  }

  /**
   * Constructs an empty AVLDictionary. keys are sorted using provided comparator.
   * <p> Time complexity: O(1)
   */
  public static <K, V> AVLDictionary<K, V> empty(Comparator<K> comparator) {
    return new AVLDictionary<>(comparator);
  }

  /**
   * Constructs an empty AVLDictionary. keys are sorted using their natural order.
   * <p> Time complexity: O(1)
   */
  public static <K extends Comparable<? super K>, V> AVLDictionary<K, V> empty() {
    return new AVLDictionary<K, V>(Comparator.naturalOrder());
  }

  /**
   * Returns a new AVLDictionary with provided entries and with keys sorted using provided comparator.
   *
   * @param comparator Comparator defining order of keys in new sorted dictionary.
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new AVLDictionary with provided entries and keys sorted using provided comparator.
   */
  @SafeVarargs
  public static <K, V> AVLDictionary<K, V> of(Comparator<K> comparator, Entry<K, V>... entries) {
    AVLDictionary<K, V> dictionary = new AVLDictionary<>(comparator);
    for (Entry<K, V> entry : entries) {
      dictionary.insert(entry);
    }
    return dictionary;
  }

  /**
   * Returns a new AVLDictionary with provided entries and with keys sorted using their natural order.
   *
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new AVLDictionary with provided entries and keys sorted using their natural order.
   */
  @SafeVarargs
  public static <K extends Comparable<? super K>, V> AVLDictionary<K, V> of(Entry<K, V>... entries) {
    return of(Comparator.naturalOrder(), entries);
  }

  /**
   * Returns a new AVLDictionary with entries in provided iterable and with keys sorted using provided comparator.
   *
   * @param comparator Comparator defining order of keys in new sorted dictionary.
   * @param iterable iterable with entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new AVLDictionary with provided iterable and keys sorted using provided comparator.
   */
  public static <K, V> AVLDictionary<K, V> from(Comparator<K> comparator, Iterable<Entry<K, V>> iterable) {
    AVLDictionary<K, V> dictionary = new AVLDictionary<>(comparator);
    for (Entry<K, V> entry : iterable) {
      dictionary.insert(entry);
    }
    return dictionary;
  }

  /**
   * Returns a new AVLDictionary with provided entries and with keys sorted using their natural order.
   *
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new AVLDictionary with provided entries and keys sorted using their natural order.
   */
  public static <K extends Comparable<? super K>, V> AVLDictionary<K, V> from(Iterable<Entry<K, V>> entries) {
    return from(Comparator.naturalOrder(), entries);
  }

  /**
   * Returns a new AVLDictionary with same elements as argument.
   * <p> Time complexity: O(n)
   *
   * @param that AVLDictionary to be copied.
   *
   * @return a new AVLDictionary with same elements as {@code that}.
   */
  public static <K, V> AVLDictionary<K, V> copyOf(AVLDictionary<K, V> that) {
    AVLDictionary<K,V> copy = new AVLDictionary<>(that.comparator); // create new AVLDictionary with same comparator
    for (Entry<K, V> entry : that) {
      copy.insert(entry);
    }
    return copy;
  }

  /**
   * Returns a new AVLDictionary with same elements as argument.
   * <p> Time complexity: O(n x log n)
   *
   * @param that Dictionary to be copied.
   *
   * @return a new AVLDictionary with same elements as {@code that}.
   */
  public static <K, V> AVLDictionary<K, V> copyOf(SortedDictionary<K, V> that) {
    AVLDictionary<K,V> copy = new AVLDictionary<>(that.comparator()); // create new AVLDictionary with same comparator
    for (Entry<K, V> entry : that) {
      copy.insert(entry);
    }
    return copy;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public boolean isEmpty() {
    return avlTree.isEmpty(); // return if AVLDictionary is empty
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public int size() {
    return avlTree.size(); // return size of AVLDictionary
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public Comparator<K> comparator() {
    return comparator; // return comparator of AVLDictionary
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public void insert(Entry<K, V> entry) {
    avlTree.insert(entry);
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public V valueOf(K key) {
    if (isEmpty()){
      throw new NoSuchElementException("valueOf on empty dictionary");
    }
    return avlTree.search(Entry.of(key, null)).value(); // return value of key in AVLDictionary
    //El método search devuelve el elemento que se le pasa por parámetro
    //Usando el .value() devuelve el valor del elemento que se le pasa por parámetro
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public boolean isDefinedAt(K key) {
    if(isEmpty()){
      return false; // return false if AVLDictionary is empty
    } else{
        return avlTree.contains(Entry.of(key, null)); // return if AVLDictionary contains key
    }
    //El método contains devuelve un booleano que indica si el elemento pasado por parámetro está en el árbol
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public void delete(K key) {
    avlTree.delete(Entry.of(key, null)); //delete key from AVLDictionary
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: near O(1)
   */
  @Override
  public void clear() {
    avlTree.clear(); // clear AVLDictionary
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public Entry<K, V> minimum() {
    if (isEmpty()){
      throw new NoSuchElementException("minimum on empty dictionary");
    }
    return avlTree.minimum(); // return minimum element of AVLDictionary
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public Entry<K, V> maximum() {
    if (isEmpty()){
      throw new NoSuchElementException("maximum on empty dictionary");
    }
    return avlTree.maximum(); // return maximum element of AVLDictionary
  }

  private class EntryIterator {
    private final Iterator<Entry<K, V>> iterator;

    private EntryIterator(Iterator<Entry<K, V>> iterator) {
      this.iterator = iterator;
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    public Entry<K, V> nextEntry() {
      return iterator.next();
    }
  }

  private final class KeyIterator extends EntryIterator implements Iterator<K> {
    private KeyIterator(Iterator<Entry<K, V>> iterator) {
      super(iterator);
    }

    public K next() {
      return nextEntry().key();
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public Iterable<K> keys() {
    return () -> new KeyIterator(avlTree.inOrder().iterator());
  }

  private final class ValueIterator extends EntryIterator implements Iterator<V> {
    private ValueIterator(Iterator<Entry<K, V>> iterator) {
      super(iterator);
    }

    public V next() {
      return nextEntry().value();
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public Iterable<V> values() {
    return () -> new ValueIterator(avlTree.inOrder().iterator());
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public Iterable<Entry<K, V>> entries() {
    return () -> (Iterator<Entry<K, V>>) new EntryIterator(avlTree.inOrder().iterator());
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return entries().iterator();
  }
}

package org.uma.ed.datastructures.dictionary;


import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Sorted dictionaries implemented using a sorted linked structure.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class SortedLinkedDictionary<K, V> extends AbstractSortedDictionary<K, V> implements SortedDictionary<K, V> {
  private static final class Node<K, V> {
    Entry<K, V> entry;
    Node<K, V> next;

    Node(Entry<K, V> entry, Node<K, V> next) {
      this.entry = entry;
      this.next = next;
    }
  }

  /**
   * References to first and last nodes in linked structure.
   */
  private Node<K, V> first, last;

  /**
   * Number of entries in dictionary.
   */
  private int size;

  /**
   * Comparator defining order of keys in dictionary.
   */
  private final Comparator<K> comparator;

  /* INVARIANT:
   * - `size` is number of entries in dictionary.
   * - nodes are sorted by key according to the order defined by `comparator`.
   * - `first` is a reference to the node storing first entry in dictionary or null if dictionary is empty.
   * - `last` is a reference to the node with last entry in dictionary or null if dictionary is empty.
   * - each node contains a reference to the next node or null if it is the last node.
   */

  /**
   * Constructs an empty SortedLinkedDictionary. Keys are sorted using provided comparator.
   * <p> Time complexity: O(1)
   */
  public SortedLinkedDictionary(Comparator<K> comparator) {
    this.first = null;
    this.last = null;
    this.size = 0;
    this.comparator = comparator;
  }

  /**
   * Constructs an empty SortedLinkedDictionary. Keys are sorted using provided comparator.
   * <p> Time complexity: O(1)
   */
  public static <K, V> SortedLinkedDictionary<K, V> empty(Comparator<K> comparator) {
    return new SortedLinkedDictionary<>(comparator);
  }

  /**
   * Constructs an empty SortedLinkedDictionary. Keys are sorted using their natural order.
   * <p> Time complexity: O(1)
   */
  public static <K extends Comparable<? super K>, V> SortedLinkedDictionary<K, V> empty() {
    return new SortedLinkedDictionary<K, V>(Comparator.naturalOrder());
  }

  /**
   * Returns a new SortedLinkedDictionary with provided entries and with keys sorted using provided comparator.
   *
   * @param comparator Comparator defining order of keys in new sorted dictionary.
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new SortedLinkedDictionary with provided entries and keys sorted using provided comparator.
   */
  @SafeVarargs
  public static <K, V> SortedLinkedDictionary<K, V> of(Comparator<K> comparator, Entry<K, V>... entries) {
    SortedLinkedDictionary<K, V> dictionary = new SortedLinkedDictionary<>(comparator);
    for (Entry<K, V> entry : entries) {
      dictionary.insert(entry);
    }
    return dictionary;
  }

  /**
   * Returns a new SortedLinkedDictionary with provided entries and with keys sorted using their natural order.
   *
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new SortedLinkedDictionary with provided entries and keys sorted using their natural order.
   */
  @SafeVarargs
  public static <K extends Comparable<? super K>, V> SortedLinkedDictionary<K, V> of(Entry<K, V>... entries) {
    return of(Comparator.naturalOrder(), entries);
  }

  /**
   * Returns a new SortedLinkedDictionary with provided entries and with keys sorted using provided comparator.
   *
   * @param comparator Comparator defining order of keys in new sorted dictionary.
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new SortedLinkedDictionary with provided entries and keys sorted using provided comparator.
   */
  public static <K, V> SortedLinkedDictionary<K, V> from(Comparator<K> comparator, Iterable<Entry<K, V>> entries) {
    SortedLinkedDictionary<K, V> dictionary = new SortedLinkedDictionary<>(comparator);
    for (Entry<K, V> entry : entries) {
      dictionary.insert(entry);
    }
    return dictionary;
  }

  /**
   * Returns a new SortedLinkedDictionary with provided entries and with keys sorted using their natural order.
   *
   * @param entries Entries to include in new dictionary.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a new SortedLinkedDictionary with provided entries and keys sorted using their natural order.
   */
  public static <K extends Comparable<? super K>, V> SortedLinkedDictionary<K, V> from(Iterable<Entry<K, V>> entries) {
    return from(Comparator.naturalOrder(), entries);
  }

  /**
   * Returns a new SortedLinkedDictionary with same elements as argument.
   * <p> Time complexity: O(n)
   *
   * @param that sorted dictionary to be copied.
   * @param <K> Type of keys.
   * @param <V> Type of values.
   *
   * @return a SortedLinkedDictionary with same keys and values as argument.
   */
  public static <K, V> SortedLinkedDictionary<K, V> copyOf(SortedDictionary<K, V> that) {
    SortedLinkedDictionary<K, V> copy = new SortedLinkedDictionary<>(that.comparator());
    for (Entry<K, V> entry : that.entries()) {
      copy.append(entry);
    }
    return copy;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public Comparator<K> comparator() {
    return comparator;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Finder is an auxiliary class used to search for an element within the sorted linked structure.
   * It maintains two pointers, `previous` and `current`, to nodes in the linked structure.
   * After a search operation:
   * - If the key is found:
   *   - `found` is set to true.
   *   - `current` points to the node containing the searched key.
   *   - `previous` points to the preceding node, or is null if the element is in the first node of the linked
   *      structure.
   * - If the key is not found:
   *   - `found` is set to false.
   *   - `current` points to the node that would follow the key if it were in the list.
   *   - `previous` points to the node that would precede the key, or is null if the element would be at the first
   *      node.
   * This class is used to implement the insert, valueOf, isDefinedAt and delete operations in a way that avoids
   * redundant searches.
   */
  private final class Finder {
    Node<K, V> previous, current;
    boolean found;

    public Finder(K key) {
      previous = null;
      current = first;

      int cmp = 0;
      while (current != null && (cmp = comparator.compare(key, current.entry.key())) > 0) {
        previous = current;
        current = current.next;
      }

      found = current != null && cmp == 0;
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */

  /*FINDER: facilita la inserción y el borrado
  - previous y current lo que hace es recorrer el diccionario
  - comparator -> devuelve -1,0,1 dependiendo del recorrido
  - recorremos la lista de forma que current apunta al elemento y previous apunta al anterior y found devuelve TRUE / FALSE dependiendo de si lo hemos
    encontrado o no
  - finder también nos sirve para buscar el sitio en el que debería ir la clave donde la tendré que insertar.
    - compraramos la clave que estamos buscando sobre la lista de clase
      - si se encuentra se machaca el valor que había (current en el valor siguiente y previous el anterior)
  */

  @Override
  public void insert(Entry<K, V> entry) {
    Finder finder = new Finder(entry.key()); //cremos un objeto finder con el elemento que queremos comprobar si está o no
    if (finder.found){ //si el elemento no se encuentra en el diccionario, se añade
      finder.current.entry = entry;
    }else{
      if(finder.previous == null){ //insertar el primero -> afecta al primero
        Node<K,V> node = new Node<>(entry, first);
        first = node;
        if (last == null){ //si el ultimo es null entonces el primero es igual al ultimo (solo hay un elemento)
          last = first;
        }
      }else{ //el elemento no es el primero
        Node<K, V> node = new Node<>(entry, finder.current);  //creamos nodo
        if (finder.current==null){ //si no se encuentra la clave -> se añade al final
          last = node; //actualización last = node
        }
        finder.previous.next = node; //se actualiza la referencia del next del elemento anterior para que su siguiente sea el nuevo nodo
      }
      size ++; //aumentamos la variable size después de haber añadido el elemento
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public V valueOf(K key) {
    Finder finder = new Finder(key);
    if(finder.found) {
    return finder.current.entry.value();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public boolean isDefinedAt(K key) {
    Finder finder = new Finder(key);
    return finder.found;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public void delete(K key) {
    Finder finder = new Finder(key);
    if (finder.found) { //si el elemento que queremos eliminar se encuentra en la lista
      if (finder.previous == null) { //primer elemento
        first = finder.current.next;
        if (first == null) { //si el siguiente es null es porque solo había un elemento
          last = null; //el ultimo lo ponemos a null
        }
      } else { //en caso de que sea cualquier otro elemento
        finder.previous.next = finder.current.next;
        if (finder.current == last) { //si el elemento es el último
          last = finder.previous; //redefinimos el último como su anterior
        }
      }
      size--; //reducimos el tamaño después de haber eliminado el elemento
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public void clear() {
    first = null;
    last = null;
    size = 0; 
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public Entry<K, V> minimum() {
    if (isEmpty()) {
      throw new NoSuchElementException("Dictionary is empty.");
    }
    return first.entry;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public Entry<K, V> maximum() {
    if (isEmpty()) {
      throw new NoSuchElementException("Dictionary is empty.");
    }
    return last.entry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<K> keys() {
    return KeyIterator::new;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<V> values() {
    return ValueIterator::new;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Entry<K, V>> entries() {
    return EntriesIterator::new;
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return entries().iterator();
  }

  private void append(Entry<K, V> entry) {
    assert first == null || comparator.compare(entry.key(), last.entry.key()) > 0;

    Node<K, V> node = new Node<>(entry, null);
    if (first == null) {
      first = node;
    } else {
      last.next = node;
    }
    last = node;
    size++;
  }

  // Almost an iterator on nodes in linked structure
  private class NodeIterator {
    Node<K, V> current;

    public NodeIterator() {
      current = first;
    }

    public boolean hasNext() {
      return current != null;
    }

    public Node<K, V> nextNode() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Node<K, V> node = current;
      current = current.next;
      return node;
    }
  }

  private final class KeyIterator extends NodeIterator implements Iterator<K> {
    @Override
    public K next() {
      return super.nextNode().entry.key();
    }
  }

  private final class ValueIterator extends NodeIterator implements Iterator<V> {
    @Override
    public V next() {
      return super.nextNode().entry.value();
    }
  }

  private final class EntriesIterator extends NodeIterator implements Iterator<Entry<K, V>> {
    @Override
    public Entry<K, V> next() {
      Node<K, V> node = super.nextNode();
      return node.entry;
    }
  }
}

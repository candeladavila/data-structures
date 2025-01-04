package org.uma.ed.datastructures.heap;

import java.util.Comparator;
import org.uma.ed.datastructures.list.ArrayList;
import org.uma.ed.datastructures.queue.Queue;

/**
 * Heap implemented using weight biased leftist heap-ordered binary trees.
 *
 * @param <T> Type of elements in heap.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class WBLeftistHeap<T> implements Heap<T> {
  private static final class Node<E> {
    E element;
    int weight; // number of elements in tree
    Node<E> left, right;

    // constructs a node given its element, weight and children
    Node(E element, int weight, Node<E> left, Node<E> right) {
      this.element = element;
      this.weight = weight;
      this.left = left;
      this.right = right;
    }

    // constructs a singleton node with given element
    Node(E element) {
      this(element, 1, null, null);
    }
  }

  /*
   * INVARIANT:
   * - All nodes adhere to the Heap Order Property (HOP): a node's element is ≤ its children's elements.
   * - The tree is structured as a weight biased leftist heap:
   *   - Each node's left child has a weight (total elements) ≥ the right child's weight.
   *   - This ensures the right spine's length is logarithmic relative to the heap's size.
   */

  /**
   * Comparator used to order elements in heap.
   */
  private final Comparator<T> comparator;

  /**
   * Reference to root of this heap.
   */
  private Node<T> root;

  private WBLeftistHeap(Comparator<T> comparator, Node<T> root) {
    this.comparator = comparator;
    this.root = root;
  }

  /**
   * Creates an empty Weight Biased Leftist Heap.
   * <p> Time complexity: O(1)
   */
  public WBLeftistHeap(Comparator<T> comparator) {
    this(comparator, null);
  }

  public static <T> WBLeftistHeap<T> empty(Comparator<T> comparator) {
    return new WBLeftistHeap<>(comparator);
  }

  public static <T extends Comparable<? super T>> WBLeftistHeap<T> empty() {
    return new WBLeftistHeap<T>(Comparator.naturalOrder());
  }

  /**
   * Constructs a Weight Biased Leftist Heap from a list of singleton nodes in O(n) time.
   * @param comparator comparator to use
   * @param nodes list of singleton nodes
   * @param <T> type of elements
   *
   * @return skew heap with elements in nodes
   */
  private static <T> WBLeftistHeap<T> merge(Comparator<T> comparator, ArrayList<Node<T>> nodes) {
    // If the list of nodes is empty, return an empty heap
    if (nodes.isEmpty()) {
      return new WBLeftistHeap<>(comparator);
    }

    // Initialize the root with the first node
    Node<T> root = nodes.get(0); //definimos la raíz

    // Merge each subsequent node into the root
    for (int i = 1; i < nodes.size(); i++) {
      root = mergeNodes(root, nodes.get(i), comparator);
    }

    return new WBLeftistHeap<>(comparator, root); //creamos el árbol izquierdista
  }

  // Método auxiliar para fusionar dos nodos
  private static <T> Node<T> mergeNodes(Node<T> node1, Node<T> node2, Comparator<T> comparator) {
    // Implementación de fusión de nodos, ya definida en la clase principal
    if (node1 == null) return node2;
    if (node2 == null) return node1;

    if (comparator.compare(node1.element, node2.element) > 0) {
      // Intercambiamos para mantener la propiedad del heap
      Node<T> temp = node1;
      node1 = node2;
      node2 = temp;
    }

    // Fusionamos el subárbol derecho de node1 con node2
    node1.right = mergeNodes(node1.right, node2, comparator);

    // Ajustamos los pesos y garantizamos la estructura WBLeftist
    int weightLeft = weight(node1.left);
    int weightRight = weight(node1.right);
    node1.weight = weightLeft + weightRight + 1;

    if (weightLeft < weightRight) {
      // Si el subárbol derecho es más pesado, intercambiamos
      Node<T> temp = node1.left;
      node1.left = node1.right;
      node1.right = temp;
    }

    return node1;
  }


  /**
   * Constructs a Weight Biased Leftist Heap from a sequence of elements and a comparator.
   * <p>
   * Time complexity: O(n)
   * @param comparator comparator to use
   * @param elements elements to insert in heap
   * @param <T> type of elements
   *
   * @return a Weight Biased Leftist Heap with elements in sequence
   */
  @SafeVarargs
  public static <T> WBLeftistHeap<T> of(Comparator<T> comparator, T... elements) {
    ArrayList<Node<T>> nodes = ArrayList.withCapacity(elements.length);
    for (T element : elements) {
      nodes.append(new Node<>(element));
    }
    return merge(comparator, nodes);
  }

  /**
   * Constructs a Weight Biased Leftist Heap from a sequence of elements and natural order comparator.
   * <p>
   * Time complexity: O(n)
   * @param elements elements to insert in heap
   * @param <T> type of elements
   *
   * @return a Weight Biased Leftist Heap with elements in sequence
   */
  @SafeVarargs
  public static <T extends Comparable<? super T>> WBLeftistHeap<T> of(T... elements) {
    return of(Comparator.naturalOrder(), elements);
  }

  /**
   * Constructs a Weight Biased Leftist Heap from an iterable collection and a comparator.
   * <p>
   * Time complexity: O(n)
   * @param comparator comparator to use
   * @param iterable collection of elements to insert in heap
   * @param <T> type of elements
   *
   * @return a Weight Biased Leftist Heap with elements in collection
   */
  public static <T> WBLeftistHeap<T> from(Comparator<T> comparator, Iterable<T> iterable) {
    ArrayList<Node<T>> nodes = ArrayList.empty();
    for (T element : iterable) {
      nodes.append(new Node<>(element));
    }
    return merge(comparator, nodes);
  }

  /**
   * Constructs a Weight Biased Leftist Heap from an iterable collection of elements and natural order comparator.
   * <p>
   * Time complexity: O(n)
   * @param iterable collection of elements to insert in heap
   * @param <T> type of elements
   *
   * @return a Weight Biased Leftist Heap with elements in collection
   */
  public static <T extends Comparable<? super T>> WBLeftistHeap<T> from(Iterable<T> iterable) {
    return from(Comparator.naturalOrder(), iterable);
  }

  /**
   * <p> Time complexity: O(n)
   */
  public static <T> WBLeftistHeap<T> copyOf(WBLeftistHeap<T> that) {
    return new WBLeftistHeap<>(that.comparator, copyOf(that.root));
  }

  // copies a tree
  private static <T> Node<T> copyOf(Node<T> node) {
    if (node == null) {
      return null;
    }
    return new Node<>(node.element, node.weight, copyOf(node.left), copyOf(node.right));
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public Comparator<T> comparator() {
    return comparator;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public boolean isEmpty() {
    return root == null;
  }

  // returns weight of a node
  private static <T> int weight(Node<T> node) {
    return node == null ? 0 : node.weight;
  }
  
  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public int size() {
    return weight(root); //el tamaño del árbol es el peso del árbol
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public void clear() {
    root = null; //garbage collector does the rest of the implementation to clear the tree
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   */
  @Override
  public void insert(T element) {
    //creamos un árbol  con ese nodo
    Node<T> singleton = new Node<>(element);
    root = merge(root, singleton);
  }

  /* Merges two heap trees along their right spines.
   * Returns merged heap. Reuses nodes during merge
   */
  private Node<T> merge(Node<T> node1, Node<T> node2) {
    //if one of the trees is empty, the result is another tree
    if (node1 == null){
      return node2;
    }
    if (node2 == null){
      return node1;
    }
    // force node1 to be smaller than node2
    if (comparator.compare(node2.element, node1.element) <0){ // si la raíz del árbol 2 es menor que el 1 -> intercambiamos
      Node<T> temp = node1;
      node1 = node2;
      node2 = temp;
    }

     //keep merging along right spine
    node1.right = merge(node1.right, node2);

    int weightLeft = weight(node1.left);
    int weightRight = weight(node1.right);
    node1.weight = weightRight + weightLeft + 1; //set new weight

    //put always heavier heap on left side
    if (weightLeft < weightRight){ // Si el peso de la izquierda es menor que la derecha -> intercambiamos
      Node<T> temp = node1.left;
      node1.left = node1.right;
      node1.right = temp;
    }
    return node1;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws <code>EmptyHeapException</code> if heap stores no element.
   */
  @Override
  public T minimum() {
    if (isEmpty()){
      throw new EmptyHeapException("minimum on emptyHeap");
    }
    return root.element;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(log n)
   *
   * @throws <code>EmptyHeapException</code> if heap stores no element.
   */
  @Override
  public void deleteMinimum() {
    if (isEmpty()){
      throw new EmptyHeapException("deleteMinimum on emptyHeap");
    }
    root = merge(root.left, root.right); //resultado de fusionar los dos subárboles de la raíz
  }

  /**
   * Returns representation of this heap as a String.
   */
  @Override
  public String toString() {
    String className = getClass().getSimpleName();
    StringBuilder sb = new StringBuilder(className).append("(");
    toString(sb, root);
    sb.append(")");
    return sb.toString();
  }

  private static void toString(StringBuilder sb, Node<?> node) {
    if (node == null) {
      sb.append("null");
    } else {
      String className = node.getClass().getSimpleName();
      sb.append(className).append("(");
      toString(sb, node.left);
      sb.append(", ");
      sb.append(node.element);
      sb.append(", ");
      toString(sb, node.right);
      sb.append(")");
    }
  }
}

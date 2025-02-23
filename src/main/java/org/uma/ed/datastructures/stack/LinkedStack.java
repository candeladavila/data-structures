package org.uma.ed.datastructures.stack;

/**
 * This class represents a Stack data structure implemented using a linked structure of nodes.
 * Each node in the structure contains a reference to the next node and an element of type T.
 * The top of the stack corresponds to the first node in the linked structure.
 *
 * @param <T> Type of elements in stack.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */


public class LinkedStack<T> extends AbstractStack<T> implements Stack<T> {
  /**
   * This class represents a node in a linked structure. Each node contains an element and a reference to the next node.
   * @param <E> Type of elements in node.
   */
  private static final class Node<E> {
    E element;
    Node<E> next;

    Node(E element, Node<E> next) { //Node Constructor -> crea un nuevo nodo pasando como parámetro el elemento y un next
      this.element = element;
      this.next = next; //en el next que pasamos como parámetro es el elemento al que se lo vamos a enganchar
    }
  }

  /**
   * Reference to node on top of stack or null if stack is empty.
   */

  //ATRIBUTOS LISTA ENLAZADA
  private Node<T> top;

  /**
   * Number of elements in stack.
   */
  private int size;

  /*
   * INVARIANT:
   *  - if stack is empty, `top` is null.
   *  - if stack is not empty, `top` references first node in stack.
   *  - each node in stack contains a reference to element that is below in stack or null if it is the node at the bottom.
   *  - `size` is number of elements in stack.
   */

  /**
   * Creates an empty LinkedStack.
   * <p> Time complexity: O(1)
   */
  public LinkedStack() { //inicialización de la pila
    top = null;
    size = 0;
  }

  /**
   * Creates an empty LinkedStack.
   * <p> Time complexity: O(1)
   *
   * @param <T> Type of elements in stack.
   *
   * @return an empty LinkedStack.
   */
  //MÉTODO FÁBRICA
  public static <T> LinkedStack<T> empty() {
    return new LinkedStack<>();
  }

  /**
   * Creates a LinkedStack with given elements.
   * <p> Time complexity: O(n)
   *
   * @param elements elements to be added to stack.
   * @param <T> Type of elements in stack.
   *
   * @return a LinkedStack with given elements.
   */
  @SafeVarargs
  //MÉTODO FÁBRICA
  public static <T> LinkedStack<T> of(T... elements) {
    LinkedStack<T> stack = new LinkedStack<>();
    for (T element : elements) {
      stack.push(element);
    }
    return stack;
  }

  /**
   * Creates a LinkedStack with elements in given iterable.
   * <p> Time complexity: O(n)
   *
   * @param iterable {@code Iterable} of elements to be added to stack.
   * @param <T> Type of elements in iterable.
   *
   * @return a LinkedStack with elements in given iterable.
   */
  //MÉTODO FÁBRICA
  public static <T> LinkedStack<T> from(Iterable<T> iterable) {
    LinkedStack<T> stack = new LinkedStack<>();
    for (T element : iterable) {
      stack.push(element);
    }
    return stack;
  }

  /**
   * Returns a new LinkedStack with same elements in same order as argument.
   * <p> Time complexity: O(n)
   *
   * @param that LinkedStack to be copied.
   *
   * @return a new LinkedStack with same elements and order as {@code that}.
   */
  //MÉTODO FÁBRICA
  public static <T> LinkedStack<T> copyOf(LinkedStack<T> that) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Returns a new LinkedStack with same elements in same order as argument.
   * <p> Time complexity: O(n)
   *
   * @param that Stack to be copied.
   *
   * @return a new LinkedStack with same elements and order as {@code that}.
   */
  public static <T> LinkedStack<T> copyOf(Stack<T> that) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public boolean isEmpty() {
    return size == 0; // si es 0 devuelvo TRUE -> está vacía
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
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public void push(T element) {
    //Crear objeto de clase node
    //Crear next y engancharlo al último -> mismo funcionamiento que los nodos de C

    //No tenemos que asegurar capacidad
    top = new Node<>(element, top);  //creamos objeto e indicamos a donde va a apuntar
    //creado nodo, enganchado al antiguo top y hemos actualizado el valor de top
    size ++;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public T top() {
    if (isEmpty()){
      throw new EmptyStackException("top on empty stack");
    }
    //lo que hacemos es sacar el último (next) y actualizar el tamaño
    //top marca el único punto de entrada y salida de la pila
    return top.element;
    //devolvemos el primer elemento de la lista -> definición de pila (sale el último en entrar)
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public void pop() {
   if (isEmpty()){
     throw new EmptyStackException("pop on empty stack");
   }
   top = top.next; //hacemos que el top apunte al siguiente para liberar el que antes era top
   size --; //reducimos el tamaño
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public void clear() {
    //simplemente asignamos a null, nos desenganchamos porque el recolector de basura se encarga de liberarlo
    top = null;
    size = 0;
  }

  /**
   * Returns a protected iterable over elements in stack.
   */
  protected Iterable<T> elements() {
    return () -> new java.util.Iterator<>() {
      Node<T> current = top;

      public boolean hasNext() {
        return current != null;
      }

      public T next() {
        if (!hasNext()) {
          throw new java.util.NoSuchElementException();
        }
        T element = current.element;
        current = current.next;
        return element;
      }
    };
  }
}

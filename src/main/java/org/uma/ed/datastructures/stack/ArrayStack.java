package org.uma.ed.datastructures.stack;

import org.uma.ed.datastructures.list.ArrayList;
import org.uma.ed.datastructures.list.List;

import java.util.Arrays;

/**
 * This class represents a Stack data structure implemented using an array of elements.
 * The size of the array (capacity) is automatically increased when it runs out of capacity.
 *
 * @param <T> The type of elements in stack.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class ArrayStack<T> extends AbstractStack<T> implements Stack<T> {
  //Implementa interfaz pila
  //Extends AbstractStack<T> tiene implementación de equals, hashcode y toString
  /**
   * Default initial capacity for stack.
   */
  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  /**
   * Array of elements in stack.
   */
  private T[] elements;

  /**
   * Number of elements in stack.
   */
  private int size;

  /*
   * INVARIANT:
   *  - elements in the stack are stored in the array in bottom to top order.
   *  - if stack is non-empty, the element at the top of the stack is stored at index size - 1 in the array.
   *  - size is the number of elements in stack.
   */

  /**
   * Creates an empty ArrayStack. Initial capacity is {@code initialCapacity} elements. Capacity is automatically
   * increased when needed.
   * <p> Time complexity: O(1)
   *
   * @param initialCapacity Initial capacity.
   *
   * @throws IllegalArgumentException if initial capacity is less than 1.
   */
  @SuppressWarnings("unchecked")
  public ArrayStack(int initialCapacity) {
    if (initialCapacity <= 0) {
      throw new IllegalArgumentException("initial capacity must be greater than 0");
    }
    elements = (T[]) new Object[initialCapacity];
    size = 0;
  }

  /**
   * Creates an empty ArrayStack with default initial capacity. Capacity is automatically increased when needed.
   * <p> Time complexity: O(1)
   */
  public ArrayStack() {

    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates an empty ArrayStack with default initial capacity. Capacity is automatically increased when needed.
   * <p> Time complexity: O(1)
   *
   * @param <T> Type of elements in stack.
   *
   * @return an empty ArrayStack.
   */
  //MÉTODO DE FÁBRICA
  public static <T> ArrayStack<T> empty() {
    return new ArrayStack<>(); //crear un nuevo stack vacío
  }

  /**
   * Creates an empty ArrayStack. Initial capacity is {@code initialCapacity} elements. Capacity is automatically
   * increased when needed.
   * <p> Time complexity: O(1)
   *
   * @param initialCapacity Initial capacity.
   *
   * @throws IllegalArgumentException if initial capacity is less than 1.
   */
  //MÉTODO DE FÁBRICA
  public static <T> ArrayStack<T> withCapacity(int initialCapacity) {
    return new ArrayStack<>(initialCapacity); //crear un nuevo array con tamaño pasado por parámetro
  }

  /**
   * Creates a ArrayStack with given elements.
   * <p> Time complexity: O(n)
   *
   * @param elements elements to be added to stack.
   * @param <T> Type of elements in stack.
   *
   * @return a ArrayStack with given elements.
   */
  @SafeVarargs
  public static <T> ArrayStack<T> of(T... elements) {
    //creamos nueva pila que tenga de tamaño el número de elementos que se pasan por parámetro
    ArrayStack<T> stack = ArrayStack.withCapacity(elements.length);
    for (T element : elements){ //recorremos los elementos que se pasan por parámetro
      stack.push(element); //añadimos el elemento a la nueva pila
    }
    return stack;
  }

  /**
   * Creates an ArrayStack with elements in given iterable.
   * <p> Time complexity: O(n)
   *
   * @param iterable {@code Iterable} of elements to be added to stack.
   * @param <T> Type of elements in iterable.
   *
   * @return an ArrayStack with elements in given iterable.
   */
  //MÉTODO DE FÁBRICA
  public static <T> ArrayStack<T> from(Iterable<T> iterable) {
    ArrayStack<T> stack = new ArrayStack<>(); //creamos nuevo stack
    for (T element : iterable) {
      stack.push(element); //añadimos los elementos del iterable que se pasa como parámetro
    }
    return stack;
  }

  /**
   * Returns a new ArrayStack with same elements in same order as argument.
   * <p> Time complexity: O(n)
   *
   * @param that ArrayStack to be copied.
   *
   * @return a new ArrayStack with same elements and order as {@code that}.
   */
  //MÉTODO DE FÁBRICA
  public static <T> ArrayStack<T> copyOf(ArrayStack<T> that) {
    ArrayStack<T> copy = new ArrayStack<>(that.elements.length);
    int size = that.size;
    /* otra forma
    for (int i = 0; i< size; i++){
      copy.push(that.elements[i];
    }
     */
    if (size > 0){
      System.arraycopy(that.elements, 0, copy.elements, 0, size);
      copy.size = size;
    }
    return copy;
  }

  /**
   * Returns a new ArrayStack with same elements in same order as argument.
   *
   * @param that Stack to be copied.
   *
   * @return a new ArrayStack with same elements and order as {@code that}.
   */

  //PILAS = LIFO (Last In First Out)
  public static <T> ArrayStack<T> copyOf(Stack<T> that) {
    ArrayStack<T> copy = ArrayStack.withCapacity(that.size() == 0 ? DEFAULT_INITIAL_CAPACITY : that.size());
    Stack<T> tempStack = new LinkedStack<>(); // Temporary stack for reversing

    // Move elements from 'that' to 'tempStack' (to reverse without losing original order)
    while (!that.isEmpty()) {
      tempStack.push(that.top());
      that.pop();
    }

    // Move elements from 'tempStack' back to 'that' and into 'copy'
    while (!tempStack.isEmpty()) {
      T element = tempStack.top();
      tempStack.pop();
      that.push(element); // Restore original stack
      copy.push(element); // Copy into new stack
    }
    return copy;
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
   * Ensures that the capacity of the ArrayStack is sufficient to hold a new element.
   * If the current capacity is not enough, it is increased by doubling its size.
   */
  private void ensureCapacity() { //COMPRUEBA QUE HAY SITIO LIBRE Y SI NO SE CREA UN NUEVO ARRAY QUE TENGA EL DOBLE DE TAMAÑO
    if (size >= elements.length) {
      elements = Arrays.copyOf(elements, 2 * elements.length);
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: mostly O(1º). O(n) when stack capacity has to be increased.
   */
  @Override
  public void push(T element) { //DEVUELVE EL ULTIMO ELEMENTO DE LA PILA
    ensureCapacity();
    elements[size] = element;
    size++;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public T top() { //DEVOLVER EL ULTIMO ELEMENTO DE LA LISTA
    if(isEmpty()) {
      throw new EmptyStackException("top on empty stack");
    }
    return elements[size-1];
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public void pop() { //SACAR EL ULTIMO ELEMENTO DE LA PILA
    if(isEmpty()){
      throw new EmptyStackException("pop on empty stack");
    }
    size--; //decrementamos primero y podemos usar elements[size] para acceder al último
    elements[size] = null;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public void clear() { //VACIAR LA PILA
    for (int i = 0; i < size; i++){
      elements[i] = null; //liberamos el elemento posición i
    }
    size = 0;
  }

  /**
   * Returns a protected iterable over elements in stack.
   */
  protected Iterable<T> elements() {
    return () -> new java.util.Iterator<>() {
      int current = size - 1;

      public boolean hasNext() {
        return current >= 0;
      }

      public T next() {
        if (!hasNext()) {
          throw new java.util.NoSuchElementException();
        }
        T element = elements[current];
        current--;
        return element;
      }
    };
  }
}

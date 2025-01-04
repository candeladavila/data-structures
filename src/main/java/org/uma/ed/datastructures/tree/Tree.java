package org.uma.ed.datastructures.tree;

import org.uma.ed.datastructures.list.ArrayList;
import org.uma.ed.datastructures.list.List;
import org.uma.ed.datastructures.queue.ArrayQueue;
import org.uma.ed.datastructures.queue.Queue;

import java.util.Comparator;
import java.util.NoSuchElementException;


/**
 * This class defines different methods to process general trees. A tree is represented by a root node. If the tree is
 * empty, this root node is null. Otherwise, the root node contains an element and a list of children nodes.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class Tree {
  /**
   * This class represents a node in a general tree. Each node contains an element and a list of children nodes.
   *
   * @param <E>
   */
  public static final class Node<E> {
    private final E element;
    private final List<Node<E>> children;

    /**
     * Creates a node with an element and no children.
     *
     * @param element Element in node.
     */
    public Node(E element) {
      this.element = element;
      this.children = ArrayList.empty();
    }

    /**
     * Creates a node with an element and a list of children.
     *
     * @param element The element in the node.
     * @param children The list of children nodes.
     * @param <T> The type of the element in the node.
     *
     * @return A new node with given element and children.
     */
    @SafeVarargs
    public static <T> Node<T> of(T element, Node<T>... children) {
      Node<T> node = new Node<>(element);
      for (Node<T> child : children) {
        node.children.append(child);
      }
      return node;
    }
  }

  /**
   * Returns the number of nodes in a tree.
   *
   * @param root The root node of the tree.
   *
   * @return The number of nodes in the tree.
   */
  public static int size(Node<?> root) {
    int size = 0; //Tamaño de este subarbol
    if (root != null) {
      size = 1; //Es el tamño de su raiz
      for (Node<?> child : root.children) {
        size += size(child); //Y se añade el tamaño de cada hijo
      }
    }
    return size; //Se devuelve el tamaño que tiene subarbol
  }

  /**
   * Returns the height of a tree.
   *
   * @param root The root node of the tree.
   *
   * @return The height of the tree.
   */
public static int height(Node<?> root) {
  if (root == null) {
    return 0;
  } else {
    int maxHeight = 0;
    for (Node<?> child : root.children) {
      int childHeight = height(child);
      if (childHeight > maxHeight) {
        maxHeight = childHeight;
      }
    }
    return maxHeight + 1;
  }
}

  /**
   * Returns the sum of elements in a tree of integers.
   *
   * @param root The root node of the tree.
   *
   * @return The sum of elements in the tree.
   */
  public static int sum(Node<Integer> root) {
    int sum;
    if (root == null){
      sum = 0;
    } else{
      sum = root.element;
      for(Node <Integer> child: root.children){
        sum += sum(child);
      }
    }
    return sum;
  }

  /**
   * Returns the maximum element in a tree.
   *
   * @param root The root node of the tree.
   * @param comparator A comparator to compare elements in the tree.
   * @param <T> The type of elements in the tree.
   *
   * @return The maximum element in the tree according to the comparator.
   */
  public static <T> T maximum(Node<T> root, Comparator<T> comparator) {
    T maximum; //creamos el elemento máximo
    if(root == null){ //si no hay elementos en el árbol
      throw new NoSuchElementException("maximum of empty tree");
    }
    else{
      maximum = root.element; //inicializamos con la raíz
      //si solo tiene un elemento no hay children, y entonces no se entra en ese bucle
      for(Node<T> child : root.children){
        T childMaximum = maximum(child, comparator); //comparamos el máximo con el resto de elementos de los hijos por si encontramos un nuevo máximo
        if (comparator.compare(childMaximum, maximum) > 0){
          maximum = childMaximum;  //actualizamos máximo si hemos encontrado uno mayor
        }
      }
    }
    return maximum; //devolvemos
  }

  /**
   * Returns the number of occurrences of an element in a tree.
   *
   * @param root The root node of the tree.
   * @param element The element to count.
   * @param <T> The type of elements in the tree.
   *
   * @return The number of occurrences of the element in the tree.
   */
  public static <T> int count(Node<T> root, T element) {
    //tenemos que recorrer todos los elementos del árbol y llevamos con un contador con el número de apariciones del elemento element
    int contador = 0;
    //compramos si hay elementos -> sino, excepción
    //Si solo tiene un elemento y es distinto de la raíz entonces devolvemos 0
    if (root != null) {
      if (root.element == element) {
        contador++;
      } else {
        //si solo tiene un elemento no hay children, y entonces no se entra en ese bucle
        for (Node<T> child : root.children) {
          contador += count(child, element); //le sumamos a contador el contador de ese elemento en sus hijos
          if (child.element == element) { // son iguales
            contador++;  //actualizamos máximo si hemos encontrado uno mayor
          }
        }
      }
    }
    return contador; //devolvemos el resultado
  }

  /**
   * Returns the leaves of a tree.
   *
   * @param root The root node of the tree.
   * @param <T> The type of elements in the tree.
   *
   * @return A list with the leaves of the tree.
   */
  public static <T> List<T> leaves(Node<T> root) {
    List<T> leaves = ArrayList.empty();
    if (root != null) {
      leaves(root, leaves);
    }
    return leaves;
  }

  /**
   * Auxiliary method to compute the leaves of a tree.
   *
   * @param root The root node of the tree.
   * @param leaves A list to store the leaves of the tree.
   * @param <T> The type of elements in the tree.
   */
  private static <T> void leaves(Node<T> root, List<T> leaves_t) {
    if(root.children.isEmpty()){ //Caso base, es una hoja.
      leaves_t.append(root.element);
    }
    else{
      for (Node<T> h:root.children){ //No es una hoja, invocamos a sus hijos para buscarlas
        leaves(h, leaves_t);
      }
    }
  }

  /**
   * Returns the preorder traversal of a tree.
   *
   * @param root The root node of the tree.
   * @param <T> The type of elements in the tree.
   *
   * @return A list with the preorder traversal of the tree.
   */
  public static <T> List<T> preorder(Node<T> root) {
    List<T> preorder = ArrayList.empty();
    if (root != null) {
      preorder(root, preorder);
    }
    return preorder;
  }

  /**
   * Auxiliary method to compute the preorder traversal of a tree.
   *
   * @param root The root node of the tree.
   * @param preorder A list to store the preorder traversal of the tree.
   * @param <T> The type of elements in the tree.
   */
  private static <T> void preorder(Node<T> root, List<T> preorder) {
    for(Node<T> child: root.children){
      preorder(child, preorder);
    }
    preorder.append(root.element);
  }

  /**
   * Returns the postorder traversal of a tree.
   *
   * @param root The root node of the tree.
   * @param <T> The type of elements in the tree.
   *
   * @return A list with the postorder traversal of the tree.
   */
  public static <T> List<T> postorder(Node<T> root) {
    List<T> postorder = ArrayList.empty();
    if (root != null) {
      postorder(root, postorder);
    }
    return postorder;
  }

  /**
   * Auxiliary method to compute the postorder traversal of a tree.
   *
   * @param root The root node of the tree.
   * @param postorder A list to store the postorder traversal of the tree.
   * @param <T> The type of elements in the tree.
   */
  private static <T> void postorder(Node<T> root, List<T> postorder) {
    for(Node<T> child: root.children){
      postorder(child, postorder);
    }
    postorder.append(root.element);
  }

  /**
   * Returns the breadth-first traversal of a tree.
   *
   * @param root The root node of the tree.
   * @param <T> The type of elements in the tree.
   *
   * @return A list with the breadth-first traversal of the tree.
   */

  //BUSQUEDA POR ANCHURA (NIVELES)
  public static <T> List<T> breadthFirst(Node<T> root) {
    List<T> breadthFirst = ArrayList.empty();
    if (root != null){
      Queue <Node<T>> queue = ArrayQueue.empty();
      queue.enqueue(root); //cargamos la raiz a la cola
      while(!queue.isEmpty()){ //mientras queden elementos en la cola
        Node<T> node = queue.first(); //cojo el primer elemento de la cola en un nodo
        queue.dequeue(); //saco ese elemento de la cola
        breadthFirst.append(node.element); //añado ese elemento
        for(Node<T> child: node.children){ //el nodo que acabo de sacar de la cola tiene hijos?  -> entonces entro al bucle
          queue.enqueue(child); //añadimos a la cola a los hijos
        }
      }
    }
    return breadthFirst;
  }
}


package org.uma.ed.datastructures.graph ;

import org.uma.ed.datastructures.dictionary.Dictionary;
import org.uma.ed.datastructures.dictionary.JDKHashDictionary;
import org.uma.ed.datastructures.list.JDKArrayList;
import org.uma.ed.datastructures.list.List;
import org.uma.ed.datastructures.priorityqueue.JDKPriorityQueue;
import org.uma.ed.datastructures.priorityqueue.PriorityQueue;
import org.uma.ed.datastructures.set.JDKHashSet;
import org.uma.ed.datastructures.set.Set;
import org.uma.ed.datastructures.tuple.Tuple2;

/**
 * Class for computing the shortest paths in a weighted graph using Dijkstra's algorithm.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class Dijkstra {
   /**
   * Computes costs of shortest paths from a source vertex to all other vertices in a weighted graph.
   *
   * @param weightedGraph The weighted graph.
   * @param source The source vertex.
   * @param <V> The type of the vertices in the graph.
   *
   * @return a dictionary where keys are vertices and values are the minimum cost to reach them from the source.
   */
   //DIJKSTRA
  public static <V> Dictionary<V, Integer> dijkstra(WeightedGraph<V, Integer> weightedGraph, V source) {
 
    // Class for representing an extension of a path from vertex source to
    // vertex destination and total cost of reaching destination.
    // This class implements Comparable interface to allow sorting of extensions based on total cost.
    record Extension<V>(V source, V destination, Integer totalCost) implements Comparable<Extension<V>> {
      @Override
      // Best extension is the one with the smallest total cost.
      // Will be used later by the priority queue.
      public int compareTo(Extension that) {
        return this.totalCost.compareTo(that.totalCost);
      }

      static <V> Extension<V> of(V source, V destination, Integer totalCost) {
        return new Extension<>(source, destination, totalCost);
      }
    }

    //1. Declare the variables we are going to use
    //Sets that contain the vertices
    Set <V> verticesOpt =  new JDKHashSet<>();//vertices that have already been visited
    Set <V> vertices = JDKHashSet.copyOf(weightedGraph.vertices());  //vertices that have not been visited yet (it starts empty)

    //Dictionary that saves the optimized route
    Dictionary <V,Integer> costOpt = new JDKHashDictionary<>(); //we create it empty

    //Priority queue that saves the posible extensions from where we are
    /*
    NOTE: JDKPriorityQueue implements a priorityqueue based on the value we add to the couple of values. The ones who have more priority are the ones
    who have less cost -> based on our problem, the first element on the priorityqueue will be the one who has less cost -> better
     */
    PriorityQueue<Extension<V>> extension = new JDKPriorityQueue<>(Extension::compareTo);


    //Rest of variables we are going to be using
    V vertex = source; //vertex we are going to use as our initial one
    int acumCost = 0;

    //2. Initial conditions
    //VerticesOpt should only contain the vertex we use as our root
    verticesOpt.insert(source);
    //Vertices should include all the vertices except from the root
    vertices.delete(source);
    //costOpt should include the first vertex
    costOpt.insert(source, acumCost); //initial cost is 0

    //3. WHILE LOOP : while there are still vertices in Set vertices
    while (!vertices.isEmpty()){
      //Find all possible extension from vertices that are included in verticesOpt with vertices from vertices
      for (WeightedGraph.Successor<V, Integer> successor: weightedGraph.successors(vertex)){ //we iterate from all the successor of a vertex
        if (!verticesOpt.contains(successor.vertex())){ //is the vertex has not been already visited
          //we add to the extension source -> destination + cost
          extension.enqueue(Extension.of(vertex, successor.vertex(), acumCost + successor.weight()));
        }
      }

      //4. Once we have analyzed and saved the data for each successor of the vertex we have to choose the one who has less cost -> first on the priority queue
      //We have to choose the parameter we have used as destination (second one)
      vertex = extension.first().destination;

      //5. Update parameters
      verticesOpt.insert(vertex); //Now we add the vertex to verticesOpt and delete it from vertices
      vertices.delete(vertex);
      costOpt.insert(vertex, extension.first().totalCost); //Remember to add as well to dictionary costOpt
      acumCost = extension.first().totalCost(); //Update acumCost
      extension.dequeue(); //Delete vertex used from extension

      //6. Create a new Extension of the vertex we have just added to verticesOpt
      PriorityQueue<Extension<V>> newExtension = new JDKPriorityQueue<>(Extension::compareTo);

      //7. Loop while to iterate from elements of the previous priorityqueue
      while (!extension.isEmpty()){ //it doesn't contain now the element we have dequeued
        if (vertex != extension.first().destination){ //while the destination of the vertex is not a vertex that has already been visited
          newExtension.enqueue(extension.first()); //we add to the new priorityqueue the new fist element
        }
        extension.dequeue(); //we extract the next best vertex after making sure it has not been visited yet
      }
      while (!newExtension.isEmpty()){
        extension.enqueue(newExtension.first());
        newExtension.dequeue();
      }
    }
    return costOpt; //8. it returns a dictionary with the shortest paths from all vertex to a given one
  }

  /**
   * Computes shortest paths (and their costs) from a source vertex to all other vertices in a weighted graph.
   *
   * @param weightedGraph The weighted graph.
   * @param source The source vertex.
   * @param <V> The type of the vertices in the graph.
   *
   * @return a dictionary where keys are vertices and values are pairs with the minimum cost to reach them from the
   * source and the path to reach them.
   */

  //DIJKSTRA PATHS
  public static <V> Dictionary<V, Tuple2<Integer, List<V>>> dijkstraPaths(WeightedGraph<V, Integer> weightedGraph, V source) {
      
    // Class for representing an extension of a path from vertex source to
    // vertex destination and total cost of reaching destination plus path from source to destination.
    // This class implements Comparable interface to allow sorting of extensions based on total cost.
    record Extension<V>(V source, V destination, Integer totalCost, List<V> path) implements Comparable<Extension<V>> {
      @Override
      // Best extension is the one with the smallest total cost.
      // Will be used later by the priority queue.
      public int compareTo(Extension that) {
        return this.totalCost.compareTo(that.totalCost);
      }

      static <V> Extension<V> of(V source, V destination, Integer totalCost, List<V> path) {
        return new Extension<>(source, destination, totalCost, path);
      }
    }

    //1. Declare de variables we are going to use
    //Sets that contain the vertices
    Set <V> verticesOpt = new JDKHashSet<>(); //vertices that have already been visited
    Set <V> vertices = JDKHashSet.copyOf(weightedGraph.vertices());  //vertices that have not been visited yet (it starts empty)

    //Priority queue that saves the posible extensions from where we are
    PriorityQueue<Extension<V>> extension = new JDKPriorityQueue<>(Extension::compareTo);

    //Dictionary that saves the optimized route from a root to a destination as well as the real path to get from one to another
    Dictionary <V,Tuple2<Integer, List<V>>> costOpt = new JDKHashDictionary<>(); //we create it empty

    //Create a List
    List<V> path = new JDKArrayList<>(); //we create it empty

    //Rest of variables we are going to be using
    V vertex = source;
    int acumCost = 0;

    //2. Initial conditions
    verticesOpt.insert(vertex); //insert in already visited vertices
    vertices.delete(vertex); //delete from not visited yet
    path.append(vertex); //append it to the path
    costOpt.insert(vertex, new Tuple2<>(acumCost, path)); //append to the final solution

    //3. WHILE LOOP
    while (!vertices.isEmpty()){ //while there are vertices that have not been analyzed yet
      for(WeightedGraph.Successor<V,Integer> successor : weightedGraph.successors(vertex)){ //iterate from the posible successor from vertex
        if (!verticesOpt.contains(successor.vertex())) { //if the vertex has not been visited already
          List<V> aux_path = new JDKArrayList<>();
          for (V vertex_aux : costOpt.valueOf(vertex)._2()) { //iterate from the list of vertex of the vertex we use as "root" (the previous path we had)
            aux_path.append(vertex_aux); // we append them to our auxiliar list
          }
          aux_path.append(successor.vertex()); //we append the successor of the vertex (next vertex of the path we are building)
          //we create a priorityqueue with all the possible continuations of the path
          extension.enqueue(Extension.of(vertex, successor.vertex(), acumCost+ successor.weight(), aux_path)); //we have in here already the best continuation
        }
      }
      //once we are here we have in the extension all the possible continuations from all the successors of the original vertex (source) to them
      vertex = extension.first().destination; //now we have in vertex the vest continuation for our path

      //we update the rest of the variables we are using
      verticesOpt.insert(vertex); //include into already visited
      vertices.delete(vertex); //delete from not visited yet
      costOpt.insert(vertex, new Tuple2<>(extension.first().totalCost, extension.first().path)); //add to the solution (dictionary)
      acumCost = extension.first().totalCost;  //update acumCost with the weight of that node
      extension.dequeue(); //we delete it from the extension we had used

      //Create a new extension for the new optimal vertex
      PriorityQueue<Extension<V>> newExtension = new JDKPriorityQueue<>(Extension::compareTo);
      while(!extension.isEmpty()){ //while there are elements we have not added to the new priorityqueue
        if (vertex != extension.first().destination){ //while the destination is different from the node we are
          newExtension.enqueue(extension.first()); //add to the new priorityqueue
        }
        extension.dequeue(); //remove from previous extension
      }
      //Once we are out of this while loop we will have a new priorityqueue with updated values of cost and new optimal value in first position
      //we copy the newExtension to the previous one
      while(!newExtension.isEmpty()){
        extension.enqueue(newExtension.first());
        newExtension.dequeue();
      }
    }

    /*Once we get out of this loop is because we have analyzed all the successors from the original vertex source and so we have in our dictionary
    the shortest path to all of them as well as the totalCost */
    return costOpt;
  }
}

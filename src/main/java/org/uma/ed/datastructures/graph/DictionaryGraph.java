package org.uma.ed.datastructures.graph;

import java.util.StringJoiner;
import org.uma.ed.datastructures.dictionary.Dictionary;
import org.uma.ed.datastructures.dictionary.JDKHashDictionary;
import org.uma.ed.datastructures.set.HashSet;
import org.uma.ed.datastructures.set.JDKHashSet;
import org.uma.ed.datastructures.set.Set;

/**
 * Class for undirected graphs implemented with a dictionary
 *
 * @param <V> Type for vertices in graph
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class DictionaryGraph<V> implements Graph<V> {
  private final Set<V> vertices;                // set with all vertices in graph
  private final Dictionary<V, Set<V>> diEdges;  // dictionary with sources as keys and Set of destinations as values

  /**
   * Creates an empty graph.
   */
  public DictionaryGraph() {
    vertices = JDKHashSet.empty();
    diEdges = JDKHashDictionary.empty();
  }

  /**
   * Creates an empty graph.
   *
   * @param <V> Type for vertices in graph.
   *
   * @return An empty DictionaryGraph.
   */
  public static <V> DictionaryGraph<V> empty() {
    return new DictionaryGraph<>();
  }

  /**
   * Creates a graph with given vertices and edges.
   *
   * @param vertices vertices to add to graph.
   * @param edges edges to add to graph.
   * @param <V> Type for vertices in graph.
   *
   * @return A DictionaryGraph with given vertices and edges.
   */
  public static <V> DictionaryGraph<V> of(Set<V> vertices, Set<Edge<V>> edges) {
    DictionaryGraph<V> graph = new DictionaryGraph<>();
    for (V vertex : vertices) {
      graph.addVertex(vertex);
    }
    for (Edge<V> edge : edges) {
      graph.addEdge(edge.vertex1(), edge.vertex2());
    }
    return graph;
  }

  /**
   * Creates a graph with same vertices and edges as given graph.
   *
   * @param graph Graph to copy.
   * @param <V> Type for vertices in graph.
   *
   * @return A DictionaryGraph with same vertices and edges as given graph.
   */
  public static <V> DictionaryGraph<V> copyOf(Graph<V> graph) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVertex(V vertex) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private void addDiEdge(V source, V destination) {
    if (!vertices.contains(source)) {
      throw new GraphException("vertex" + source + " is not in graph");
    }
    if(!vertices.contains(destination)){
      throw new GraphException("vertex" + destination + " is not in graph");
    }
    Set<V> destinations = diEdges.valueOf(source);
    if(destinations == null){
      destinations = HashSet.empty();
      diEdges.insert(source, destinations);
    }
    destinations.insert(destination);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addEdge(V vertex1, V vertex2) {
    addDiEdge(vertex1, vertex2);
    addDiEdge(vertex2, vertex1);
    //lo tenemos que hacer en los dos sentidos porque no está dirigido y entonces hay que poner las conexiones en ambos sentidos
  }

  private void deleteDiEdge(V source, V destination) {
    Set<V> destinations = diEdges.valueOf(source);
    if(destinations != null) {
      destinations.delete(destination);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteEdge(V vertex1, V vertex2) {
    //eliminar una arista que une dos vértices dados en los dos sentidos
      deleteDiEdge(vertex1, vertex2);
      deleteDiEdge(vertex2, vertex1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteVertex(V vertex) {
    vertices.delete(vertex); //remove from set of vertex
    diEdges.delete(vertex);  //remove all edges tarting from vertex
    //remove all edges leading to vertex
    for(V source: vertices){
      deleteEdge(source, vertex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<V> vertices() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Edge<V>> edges() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int numberOfVertices() {
    return vertices.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int numberOfEdges() {
    if(vertices.isEmpty()){
      return 0;
    } else{
        int edges = 0;
        for(V vertex : vertices){
            edges += diEdges.valueOf(vertex).size();
        }
        return edges / 2; //como no es dirigido cada arista se representa dos veces así que el número final lo dividimos entre dos
    }
  }

  /**
   * Returns the successors of a vertex in graph (i.e. vertices to which there is an edge from given vertex).
   *
   * @param vertex vertex for which we want to obtain its successors.
   *
   * @return Successors of a vertex.
   */
  @Override
  public Set<V> successors(V vertex) {
    //un vértice se considera sucesor si hay una arista que sale de él
    return diEdges.valueOf(vertex);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int degree(V vertex) {
    //el grado de un vértice es el número de aristas que salen de el
    return diEdges.valueOf(vertex).size();
  }

  @Override
  public String toString() {
    String className = getClass().getSimpleName();

    StringJoiner verticesSJ = new StringJoiner(", ", "vertices(", ")");
    for (V vertex : vertices()) {
      verticesSJ.add(vertex.toString());
    }

    StringJoiner edgesSJ = new StringJoiner(", ", "edges(", ")");
    for (Edge<V> edge : edges()) {
      edgesSJ.add(edge.toString());
    }

    StringJoiner sj = new StringJoiner(", ", className + "(", ")");
    sj.add(verticesSJ.toString());
    sj.add(edgesSJ.toString());
    return sj.toString();
  }
}

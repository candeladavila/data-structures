package org.uma.ed.datastructures.graph;

import org.uma.ed.datastructures.list.ArrayList;
import org.uma.ed.datastructures.list.List;
import org.uma.ed.datastructures.set.HashSet;
import org.uma.ed.datastructures.set.Set;
import org.uma.ed.datastructures.stack.ArrayStack;
import org.uma.ed.datastructures.stack.Stack;

public class DFT<V> {
    private final List<V> traversal; //traversal

    private DFT(Traversable<V> graph, V source) {
        //SI NO LO TUVIÉRAMOS IMPLEMENTADO PODEMOS USAR LOS JDK
        //already visited nodes
        //Set<V> visited = JDKHashSet.empty();
        //traversal ) JDKArrayList.empty();

        //SI LO TUVIÉRAMOS IMPLEMENTADO DE LAS CLASES ANTERIORES
        Set<V> visited = HashSet.empty();
        traversal = ArrayList.empty();

        //stack of pending nodes
        //Stack<V> stack = JDKStack.empty();
        //stack.insert(source);
        Stack<V> stack = ArrayStack.of(source);

        while(!stack.isEmpty()){
            V vertex = stack.top();
            stack.pop();

            if(!visited.contains(vertex)){
                visited.insert(vertex);
                traversal.append(vertex);

                for(V successor : graph.successors(vertex)){
                    if(!visited.contains(successor)){
                        stack.push(successor);
                    }
                }
            }
        }

    }

}

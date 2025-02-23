package org.uma.ed.demos.queue;

import org.uma.ed.datastructures.queue.ArrayQueue;
import org.uma.ed.datastructures.queue.LinkedQueue;
import org.uma.ed.datastructures.queue.Queue;
import org.uma.ed.datastructures.range.Range;

/**
 * Simple Queue demo.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class QueueDemo {
  public static void main(String[] args) {
    Queue<Integer> q1 = LinkedQueue.empty();
    q1.enqueue(0);
    q1.enqueue(1);
    q1.enqueue(2);
    q1.enqueue(3);
    int x = q1.first();
    System.out.println(x);
    q1.dequeue();
    System.out.println(q1);
    System.out.println();

    Queue<Integer> q2 = ArrayQueue.empty();
    q2.enqueue(1);
    q2.enqueue(2);
    q2.enqueue(3);
    System.out.println(q2);
    System.out.println();

    Queue<Integer> q3 = ArrayQueue.of(1, 2, 3);
    System.out.println(q3);
    System.out.println();

    System.out.println(q1.equals(q2));
    System.out.println(q1.equals(q3));
    System.out.println(q2.equals(q3));
    System.out.println();

    Queue<Integer> q4 = ArrayQueue.from(Range.inclusive(0, 10, 2));
    System.out.println(q4);
  }
}

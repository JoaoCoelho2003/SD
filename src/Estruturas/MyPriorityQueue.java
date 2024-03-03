package Estruturas;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Comparator;

public class MyPriorityQueue<Type> implements Iterable<Type> {

    private LinkedList<Type> queue;
    private Comparator<Type> comparator;

    public MyPriorityQueue(Comparator<Type> comparator) {
        this.queue = new LinkedList<>();
        this.comparator = comparator;
    }

    public void add(Type element) {
        queue.add(element);
        queue.sort(comparator);
    }

    public Type remove() {
        return queue.removeFirst();
    }

    public boolean offer(Type element) {
        queue.add(element);
        queue.sort(comparator);
        return true;
    }

    public Type poll() {
        return queue.removeFirst();
    }

    public Type peek() {
        return queue.getFirst();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // Implementing the Iterable interface
    @Override
    public Iterator<Type> iterator() {
        return queue.iterator();
    }
}
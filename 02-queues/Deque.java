/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Coursera ID:       N/A
 *  Last modified:     26 Jul 2025
 *  Docstring inspiration taken from Robert Sedgewick and Kevin Wayne
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implement the Deque class that can work for any <Item>
 *
 * @param <Item> the type of objects that the Deque will contain
 */
public class Deque<Item> implements Iterable<Item> {

    // instance variables
    private Node start; // start node
    private Node end;   // end node
    private int n;      // number of elements in deque

    /**
     * Define helper class Node for the linked-list Deque
     */
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    /**
     * Initializes an empty deque
     */
    public Deque() {
        // A deque has a front and an end
        // an empty linked list implementation should have a new item
        // the front and the end should be the same
        // the pointers to "next" and "previous" should be null
        start = null;
        end = null;
        n = 0;
    }

    /**
     * Is this deque empty?
     *
     * @return true if the Deque is empty; false otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of items in the deque
     *
     * @return the number of items in the deque
     */
    // return the number of items on the deque
    public int size() {
        return n;
    }

    /**
     * Adds an item to the front of the deque
     *
     * @param item the item to add to the front of the deque
     * @throws java.lang.IllegalArgumentException if adding a null item
     */
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item");
        if (n == 0) {
            start = new Node();
            start.item = item;
            start.next = null;
            start.prev = null;
            end = start;
            n = 1;
        }
        else {
            Node oldStart = start;
            start = new Node();
            start.item = item;
            start.next = oldStart;
            start.prev = null;
            oldStart.prev = start;
            n++;
        }


    }

    /**
     * Adds an item to the back of the deque
     *
     * @param item the item to add to the back of the deque
     * @throws java.lang.IllegalArgumentException if adding a null item
     */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item");
        if (n == 0) {
            end = new Node();
            end.item = item;
            end.next = null;
            end.prev = null;
            start = end;
            n = 1;
        }
        else {
            Node oldEnd = end;
            end = new Node();
            end.item = item;
            end.next = null;
            end.prev = oldEnd;
            oldEnd.next = end;
            n++;
        }
    }

    /**
     * Removes and returns an item from the front of the deque
     *
     * @return the item from the front of the deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    public Item removeFirst() {
        if (n == 0) {
            throw new NoSuchElementException("Cannot remove from empty deque");
        }
        Node oldStart = start;
        start = oldStart.next;
        if (start != null) {
            start.prev = null;
        }
        n--;
        if (n == 0) {
            end = null;
        }
        return oldStart.item;
    }

    /**
     * Removes and returns an item from the back of the deque
     *
     * @return the item from the back of the deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    public Item removeLast() {
        if (n == 0) {
            throw new NoSuchElementException("Cannot remove from empty deque");
        }
        Node oldEnd = end;
        end = oldEnd.prev;
        if (end != null) {
            end.next = null;
        }
        n--;
        if (n == 0) {
            start = null;
        }
        return oldEnd.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new F2bDequeIterator();

    }

    // implements an iterator for a deque from front to back
    // see https://algs4.cs.princeton.edu/13stacks/LinkedQueue.java.html
    private class F2bDequeIterator implements Iterator<Item> {
        private Node current = start;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No next deque element");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() iterator not implemented");
        }
    }

    /**
     * Unit tests the {@code Deque} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Deque<String> dequeStart = new Deque<String>();
        Deque<String> dequeStart2 = new Deque<String>();
        Deque<String> dequeEnd = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            dequeStart.addFirst(item);
            dequeStart2.addFirst(item);
            dequeEnd.addLast(item);
        }

        StdOut.println("====================");
        StdOut.println("ForwardDeque (" + dequeStart.n + "):");
        StdOut.println("Remove from start");
        while (!dequeStart.isEmpty()) {
            StdOut.print(dequeStart.removeFirst() + " ");
        }
        StdOut.println("");
        StdOut.println("====================");

        StdOut.println("====================");
        StdOut.println("ForwardDeque2 (" + dequeStart2.n + "):");
        StdOut.println("Remove from end");
        while (!dequeStart2.isEmpty()) {
            StdOut.print(dequeStart2.removeLast() + " ");
        }
        StdOut.println("");
        StdOut.println("====================");

        StdOut.println("====================");
        StdOut.println("Backward Deque (" + dequeEnd.n + "):");
        StdOut.println("remove via iteration (Front to back)");
        for (String nodeItem : dequeEnd) {
            StdOut.print(nodeItem + " ");
        }
        StdOut.println("");
        StdOut.println("====================");

    }
}

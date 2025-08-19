/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Coursera ID:       N/A
 *  Last modified:     18 Aug 2025
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // https://algs4.cs.princeton.edu/13stacks/ResizingArrayBag.java.html
    private static final int INIT_CAPACITY = 8;
    private Item[] rQueue;
    private int n;
    private int firstEmpty;
    private int rIndex;

    // construct an empty randomized rQueue
    public RandomizedQueue() {
        rQueue = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
        firstEmpty = 0;

    }

    // is the randomized rQueue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized rQueue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot enqueue null item");

        // check if we have any space in the array, if not, we need to resize
        if (n >= rQueue.length) {
            resize(rQueue.length * 2); // directly modifies rQueue array
        }
        rQueue[firstEmpty] = item;
        n++;
        firstEmpty++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) throw new NoSuchElementException("Cannot dequeue empty RandomizedQueue");
        rIndex = StdRandom.uniformInt(n);
        int last = firstEmpty - 1;
        swap(rIndex);
        Item returnItem = rQueue[last];
        rQueue[last] = null;
        n--;
        firstEmpty--;

        // if reaches 1/4, resize to 1/2 size, down to a min of 8
        // e.g. if a 16 array reaches 4, resize to 8
        // int oldSize = rQueue.length;
        if (n > 0
                && n <= rQueue.length / 4
                && n >= INIT_CAPACITY / 2) {
            resize(rQueue.length / 2); // shrink rQueue and avoid thrashing
            // StdOut.println("Resize (" + n + "):\t" + oldSize + "\t->\t" + rQueue.length);
        }
        return returnItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (n == 0) throw new NoSuchElementException("Cannot sample empty RandomizedQueue");
        rIndex = StdRandom.uniformInt(n);
        Item sampleItem = rQueue[rIndex];
        return sampleItem;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int count;
        private int[] indexArray = new int[n];

        // private Item[] iteratorQueue = rQueue;
        RandomIterator() {
            for (int i = 0; i < n; i++) {
                indexArray[i] = i;
            }
            StdRandom.shuffle(indexArray);
        }

        public boolean hasNext() {
            return count < n;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No next RandomizedQueue element");
            Item item = rQueue[indexArray[count]];
            count++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() iterator not supported");
        }
    }

    private Item getItem(int index) {
        return rQueue[index];
    }

    private int getLength() {
        return rQueue.length;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rQueue1 = new RandomizedQueue<String>();

        for (int i = 1; i < 33; i++) {
            rQueue1.enqueue(String.valueOf(i));
        }

        StdOut.println("====================");
        StdOut.println("rQueue1 length:\t(" + rQueue1.getLength() + ")");
        StdOut.println("rQueue1 count:\t(" + rQueue1.size() + ")");

        StdOut.println("====================");
        StdOut.println("Printing items:");
        StdOut.print("\t");
        for (int i = 0; i < rQueue1.size(); i++) {
            StdOut.print(rQueue1.getItem(i) + " ");
        }
        StdOut.println();
        StdOut.println("====================");

        StdOut.println("====================");
        StdOut.println("Sampling items:");
        for (int i = 0; i < 30; i++) {
            StdOut.println("\t" + rQueue1.sample());
        }
        StdOut.println("====================");

        StdOut.println("====================");
        StdOut.println("Dequeue items:");
        for (int i = rQueue1.size() - 1; i >= 0; i--) {
            StdOut.println("\t" + rQueue1.dequeue() + "\t(" + rQueue1.getLength() + ")");
        }
        StdOut.println("====================");

        StdOut.println("====================");
        StdOut.println("Iterator 1:");
        RandomizedQueue<String> rQueue2 = new RandomizedQueue<String>();
        for (int i = 0; i < 8; i++) {
            rQueue2.enqueue(String.valueOf(i));
        }
        for (String queueItem : rQueue2) {
            StdOut.print("\t" + queueItem + " ");
        }
        StdOut.println();
        StdOut.println("Iterator 2:");
        for (String queueItem : rQueue2) {
            StdOut.print("\t" + queueItem + " ");
        }
        StdOut.println();
        StdOut.println("====================");
    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];

        for (int i = 0; i < n; i++) {
            newArray[i] = rQueue[i];
        }
        rQueue = newArray;


    }

    // bubble swap the selected index and the lastFilled index to maintain
    // data continuity
    private void swap(int index) {
        int lastFilled = firstEmpty - 1;
        Item toSwap = rQueue[index];
        rQueue[index] = rQueue[lastFilled];
        rQueue[lastFilled] = toSwap;
    }
}

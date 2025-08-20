/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Coursera ID:       N/A
 *  Last modified:     18 Aug 2025
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    /**
     * @param args the command-line arguments
     *             * k (String): the number of items (strings) to print to console
     *             * args[1:n] (String): the items from which to create a permutation
     * @implNote uses Knuth's algorithm to select which elements to output without
     * using too much memory for large n
     * @see RandomizedQueue
     */
    public static void main(String[] args) {
        // k is the number of items to print at most
        int k = Integer.parseInt(args[0]);
        if (k == 0) return; // special handling for early exit

        RandomizedQueue<String> rQueue = new RandomizedQueue<String>();

        int counter = 0;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            counter++;
            // Knuth's Algorithm for determining if we should add the next
            // item to the array
            if (counter > k) {
                if (StdRandom.bernoulli((double) k / counter)) {
                    // because dequeue shuffles array so that it removes the
                    // last item, we don't need to replace a specific
                    // index and can enqueue the relevant item after making
                    // space
                    rQueue.dequeue();
                    rQueue.enqueue(item);
                }

            }
            else {
                rQueue.enqueue(item);
            }
        }

        int count = 0;
        for (String permutationItem : rQueue) {
            StdOut.println(permutationItem);
            count++;
            if (count >= k) {
                return;
            }
        }

    }
}

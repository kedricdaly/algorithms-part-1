/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Coursera ID:       N/A
 *  Last modified:     18 Aug 2025
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        // k is the number of items to print at most
        int k = Integer.parseInt(args[0]);
        if (k == 0) return; // special handling for early exit

        RandomizedQueue<String> rQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rQueue.enqueue(item);
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

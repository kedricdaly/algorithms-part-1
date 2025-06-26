/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Last modified:     22 Jun 2025
 *  https://coursera.cs.princeton.edu/algs4/assignments/hello/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        /* Use Knuth's Algorithm
            Each word will have a 1/i chance to be the "champion".
            Note: the StdIn functions are *blocking*, so if calling from the
            command line, you will need to send <ctrl+d> or <ctrl+z> on a
            separate line to correctly tell StdIn that it has reached the
            end of the relevant input.
            https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdIn.html#readString--
         */

        String champion = "";
        int counter = 0;
        while (!StdIn.isEmpty()) {
            counter += 1;
            String thisWord = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / counter)) {
                champion = thisWord;
            }
        }
        StdOut.println(champion);
    }
}

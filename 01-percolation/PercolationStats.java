/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Coursera User ID:  N/A
 *  Last modified:     29 Jun 2025
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] proportions;
    private double tStatistic = 1.96;
    private int trialDim;
    private int trialNTrials;
    // private Stopwatch stopwatch;
    // private double elapsedTime;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n or trials <= 0");
        }

        // stopwatch = new Stopwatch();

        trialDim = n;
        trialNTrials = trials;

        proportions = new double[trials];
        for (int i = 0; i < trials; i++) {

            // StdOut.println("==============================");
            // StdOut.println("Starting trial with size " + n);
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int siteToOpen = StdRandom.uniformInt(1, n * n + 1); // right side is open bound

                int[] site2D = this.oneD2TwoD(siteToOpen);
                int row = site2D[0];
                int col = site2D[1];

                if (!perc.isOpen(row, col)) {
                    // StdOut.println("opening site " + siteToOpen);
                    perc.open(row, col);

                    // StdOut.println(
                    //         "openSites|blockedSites: " + perc.numberOfOpenSites() + "|"
                    //                + blockedSites);
                }

            }

            int nSitesToPercolate = perc.numberOfOpenSites();
            proportions[i] = (double) nSitesToPercolate / (n * n); // n * n is total sites

        }
        // elapsedTime = stopwatch.elapsedTime();

    }

    // sample mean of percolation threshold
    public double mean() {

        /*
        double[] proportionsArray = new double[proportions.length];
        for (int i = 0; i < proportions.length; i++) {
            proportionsArray[i] = proportions[i];
        }
        */
        return StdStats.mean(proportions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {

        // double[] stddevs = new double[proportions.length];

        /*
        double mean = this.mean();

        for (int i = 0; i < proportions.length; i++) {
            stddevs[i] = Math.sqrt(Math.pow((proportions[i] - mean), 2));
        }
        */


        return StdStats.stddev(this.proportions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - ((tStatistic * this.stddev()) / Math.sqrt(trialNTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + ((tStatistic * this.stddev()) / Math.sqrt(trialNTrials));
    }

    // test client
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        // double elapsedBase = PercolationStats.trialRun(n, trials);
        // double nTimes2 = PercolationStats.trialRun(n * 2, trials);
        // double trialsTimes2 = PercolationStats.trialRun(n, trials * 2);
        // double bothTimes2 = PercolationStats.trialRun(n * 2, trials * 2);

        /*
        StdOut.println(elapsedBase
                               + "|" + nTimes2 / elapsedBase
                               + "|" + trialsTimes2 / elapsedBase
                               + "|" + bothTimes2 / elapsedBase);
         */
        PercolationStats test = new PercolationStats(n, trials);
        StdOut.println("mean\t\t\t\t\t = " + test.mean());
        StdOut.println("stddev\t\t\t\t\t = " + test.stddev());
        StdOut.println(
                "95% confidence interval\t = [" + test.confidenceLo() + ", " + test.confidenceHi()
                        + "]");

    }

    private static double trialRun(int n, int trials) {
        PercolationStats test = new PercolationStats(n, trials);
        /* StdOut.println(
                "Time for " + test.trialNTrials + " trials of grid size " + test.trialDim + ":\t"
                        + test.elapsedTime);

         */
        StdOut.println("mean\t\t\t\t\t = " + test.mean());
        StdOut.println("stddev\t\t\t\t\t = " + test.stddev());
        StdOut.println(
                "95% confidence interval\t = [" + test.confidenceLo() + ", " + test.confidenceHi()
                        + "]");
        // return test.elapsedTime;
        return -1;
    }

    private int[] oneD2TwoD(int nodeId) {

        int[] output = { 0, 0 };
        int row = (nodeId - 1) / this.trialDim + 1;
        int col = nodeId % this.trialDim;

        if (row == 0) row = 1;
        if (col == 0) col = this.trialDim; // check for right side node

        output[0] = row;
        output[1] = col;
        return output;
    }
}

/* *****************************************************************************
 *  Name:              Kedric Daly
 *  Coursera ID:       N/A
 *  Last modified:     25 Jun 2025
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Provides an interface for creating and manipulating a percolation grid
 * made up of individual sites.
 */
public class Percolation {

    // instance members
    private boolean[][] grid;
    private int nAllElem;
    private int nGridElem;
    private int gridDim;
    private int numOpenSites = 0;
    private int topIdx;
    private int bottomIdx;
    private boolean gridTop = true;
    private boolean gridBottom = true;
    private WeightedQuickUnionUF ufGridTopOnly;
    private WeightedQuickUnionUF ufGridAll;


    /**
     * The constructor for a percolation grid object
     *
     * @param n the single dimension for an n x n grid of percolation sites.
     *          must be a positive integer
     */
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) {
            String exceptionText =
                    "Grid must be initialized with a positive integer";
            throw new IllegalArgumentException(exceptionText);
        }

        gridDim = n;
        grid = new boolean[n][n];
        nGridElem = (int) Math.pow(n, 2);
        nAllElem = nGridElem + 2; // top is elem 0, bot is elem n^2 + 1
        ufGridTopOnly = new WeightedQuickUnionUF(nGridElem + 1);
        ufGridAll = new WeightedQuickUnionUF(nAllElem);
        topIdx = 0;
        bottomIdx = nGridElem + 1;

        /*
        // connect top
        for (int i = 1; i <= gridDim; i++) {
            ufGridTopOnly.union(0, i);
            ufGridAll.union(0, i);
            // StdOut.println("Union site " + i + " to the top virtual site");
        }

        // connect bottom
        for (int i = nGridElem; i > nGridElem - gridDim; i--) {
            ufGridAll.union(bottomIdx, i);
            // StdOut.println("Union site " + i + " to the bottom virtual site");
        }
        */

    }

    /**
     * Checks to make sure inputs to Percolation methods are valid
     *
     * @param row the 1-indexed row for a Percolation grid
     * @param col the 1-indexed column for a Percolation grid
     * @throws IllegalArgumentException if row or col is out of bounds of
     *                                  the grid dimensions
     */
    private void checkInputs(int row, int col) {
        // requires 1-indexed row and col
        if (row < 1 || row > this.gridDim) {
            throw new IllegalArgumentException("row index " + row + " out of bounds");
        }
        if (col < 1 || col > this.gridDim) {
            throw new IllegalArgumentException("col index " + col + " out of bounds");
        }
    }

    /**
     * Opens a site in the percolation grid at [row, col] (1-indexed)
     * Opening a site involves:
     * <li>marking the site as open</li>
     * <li>unioning any surrounding sites, including virtual sites,
     * if applicable</li>
     *
     * @param row The 1-indexed row dimension of where to open a site
     * @param col The 1-indexed column dimension of where to open a site
     */
    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // row must be 1-indexed

        checkInputs(row, col);

        int zRow = row - 1;
        int zCol = col - 1;

        if (!this.grid[zRow][zCol]) {
            this.grid[zRow][zCol] = true;
            numOpenSites++;
            // StdOut.println("Num Open Sites: " + numOpenSites);

            unionSurroundingSites(row, col); // requires 1-indexed
            /* try {

            }
            catch (java.lang.IndexOutOfBoundsException e) {
                StdOut.println(e);
            }
            */

        }

        // int oneD = this.twoD2OneD(row, col);

        /* StdOut.println(
                "Root of opening site (" + row + "," + col + ")[" + oneD + "]: "
                        + this.ufGridAll.find(oneD));
        */

    }

    /**
     * Checks for open surrounding sites, and if they are open, perform a union.
     * This includes checks for a virtual site.
     * With the current implementation, there are 2 weightedQF data structures
     * One to keep track of percolation, and another to ensure no backwash occurs.
     *
     * @param row 1-indexed row coordinate to check for open surrounding sites
     * @param col 1-indexed column coordinate to check for open surrounding sites
     */
    private void unionSurroundingSites(int row, int col) {

        // StdOut.print("Opening Site: ");
        int openingSite = twoD2OneD(row, col);
        // StdOut.print("Left Site: ");
        int leftSite = twoD2OneD(row, col - 1);
        // StdOut.print("Right Site: ");
        int rightSite = twoD2OneD(row, col + 1);
        // StdOut.print("Up Site: ");
        int upSite = twoD2OneD(row - 1, col);
        // StdOut.print("Down Site: ");
        int downSite = twoD2OneD(row + 1, col);

        // left site
        if (col > 1 && isOpen(row, col - 1)) {
            // StdOut.println("Left Site " + leftSite);
            ufGridTopOnly.union(openingSite, leftSite);
            ufGridAll.union(openingSite, leftSite);
            // StdOut.println("Union left " + openingSite + " with " + leftSite);

        }

        // right site
        if (col < gridDim && isOpen(row, col + 1)) {
            // StdOut.println("Right Site " + rightSite);
            ufGridTopOnly.union(openingSite, rightSite);
            ufGridAll.union(openingSite, rightSite);
            // StdOut.println("Union right " + openingSite + " with " + rightSite);
        }

        // up site
        if (row > 1 && isOpen(row - 1, col)) {
            // StdOut.println("Up Site " + upSite);
            ufGridTopOnly.union(openingSite, upSite);
            ufGridAll.union(openingSite, upSite);
            // StdOut.println("Union up " + openingSite + " with " + upSite);
        }

        // down site
        if (row < gridDim && isOpen(row + 1, col)) {
            // StdOut.println("Down Site " + downSite);
            ufGridTopOnly.union(openingSite, downSite);
            ufGridAll.union(openingSite, downSite);
            // StdOut.println("Union down " + openingSite + " with " + downSite);
        }

        // top virtual site
        if (row == 1) {
            ufGridTopOnly.union(openingSite, this.topIdx);
            ufGridAll.union(openingSite, this.topIdx);
        }

        // bottom virtual site
        if (row == this.gridDim) {
            ufGridAll.union(openingSite, this.bottomIdx);
        }

    }

    /**
     * Checks if a site is open by checking relevant data structure value
     *
     * @param row 1-indexed row coordinate to check for an open site
     * @param col 1-indexed column coordinate to check for an open site
     * @return The Boolean of the Percolation grid indicating if a site is open
     * <li><code>True</code>: the site is open</li>
     * <li><code>False</code>: the site is not open</li>
     */
    public boolean isOpen(int row, int col) {

        checkInputs(row, col);

        int zRow = row - 1;
        int zCol = col - 1;

        return grid[zRow][zCol];
    }

    /**
     * Checks if a site is full. A site is full if it is connected to the top
     * of the grid.
     *
     * @param row 1-indexed row coordinate to check if a site is full
     * @param col 1-indexed column coordinate to check if a site is full
     * @return <li><code>True</code>: if the site is open</li>
     * <li><code>False</code>: if the site is not open</li>
     */
    public boolean isFull(int row, int col) {

        checkInputs(row, col);

        if (isOpen(row, col)
                && this.ufGridTopOnly.find(this.twoD2OneD(row, col)) == ufGridTopOnly.find(
                topIdx)) {
            return true;
        }

        return false;
    }

    /**
     * Gives the number of open sites in the Percolation grid
     *
     * @return The number of open sites in the grid
     */
    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpenSites;
    }

    /**
     * Determines if a given grid Percolates. A grid percolates if any site
     * on the top row of the grid is connected to any site in the bottom row
     * of the grid
     *
     * @return <code>True</code> if the grid percolates
     * <code>False</code> if the grid does not percolate
     */
    // does the system percolate?
    public boolean percolates() {

        // StdOut.println("bottomIdx: " + bottomIdx);
        int topRoot = this.ufGridAll.find(topIdx);
        int botRoot = this.ufGridAll.find(bottomIdx);
        // StdOut.println("Root of topIdx: " + topRoot);
        // StdOut.println("Root of bottomIdx: " + botRoot);

        // int root = nAllElem;
        if (botRoot == topRoot) {
            // should work both directions top-down and bottom-up
            // required because always connecting to larger tree
            return true;
        }
        return false;
    }

    /**
     * Main entry. Implemented as a testing client.
     * Performs various tests for troubleshooting purposes.
     *
     * @param args input arguments for testing
     */
    // test client (optional)
    public static void main(String[] args) {
        int n = 5;
        Percolation myGrid = new Percolation(n);

        StdOut.println("====================");
        myGrid.printGrid();
        StdOut.println("Site (1,2) is open: " + myGrid.isOpen(1, 2));
        // myGrid.grid[0][1] = true;
        myGrid.open(1, 2);
        myGrid.printGrid();
        StdOut.println("Site (1,2) is open: " + myGrid.isOpen(1, 2));
        myGrid.open(1, 1);
        myGrid.printGrid();
        StdOut.println("Site (1,1) is open: " + myGrid.isOpen(1, 1));
        StdOut.print("Are (1,1) and (1,2) connected? ");
        StdOut.println(myGrid.ufGridAll.find(myGrid.twoD2OneD(1, 1))
                               == myGrid.ufGridAll.find(myGrid.twoD2OneD(1, 2)));
        StdOut.println("====================");

        // test oneD2TwoD()
        StdOut.println("====================");
        boolean test1 = myGrid.testoneD2TwoD(3);
        StdOut.println("Test of grid size 3: " + test1);

        boolean test2 = myGrid.testoneD2TwoD(5);
        StdOut.println("Test of grid size 5: " + test2);
        StdOut.println("====================");

        // ArrayList<Integer> testList1 = new ArrayList<Integer>(Arrays.asList(2, 2));
        StdOut.println("====================");
        boolean test3 = myGrid.twoD2OneD(2, 3) == n + 3;
        StdOut.println("Test of twoD2OneD (2,3) should be n + 3: n == " + n + " : " + test3);
        StdOut.println("====================");

        StdOut.println("====================");
        /*
        using try/catch block results in a "spotbug" error
        try {
            myGrid.isOpen(0, 1);
        }
        catch (java.lang.IndexOutOfBoundsException e) {
            StdOut.println(e);
        }

        try {
            myGrid.isOpen(1, n + 1);
        }
        catch (java.lang.IndexOutOfBoundsException e) {
            StdOut.println(e);
        }
        */
        StdOut.println("====================");

        StdOut.println("====================");
        StdOut.println("Testing union of top sites:");
        for (int i = 1; i <= myGrid.gridDim; i++) {
            StdOut.println("Site " + i + ": " + myGrid.ufGridAll.find(i));
        }

        StdOut.println("Testing union of bottom sites:");
        for (int i = myGrid.nGridElem; i > myGrid.nGridElem - myGrid.gridDim; i--) {
            StdOut.println("Site " + i + ": " + myGrid.ufGridAll.find(i));
        }
        StdOut.println("====================");

    }

    /**
     * Gives a visual representation of grid openness
     */
    private void printGrid() {
        int n = this.grid.length;
        int counter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.grid[i][j]) {
                    StdOut.print("1 ");
                }
                else {
                    StdOut.print("0 ");
                }
                // StdOut.print("Site " + counter + ": " + this.grid[i][j]);
                counter++;
            }
            StdOut.print("\n");
        }
    }

    /**
     * Converts a one-dimensional grid position to two-dimensional grid coordinates
     *
     * @param nodeId The 1-dimensional nodeId
     * @return <code>int[]</code> with structure:
     * <li><code>int[0]</code> the 1-indexed row dimension of the nodeId</li>
     * <li><code>int[1]</code> the 1-indexed column dimension of the nodeId</li>
     */
    private int[] oneD2TwoD(int nodeId) {

        int row = (nodeId - 1) / this.gridDim + 1;
        int col = nodeId % this.gridDim;
        int[] output = { 0, 0 };

        if (row == 0) row = 1;
        if (col == 0) col = this.gridDim; // check for right side node

        // ArrayList<Integer> output = new ArrayList<Integer>();
        output[0] = row;
        output[1] = col;
        // output.add(row);
        // output.add(col);
        return output;
    }

    /**
     * Converts two-dimensional grid coordinates to a one-dimensional grid position
     *
     * @param row the 1-indexed row dimension of the site to convert
     * @param col the 1-indexed column dimension of the site to convert
     * @return <code>int</code> of the one-dimensional grid position
     */
    private int twoD2OneD(int row, int col) {
        // requires 1-indexed grid row and col
        // StdOut.println(
        //         "twoD2OneD (" + row + "," + col + ") -> " + (((row - 1) * this.gridDim) + col));
        return ((row - 1) * this.gridDim) + col;
    }

    /**
     * A short method for testing one-dimensional to two-dimensional conversion
     *
     * @param n The 1-dimensional grid node position
     * @return <li><code>True</code> if the test passes</li>
     * <li><code>False</code> if the test does not pass</li>
     */
    private boolean testoneD2TwoD(int n) {
        // no List<> or ArrayList<> allowed
        Percolation testGrid = new Percolation(n);

        int[] nPlus1 = { 0, 0 };
        nPlus1[0] = 2;
        nPlus1[1] = 1;
        int[] testResult = testGrid.oneD2TwoD(n + 1);

        return (testResult[0] == nPlus1[0])
                && (testResult[1] == nPlus1[1]); // should return (2,1) == (2,1) therefore true;


    }

}

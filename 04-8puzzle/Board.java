/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] tiles;
    private int n; // single dimension

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
    }

    // string representation of this board
    // from https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/faq.php
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int position = 0;
        int hammingCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != position + 1) {
                    hammingCount = hammingCount + 1;
                }
                position++;
            }
        }
        return hammingCount - 1; // need to subtract 1 for "zero" position
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dim = this.n;
        int manhattanDistance = 0;
        for (int i = 0; i < (Math.pow(dim, 2)); i++) {
            int currentRow = rowFrom1D(i, dim);
            int currentCol = colFrom1D(i, dim);
            int tileVal = tiles[currentRow][currentCol];
            if (tileVal != 0 && tileVal != (i + 1)) {
                // # of moves is the grid distance.
                // Subtract the desired from the current, abs value
                // need to offset tileVal to align with 1D-2D conversion functions
                int desiredRow = rowFrom1D(tileVal - 1, dim);
                int desiredCol = colFrom1D(tileVal - 1, dim);
                int rowDiff = Math.abs(desiredRow - currentRow);
                int colDiff = Math.abs(desiredCol - currentCol);
                manhattanDistance = manhattanDistance + rowDiff + colDiff;
            }
        }
        return manhattanDistance;
    }

    /*
    // is this board the goal board?
    public boolean isGoal()

    // does this board equal y?
    public boolean equals(Object y)

    // all neighboring boards
    public Iterable<Board> neighbors()

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

    }
    */

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] testTiles = new int[][] { { 1, 2 }, { 3, 0 } };
        Board testBoard = new Board(testTiles);
        StdOut.println(testBoard.toString());
        StdOut.println("Dimension: " + testBoard.dimension());

        StdOut.println("Test Distances");
        testDistances();

        // StdOut.println("Test 1D-2D conversions");
        // testConversions();

    }

    private static void testDistances() {
        int[][] testDistancesTiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board testDistances = new Board(testDistancesTiles);
        StdOut.println(testDistances.toString());
        StdOut.println("Hamming Dist (target = 5): " + testDistances.hamming());
        StdOut.println("Manhattan Dist (target = 10): " + testDistances.manhattan());
    }

    // 3, 3 should return 9
    private static int xyTo1D(int x, int y, int dim) {
        return (x * dim) + (y % dim);
    }

    // zero-ordered row
    private static int rowFrom1D(int position, int dim) {
        return position / dim;
    }

    // zero-ordered column
    private static int colFrom1D(int position, int dim) {
        return position % dim;
    }

    private static void testConversions() {
        int[][] testConversionsTiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board testConversions = new Board(testConversionsTiles);
        int dim = testConversions.dimension();
        // dim will be 3
        StdOut.println("[1,2] -> 5: " + (xyTo1D(1, 2, dim) == 5));
        StdOut.println("[2,1] -> 7: " + (xyTo1D(2, 1, dim) == 7));
        StdOut.println("6 -> row 2: " + (rowFrom1D(6, dim) == 2));
        StdOut.println("2 -> row 0: " + (rowFrom1D(2, dim) == 0));
        StdOut.println("0 -> row 0: " + (rowFrom1D(0, dim) == 0));
        StdOut.println("8 -> row 2: " + (rowFrom1D(8, dim) == 2));
        StdOut.println("3 -> row 1: " + (rowFrom1D(3, dim) == 1));
        StdOut.println("1 -> col 1: " + (colFrom1D(1, dim) == 1));
        StdOut.println("2 -> col 2: " + (colFrom1D(2, dim) == 2));
        StdOut.println("3 -> col 0: " + (colFrom1D(3, dim) == 0));
        StdOut.println("4 -> col 1: " + (colFrom1D(4, dim) == 1));
        StdOut.println("8 -> col 2: " + (colFrom1D(8, dim) == 2));
    }

}

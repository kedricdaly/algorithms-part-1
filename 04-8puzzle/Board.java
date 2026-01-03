/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private int[][] tiles;
    private int n; // single dimension
    private int[] emptyLoc = new int[2];

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        this.emptyLoc = findEmptyLocation(this.tiles);
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


    // is this board the goal board?
    public boolean isGoal() {
        int position = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (tiles[i][j] != position + 1 && tiles[i][j] != 0) {
                    return false;
                }
                position++;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board checkBoard = (Board) y;
        return Arrays.deepEquals(this.tiles, checkBoard.tiles);
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        // overall approach:
        // 1. find the empty board spot (can this be cached?)
        // 2. check the neighboring spots. If there is a tile there, exchange and return
        //    if the swap would go outside the board boundaries, ignore.
        int emptyRow = this.emptyLoc[0];
        int emptyCol = this.emptyLoc[1];

        int left = emptyCol - 1;
        int right = emptyCol + 1;
        int up = emptyRow - 1;
        int down = emptyRow + 1;

        Queue<Board> neighbors = new Queue<Board>();

        // likely a way to not copy this code for the 4 possibilities
        // could create 4 int[2] pairs for different possibilities in a new array
        // and then only keep valid ones and loop over the valid entries.
        if (left >= 0 && left < this.n) {
            int swapTileVal = this.tiles[emptyRow][left];
            // need to copy the nxn array into a new one so that manipulations
            // do not change the underlying object data
            // if try to use this.tiles, manipulations on the new Board tiles
            // changes the original data.
            int[][] tempTiles = new int[this.n][this.n];
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    tempTiles[i][j] = this.tiles[i][j];
                }
            }
            Board tempBoard = new Board(tempTiles);
            tempBoard.tiles[emptyRow][left] = 0;
            tempBoard.tiles[emptyRow][emptyCol] = swapTileVal;
            neighbors.enqueue(tempBoard);
        }

        if (right >= 0 && right < this.n) {
            int swapTileVal = this.tiles[emptyRow][right];
            int[][] tempTiles = new int[this.n][this.n];
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    tempTiles[i][j] = this.tiles[i][j];
                }
            }
            Board tempBoard = new Board(tempTiles);
            tempBoard.tiles[emptyRow][right] = 0;
            tempBoard.tiles[emptyRow][emptyCol] = swapTileVal;
            neighbors.enqueue(tempBoard);
        }

        if (up >= 0 && up < this.n) {
            int swapTileVal = this.tiles[up][emptyCol];
            int[][] tempTiles = new int[this.n][this.n];
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    tempTiles[i][j] = this.tiles[i][j];
                }
            }
            Board tempBoard = new Board(tempTiles);
            tempBoard.tiles[up][emptyCol] = 0;
            tempBoard.tiles[emptyRow][emptyCol] = swapTileVal;
            neighbors.enqueue(tempBoard);
        }

        if (down >= 0 && down < this.n) {
            int swapTileVal = this.tiles[down][emptyCol];
            int[][] tempTiles = new int[this.n][this.n];
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    tempTiles[i][j] = this.tiles[i][j];
                }
            }
            Board tempBoard = new Board(tempTiles);
            tempBoard.tiles[down][emptyCol] = 0;
            tempBoard.tiles[emptyRow][emptyCol] = swapTileVal;
            neighbors.enqueue(tempBoard);
        }
        return neighbors;
    }

    /*
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
        StdOut.println("Test isGoal(), size 2 (target true):" + testBoard.isGoal());

        // StdOut.println("Test Distances");
        // testDistances();

        // StdOut.println("Test isGoal()");
        // testIsGoal();

        // StdOut.println("Test equals()");
        // testEquals();

        StdOut.println("Test neighbors");
        testNeighbors();

        // StdOut.println("Test 1D-2D conversions");
        // testConversions();

    }

    private static void testDistances() {
        int[][] testDistancesTiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board testDistances = new Board(testDistancesTiles);
        StdOut.println(testDistances.toString());
        StdOut.println("\tHamming Dist (target = 5): " + testDistances.hamming());
        StdOut.println("\tManhattan Dist (target = 10): " + testDistances.manhattan());
    }

    public static void testIsGoal() {
        int[][] testGoalTiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board testGoalBad = new Board(testGoalTiles);
        StdOut.println("\tTest isGoal(), size 3 (target false): " + testGoalBad.isGoal());

        int[][] testGoalGoodTiles = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board testGoalGood = new Board(testGoalGoodTiles);
        StdOut.println("\tTest isGoal(), size 3 (target true): " + testGoalGood.isGoal());
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

    // find the empty spot on the board
    private int[] findEmptyLocation(int[][] boardTiles) {
        int[] emptyLocation = new int[2];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (boardTiles[i][j] == 0) {
                    emptyLocation[0] = i;
                    emptyLocation[1] = j;
                }
            }
        }
        return emptyLocation;
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

    private static void testEquals() {
        int[][] testEqualsTiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] testNonEqualsTiles = new int[][] { { 1, 8, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board testEqualsBoard = new Board(testEqualsTiles);
        Board testEqualsBoard2 = new Board(testEqualsTiles);
        Board testNonEqualBoard = new Board(testNonEqualsTiles);
        StdOut.println("\tCheck same board equality (target: true): " + testEqualsBoard.equals(
                testEqualsBoard));
        StdOut.println("\tCheck duplicate board equality (target: true): " + testEqualsBoard.equals(
                testEqualsBoard2));
        StdOut.println(
                "\tCheck non-duplicate board equality (target: false): " + testEqualsBoard.equals(
                        testNonEqualBoard));
        StdOut.println("\tCheck null (target: false): " + testEqualsBoard.equals(null));
        StdOut.println("\tCheck non-Board object (target: false): " + testEqualsBoard.equals(1));
    }

    private static void testNeighbors() {
        int[][] testOrigTiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] testLeftTiles = new int[][] { { 8, 1, 3 }, { 0, 4, 2 }, { 7, 6, 5 } };
        int[][] testRightTiles = new int[][] { { 8, 1, 3 }, { 4, 2, 0 }, { 7, 6, 5 } };
        int[][] testUpTiles = new int[][] { { 8, 0, 3 }, { 4, 1, 2 }, { 7, 6, 5 } };
        int[][] testDownTiles = new int[][] { { 8, 1, 3 }, { 4, 6, 2 }, { 7, 0, 5 } };
        Board orig = new Board(testOrigTiles);
        Board left = new Board(testLeftTiles);
        Board right = new Board(testRightTiles);
        Board up = new Board(testUpTiles);
        Board down = new Board(testDownTiles);
        Iterable<Board> neighbors = orig.neighbors();

        StdOut.println("Original Board:");
        StdOut.println(orig.toString());

        for (Board b : neighbors) {
            StdOut.println(b.toString());
            if (b.equals(left)) {
                StdOut.println("Matches left\n");
            }
            if (b.equals(right)) {
                StdOut.println("Matches right\n");
            }
            if (b.equals(up)) {
                StdOut.println("Matches up\n");
            }
            if (b.equals(down)) {
                StdOut.println("Matches down\n");
            }
        }

    }

}

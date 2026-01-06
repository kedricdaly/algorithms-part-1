/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private boolean initialSolvable = false;
    private SearchNode finalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException("Initial board cannot be null");
        }

        boolean isSolved = false;

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        SearchNode firstNode = new SearchNode(null, initial);
        SearchNode firstNodeTwin = new SearchNode(null, initial.twin());
        /*
        StdOut.println("Initial:");
        StdOut.println(firstNode.toString());
        StdOut.println("Twin:");
        StdOut.print(firstNodeTwin.toString());
        */
        minPQ.insert(firstNode);
        twinPQ.insert(firstNodeTwin);

        // do the A* algo, switching between initial and twin
        // only one will reach the goal board
        while (!isSolved) {
            // StdOut.println("=====Regular A* line=====");
            SearchNode minNode = nextAStar(minPQ);
            if (minNode.nodeBoard.isGoal()) {
                isSolved = true;
                initialSolvable = true;
                this.finalNode = minNode;
                Iterable<Board> finalSolution = this.solution();
                // StdOut.println("Initial board solvable.");
                /* don't print to StdOut
                for (Board b : finalSolution) {
                    StdOut.println(b.toString());
                }
                 */

            }
            if (isSolved) break;

            // StdOut.println("=====Twin A* line=====");
            SearchNode minNodeTwin = nextAStar(twinPQ);
            if (minNodeTwin.nodeBoard.isGoal()) {
                isSolved = true;
                // StdOut.println("Initial board unsolvable because twin is solvable.");
            }

        }

        // StdOut.println(minPQ.toString());
    }

    // do the next step of A* algorithm
    private SearchNode nextAStar(MinPQ<SearchNode> pq) {
        SearchNode poppedNode = pq.delMin();
        // StdOut.println("Popped min node\n" + poppedNode.toString());
        if (poppedNode.nodeBoard.isGoal()) {
            return poppedNode; // no need to get neighbors for goal
        }
        Iterable<Board> newNeighbors = poppedNode.nodeBoard.neighbors();
        for (Board neighbor : newNeighbors) {
            if ((poppedNode.prev != null) && neighbor.equals(poppedNode.prev.nodeBoard)) {
                continue; // skip previously used boards. Critical optimization
            }
            SearchNode neighborSearchNode = new SearchNode(poppedNode, neighbor);
            pq.insert(neighborSearchNode);
            // StdOut.println("Added neighbor\n" + neighborSearchNode.toString());
        }
        return poppedNode;
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return initialSolvable;
    }


    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // return final searchNode.nMoves;
        // StdOut.println("moves() not yet implemented.");
        if (!initialSolvable) return -1;
        return finalNode.nMoves;
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // use a stack from final to initial

        if (!initialSolvable) {
            return null; // requirement
        }

        Stack<Board> solutionStack = new Stack<Board>();
        SearchNode tempNode = finalNode;
        while (tempNode.prev != null) {
            solutionStack.push(tempNode.nodeBoard);
            tempNode = tempNode.prev;
        }

        // final push of initial board, since tempNode.prev will be null
        solutionStack.push(tempNode.nodeBoard);

        return solutionStack;
    }


    public static void main(String[] args) {
        // see puzzleChecker

    }

    private class SearchNode implements Comparable<SearchNode> {
        // define a node that has:
        // 1. the board
        // 2. the hamming & manhattan priorities
        // 3. count of moves so far
        // 4. pointer to previous node
        Board nodeBoard;
        // int hammingPriority;
        int manhattanPriority;
        int nMoves;
        SearchNode prev = null;

        private SearchNode(SearchNode prevNode, Board newBoard) {
            nodeBoard = newBoard;
            if (prevNode == null) {
                nMoves = 0;
            }
            else {
                nMoves = prevNode.nMoves + 1;
            }
            // hammingPriority = nodeBoard.hamming() + nMoves;
            manhattanPriority = nodeBoard.manhattan() + nMoves;
            prev = prevNode;

        }

        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("SearchNode" + "\n");
            // s.append("\tHamming Priority: " + hammingPriority + "\n");
            s.append("\tManhattan Priority: " + manhattanPriority + "\n");
            s.append("\tMoves: " + nMoves + "\n");
            s.append(this.nodeBoard.toString());

            return s.toString();
        }

        public int compareTo(SearchNode that) {
            if (this.manhattanPriority == that.manhattanPriority) {
                return 0;
                /*
                if (this.hammingPriority == that.hammingPriority) {
                    return 0; // tied
                }
                else {
                    return (this.hammingPriority - that.hammingPriority); // tie-breaker

                 */
            }

            return (this.manhattanPriority
                    - that.manhattanPriority); // negative int if other hamming priority is better
        }

        /*
        Ended up using manhattan distance as default ordering method
        public int manhattanVs(SearchNode that) {
            return (this.manhattanPriority - that.manhattanPriority);
        }

        public Comparator<SearchNode> manhattanOrder() {
            return (s1, s2) -> Integer.compare(manhattanVs(s1), manhattanVs(s2));
        }

         */

    }
}



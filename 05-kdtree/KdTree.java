/* *****************************************************************************
 *  Name: Kedric Daly
 *  Date: 12 Jan 2026
 *  Description: A mutable data type KdTree.java that represents a set of points in the unit square
 *     Uses a 2d tree to implement the same API as PointSET
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private static final int NUM_DIMS = 2; // this is a 2D tree
    private static final int VERTICAL = 0; // split on x-coordinate, draw vertical line
    private static final int HORIZONTAL = 1; // split on y-coordinate, draw horizontal line
    private static final double XMIN = 0;
    private static final double YMIN = 0;
    private static final double XMAX = 1;
    private static final double YMAX = 1;

    private Node root;
    private int nNodes;

    private static class Node {
        private Point2D nodePoint;  // the Point2D
        private int splitDim;  // extra storage for what kind of bisection is done
        private RectHV rect;        // the axis-aligned rectangle corresponding to this node
        private Node leftBottom;    // pointer to leftBottom child
        private Node rightTop;      // pointer to rightTop child

        public Node(Point2D p, int dimToUse, RectHV rectToUse) {
            this.nodePoint = p;
            this.splitDim = dimToUse % NUM_DIMS;
            this.leftBottom = null;
            this.rightTop = null;
            this.rect = rectToUse;
        }

        private boolean contains(Node n, Point2D p) {
            if (n.nodePoint.compareTo(p) == 0) return true;

            int cmp = compareNodePoints(n, p);
            // if (n.splitDim == VERTICAL) {
            //     cmp = Double.compare(p.x(),
            //                          n.nodePoint.x()); // leftBottom or rightTop, a vertical split
            //     if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
            //         cmp = 1; // go rightTop if same x-coordinate, but not same y-coordinate
            //     }
            // }
            // else if (n.splitDim == HORIZONTAL) {
            //     cmp = Double.compare(p.y(), n.nodePoint.y()); // above or below, a horizontal split
            // }
            // else throw new RuntimeException("Cannot split on unknown dimension");

            boolean res = false;
            if (cmp < 0) {
                if (n.leftBottom != null) res = n.contains(n.leftBottom, p);

            }
            else if (cmp > 0) {
                if (n.rightTop != null) res = n.contains(n.rightTop, p);
            }
            return res;
        }

        private int compareNodePoints(Node n, Point2D p) {
            int cmp = 0;
            if (n.splitDim == VERTICAL) {
                cmp = Double.compare(p.x(),
                                     n.nodePoint.x()); // leftBottom or rightTop, a vertical split
                // if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
                //     cmp = 1; // go rightTop if same x-coordinate, but not same y-coordinate
                // }
            }
            else if (n.splitDim == HORIZONTAL) {
                cmp = Double.compare(p.y(), n.nodePoint.y()); // above or below, a horizontal split
                // if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
                //     cmp = 1; // go rightTop if same y-coordinate but not same x-coordinate
                // }
            }
            else throw new RuntimeException("Cannot split on unknown dimension");

            if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
                cmp = 1; // go rightTop if same split coordinate but not same other coordinate
            }

            return cmp;
        }

        private void draw() {

            // need to pass in x/y coords so that can transfer previous rectangle.
            // a given node's rectangle does not cover the entire width or height
            if (this.leftBottom != null) this.leftBottom.draw();
            if (this.rightTop != null) this.rightTop.draw();

            // base case

            StdDraw.setPenRadius();
            if (splitDim == VERTICAL) { // vertical split
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(this.nodePoint.x(), this.rect.ymin(), this.nodePoint.x(),
                             this.rect.ymax());
            }
            else if (splitDim == HORIZONTAL) { // horizontal split
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(this.rect.xmin(), this.nodePoint.y(), this.rect.xmax(),
                             this.nodePoint.y());
            }
            else throw new RuntimeException("Unknown split dimension");

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(this.nodePoint.x(), this.nodePoint.y());

        }

    }


    // construct an empty set of points
    public KdTree() {
        root = null;
        this.nNodes = 0;
    }

    // is the set empty? {
    public boolean isEmpty() {
        return (this.nNodes == 0);
    }

    // number of points in the set
    public int size() {
        return this.nNodes;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot insert null point");

        root = insert(root, p, VERTICAL, new RectHV(XMIN, YMIN, XMAX, YMAX));
    }

    // private, recursive helper method to insert new node in kdtree
    // currently only 2 dimensions, but could work for higher dimensions
    // alternate inserting the node based on the level we are at by comparing
    // the x- or y-coordinate, as applicable
    private Node insert(Node n, Point2D p, int dimToUse, RectHV thisRect) {
        if (n == null) {
            n = new Node(p, dimToUse, thisRect); // insert the node
            this.nNodes++;
            return n;
        }

        int cmp = compareNodePoints(n, p);
        // int cmp = 0;
        // dimToUse = dimToUse % NUM_DIMS; // loop back around if we go through all k-dimensions
        // if (dimToUse == VERTICAL) {
        //     cmp = Double.compare(p.x(),
        //                          n.nodePoint.x()); // leftBottom or rightTop, a vertical split
        //     if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
        //         cmp = 1; // go rightTop if same x-coordinate, but not same y-coordinate
        //     }
        // }
        // else if (dimToUse == HORIZONTAL) {
        //     cmp = Double.compare(p.y(), n.nodePoint.y()); // above or below, a horizontal split
        // }
        // else throw new RuntimeException("Cannot split on unknown dimension");


        // replace one point in child node's rectangle using parent node's point information
        RectHV rectToUse = null;
        int nextNodeDim = (n.splitDim + 1)
                % NUM_DIMS; // loop back around if we go through all k-dimensions
        if (cmp < 0) {

            if (dimToUse == VERTICAL) {
                rectToUse = new RectHV(n.rect.xmin(), n.rect.ymin(), n.nodePoint.x(),
                                       n.rect.ymax());
            }
            else if (dimToUse == HORIZONTAL) {
                rectToUse = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(),
                                       n.nodePoint.y());
            }
            else throw new RuntimeException("Unknown dimension on insert");
            n.leftBottom = insert(n.leftBottom, p, nextNodeDim, rectToUse);
        }
        else if (cmp > 0) {
            if (dimToUse == VERTICAL) {
                rectToUse = new RectHV(n.nodePoint.x(), n.rect.ymin(), n.rect.xmax(),
                                       n.rect.ymax());
            }
            else if (dimToUse == HORIZONTAL) {
                rectToUse = new RectHV(n.rect.xmin(), n.nodePoint.y(), n.rect.xmax(),
                                       n.rect.ymax());
            }
            else throw new RuntimeException("Unknown dimension on insert");
            n.rightTop = insert(n.rightTop, p, nextNodeDim, rectToUse);
        }
        // else n.nodePoint = p; // points must be equal
        return n;
    }

    // does the k-d tree contain point p?
    // this is the main search algo
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot search for null point");
        if (this.root == null) return false; // cannot have a point in an empty tree
        return root.contains(root, p);
    }


    // draw all points to standard draw
    // use recursive helper method
    public void draw() {
        root.draw();
    }


    // all points that are inside the rectangle (or on the boundary)
    // sweep line algo?
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Cannot search null rectangle");

        Stack<Point2D> insidePoints = new Stack<Point2D>();

        // search BOTH children of a node
        // prune the children of a node only if a node's rectangle does not intersect the
        // query rectangle (in this case, the param "rect"
        range(rect, root, insidePoints); // mutates stack

        // for (Point2D p : points) {
        //     if (rect.contains(p)) {
        //         insidePoints.enqueue(p);
        //     }
        // }

        return insidePoints;
    }

    private void range(RectHV rect, Node n, Stack<Point2D> insidePoints) {
        // 1. Check rectangle overlap
        // 2. If rectangle overlap, check if point is in range
        // 3. Search BOTH children of node
        // 4. If there is no overlap, prune search (do nothing) and do not search children of node

        if (this.isEmpty()) return;

        if (n == null) return; // base case

        if (!rect.intersects(n.rect)) return;

        if (rect.contains(n.nodePoint)) {
            insidePoints.push(n.nodePoint);
        }
        range(rect, n.leftBottom, insidePoints);
        range(rect, n.rightTop, insidePoints);


    }


    // a nearest neighbor in the set to point p; null if the set is empty
    // use k-d tree bisection algo
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Cannot find Nearest Neighbor for null point");

        if (this.isEmpty()) return null;

        // double minDist = Double.POSITIVE_INFINITY;
        // Point2D minPoint = new Point2D(XMAX, YMAX);

        Point2D minPoint = nearest(root, p, null); // will update minPoint as needed
        // for (Point2D that : points) {
        //     double thisDist = p.distanceTo(that);
        //     if (thisDist < minDist) {
        //         minDist = thisDist;
        //         minPoint = new Point2D(that.x(), that.y());
        //     }
        // }
        return minPoint;
    }

    // helper function for recursive nearest neighbor search
    // 1. check rectangle intersection
    // 2. If intersection, check nodePoint vs minPoint and update if needed
    // 3. Search BOTH children nodes of a compared node, using insertion order
    // 4. Prune search if there is no rectangle intersection
    private Point2D nearest(Node n, Point2D p, Point2D minPoint) {
        if (n == null) return minPoint; // base case
        // if (!n.rect.contains(p)) return minPoint; // prune search

        double minDist;
        if (minPoint != null) minDist = p.distanceSquaredTo(minPoint);
        else minDist = Double.POSITIVE_INFINITY;

        if (n.rect.distanceSquaredTo(p) > minDist) return minPoint; // prune search

        // check distances
        double nodeDist = p.distanceSquaredTo(n.nodePoint);

        if (nodeDist < minDist) minPoint = n.nodePoint;

        int cmp = compareNodePoints(n, p);

        if (cmp == 0)
            return n.nodePoint; // implies there is a nodePoint EXACTLY at point p so it must be the min point
        else if (cmp < 0) {
            minPoint = nearest(n.leftBottom, p, minPoint);
            minPoint = nearest(n.rightTop, p, minPoint);
        }
        else { // if (cmp > 0)
            minPoint = nearest(n.rightTop, p, minPoint);
            minPoint = nearest(n.leftBottom, p, minPoint);
        }

        return minPoint;
    }

    private int compareNodePoints(Node n, Point2D p) {
        int cmp = 0;
        if (n.splitDim == VERTICAL) {
            cmp = Double.compare(p.x(),
                                 n.nodePoint.x()); // leftBottom or rightTop, a vertical split
            // if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
            //     cmp = 1; // go rightTop if same x-coordinate, but not same y-coordinate
            // }
        }
        else if (n.splitDim == HORIZONTAL) {
            cmp = Double.compare(p.y(), n.nodePoint.y()); // above or below, a horizontal split
            // if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
            //     cmp = 1; // go rightTop if same y-coordinate but not same x-coordinate
            // }
        }
        else throw new RuntimeException("Cannot split on unknown dimension");

        if (cmp == 0 && p.compareTo(n.nodePoint) != 0) {
            cmp = 1; // go rightTop if same split coordinate but not same other coordinate
        }

        return cmp;
    }

    public static void main(String[] args) {
        testKdTree();
    }

    private static void testKdTree() {
        KdTree kdtree = new KdTree();
        int n = 10;
        Point2D testPoint = new Point2D(-1, -1);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniformDouble(0.0, 1.0);
            double y = StdRandom.uniformDouble(0.0, 1.0);
            kdtree.insert(new Point2D(x, y));
            if (i == n - 1) {
                testPoint = new Point2D(x, y);
            }
            StdOut.printf("%8.6f %8.6f\n", x, y);
        }

        StdOut.println(
                "Does kdTree contain Point" + testPoint + "?(True): " + kdtree.contains(testPoint));
        StdOut.println("Does kdTree contain Point (2, 2)?(False): " + kdtree.contains(
                new Point2D(2, 2)));
    }


}

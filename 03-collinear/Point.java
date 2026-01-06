/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {

        // if the point is the same, return negative infinity
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }

        // if the line segment is vertical (x0 == x1) return positive infinity
        // needed to avoid potential divide by zero issue
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }

        // if the line segment is horizontal (y0 == y1) return 0
        if (this.y == that.y) {
            return 0;
        }

        return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.x == that.x && this.y == that.y) {
            return 0;
        }

        if (this.y == that.y) {
            return (this.x - that.x); // negative int if this.x < that.x (and y0 == y1)
        }

        return (this.y - that.y); // negative int if this.y < that.y
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        // return new Comparator<Point>() {
        //
        //     @Override
        //     public int compare(Point p, Point q) {
        //         if (Double.compare(slopeTo(p), slopeTo(q)) == 0) {
        //             return p.compareTo(q);
        //         }
        //         return Double.compare(slopeTo(p), slopeTo(q));
        //     }
        // };
        return (p1, p2) -> Double.compare(slopeTo(p1), slopeTo(p2)); // old version
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        testSlopeTo();
        testCompareTo();
    }

    private static void testSlopeTo() {

        System.out.println("Test slopeTo()");

        Point origin = new Point(0, 0);
        Point vert1 = new Point(0, 1);
        Point origin2 = new Point(0, 0);
        Point horiz1 = new Point(1, 0);
        Point boxCorner = new Point(1, 1);
        Point p12 = new Point(1, 2);
        Point p21 = new Point(2, 1);

        // check slopeTo method
        System.out.println(
                "(0,0) (0,0)\tNegative Inf: " + origin.slopeTo(origin2));
        System.out.println(
                "(0,0) (0,1)\tPositive Inf: " + origin.slopeTo(vert1));
        System.out.println("(0,0) (1,0)\t0: " + origin.slopeTo(horiz1));
        System.out.println("(0,1) (1,1)\t0: " + vert1.slopeTo(boxCorner));
        System.out.println("(0,0) (1,1)\t1: " + origin.slopeTo(boxCorner));
        System.out.println("(0,0) (1,2)\t2: " + origin.slopeTo(p12));
        System.out.println("(0,0) (2,1)\t0.5: " + origin.slopeTo(p21));
        System.out.println("(2,1) (0,0)\t-0.5: " + p21.slopeTo(origin));
    }

    private static void testCompareTo() {
        System.out.println("Test compareTo()");

        Point origin = new Point(0, 0);
        Point vert1 = new Point(0, 1);
        Point origin2 = new Point(0, 0);
        Point horiz1 = new Point(1, 0);
        Point boxCorner = new Point(1, 1);
        Point p12 = new Point(1, 2);
        Point p21 = new Point(2, 1);

        System.out.println(
                "(0,0) (0,0)\t0: " + origin.compareTo(origin2));
        System.out.println(
                "(0,0) (0,1)\t-1: " + origin.compareTo(vert1));
        System.out.println("(0,0) (1,0)\t-1: " + origin.compareTo(horiz1));
        System.out.println("(0,1) (1,1)\t-1: " + vert1.compareTo(boxCorner));
        System.out.println("(0,0) (1,1)\t-1: " + origin.compareTo(boxCorner));
        System.out.println("(0,0) (1,2)\t-2: " + origin.compareTo(p12));
        System.out.println("(0,0) (2,1)\t-1: " + origin.compareTo(p21));
        System.out.println("(2,1) (0,0)\t 1: " + p21.compareTo(origin));
    }
}

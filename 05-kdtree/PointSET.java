/* *****************************************************************************
 *  Name: Kedric Daly
 *  Date: 12 Jan 2026
 *  Description: A mutable data type PointSET.java that represents a set of points in the unit square
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }


    // is the set empty? {
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot insert null point");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot search for null point");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {

        for (Point2D p : points) {
            p.draw();
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    // this is brute force class, don't need to do sweep line algo
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Cannot search null rectangle");

        Queue<Point2D> insidePoints = new Queue<Point2D>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                insidePoints.enqueue(p);
            }
        }

        return insidePoints;
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    // use k-d tree bisection algo
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot find NN for null point");

        if (points.isEmpty()) return null;

        double minDist = Double.POSITIVE_INFINITY;
        Point2D minPoint = new Point2D(1, 1);

        for (Point2D that : points) {
            double thisDist = p.distanceSquaredTo(that);
            if (thisDist < minDist) {
                minDist = thisDist;
                minPoint = that;
            }
        }
        return minPoint;
    }


    public static void main(String[] args) {

    }
}

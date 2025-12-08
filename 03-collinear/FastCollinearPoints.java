/* *****************************************************************************
 *  Name: Kedric Daly
 *  Date: 2025 Nov 21
 *  Description: A fast method for collinear points (sorting-based)
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private Point[] locPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // check inputs
        locPoints = checkInputs(points);
        Point[] locPointsBak = locPoints.clone(); // natural order clone

        // for each point p, treat it as the origin
        // find the slope to each other point q
        // sort the points based on the slope they make with p
        // check if there are at least 3 points in the sorted order
        // if there are, they are collinear with p, forming a 4-point segment
        // get the maximal segment
        ArrayList<LineSegment> segmentArray = new ArrayList<LineSegment>();

        // note: for the following loop, "p" is the origin point
        // other references to points Q, R, S should be imagined as P -> Q -> R -> S
        // The goal of these loops are to look at every point R and compare the slopes to
        // the previous point and, if applicable, the next point. If the slopes from P to any other
        // points are the same, they are collinear. if slopePQ != slopePR, we continue the loop
        // and if slopePR == slopePS, we continue the loop (and increment the collinear point
        // counter) because there will be a longer line segment.
        //
        // Additionally, for a given set of 4+ collinear points, we sort the sub-array using the
        // natural order. If P > Q, then the segment is a repeat of the ordered segment
        // For example, if we imagine a set of collinear points A -> B -> C -> D, A < B < C < D.
        // Therefore, we can compare the origin point (P, above) to the start of the collinear
        // sub-array. Only A should pass this logical test, so that we only add the segment once.
        // E.g. if we are looping and have B as the origin so we get B -> A -> C -> D as the set of
        // origin + collinear sub-array, we would not add this because B > A. Instead the segment
        // gets added when A is treated as the origin.
        outerLoop:
        for (int i = 0; i < locPoints.length; i++) {
            Point p = locPointsBak[i];
            Arrays.sort(locPoints, p.slopeOrder()); // places p at position 0
            int nCollinearPoints = 2; // start at 2 because initial comparison is 2 points

            for (int j = 1; j < locPoints.length; j++) {
                double slopePQ = p.slopeTo(locPoints[j - 1]);
                double slopePR = p.slopeTo(locPoints[j]);

                if (Double.compare(slopePQ, slopePR) == 0) {

                    nCollinearPoints++;
                    int start = j - nCollinearPoints + 2;

                    // check if there will be a longer line segment
                    if (j < locPoints.length - 1) {
                        double slopePS = p.slopeTo(locPoints[j + 1]);
                        if (Double.compare(slopePR, slopePS) == 0) {
                            continue; // there will be a longer line segment
                        }
                    }
                    // if start of a line segment is near the end of slopeOrder, cannot have 4
                    // collinear points
                    if (start >= locPoints.length - 2) {
                        continue outerLoop;
                    }

                    if (nCollinearPoints >= 4) {
                        // sort sub-array and check if we have the correct origin for a segment
                        Arrays.sort(locPoints, start, j + 1);
                        if (p.compareTo(locPoints[start]) > 0) {
                            nCollinearPoints = 2;
                            continue; // will not be a min/max line segment, but could be for a later segment
                            // e.g. if it is the end of one segment, but beginning of another
                        }
                        LineSegment thisSegment = new LineSegment(p, locPoints[j]);
                        segmentArray.add(thisSegment);
                    }
                }
                else {
                    nCollinearPoints = 2;
                }
            }
        }
        segments = new LineSegment[segmentArray.size()];
        segmentArray.toArray(segments);
    }

    /*
    Checks the input for null point entries and duplicate point entries
    Side effects: sorts locPoints in (x,y) point order (default sort)
        Point order means y-value first, with ties decided by x-value
     */
    private Point[] checkInputs(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null constructor argument");
        locPoints = Arrays.copyOf(points, points.length);
        for (Point point : locPoints) {
            if (point == null) throw new IllegalArgumentException("A point in the input is null");
        }

        Arrays.sort(locPoints); // natural order sort

        // check for duplicates
        for (int i = 1; i < points.length; i++) {
            if (locPoints[i].compareTo(locPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("Duplicate point in input array");
            }
        }
        return locPoints;
    }


    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }


    /*
    Unit testing
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

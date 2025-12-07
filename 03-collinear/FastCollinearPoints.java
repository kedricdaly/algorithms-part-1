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
        Point[] locPointsBak = locPoints.clone();
        // Arrays.sort(locPointsBak); // natural order sort - already sorted in checkInputs()

        // for each point p, treat it as the origin
        // find the slope to each other point q
        // sort the points based on the slope they make with p
        // check if there are at least 3 points in the sorted order
        // if there are, they are collinear with p, forming a 4-point segment
        // get the maximal segment
        ArrayList<LineSegment> segmentArray = new ArrayList<LineSegment>();

        outerLoop:
        for (int i = 0; i < locPoints.length; i++) {
            Point p = locPointsBak[i];
            // locPoints = locPointsBak.clone(); // reset to natural order
            Arrays.sort(locPoints, p.slopeOrder()); // places p at position 0
            int nCollinearPoints = 2; // start at 2 because initial comparison is 2 points
            int start = 0;
            int end = 0;
            for (int j = 1; j < locPoints.length; j++) {
                // Point q = locPoints[j];
                double currentTestSlope = p.slopeTo(locPoints[j - 1]);
                double slopePQ = p.slopeTo(locPoints[j]);
                if (Double.compare(currentTestSlope, slopePQ) == 0) {

                    nCollinearPoints++;
                    start = j - nCollinearPoints + 2;
                    // if start of a line segment is near the end of slopeOrder, cannot have 4
                    // collinear points
                    if (start >= locPoints.length - 2) {
                        continue;
                    }
                    // System.out.println("Start of subarray: " + start);


                    if (nCollinearPoints == 4) {
                        end = j;
                        Arrays.sort(locPoints, start, end + 1);
                        // System.out.print(
                        //         "Comparing " + p.toString() + " to " + locPoints[start].toString());
                        // System.out.println(
                        //         ": " + p.compareTo(
                        //                 locPoints[start]));
                        if (p.compareTo(locPoints[start]) > 0) {
                            // continue outerLoop; // will not be a min/max line segment
                            nCollinearPoints = 2;
                            continue; // will not be a min/max line segment, but could be for a later segment
                            // e.g. if it is the end of one segment, but beginning of another
                        }
                        LineSegment thisSegment = new LineSegment(p, locPoints[j]);
                        segmentArray.add(thisSegment);
                    }
                    else if (nCollinearPoints > 4) {
                        end = j;
                        Arrays.sort(locPoints, start, end + 1);
                        // System.out.print(
                        //         " >4 Comparing " + p.toString() + " to "
                        //                 + locPoints[start].toString());
                        // System.out.println(
                        //         ": " + p.compareTo(
                        //                 locPoints[start]));
                        if (p.compareTo(locPoints[start]) > 0 && segmentArray.size() >= 1) {
                            // if true, mistakenly added segment too early and need to remove
                            segmentArray.remove(segmentArray.size() - 1);
                            continue; // will not be a min/max line segment
                        }
                        LineSegment thisSegment = new LineSegment(p, locPoints[j]);
                        segmentArray.set(segmentArray.size() - 1, thisSegment); // replace
                    }
                }
                else {
                    nCollinearPoints = 2;
                    start = j;
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

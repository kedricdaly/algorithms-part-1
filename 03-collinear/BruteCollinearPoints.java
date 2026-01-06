/* *****************************************************************************
 *  Name: Kedric Daly
 *  Date: 2025 Nov 21
 *  Description: A brute force method for collinear points
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private int nSegments = 0;
    private LineSegment[] segments;
    private Point[] locPoints;

    public BruteCollinearPoints(Point[] points) {
        // check inputs
        locPoints = checkInputs(points);

        // need array list for dynamic sizing, otherwise need to copy to new
        // array each time we want to add a LineSegment
        ArrayList<LineSegment> segmentArray = new ArrayList<LineSegment>();

        // don't pre-optimize. Need to loop through 4 loops anyway for brute force
        bruteLoop:
        for (int w = 0; w < locPoints.length - 3; w++) {
            Point p = locPoints[w];
            for (int x = w + 1; x < locPoints.length - 2; x++) {
                Point q = locPoints[x];
                for (int y = x + 1; y < locPoints.length - 1; y++) {
                    Point r = locPoints[y];
                    if (p.slopeTo(q) != p.slopeTo(r)) {
                        continue;
                    }
                    for (int z = y + 1; z < locPoints.length; z++) {
                        Point s = locPoints[z];
                        if (p.slopeTo(q) != p.slopeTo(s)) {
                            continue;
                        }

                        // sort p, q, r, s and then add the segment to LineSegment[] segments;
                        // Point[] pointsToSort = new Point[] { p, q, r, s };
                        // Arrays.sort(pointsToSort);
                        LineSegment thisSegment = new LineSegment(p, s);
                        // check for duplicates
                        // bruteCollinear should never have more than 4 collinear points for
                        // a segment, so skip duplicate check
                        // for (int k = 0; k < nSegments; k++) {
                        //     if (thisSegment.q.equals(segmentArray.get(k).q)) {
                        //         continue bruteLoop;
                        //     }
                        // }

                        // only add if we don't find any duplicates
                        segmentArray.add(thisSegment);
                        nSegments++;
                    }
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

        Arrays.sort(locPoints);

        // check for duplicates
        for (int i = 1; i < points.length; i++) {
            if (locPoints[i].compareTo(locPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("Duplicate point in input array");
            }
        }
        return locPoints;
    }

    /*
    Return the number of segments with 4 collinear points
     */
    public int numberOfSegments() {
        return nSegments;
    }

    /*
    Return each segment p->s exactly once
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    /*
    unit testing for BruteCollinearPoints
     */
    public static void main(String[] args) {
        // read the n points from a file
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

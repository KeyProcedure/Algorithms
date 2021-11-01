import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Dependencies: Point.java, LineSegment.java
 *
 * Implements a brute force algorithm to examine 4 points at a time and checks
 * whether they all lie on the same line segment.
 * Confirms whether 4 points p, q, r, and s are collinear, by checking whether
 * the three slopes between p and q, between p and r, and between p and s are all equal.
 */
public class BruteCollinearPoints {
  private LineSegment[] lineSegments;
  private int lineCount;

  /**
   * Finds all line segments containing 4 points by comparing their slopes.
   * @param points array of points used to find line segments
   * @throws IllegalArgumentException if points array is null
   * @throws IllegalArgumentException if points array is empty
   * @throws IllegalArgumentException if a point in the points array is null
   */
  public BruteCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("The array of points has not been initialized.");

    if (points.length == 0)
      throw new IllegalArgumentException("The array of points is empty.");

    for (Point element : points) {
      if (element == null)
        throw new IllegalArgumentException("An element in the array of points has not been initialized.");
    }

    searchForDuplicates(points);

    Point[] pointsCopy = new Point[points.length];
    System.arraycopy(points, 0, pointsCopy, 0, points.length);

    lineSegments = new LineSegment[1];
    lineCount = 0;

    // brute force comparison of the slopes of all points, if a line is found, add it to the lineSegments array
    for (Point i : pointsCopy)
      for (Point j : pointsCopy)
        for (Point k : pointsCopy)
          for (Point m : pointsCopy) {
            if (j.slopeTo(i) == m.slopeTo(k) && k.slopeTo(j) == m.slopeTo(k) &&
                    m.compareTo(k) > 0 && k.compareTo(j) > 0 && j.compareTo(i) > 0) {
              lineSegments[lineCount++] = new LineSegment(i, m);
              if (lineCount == lineSegments.length)
                resize(2 * lineSegments.length);
            }
          }

    resize(lineCount);
  }

  /**
   * Brute force search to find a duplicate point.
   * @param points array of points used to find duplicates
   * @throws IllegalArgumentException if a repeated element exists in the array
   */
  private void searchForDuplicates(Point[] points) {
    for (int i = 0; i < points.length; i++)
      for (int j = i + 1; j < points.length; j++) {
        if (points[i].compareTo(points[j]) == 0)
          throw new IllegalArgumentException("Repeated element in the array of points.");
      }
  }

  /**
   * Retrieves the number of line segments found.
   * @return number of lines found
   */
  public int numberOfSegments() {
    return lineCount;
  }

  /**
   * Hard copies the array of line segments.
   * @return a copy of the line segments
   */
  public LineSegment[] segments() {
    LineSegment[] lineSegmentsCopy = new LineSegment[lineCount];
    System.arraycopy(lineSegments, 0, lineSegmentsCopy, 0, lineCount);
    return lineSegmentsCopy;
  }

  /**
   * Resizes the array of line segments.
   * @param capacity new array capacity
   */
  private void resize(int capacity) {
    LineSegment[] copy = new LineSegment[capacity];
    if (lineCount > 0)
      System.arraycopy(lineSegments, 0, copy, 0, lineCount);
    lineSegments = copy;
  }

  /**
   * Reads the input file, prints to standard output the line segments that are
   * discovered, one per line; and draws to standard draw the line segments.
   * @param args not used
   */
  public static void main(String[] args) {
    final String fileName = "03_collinear/test/grid4x4.txt"; // input test file
    In in = new In(fileName);
    int n = in.readInt(); // read the n points from a file

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
    for (Point p : points)
      p.draw();

    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      if (segment != null) {
        StdOut.println(segment);
        segment.draw();
      }
    }
    StdDraw.show();
  }
}
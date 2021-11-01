import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/**
 * Dependencies: Point.java, LineSegment.java
 *
 * Implements merge sort to sort points based on the slope they make relative to a
 * single point. Searches the array of sorted points to find sets of 4 or more collinear points.
 * Repeats this algorithm for all points in the array to find all sets of collinear points.
 */
public class FastCollinearPoints {
  private int lineCount;
  private LineSegment[] lineSegments;

  /**
   * Finds all line segments containing 4 points by comparing their slopes.
   * @param points array of points used to find lines
   * @throws IllegalArgumentException if points array is null
   * @throws IllegalArgumentException if points array is empty
   * @throws IllegalArgumentException if a point in the points array is null
   */
  public FastCollinearPoints(Point[] points) {
    if (points == null)
    throw new IllegalArgumentException("The array of points has not been initialized.");

    if (points.length == 0)
      throw new IllegalArgumentException("The array of points is empty.");

    for (Point element : points) {
      if (element == null)
        throw new IllegalArgumentException("An element in the array of points is null.");
    }

    searchForDuplicates(points);

    lineSegments = new LineSegment[1];
    lineCount = 0;

    // for each point p in the array, sort the points based on the slope they make
    // with the point p, then find line segments
    for (Point p : points) {
      Point[] aux = new Point[points.length];
      Point[] pointPOrdered = new Point[points.length];
      System.arraycopy(points, 0, pointPOrdered, 0, points.length);
      sort(pointPOrdered, aux, p.slopeOrder(), 0, points.length - 1);
      findSegment(pointPOrdered);
    }

    resize(lineCount);
  }

  /**
   * Sorts the points array, then searches to find a duplicate point.
   * @param points array of points used to find duplicates
   * @throws IllegalArgumentException if a repeated element exists in the array
   */
  private void searchForDuplicates(Point[] points) {
    Point[] aux = new Point[points.length];
    sort(points, aux, Comparator.naturalOrder(), 0, points.length - 1);

    for (int i = 0; i < points.length - 1; i++)
      if (points[i].compareTo(points[i + 1]) == 0)
        throw new IllegalArgumentException("Repeated element in the array of points.");
  }

  /**
   * Finds line segments consisting of collinear points by comparing their
   * slopes, adds line segment to lineSegments array.
   * @param sortedPoints array of points sorted in slope order relative to a single point
   */
  private void findSegment(Point[] sortedPoints) {
    for (int j = 1; j < sortedPoints.length - 2; j++) {
      Point lowPoint = sortedPoints[0];
      Point highPoint = sortedPoints[0];

      if (sortedPoints[j].slopeTo(lowPoint) == sortedPoints[j + 1].slopeTo(lowPoint) &&
              sortedPoints[j + 1].slopeTo(lowPoint) == sortedPoints[j + 2].slopeTo(lowPoint) &&
              sortedPoints[j].compareTo(lowPoint) > 0) {
        if (sortedPoints[j].compareTo(lowPoint) < 0)
          lowPoint = sortedPoints[j];

        if (sortedPoints[j + 2].compareTo(highPoint) > 0)
          highPoint = sortedPoints[j + 2];

        lineSegments[lineCount++] = new LineSegment(lowPoint, highPoint);

        if (lineCount == lineSegments.length)
          resize(2 * lineSegments.length);
      }
    }
  }

  /**
   * Recursively stable sorts points, based on slope order.
   * @param pointsArray array of points to be sorted
   * @param aux auxiliary array
   * @param comparator comparison method used to determine sorting order
   * @param low lowest index of the array
   * @param high highest index of the array
   */
  private static void sort(Point[] pointsArray, Point[] aux, Comparator<Point> comparator, int low, int high) {
    if (high <= low)
      return;

    int mid = low + (high - low) / 2;
    sort(pointsArray, aux, comparator, low, mid);
    sort(pointsArray, aux, comparator, mid + 1, high);
    merge(pointsArray, aux, comparator, low, mid, high);
  }

  /**
   * Merges two sorted sub-arrays, based on slope order.
   * @param pointsArray sorted array of points
   * @param aux auxiliary array
   * @param comparator comparison method used to determine sorting order
   * @param low lowest index of the array
   * @param mid middle index of the array
   * @param high highest index of the array
   */
  private static void merge(Point[] pointsArray, Point[] aux, Comparator<Point> comparator, int low, int mid, int high) {
    assert isSorted(pointsArray, comparator, low, mid); // pre-condition: a[lo..mid] sorted
    assert isSorted(pointsArray, comparator, mid + 1, high); // pre-condition: a[mid+1..hi] sorted
    for (int k = low; k <= high; k++)
      aux[k] = pointsArray[k];
    int i = low, j = mid + 1;
    for (int k = low; k <= high; k++) {
      if (i > mid) pointsArray[k] = aux[j++];
      else if (j > high) pointsArray[k] = aux[i++];
      else if (less(comparator, aux[j], aux[i])) {
        pointsArray[k] = aux[j++];
      } else pointsArray[k] = aux[i++];
    }
    assert isSorted(pointsArray, comparator, low, high); // post-condition: a[lo..hi] sorted
  }

  /**
   * Verifies that an array is sorted, based on slope order.
   * @param pointsArray sorted array of points
   * @param comparator comparison method used to determine sorting order
   * @param start index position to start search
   * @param end index position to end search
   * @return true if the array is sorted, false otherwise
   */
  private static boolean isSorted(Point[] pointsArray, Comparator<Point> comparator, int start, int end) {
    for (int i = start; i <= end; i++) {
      if (less(comparator, pointsArray[i], pointsArray[i - 1]))
        return false;
    }
    return true;
  }

  /**
   * Determines whether one point is less than another by comparing their y and x coordinates.
   * @param comparator comparison method used to compare the coordinates of two points
   * @param v point to be compared
   * @param w point to be compared
   * @return true if one point is less than or equal to another, false otherwise
   */
  private static boolean less(Comparator<Point> comparator, Point v, Point w) {
    if (comparator.compare(v, w) == 0)
      return v.compareTo(w) < 0;
    return comparator.compare(v, w) < 0;
  }

  /**
   * Resizes the lineSegment array to a new capacity.
   * @param capacity size of the new array
   */
  private void resize(int capacity) {
    LineSegment[] copy = new LineSegment[capacity];
    for (int i = 0; i < lineCount; i++) {
      copy[i] = lineSegments[i];
    }
    lineSegments = copy;
  }

  /**
   * Retrieves the number of line segments found.
   * @return number of lines found
   */
  public int numberOfSegments() {
    return lineCount;
  }

  /**
   * Retrieves a copy of the line segments found.
   * @return array of line segment
   */
  public LineSegment[] segments() {
    LineSegment[] segmentArray = new LineSegment[lineCount];
    for (int i = 0; i < lineCount; i++) {
      segmentArray[i] = lineSegments[i];
    }
    return segmentArray;
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
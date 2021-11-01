import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

/**
 * An immutable data type for points in the plane.
 */
public class Point implements Comparable<Point> {
  private final int x;     // x-coordinate of this point
  private final int y;     // y-coordinate of this point

  /**
   * Initializes a new point.
   * @param x x-coordinate of the point
   * @param y y-coordinate of the point
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point to standard draw.
   * @param that the other point
   */
  public void drawTo(Point that) {
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point.
   * If the two points are (x0, y0) and (x1, y1), then the slope
   * is (y1 - y0) / (x1 - x0).
   * The slope is defined to be +0.0 if the line segment connecting the two
   * points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical;
   * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
   * @param that the other point
   * @return the slope between this point and the that point
   * @throws NullPointerException if the point is null
   */
  public double slopeTo(Point that) {
    if (that == null)
      throw new NullPointerException("Specified point has not been initialized.");

    double xSlope = that.x - this.x;
    double ySlope = that.y - this.y;

    if (xSlope == 0 && ySlope == 0)
      return Double.NEGATIVE_INFINITY; // line consisting of only itself as a point, slope = negative infinity

    if (ySlope == 0)
      return 0; // horizontal line segment, slope = 0

    if (xSlope == 0)
      return Double.POSITIVE_INFINITY; // vertical line segment, slope = positive infinity

    return (ySlope / xSlope); // slope = y / x
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate.
   * The invoking point (x0, y0) is less than the argument point
   * (x1, y1) if either y0 < y1 or if y0 = y1 and x0 < x1.
   * @param that the other point
   * @return 0 if this point is equal to the argument point (x0 = x1 and y0 = y1);
   * a negative integer if this point is less than the argument point;
   * a positive integer if this point is greater than the argument point
   * @throws NullPointerException if the point is null
   */
  public int compareTo(Point that) {
    if (that == null)
      throw new NullPointerException("Specified point has not been initialized.");

    if (this.x == that.x && this.y == that.y)
      return 0;

    if (this.y < that.y || (this.y == that.y && this.x < that.x))
      return -1;

    return 1;
  }

  /**
   * Compares two points by the slope they make with this point.
   * The slope is defined as in the slopeTo() method.
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    return new SlopeOrder();
  }

  /**
   * Compares the slopes that q1 and q2 make with the invoking object p.
   * @throws NullPointerException if point q1 is null
   * @throws NullPointerException if point q2 is null
   */
  private class SlopeOrder implements Comparator<Point> {
    @Override
    public int compare(Point q1, Point q2) {
      if (q1 == null)
        throw new NullPointerException("Point q1 has not been initialized.");
      if (q2 == null)
        throw new NullPointerException("Point q2 has not been initialized.");
      return Double.compare(slopeTo(q1), slopeTo(q2));
    }
  }

  /**
   * Creates a string representation of this point.
   * @return a string representation of this point
   */
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    Point p = new Point(7, 4);
    Point q = new Point(7, 4);
    Point r = new Point(3, 6);
    System.out.println(p.slopeOrder().compare(q, r));
  }
}
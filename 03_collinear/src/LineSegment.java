/**
 *  Dependencies: Point.java
 *
 *  An immutable data type for Line segments in the plane.
 */
public class LineSegment {
  private final Point p;   // one endpoint of this line segment
  private final Point q;   // the other endpoint of this line segment

  /**
   * Initializes a new line segment.
   * @param p one endpoint
   * @param q the other endpoint
   * @throws NullPointerException if either p or q is null
   */
  public LineSegment(Point p, Point q) {
    if (p == null)
      throw new NullPointerException("Point p has not been initialized.");
    if (q == null)
      throw new NullPointerException("Point q has not been initialized.");
    this.p = p;
    this.q = q;
  }

  /**
   * Draws this line segment to standard draw.
   */
  public void draw() {
    p.drawTo(q);
  }

  /**
   * Creates a string representation of this line segment.
   * @return a string representation of this line segment
   */
  public String toString() {
    return p + " -> " + q;
  }

  /**
   * Throws an exception if called. Hashing does not typically lead to good
   * *worst-case* performance guarantees, as required in this assignment.
   * @throws UnsupportedOperationException if this method is called
   */
  public int hashCode() {
    throw new UnsupportedOperationException("hasCode is an unsupported function.");
  }
}
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Brute force implementation of a red–black BST that represents a set of points
 * in the unit square.
 */
public class PointSET {
  private final SET<Point2D> pointSet;

  /**
   * Constructs an empty tree of points
   */
  public PointSET() {
    this.pointSet = new SET<>();
  }

  /**
   * Checks if the tree is empty.
   * @return true if the tree is empty, false otherwise
   */
  public boolean isEmpty() {
    return pointSet.isEmpty();
  }

  /**
   * Retrieves the number of points in the tree.
   * @return size of pointSet
   */
  public int size() {
    return pointSet.size();
  }

  /**
   * Adds the point to the tree (if it is not already in the tree).
   * @param p point to be added
   * @throws IllegalArgumentException if the point p is null
   */
  public void insert(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException("Point p has not been initialized.");

    if (!contains(p))
      pointSet.add(p);
  }

  /**
   * Checks whether the tree contains point p.
   * @param p point to check
   * @return true if the point exists in the tree, false otherwise
   */
  public boolean contains(Point2D p) {
    return pointSet.contains(p);
  }

  /**
   * Draws all points to standard draw.
   */
  public void draw() {
    for (Point2D point : pointSet) {
      StdDraw.point(point.x(), point.y());
    }
  }

  /**
   * A stack of points that are inside the rectangle (or on the boundary).
   * @param rect rectangle to search within
   * @return stack of points contained in the specified rectangle
   * @throws IllegalArgumentException if the rectangle is null
   */
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null)
      throw new IllegalArgumentException("Rectangle has not been initialized.");

    Stack<Point2D> rectPointsStack = new Stack<>();
    for (Point2D point : pointSet) {
      if (rect.contains(point))
        rectPointsStack.push(point);
    }
    return rectPointsStack;
  }

  /**
   * Locates the nearest point to point p.
   * @param p point used as the origin of the search
   * @return nearest neighbor in the tree to point p; null if the tree is empty
   * @throws IllegalArgumentException if point p is null
   */
  public Point2D nearest(Point2D p) {
    if (p == null)
      throw new IllegalArgumentException("Point p has not been initialized.");

    if (isEmpty())
      return null;

    Point2D nearestPoint = pointSet.min();
    double minDistance = nearestPoint.distanceSquaredTo(p);

    for (Point2D point : pointSet) {
      if (point.distanceSquaredTo(p) < minDistance) {
        nearestPoint = point;
        minDistance = nearestPoint.distanceSquaredTo(p);
      }
    }
    return nearestPoint;
  }
}
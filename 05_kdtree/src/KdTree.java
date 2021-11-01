import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A 2d implementation of a BST (uses two-dimensional keys).
 * Builds a BST with points in the nodes, using the x- and y-coordinates of the
 * points as keys in strictly alternating sequence.
 */
public class KdTree {
  private static class Node {
    private Point2D point;
    private RectHV rect;
    private Node left;
    private Node right;
  }

  private Node root;
  private Node champion;
  private double championDistance;
  private int nodeCount;
  private Stack<Point2D> rangeStack;

  /**
   * Constructs an empty tree of points.
   */
  public KdTree() {
    this.nodeCount = 0;
    rangeStack = new Stack<>();
  }

  /**
   * Checks whether the tree is empty.
   * @return true if the kd-tree is empty, false otherwise
   */
  public boolean isEmpty() {
    return nodeCount == 0;
  }

  /**
   * Retrieves the number of points in the tree.
   * @return number of nodes
   */
  public int size() {
    return nodeCount;
  }

  /**
   * Adds the point to the tree (if it is not already in the tree).
   * @param point point to be added
   * @throws IllegalArgumentException if the point is null
   */
  public void insert(Point2D point) {
    if (point == null)
      throw new IllegalArgumentException("Point has not been initialized.");

    if (contains(point))
      return;

    champion = root;
    root = insert(root, point, true);
    nodeCount++;
  }

  /**
   * Recursively inserts points based on their position relative to the root.
   * If the point to be inserted has a smaller x-coordinate than the point at the root,
   * go left; otherwise go right;
   * then at the next level, use the y-coordinate, if the point to be inserted has a
   * smaller y-coordinate than the point in the node, go left; otherwise go right;
   * then at the next level, the x-coordinate, and so forth.
   * @param xNode node to be inserted
   * @param point point to be inserted
   * @param vertical orientation of the point's splitting line
   * @return root node
   */
  private Node insert(Node xNode, Point2D point, boolean vertical) {
    if (xNode == null)
      return rect(point, vertical);

    if (xNode.point.x() == point.x() && xNode.point.y() == point.y())
      return xNode;

    // vertical splitting line
    if (vertical) {
      if (point.x() < xNode.point.x()) {
        champion = xNode;
        xNode.left = insert(xNode.left, point, false);
      }
      else {
        champion = xNode;
        xNode.right = insert(xNode.right, point, false);
      }
    }

    // horizontal splitting line
    else {
      if (point.y() < xNode.point.y()) {
        champion = xNode;
        xNode.left = insert(xNode.left, point, true);
      }
      else {
        champion = xNode;
        xNode.right = insert(xNode.right, point, true);
      }
    }
    return xNode;
  }

  /**
   * Creates the axis-aligned rectangle for the node.
   * @param point point to be inserted
   * @param vertical orientation of the point's splitting line
   * @return root node
   */
  private Node rect(Point2D point, boolean vertical) {
    Node newNode = new Node();
    newNode.point = point;
    if (champion == null)
      newNode.rect = new RectHV(0, 0, 1, 1);

    else if (!vertical) {
      if (point.x() < champion.point.x())
        newNode.rect = new RectHV(champion.rect.xmin(), champion.rect.ymin(),
                champion.point.x(), champion.rect.ymax());
      else
        newNode.rect = new RectHV(champion.point.x(), champion.rect.ymin(),
                champion.rect.xmax(), champion.rect.ymax());
    }
    else {
      if (point.y() < champion.point.y())
        newNode.rect = new RectHV(champion.rect.xmin(), champion.rect.ymin(),
                champion.rect.xmax(), champion.point.y());
      else
        newNode.rect = new RectHV(champion.rect.xmin(), champion.point.y(),
                champion.rect.xmax(), champion.rect.ymax());
    }
    return newNode;
  }

  /**
   * Verifies that the tree contains the point.
   * @param point point to find
   * @return true if the point exists in the tree, false otherwise
   */
  public boolean contains(Point2D point) {
    if (isEmpty())
      return false;

    Node xNode = contains(root, point);
    return (xNode != null);
  }

  /**
   * Searches the tree to find a point.
   * @param xNode root node
   * @param point point to find
   * @return node containing the point, or an empty node
   */
  private Node contains(Node xNode, Point2D point) {
    boolean vertical = true;
    while (xNode != null) {
      if (xNode.point.x() == point.x() && xNode.point.y() == point.y())
        return xNode;

      if (vertical) {
        if (point.x() < xNode.point.x())
          xNode = xNode.left;
        else
          xNode = xNode.right;
      }
      else {
        if (point.y() < xNode.point.y())
          xNode = xNode.left;
        else
          xNode = xNode.right;
      }
      vertical = !vertical;
    }
    return null;
  }

  /**
   * Draws all points to standard draw.
   */
  public void draw() {
    StdDraw.setPenColor(StdDraw.BLACK);
    drawPoints(root, true);
  }

  /**
   * Recursively draws points and their horizontal/vertical splits.
   * @param xNode root node
   * @param vertical orientation of the point's splitting line
   */
  private void drawPoints(Node xNode, boolean vertical) {
    if (xNode == null)
      return;

    if (vertical) {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.015);
      xNode.point.draw();
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.setPenRadius(0.005);
      StdDraw.line(xNode.point.x(), xNode.rect.ymin(), xNode.point.x(),
              xNode.rect.ymax());
      drawPoints(xNode.left, false);
      drawPoints(xNode.right, false);
    }
    else {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.015);
      xNode.point.draw();
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.setPenRadius(0.005);
      StdDraw.line(xNode.rect.xmin(), xNode.point.y(), xNode.rect.xmax(),
              xNode.point.y());
      drawPoints(xNode.left, true);
      drawPoints(xNode.right, true);
    }
  }

  /**
   * Creates a stack of all points that are inside the rectangle (or on the boundary).
   * @param rect axis-aligned rectangle
   * @return stack of points within the rectangle
   * @throws IllegalArgumentException if the rectangle is null
   */
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null)
      throw new IllegalArgumentException("Rectangle has not been initialized.");

    rangeStack = new Stack<>();
    rangeSearch(root, rect, true);
    return rangeStack;
  }

  /**
   * Finds all points contained in a given query rectangle, starts at the root and
   * recursively searches for points in both subtrees using the following pruning rule:
   * if the query rectangle does not intersect the rectangle corresponding to a node,
   * there is no need to explore that node (or its subtrees).
   * A subtree is searched only if it might contain a point contained in the query rectangle.
   * @param xNode root node
   * @param rect rectangle to be searched within
   * @param vertical orientation of the point's splitting line
   */
  private void rangeSearch(Node xNode, RectHV rect, boolean vertical) {
    if (xNode == null)
      return;

    // vertical splitting line
    if (vertical) {
      RectHV nodeSplitLine = new RectHV(xNode.point.x(), xNode.rect.ymin(), xNode.point.x(),
              xNode.rect.ymax());

      // query rectangle intersects splitting line
      if (rect.intersects(nodeSplitLine)) {
        if (rect.contains(xNode.point))
          rangeStack.push(xNode.point);
        rangeSearch(xNode.left, rect, false);
        rangeSearch(xNode.right, rect, false);
      }
      else if (xNode.left != null && rect.intersects(xNode.left.rect))
        rangeSearch(xNode.left, rect, false);
      else if (xNode.right != null && rect.intersects(xNode.right.rect))
        rangeSearch(xNode.right, rect, false);
    }

    // horizontal splitting line
    else {
      RectHV nodeSplitLine = new RectHV(xNode.rect.xmin(), xNode.point.y(), xNode.rect.xmax(),
              xNode.point.y());

      // query rectangle intersects splitting line
      if (rect.intersects(nodeSplitLine)) {
        if (rect.contains(xNode.point))
          rangeStack.push(xNode.point);
        rangeSearch(xNode.left, rect, true);
        rangeSearch(xNode.right, rect, true);
      }
      else if (xNode.left != null && rect.intersects(xNode.left.rect))
        rangeSearch(xNode.left, rect, true);
      else if (xNode.right != null && rect.intersects(xNode.right.rect))
        rangeSearch(xNode.right, rect, true);
    }
  }

  /**
   * Finds a point nearest the query point.
   * @param queryPoint point used as the origin of the search
   * @return a nearest neighbor in the tree to point p; null if the tree is empty
   * @throws IllegalArgumentException if the query point is null
   */
  public Point2D nearest(Point2D queryPoint) {
    if (queryPoint == null)
      throw new IllegalArgumentException("The specified point has not been initialized.");
    if (isEmpty())
      return null;

    champion = root;
    championDistance = champion.point.distanceSquaredTo(queryPoint);
    nearSearch(root, queryPoint);
    return champion.point;
  }

  /**
   * Finds the closest point to a given query point, starts at the root and recursively searches
   * in both subtrees using the following pruning rule:
   * if the closest point discovered so far is closer than the distance between the query point
   * and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees).
   * Search a node only if it might contain a point that is closer than the best one found so far.
   * When there are two possible subtrees to go down, the subtree that is on the same side of the splitting line
   * as the query point is chosen as the first subtree to explore—the closest point found while exploring the
   * first subtree may enable pruning of the second subtree.
   * @param xNode root node
   * @param queryPoint point used as the origin of the search
   */
  private void nearSearch(Node xNode, Point2D queryPoint) {
    boolean goLeft = xNode.left != null;
    boolean goRight = xNode.right != null;
    double leftRectDistance = 0;
    double rightRectDistance = 0;

    if (goLeft)
      leftRectDistance = xNode.left.rect.distanceSquaredTo(queryPoint);
    if (goRight)
      rightRectDistance = xNode.right.rect.distanceSquaredTo(queryPoint);

    if (goLeft && goRight && championDistance > leftRectDistance && championDistance > rightRectDistance) {
      if (xNode.left.point.distanceSquaredTo(queryPoint) < championDistance) {
        champion = xNode.left;
        championDistance = champion.point.distanceSquaredTo(queryPoint);
      }
      nearSearch(xNode.left, queryPoint);
      if (xNode.right.point.distanceSquaredTo(queryPoint) < championDistance) {
        champion = xNode.right;
        championDistance = champion.point.distanceSquaredTo(queryPoint);
      }
      nearSearch(xNode.right, queryPoint);
    }
    if (goLeft) {
      if (championDistance > leftRectDistance) {
        if (xNode.left.point.distanceSquaredTo(queryPoint) < championDistance) {
          champion = xNode.left;
          championDistance = champion.point.distanceSquaredTo(queryPoint);
        }
        nearSearch(xNode.left, queryPoint);
      }
    }
    if (goRight) {
      if (championDistance > rightRectDistance) {
        if (xNode.right.point.distanceSquaredTo(queryPoint) < championDistance) {
          champion = xNode.right;
          championDistance = champion.point.distanceSquaredTo(queryPoint);
        }
        nearSearch(xNode.right, queryPoint);
      }
    }
  }
}
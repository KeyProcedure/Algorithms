import edu.princeton.cs.algs4.Stack;

/**
 * Dependencies: Pixel.java
 *
 * Computes the shortest path through the energy matrix, selecting
 * a series of adjacent pixels with the least amount of cumulative energy.
 */
public class ShortestPath {
  private Pixel[][] pathTo;
  private double[][] energyTo;
  private int pathIndex;

  /**
   * Finds the shortest path through the energy matrix.
   * Assigns the values of energy required to travel to a specific pixel from an
   * adjacent pixel, then finds the most efficient path through the matrix.
   * @param energyArray energy matrix
   * @param width width of the picture
   * @param height height of the picture
   */
  public ShortestPath(double[][] energyArray, int width, int height) {
    pathTo = new Pixel[width][height];
    energyTo = new double[width][height];

    // sets initial values for the energy required to travel to a pixel
    for (int x = 0; x < width; x++) {
      energyTo[x][0] = 1000.0;
    }
    for (int y = 1; y < height; y++) {
      for (int x = 0; x < width; x++) {
        energyTo[x][y] = Double.POSITIVE_INFINITY;
      }
    }

    for (int y = 0; y < height - 1; y++) {
      for (int x = 0; x < width; x++) {
        for (Pixel adjacentPixel : adjacentPixels(x, y, width)) {
          relax(new Pixel(x, y), adjacentPixel, energyArray, height);
        }
      }
    }
  }

  /**
   * Creates a stack of pixels adjacent to the specified pixel's coordinates.
   * @param x x-coordinate of the pixel
   * @param y y-coordinate of the pixel
   * @param width width of the picture
   * @return stack of adjacent pixels
   */
  private Stack<Pixel> adjacentPixels(int x, int y, int width) {
    Stack<Pixel> adjacentStack = new Stack<>();
    if (x - 1 >= 0)
      adjacentStack.push(new Pixel(x - 1, y + 1));
    if (x + 1 < width)
      adjacentStack.push(new Pixel(x + 1, y + 1));
    adjacentStack.push(new Pixel(x, y + 1));
    return adjacentStack;
  }

  /**
   * Finds the most efficient path to a pixel by computing the energy
   * required to travel from the fromPixel to the toPixel.
   * If the energy required to travel to a pixel is greater than the energy
   * required to reach that pixel from the fromPixel, reassigns/relaxes the energy matrix
   * to reflect the more efficient path.
   * @param fromPixel pixel traveling from
   * @param toPixel pixel traveling to
   * @param energyArray energy matrix
   * @param height height of the picture
   */
  private void relax(Pixel fromPixel, Pixel toPixel, double[][] energyArray, int height) {
    int toX = toPixel.getX();
    int toY = toPixel.getY();
    int fromX = fromPixel.getX();
    int fromY = fromPixel.getY();

    if (energyTo[toX][toY] > energyTo[fromX][fromY] + energyArray[toX][toY]) {
      energyTo[toX][toY] = energyTo[fromX][fromY] + energyArray[toX][toY];
      pathTo[toX][toY] = fromPixel;

      if (toY == height - 1) {
        if (energyTo[pathIndex][toY] > energyTo[toX][toY])
          pathIndex = toX;
        else if (energyTo[pathIndex][toY] == energyTo[toX][toY] && pathIndex > toX)
          pathIndex = toX;
      }
    }
  }

  /**
   * Collects the pixels that form the most efficient seam through the
   * energy matrix.
   * @return array of pixels forming the seam
   */
  public int[] getPath() {
    int height = energyTo[0].length;
    int[] pathArray = new int[height];
    Pixel pixel = new Pixel(pathIndex, height - 1);
    pathArray[height - 1] = (pixel.getX());

    for (int i = height - 2; i >= 0; i--) {
      pixel = pathTo[pixel.getX()][pixel.getY()];
      pathArray[i] = pixel.getX();
    }

    return pathArray;
  }
}
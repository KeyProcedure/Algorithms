/**
 * Representation of a single pixel.
 * pixel (x, y) refers to the pixel in column x and row y,
 * with pixel (0, 0) in the upper left corner and pixel (W − 1, H − 1)
 * in the bottom right corner.
 */
public class Pixel {
  private final int x;
  private final int y;

  /**
   * Creates a pixel with x and y coordinates.
   * @param x x-coordinate of pixel
   * @param y y-coordinate of pixel
   */
  public Pixel(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the x-coordinate of the pixel.
   * @return x-coordinate of the pixel
   */
  public int getX() {
    return x;
  }

  /**
   * Retrieves the y-coordinate of the pixel.
   * @return y-coordinate of the pixel
   */
  public int getY() {
    return y;
  }
}
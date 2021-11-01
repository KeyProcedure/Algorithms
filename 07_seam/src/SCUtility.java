import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Dependencies: SeamCarver.java
 *
 * Utility functions for testing SeamCarver.java.
 */
public class SCUtility {

  /**
   * Creates a picture with a random width-by-height array of tiles.
   * @param width  width of the picture
   * @param height height of the picture
   * @return picture with randomly selected colors
   */
  public static Picture randomPicture(int width, int height) {
    Picture picture = new Picture(width, height);
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        int r = StdRandom.uniform(255);
        int g = StdRandom.uniform(255);
        int b = StdRandom.uniform(255);
        Color color = new Color(r, g, b);
        picture.set(col, row, color);
      }
    }
    return picture;
  }

  /**
   * Converts a SeamCarver object containing a picture into an array
   * reflecting the pixel's energy values.
   * @param sc SeamCarver object
   * @return array of energy values
   */
  public static double[][] toEnergyMatrix(SeamCarver sc) {
    double[][] returnDouble = new double[sc.width()][sc.height()];
    for (int col = 0; col < sc.width(); col++)
      for (int row = 0; row < sc.height(); row++)
        returnDouble[col][row] = sc.energy(col, row);
    return returnDouble;
  }

  /**
   * Displays gray values as energy (converts to picture, calls show).
   * @param sc SeamCarver object
   */
  public static void showEnergy(SeamCarver sc) {
    doubleToPicture(toEnergyMatrix(sc)).show();
  }

  /**
   * Converts a SeamCarver's picture to a picture containing the pixel's
   * energy values.
   * @param sc SeamCarver object
   * @return picture of energy values
   */
  public static Picture toEnergyPicture(SeamCarver sc) {
    double[][] energyMatrix = toEnergyMatrix(sc);
    return doubleToPicture(energyMatrix);
  }

  /**
   * Converts a double matrix of values into a normalized picture.
   * Values are normalized by the maximum grayscale value (ignoring border pixels).
   * @param grayValues matrix of energy values
   * @return greyscale picture representing the energy of pixel's
   */
  public static Picture doubleToPicture(double[][] grayValues) {

    // each 1D array in the matrix represents a single column, so the number
    // of 1D arrays is the width, and length of each array is the height
    int width = grayValues.length;
    int height = grayValues[0].length;

    Picture picture = new Picture(width, height);

    // maximum grayscale value (ignoring border pixels)
    double maxVal = 0;
    for (int col = 1; col < width - 1; col++) {
      for (int row = 1; row < height - 1; row++) {
        if (grayValues[col][row] > maxVal)
          maxVal = grayValues[col][row];
      }
    }

    if (maxVal == 0)
      return picture; // return black picture

    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++) {
        float normalizedGrayValue = (float) grayValues[col][row] / (float) maxVal;
        if (normalizedGrayValue >= 1.0f) normalizedGrayValue = 1.0f;
        picture.set(col, row, new Color(normalizedGrayValue, normalizedGrayValue, normalizedGrayValue));
      }
    }

    return picture;
  }

  /**
   * Overlays red pixels over the calculated seam. Due to the lack of a copy
   * constructor, it also alters the original picture.
   * @param picture     original picture
   * @param horizontal  orientation of seam
   * @param seamIndices array of seams indices
   * @return picture containing an overlay of pixels over the seam
   */
  public static Picture seamOverlay(Picture picture, boolean horizontal, int[] seamIndices) {
    Picture overlaid = new Picture(picture.width(), picture.height());
    int width = picture.width();
    int height = picture.height();

    for (int col = 0; col < width; col++)
      for (int row = 0; row < height; row++)
        overlaid.set(col, row, picture.get(col, row));

    // if horizontal seam, then set one pixel in every column
    if (horizontal) {
      for (int col = 0; col < width; col++)
        overlaid.set(col, seamIndices[col], Color.RED);
    }

    // if vertical, put one pixel in every row
    else {
      for (int row = 0; row < height; row++)
        overlaid.set(seamIndices[row], row, Color.RED);
    }
    return overlaid;
  }
}
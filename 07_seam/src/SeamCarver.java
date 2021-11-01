import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

/**
 * Dependencies: ShortestPath.java
 *
 * Implements a content-aware image resizing technique where the image is reduced
 * in size by one pixel of height (or width) at a time.
 * Computes the energy values of each pixel in a picture, finds a horizontal
 * or vertical seam of adjacent pixels containing the least amount of energy,
 * then removes the seam from the picture.
 */
public class SeamCarver {
  private int width;
  private int height;
  private Picture picture;
  private double[][] energyArray;

  /**
   * Creates a seam carver object based on the given picture.
   * @param picture picture to alter
   * @throws IllegalArgumentException if the picture is null
   */
  public SeamCarver(Picture picture) {
    if (picture == null)
      throw new IllegalArgumentException("The picture not been initialized.");

    this.picture = new Picture(picture);
    width = this.picture.width();
    height = this.picture.height();
    calculateEnergy();
  }

  /**
   * Calculates the energy value of each pixel.
   */
  private void calculateEnergy() {
    energyArray = new double[width][height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        energyArray[x][y] = energy(x, y);
      }
    }
  }

  /**
   * Retrieves the current picture.
   * @return current picture
   */
  public Picture picture() {
    Picture newPicture = new Picture(picture);
    return newPicture;
  }

  /**
   * Retrieves the width of the current picture.
   * @return width of the picture
   */
  public int width() {
    return width;
  }

  /**
   * Retrieves the height of the current picture.
   * @return height of the picture
   */
  public int height() {
    return height;
  }

  /**
   * Calculates the energy of the pixel at column x and row y by comparing the pixel's
   * gradient level to adjacent pixels.
   * See the specification sheet for the formula used to compute the energy.
   * @param x x-coordinate of the pixel
   * @param y y-coordinate of the pixel
   * @return energy value of the pixel
   */
  public double energy(int x, int y) {
    validateWidth(x);
    validateHeight(y);

    // pixels at the borders of the image have an energy value of 1000
    if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
      return 1000;

    int xGrade = xGradient(x, y);
    int yGrade = yGradient(x, y);
    return Math.sqrt(xGrade + yGrade);
  }

  /**
   * Validates the x-coordinate of the pixel.
   * @param x x-coordinate of the pixel
   * @throws IllegalArgumentException if the index x is out of bounds
   */
  private void validateWidth(int x) {
    if (x < 0 || x > width - 1)
      throw new IllegalArgumentException("Index (" + x + ") is out of bounds (" + width + ").");
  }

  /**
   * Validates the y-coordinate of the pixel.
   * @param y y-coordinate of the pixel
   * @throws IllegalArgumentException if the index y is out of bounds
   */
  private void validateHeight(int y) {
    if (y < 0 || y > height - 1)
      throw new IllegalArgumentException("Index (" + y + ") out of bounds (" + height + ").");
  }

  /**
   * Computes the x gradient of the pixel by comparing the gradient levels of the pixels
   * to the left and right of the pixel.
   * @param x x-coordinate of the pixel
   * @param y y-coordinate of the pixel
   * @return x gradient of the pixel
   */
  private int xGradient(int x, int y) {
    int rightPixel = picture.getRGB(x + 1, y);
    int leftPixel = picture.getRGB(x - 1, y);
    return gradient(rightPixel, leftPixel);
  }

  /**
   * Computes the y gradient of the pixel by comparing the gradient levels of the pixels
   * above and below the pixel.
   * @param x x-coordinate of the pixel
   * @param y y-coordinate of the pixel
   * @return y gradient of the pixel
   */
  private int yGradient(int x, int y) {
    int belowPixel = picture.getRGB(x, y + 1);
    int abovePixel = picture.getRGB(x, y - 1);
    return gradient(belowPixel, abovePixel);
  }

  /**
   * Computes the gradient difference between two pixels.
   * @param thisPixel a pixel being used to compute the gradient
   * @param thatPixel a pixel being used to compute the gradient
   * @return gradient level
   */
  private int gradient(int thisPixel, int thatPixel) {
    int redGradient = ((thisPixel >> 16) & 0x0ff) - ((thatPixel >> 16) & 0x0ff);
    int greenGradient = ((thisPixel >> 8) & 0x0ff) - ((thatPixel >> 8) & 0x0ff);
    int blueGradient = (thisPixel & 0x0ff) - (thatPixel & 0x0ff);
    return ((redGradient * redGradient) + (greenGradient * greenGradient) +
            (blueGradient * blueGradient));
  }

  /**
   * Computes a sequence of pixels constructing the vertical seam.
   * @return seam in the form of an array of pixels
   */
  public int[] findVerticalSeam() {
    ShortestPath path = new ShortestPath(energyArray, width, height);
    return path.getPath();
  }

  /**
   * Creates a new picture with the vertical seam removed from the current picture,
   * updates the energy matrix.
   * @param seam sequence of pixels to be removed
   * @throws IllegalArgumentException if the width of the picture is <= 1
   * @throws IllegalArgumentException if the seam of pixels is null
   * @throws IllegalArgumentException if the number of pixels in the seam array
   * is not the same as the number of pixels in the picture's height
   */
  public void removeVerticalSeam(int[] seam) {
    if (width <= 1)
      throw new IllegalArgumentException("Width of the current picture is less than or equal to 1.");

    if (seam == null)
      throw new IllegalArgumentException("Seam of pixels has not been initialized.");

    if (seam.length != height)
      throw new IllegalArgumentException("Seam of pixels is not the same length as the picture's height");

    validateVerticalSeam(seam);

    double[] newEnergyRow = new double[width - 1];
    Picture newPicture = new Picture(width - 1, height);
    Color[] newColorRow = new Color[width - 1];

    for (int index = 0; index < seam.length; index++) {
      for (int getI = 0, setI = 0; setI < width - 1; getI++, setI++) {
        if (getI != seam[index]) {
          newColorRow[setI] = picture.get(getI, index);
          newEnergyRow[setI] = energyArray[getI][index];
        }
        else if (seam[index] == getI && getI + 1 < width) {
          newColorRow[setI] = picture.get(++getI, index);
          newEnergyRow[setI] = energyArray[getI][index];
        }
      }

      for (int i = 0; i < width - 1; i++) {
        newPicture.set(i, index, newColorRow[i]);
        energyArray[i][index] = newEnergyRow[i];
      }
    }

    picture = new Picture(newPicture);
    width--;
    calcVerticalSeamEnergy(seam);
  }

  /**
   * Updates the energy matrix with the seam removed.
   * @param seam sequence of pixels to be removed
   */
  private void calcVerticalSeamEnergy(int[] seam) {
    for (int x = 0, y = 0; x < seam.length; x++, y++) {
      if (seam[x] == 0)
        energyArray[seam[x]][y] = energy(seam[x], y);
      else if (seam[x] == width)
        energyArray[seam[x] - 1][y] = energy((seam[x] - 1), y);
      else {
        energyArray[seam[x]][y] = energy(seam[x], y);
        energyArray[seam[x] - 1][y] = energy((seam[x] - 1), y);
      }
    }
  }

  /**
   * Validates the vertical seam by comparing the x-coordinates of each adjacent
   * pixel in the seam array.
   * @param seam sequence of pixels to be removed
   * @throws IllegalArgumentException if two pixels with adjacent seam array
   * indices are not adjacent in the picture
   */
  private void validateVerticalSeam(int[] seam) {
    int adjacent = seam[0];

    for (int seamPixel : seam) {
      validateWidth(seamPixel);
      if (Math.abs(seamPixel - adjacent) > 1)
        throw new IllegalArgumentException(
                "Two adjacent pixels (" + adjacent + ", " + seamPixel + ") differ by more than 1.");

      adjacent = seamPixel;
    }
  }

  /**
   * Computes a sequence of pixels constructing the horizontal seam.
   * @return seam in the form of an array of pixels
   */
  public int[] findHorizontalSeam() {
    double[][] tempEnergyArray = new double[height][width];
    for (int y = 0; y < width; y++) {
      for (int x = 0; x < height; x++) {
        tempEnergyArray[x][y] = energy(y, x);
      }
    }
    ShortestPath path = new ShortestPath(tempEnergyArray, height, width);
    return path.getPath();
  }

  /**
   * Creates a new picture with the horizontal seam removed from the current picture,
   * updates the energy matrix.
   * @param seam sequence of pixels to be removed
   * @throws IllegalArgumentException if the height of the picture is <= 1
   * @throws IllegalArgumentException if the seam of pixels is null
   * @throws IllegalArgumentException if the number of pixels in the seam array
   * is not the same as the number of pixels in the picture's width
   */
  public void removeHorizontalSeam(int[] seam) {
    if (height <= 1)
      throw new IllegalArgumentException("Height of the current picture is less than or equal to 1.");

    if (seam == null)
      throw new IllegalArgumentException("Seam of pixels has not been initialized.");

    if (seam.length != width)
      throw new IllegalArgumentException("Seam of pixels is not the same length as the picture's width");

    validateHorizontalSeam(seam);

    double[] newEnergyCol = new double[height - 1];
    Picture newPicture = new Picture(width, height - 1);
    Color[] newColorCol = new Color[height - 1];

    for (int index = 0; index < seam.length; index++) {
      for (int getI = 0, setI = 0; setI < height - 1; getI++, setI++) {
        if (getI != seam[index]) {
          newColorCol[setI] = picture.get(index, getI);
          newEnergyCol[setI] = energyArray[index][getI];
        }
        else if (getI == seam[index] && getI + 1 < height) {
          newColorCol[setI] = picture.get(index, ++getI);
          newEnergyCol[setI] = energyArray[index][getI];
        }
      }
      for (int i = 0; i < height - 1; i++) {
        newPicture.set(index, i, newColorCol[i]);
        energyArray[index][i] = newEnergyCol[i];
      }
    }

    picture = new Picture(newPicture);
    height--;
    calcHorizontalSeamEnergy(seam);
  }

  /**
   * Validates the horizontal seam by comparing the y-coordinates of each adjacent pixel
   * in the seam array.
   * @param seam sequence of pixels to be removed
   * @throws IllegalArgumentException if two pixels with adjacent seam array
   * indices are not adjacent in the picture
   */
  private void validateHorizontalSeam(int[] seam) {
    int adjacent = seam[0];
    for (int seamPixel : seam) {
      validateHeight(seamPixel);
      if (Math.abs(seamPixel - adjacent) > 1) {
        throw new IllegalArgumentException(
                "Two adjacent pixels (" + adjacent + ", " + seamPixel + ") differ by more than 1.");
      }
      adjacent = seamPixel;
    }
  }

  /**
   * Updates the energy matrix with the seam removed.
   * @param seam sequence of pixels to be removed
   */
  private void calcHorizontalSeamEnergy(int[] seam) {
    for (int x = 0, y = 0; y < seam.length; x++, y++) {
      if (seam[y] == 0)
        energyArray[x][seam[y]] = energy(x, seam[y]);
      else if (seam[y] == height)
        energyArray[x][seam[y] - 1] = energy(x, seam[y] - 1);
      else {
        energyArray[x][seam[y]] = energy(x, seam[y]);
        energyArray[x][seam[y] - 1] = energy(x, seam[y] - 1);
      }
    }
  }
}
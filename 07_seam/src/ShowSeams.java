import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

/**
 * Dependencies: SeamCarver.java SCUtility.java
 *
 * Read an image from a file.
 * Show 3 images (original image as well as horizontal and vertical seams of that image).
 * Each image hides the previous one - drag them to see all three.
 */
public class ShowSeams {

  /**
   * Displays the horizontal seam.
   * @param sc SeamCarver object
   */
  private static void showHorizontalSeam(SeamCarver sc) {
    Picture picture = SCUtility.toEnergyPicture(sc);
    int[] horizontalSeam = sc.findHorizontalSeam();
    Picture overlay = SCUtility.seamOverlay(picture, true, horizontalSeam);
    overlay.show();
  }

  /**
   * Displays the vertical seam.
   * @param sc SeamCarver object
   */
  private static void showVerticalSeam(SeamCarver sc) {
    Picture picture = SCUtility.toEnergyPicture(sc);
    int[] verticalSeam = sc.findVerticalSeam();
    Picture overlay = SCUtility.seamOverlay(picture, false, verticalSeam);
    overlay.show();
  }

  public static void main(String[] args) {
    final String fileName = "07_seam/test/chameleon.png"; // input test file

    Picture picture = new Picture(fileName);
    StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
    picture.show();
    SeamCarver sc = new SeamCarver(picture);

    StdOut.printf("Displaying horizontal seam calculated.\n");
    showHorizontalSeam(sc);

    StdOut.printf("Displaying vertical seam calculated.\n");
    showVerticalSeam(sc);
  }
}
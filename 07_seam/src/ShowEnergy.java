import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

/**
 * Dependencies: SeamCarver.java SCUtility.java
 *
 * Reads an image from a file.
 * Shows the original image and the image's energy values (only useful if image is large enough).
 *
 */
public class ShowEnergy {
  public static void main(String[] args) {
    final String fileName = "07_seam/test/chameleon.png"; // input test file
    Picture picture = new Picture(fileName);

    StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
    picture.show();
    SeamCarver sc = new SeamCarver(picture);
    StdOut.printf("Displaying energy calculated for each pixel.\n");
    SCUtility.showEnergy(sc);
  }
}
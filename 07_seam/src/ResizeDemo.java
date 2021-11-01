import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Dependencies: SeamCarver.java SCUtility.java
 *
 * Reads an image from a file. Uses SeamCarver to remove the number of rows and
 * columns specified by local variables.
 * Shows the images and prints the time elapsed to screen.
 */
public class ResizeDemo {
  public static void main(String[] args) {
    int removeColumns = 200;                               // number of cols to remove
    int removeRows = 100;                                  // number of rows to remove
    final String fileName = "07_seam/test/chameleon.png";  // input test file
    Picture inputImg = new Picture(fileName);

    StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
    SeamCarver sc = new SeamCarver(inputImg);

    Stopwatch sw = new Stopwatch();

    for (int i = 0; i < removeRows; i++) {
      int[] horizontalSeam = sc.findHorizontalSeam();
      sc.removeHorizontalSeam(horizontalSeam);
    }

    for (int i = 0; i < removeColumns; i++) {
      int[] verticalSeam = sc.findVerticalSeam();
      sc.removeVerticalSeam(verticalSeam);
    }
    Picture outputImg = sc.picture();

    StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

    StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
    inputImg.show();
    outputImg.show();
  }
}
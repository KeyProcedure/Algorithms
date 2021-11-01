import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Dependencies: PercolationVisualizer.java Percolation.java
 * StdDraw.java StdOut.java
 *
 * This program constructs an n dimension grid.
 * Sites are opened through mouse clicks on the grid.
 * After each site is opened, full sites are drawn in light blue,
 * open sites (that aren't full) in white, and blocked sites in black.
 */
public class InteractivePercolationVisualizer {

  /**
   * Creates a visual representation of the percolation grid, provides the
   * ability to open grid sites through mouse clicks.
   * @param args not used
   */
  public static void main(String[] args) {
    int n = 10; // n-by-n percolation system, default value is 10.

    // repeatedly open sites specified by mouse clicks and draw resulting system
    StdOut.println(n);

    StdDraw.enableDoubleBuffering();
    Percolation perc = new Percolation(n);
    PercolationVisualizer.draw(perc, n);
    StdDraw.show();

    while (true) {
      if (StdDraw.isMousePressed()) { // detected mouse click

        // screen coordinates
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();

        // convert to row i, column j
        int i = (int) (n - Math.floor(y));
        int j = (int) (1 + Math.floor(x));

        // open site (i, j) provided it's in bounds
        if (i >= 1 && i <= n && j >= 1 && j <= n) {
          if (!perc.isOpen(i, j))
            StdOut.println(i + " " + j);
          perc.open(i, j);
        }

        // draw n-by-n percolation system
        PercolationVisualizer.draw(perc, n);
        StdDraw.show();
      }
      StdDraw.pause(20);
    }
  }
}
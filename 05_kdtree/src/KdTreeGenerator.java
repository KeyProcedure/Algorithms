import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Creates n random points in the unit square and prints to standard output.
 */
public class KdTreeGenerator {

  public static void main(String[] args) {
    int n = 5;
    for (int i = 0; i < n; i++) {
      double x = StdRandom.uniform(0.0, 1.0);
      double y = StdRandom.uniform(0.0, 1.0);
      StdOut.printf("%8.6f %8.6f\n", x, y);
    }
  }
}
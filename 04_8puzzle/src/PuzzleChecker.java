import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Dependencies: Board.java Solver.java
 *
 * Creates an initial board from the file specified, and finds the minimum number
 * of moves to reach the goal state.
 */
public class PuzzleChecker {

  public static void main(String[] args) {

    // read in the board specified in the filename
    final String fileName = "04_8puzzle/test/puzzle10.txt"; // input test file
    In in = new In(fileName);
    int n = in.readInt();
    int[][] tiles = new int[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        tiles[i][j] = in.readInt();
      }
    }

    // solve the slider puzzle
    Board initial = new Board(tiles);
    Solver solver = new Solver(initial);
    StdOut.println("Moves: " + solver.moves());
  }
}
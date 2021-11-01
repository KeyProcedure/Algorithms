import edu.princeton.cs.algs4.Stack;

/**
 * A representation of an n-by-n board with sliding tiles.
 */
public class Board {
  private final int[][] tiles;
  private final int nSize;
  private int i0, j0;
  private int hammingCount;
  private int manhattanCount;

  /**
   * Creates a board from an n-by-n array of tiles,
   * where tiles[row][col] = tile at (row, col).
   * @param tiles a 2-dimensional array representing the tiles of the board
   */
  public Board(int[][] tiles) {
    this.nSize = tiles.length;
    this.tiles = new int[nSize][nSize];

    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        this.tiles[i][j] = tiles[i][j];
        if (this.tiles[i][j] == 0) {
          i0 = i;
          j0 = j;
        }
      }
    }
    calcHamming();
    calcManhattan();
  }

  /**
   * Calculates how close a board is to the goal board.
   * The Manhattan distance between a board and the goal board is the sum of the
   * Manhattan distances (sum of the vertical and horizontal distance) from the
   * current position of the tiles to their goal positions.
   */
  private void calcManhattan() {
    manhattanCount = 0;
    int tile;

    // for each tile, if it's not in the goal position, calculate the distance
    // from its current position to the goal position
    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        tile = tiles[i][j];
        if (tile != ((j + 1) + (i * nSize))) {
          manhattanCount += Math.abs(((tile - 1) / nSize) - i); // row distance
          manhattanCount += Math.abs(((tile - 1) % nSize) - j); // col distance
        }
      }
    }
    manhattanCount -= Math.abs((-1 / nSize) - i0);
    manhattanCount -= Math.abs((-1 % nSize) - j0);
  }

  /**
   * Retrieves the sum of Manhattan distances between the tiles and the goal board.
   * @return manhattan distance of board
   */
  public int manhattan() {
    return manhattanCount;
  }

  /**
   * Calculates how close a board is to the goal board.
   * The Hamming distance between a board and the goal board is the number of
   * tiles in the wrong position.
   */
  private void calcHamming() {
    hammingCount = 0;

    // for each tile, if it's not in the goal position, increment counter
    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        if (tiles[i][j] != (j + 1) + (i * nSize))
          hammingCount++;
      }
    }
    hammingCount--;
  }

  /**
   * Retrieves the number of tiles out of place.
   * @return hamming distance of the board
   */
  public int hamming() {
    return hammingCount;
  }

  /**
   * Retrieves the board dimension
   * @return size of the board
   */
  public int dimension() {
    return nSize;
  }

  /**
   * Determines whether the board is in the goal state, calculated using the
   * Manhattan distance.
   * @return true if the board has been solved, false otherwise
   */
  public boolean isGoal() {
    return manhattanCount == 0;
  }

  /**
   * Checks for equivalences between two boards by comparing their size and tile
   * positions.
   * @param board2 board to be compared
   * @return true if the boards are equivalent, false otherwise
   */
  public boolean equals(Object board2) {
    if (board2 == null)
      return false;

    if (board2.getClass() != this.getClass())
      return false;

    if (((Board) board2).nSize != this.nSize)
      return false;

    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        if (this.tiles[i][j] != ((Board) board2).tiles[i][j])
          return false;
      }
    }
    return true;
  }

  /**
   * Depending on the blank square, creates either 2, 3, or 4 neighboring boards
   * which reflect the boards that would result from moving the blank tile.
   * @return stack containing the neighboring boards
   */
  public Iterable<Board> neighbors() {
    Stack<Board> boardStack = new Stack<>();
    int swap;
    if (i0 + 1 < nSize) {
      swap = tiles[i0][j0];
      tiles[i0][j0] = tiles[i0 + 1][j0];
      tiles[i0 + 1][j0] = swap;
      Board board1 = new Board(tiles);
      boardStack.push(board1);
      tiles[i0 + 1][j0] = tiles[i0][j0];
      tiles[i0][j0] = 0;
    }
    if (j0 + 1 < nSize) {
      swap = tiles[i0][j0];
      tiles[i0][j0] = tiles[i0][j0 + 1];
      tiles[i0][j0 + 1] = swap;
      Board board2 = new Board(tiles);
      boardStack.push(board2);
      tiles[i0][j0 + 1] = tiles[i0][j0];
      tiles[i0][j0] = 0;
    }
    if (i0 - 1 >= 0) {
      swap = tiles[i0][j0];
      tiles[i0][j0] = tiles[i0 - 1][j0];
      tiles[i0 - 1][j0] = swap;
      Board board3 = new Board(tiles);
      boardStack.push(board3);
      tiles[i0 - 1][j0] = tiles[i0][j0];
      tiles[i0][j0] = 0;
    }
    if (j0 - 1 >= 0) {
      swap = tiles[i0][j0];
      tiles[i0][j0] = tiles[i0][j0 - 1];
      tiles[i0][j0 - 1] = swap;
      Board board4 = new Board(tiles);
      boardStack.push(board4);
      tiles[i0][j0 - 1] = tiles[i0][j0];
      tiles[i0][j0] = 0;
    }
    return boardStack;
  }

  /**
   * A board that is obtained by exchanging a single pair of tiles.
   * @return board with a single tile swapped
   */
  public Board twin() {
    int swap;
    if (i0 == 0) {
      swap = tiles[1][0];
      tiles[1][0] = tiles[1][1];
      tiles[1][1] = swap;
      Board twinBoard = new Board(tiles);
      swap = tiles[1][1];
      tiles[1][1] = tiles[1][0];
      tiles[1][0] = swap;
      return twinBoard;
    }
    else {
      swap = tiles[0][0];
      tiles[0][0] = tiles[0][1];
      tiles[0][1] = swap;
      Board twinBoard = new Board(tiles);
      swap = tiles[0][1];
      tiles[0][1] = tiles[0][0];
      tiles[0][0] = swap;
      return twinBoard;
    }
  }

  /**
   * String representation of this board.
   * @return string format of board
   */
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(nSize + "\n");
    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        s.append(String.format("%2d ", tiles[i][j]));
      }
      s.append("\n");
    }
    return s.toString();
  }
}
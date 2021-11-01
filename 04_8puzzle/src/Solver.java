import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;

/**
 * Dependencies: Board.java
 *
 * Implements an A* search algorithm using a priority queue.
 * Defines a search node of the game to be a board, the number of moves made to
 * reach the board, and the previous search node.
 * Inserts the initial search node (the initial board, 0 moves, and a null previous search node)
 * into a priority queue.
 * Then, deletes from the priority queue the search node with the minimum priority, and inserts onto
 * the priority queue all neighboring search nodes (those that can be reached in one move from the
 * dequeued search node).
 * Repeats this procedure until the search node dequeued corresponds to the goal board.
 */
public class Solver {
  private Stack<Board> boardStack = new Stack<>();
  private int moves = 0;

  /**
   * Inner node class.
   * Each node contains a board, the number of moves so far, the manhattan distance
   * to the goal state, and a reference to the previous node.
   */
  private static class Node {
    private Board board;
    private int numMoves;
    private int manhattan;
    private Node prev;
  }

  /**
   * Finds a solution to the initial board (using the A* algorithm).
   * Uses a twin board to determine whether the board is solvable.
   * @param initial board in the initial state
   * @throws IllegalArgumentException if the board is null
   */
  public Solver(Board initial) {
    if (initial == null)
      throw new IllegalArgumentException("Board has not been initialized.");

    Comparator<Node> comparator = new NodeComparator();

    // creates a node for the initial board, adds it to the priority queue
    MinPQ<Node> pQ = new MinPQ<>(comparator);
    Node pQNode = new Node();
    pQNode.board = initial;
    pQNode.numMoves = 0;
    pQNode.manhattan = pQNode.board.manhattan();
    pQNode.prev = null;
    pQ.insert(pQNode);

    // creates a node for the initial twin board, adds it to the priority queue
    MinPQ<Node> pQTwin = new MinPQ<>(comparator);
    Node pQNodeTwin = new Node();
    pQNodeTwin.board = pQNode.board.twin();
    pQNodeTwin.numMoves = 0;
    pQNodeTwin.manhattan = pQNodeTwin.board.manhattan();
    pQNodeTwin.prev = null;
    int movesTwin = 0;
    pQTwin.insert(pQNodeTwin);

    // while the current board or the twin board has not been solved
    while (!pQNode.board.isGoal() && !pQNodeTwin.board.isGoal()) {
      pQ.delMin();
      moves++;
      pQTwin.delMin();
      movesTwin++;

      // find a neighboring board that is not equivalent to the board's previous
      // position, add it to the priority queue
      for (Board boardElement : pQNode.board.neighbors()) {
        if (moves == 1 || !boardElement.equals(pQNode.prev.board)) {
          Node newNode = new Node();
          newNode.board = boardElement;
          newNode.numMoves = pQNode.numMoves + 1;
          newNode.manhattan = newNode.board.manhattan();
          newNode.prev = pQNode;
          pQ.insert(newNode);
        }
      }

      // find a twin neighboring board that is not equivalent to the twin board's
      // previous position, add it to the twin priority queue
      for (Board boardElement : pQNodeTwin.board.neighbors()) {
        if (movesTwin == 1 || !boardElement.equals(pQNodeTwin.prev.board)) {
          Node newNodeTwin = new Node();
          newNodeTwin.board = boardElement;
          newNodeTwin.numMoves = pQNodeTwin.numMoves + 1;
          newNodeTwin.manhattan = newNodeTwin.board.manhattan();
          newNodeTwin.prev = pQNodeTwin;
          pQTwin.insert(newNodeTwin);
        }
      }

      // retrieve the node with the smallest priority (closest to being solved) from the queue
      pQNode = pQ.min();
      pQNodeTwin = pQTwin.min();
    }

    // board is not solvable
    if (pQNodeTwin.board.isGoal())
      moves = -1;
    else {
      moves = pQNode.numMoves;
      while (pQNode != null) {
        boardStack.push(pQNode.board);
        pQNode = pQNode.prev;
      }
    }
  }

  /**
   * Compares the distance to the goal state of two boards.
   */
  private static class NodeComparator implements Comparator<Node> {
    public int compare(Node first, Node second) {
      if ((first.manhattan + first.numMoves) < (second.manhattan + second.numMoves))
        return -1;
      if ((first.manhattan + first.numMoves) > (second.manhattan + second.numMoves))
        return 1;
      return Integer.compare(first.manhattan, second.manhattan);
    }
  }

  /**
   * Checks whether the board is solvable.
   * @return true if the board can be solved, false otherwise
   */
  public boolean isSolvable() {
    return moves != -1;
  }

  /**
   * Retrieves the minimum number of moves to solve the initial board
   * @return number of moves required to solve the initial board, -1 if unsolvable
   */
  public int moves() {
    return moves;
  }

  /**
   * Retrieves the sequence of boards in the shortest solution.
   * @return stack of boards representing the path to the solution, null if unsolvable
   */
  public Iterable<Board> solution() {
    if (isSolvable())
      return boardStack;
    return null;
  }

  /**
   * Takes the name of an input file and prints the minimum number of moves to
   * solve the puzzle, and a corresponding solution.
   * @param args not used
   */
  public static void main(String[] args) {

    // read in the board specified in the filename
    final String fileName = "04_8puzzle/test/puzzle04.txt"; // input test file
    In in = new In(fileName);
    int n = in.readInt();

    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        tiles[i][j] = in.readInt();
      }
    }

    Board board = new Board(tiles);
    Solver solver = new Solver(board);

    if (solver.isSolvable()) {
      System.out.println("SOLUTION: ");
      for (Board boardElement : solver.solution()) {
        System.out.println(boardElement);
      }
    }

    System.out.println("Moves: " + solver.moves);
  }
}
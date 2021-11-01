import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Dependencies: StdRandom.java In.java StdOut.java
 *
 * A representation of a Boggle board.
 */
public class BoggleBoard {

  // the 16 Boggle dice (1992 version)
  private static final String[] BOGGLE_1992 = {
          "LRYTTE", "VTHRWE", "EGHWNE", "SEOTIS",
          "ANAEEG", "IDSYTT", "OATTOW", "MTOICU",
          "AFPKFS", "XLDERI", "HCPOAS", "ENSIEU",
          "YLDEVR", "ZNRNHL", "NMIQHU", "OBBAOJ"
  };

  // the 16 Boggle dice (1983 version)
  private static final String[] BOGGLE_1983 = {
          "AACIOT", "ABILTY", "ABJMOQ", "ACDEMP",
          "ACELRS", "ADENVZ", "AHMORS", "BIFORX",
          "DENOSW", "DKNOTU", "EEFHIY", "EGINTV",
          "EGKLUY", "EHINPS", "ELPSTU", "GILRUW",
  };

  // the 25 Boggle Master / Boggle Deluxe dice
  private static final String[] BOGGLE_MASTER = {
          "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
          "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
          "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR",
          "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
          "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
  };

  // the 25 Big Boggle dice
  private static final String[] BOGGLE_BIG = {
          "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
          "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCENST",
          "CEIILT", "CEILPT", "CEIPST", "DDHNOT", "DHHLOR",
          "DHLNOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
          "FIPRSY", "GORRVW", "IPRRRY", "NOOTUW", "OOOTTU"
  };


  // letters and frequencies of letters in the English alphabet
  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final double[] FREQUENCIES = {
          0.08167, 0.01492, 0.02782, 0.04253, 0.12703, 0.02228,
          0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025,
          0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987,
          0.06327, 0.09056, 0.02758, 0.00978, 0.02360, 0.00150,
          0.01974, 0.00074
  };

  private final int numRows;        // number of rows
  private final int numCols;        // number of columns
  private char[][] board;     // the m-by-n array of characters

  /**
   * Initializes a random 4-by-4 board, by rolling the Hasbro dice.
   */
  public BoggleBoard() {
    numRows = 4;
    numCols = 4;
    StdRandom.shuffle(BOGGLE_1992);
    board = new char[numRows][numCols];
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        String letters = BOGGLE_1992[numCols * row + col];
        int r = StdRandom.uniform(letters.length());
        board[row][col] = letters.charAt(r);
      }
    }
  }

  /**
   * Initializes a board from the given file name.
   * @param filename the name of the file containing the Boggle board
   * @throws IllegalArgumentException if the number of rows is <= 0
   * @throws IllegalArgumentException if the number of cols is <= 0
   * @throws IllegalArgumentException if the letter contains more than one char
   * @throws IllegalArgumentException if the letter is not in the alphabet
   */
  public BoggleBoard(String filename) {
    In in = new In(filename);
    numRows = in.readInt();
    numCols = in.readInt();
    if (numRows <= 0)
      throw new IllegalArgumentException("The number of rows must be a positive integer.");
    if (numCols <= 0)
      throw new IllegalArgumentException("The number of columns must be a positive integer.");
    board = new char[numRows][numCols];
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        String letter = in.readString().toUpperCase();
        if (letter.equals("QU"))
          board[row][col] = 'Q';
        else if (letter.length() != 1)
          throw new IllegalArgumentException("Invalid character: " + letter);
        else if (!ALPHABET.contains(letter))
          throw new IllegalArgumentException("Invalid character: " + letter);
        else
          board[row][col] = letter.charAt(0);
      }
    }
  }

  /**
   * Initializes a random rows-by-cols board, according to the frequency
   * of letters in the English language.
   * @param numRows the number of rows
   * @param numCols the number of columns
   * @throws IllegalArgumentException if the number of rows is <= 0
   * @throws IllegalArgumentException if the number of cols is <= 0
   */
  public BoggleBoard(int numRows, int numCols) {
    this.numRows = numRows;
    this.numCols = numCols;
    if (numRows <= 0)
      throw new IllegalArgumentException("Number of rows must be a positive integer.");
    if (numCols <= 0)
      throw new IllegalArgumentException("Number of columns must be a positive integer.");
    board = new char[numRows][numCols];
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        int r = StdRandom.discrete(FREQUENCIES);
        board[row][col] = ALPHABET.charAt(r);
      }
    }
  }

  /**
   * Initializes a board from the given 2d character array,
   * with 'Q' representing the two-letter sequence "Qu".
   * @param tiles 2d character array
   * @throws IllegalArgumentException if the number of rows is <= 0
   * @throws IllegalArgumentException if the number of cols is <= 0
   * @throws IllegalArgumentException if the width and height of the tiles array
   * is not the same
   * @throws IllegalArgumentException if a tile contains an invalid character
   */
  public BoggleBoard(char[][] tiles) {
    this.numRows = tiles.length;
    if (numRows == 0)
      throw new IllegalArgumentException("The number of rows must be a positive integer.");
    this.numCols = tiles[0].length;
    if (numCols == 0)
      throw new IllegalArgumentException("The number of columns must be a positive integer.");
    board = new char[numRows][numCols];
    for (int row = 0; row < numRows; row++) {
      if (tiles[row].length != numCols)
        throw new IllegalArgumentException("char[][] array is ragged.");
      for (int col = 0; col < numCols; col++) {
        if (ALPHABET.indexOf(tiles[row][col]) == -1)
          throw new IllegalArgumentException("Invalid character: " + tiles[row][col]);
        board[row][col] = tiles[row][col];
      }
    }
  }

  /**
   * Returns the number of rows.
   * @return number of rows
   */
  public int rows() {
    return numRows;
  }

  /**
   * Returns the number of columns.
   * @return number of columns
   */
  public int cols() {
    return numCols;
  }

  /**
   * Returns the letter in the specified row and column.
   * @param row the row
   * @param col the column
   * @return the letter in the specified row and column
   * with 'Q' representing the two-letter sequence "Qu".
   */
  public char getLetter(int row, int col) {
    return board[row][col];
  }

  /**
   * Returns a string representation of the board.
   * @return a string representation of the board, replacing 'Q' with "Qu"
   */
  public String toString() {
    StringBuilder sb = new StringBuilder(numRows + " " + numCols + "\n");
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        sb.append(board[row][col]);
        if (board[row][col] == 'Q') sb.append("u ");
        else sb.append("  ");
      }
      sb.append("\n");
    }
    return sb.toString().trim();
  }

  /**
   * Unit tests the BoggleBoard data type.
   */
  public static void main(String[] args) {

    // initialize a 4-by-4 board using Hasbro dice
    StdOut.println("Hasbro board:");
    BoggleBoard board1 = new BoggleBoard();
    StdOut.println(board1);
    StdOut.println();

    // initialize a 4-by-4 board using letter frequencies in English language
    StdOut.println("Random 4-by-4 board:");
    BoggleBoard board2 = new BoggleBoard(4, 4);
    StdOut.println(board2);
    StdOut.println();

    // initialize a 4-by-4 board from a 2d char array
    StdOut.println("4-by-4 board from 2D character array:");
    char[][] a = {
            {'D', 'O', 'T', 'Y'},
            {'T', 'R', 'S', 'F'},
            {'M', 'X', 'M', 'O'},
            {'Z', 'A', 'B', 'W'}
    };
    BoggleBoard board3 = new BoggleBoard(a);
    StdOut.println(board3);
    StdOut.println();

    // initialize a 4-by-4 board from a file
    String filename = "09_boggle/test/board-quinquevalencies.txt";
    StdOut.println("4-by-4 board from file " + filename + ":");
    BoggleBoard board4 = new BoggleBoard(filename);
    StdOut.println(board4);
    StdOut.println();
  }
}
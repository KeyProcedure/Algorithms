import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

/**
 * Dependencies: BoggleBoard.java TrieST26.java
 *
 * Implements a 26-way trie to find all valid words in a given Boggle board by
 * searching for valid prefixes using a given dictionary.
 */
public class BoggleSolver {
  private final TrieST26<Integer> dictionary;

  /**
   * Initializes the trie using the given array of strings as the dictionary.
   * Assumes that each word in the dictionary contains only the uppercase letters A through Z.
   * @param dictionary dictionary of words
   */
  public BoggleSolver(String[] dictionary) {
    this.dictionary = new TrieST26<>();
    int i = 0;
    for (String word : dictionary) {
      this.dictionary.put(word, i++);
    }
  }

  /**
   * Retrieves all the valid words consisting of the board's tiles.
   * @param board BoggleBoard
   * @return a set of all valid words
   */
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    if (board.cols() < 1 || board.rows() < 1)
      return null;

    HashSet<String> validWordSet = new HashSet<>();
    StringBuilder word;
    HashSet<Integer> usedTiles;
    for (int row = 0; row < board.rows(); row++) {
      for (int col = 0; col < board.cols(); col++) {
        word = new StringBuilder();
        usedTiles = new HashSet<>();
        searchBoard(board, validWordSet, usedTiles, word, row, col);
      }
    }
    return validWordSet;
  }

  /**
   * Recursively searches the BoggleBoard for possible words, traversing through
   * the tiles, searching the trie for prefix matches, and adding the words found
   * to the valid word set.
   * @param board BoggleBoard
   * @param validWordSet set of valid words
   * @param usedTileSet set of used tiles
   * @param word prefix/word to search for
   * @param row row of tile
   * @param col col of tile
   */
  private void searchBoard(BoggleBoard board, HashSet<String> validWordSet, HashSet<Integer> usedTileSet,
                           StringBuilder word, int row, int col) {
    char tile = board.getLetter(row, col);
    boolean qFlag = false;

    // check if tile has been used
    if (usedTileSet.contains(board.cols() * row + col))
      return;

    // add tile character to current string
    word.append(tile);
    if (tile == 'Q') {
      qFlag = true;
      word.append("U");
    }

    // add tile to used tile list
    usedTileSet.add(board.cols() * row + col);

    String wordToString = word.toString();
    if (dictionary.hasPrefix(wordToString)) {
      if (wordToString.length() >= 3 && dictionary.contains(wordToString))
        validWordSet.add(wordToString);

      if (row + 1 < board.rows())
        searchBoard(board, validWordSet, usedTileSet, word, row + 1, col);

      if (col + 1 < board.cols())
        searchBoard(board, validWordSet, usedTileSet, word, row, col + 1);

      if (row - 1 >= 0)
        searchBoard(board, validWordSet, usedTileSet, word, row - 1, col);

      if (col - 1 >= 0)
        searchBoard(board, validWordSet, usedTileSet, word, row, col - 1);

      if (row - 1 >= 0 && col - 1 >= 0)
        searchBoard(board, validWordSet, usedTileSet, word, row - 1, col - 1);

      if (row - 1 >= 0 && col + 1 < board.cols())
        searchBoard(board, validWordSet, usedTileSet, word, row - 1, col + 1);

      if (row + 1 < board.rows() && col - 1 >= 0)
        searchBoard(board, validWordSet, usedTileSet, word, row + 1, col - 1);

      if (row + 1 < board.rows() && col + 1 < board.cols())
        searchBoard(board, validWordSet, usedTileSet, word, row + 1, col + 1);

    }

    // remove current tile from used tile list
    usedTileSet.remove(board.cols() * row + col);

    // remove current tile character from the word string
    word.deleteCharAt(word.length() - 1);

    // if tiles is QU, remove another character from the word string
    if (qFlag)
      word.deleteCharAt(word.length() - 1);
  }

  /**
   * Determines the score of a word, if it exists in the dictionary.
   * Assumes the word contains only the uppercase letters A through Z.
   * @param word word to score
   * @return score of the word, 0 if it is not in the dictionary
   */
  public int scoreOf(String word) {
    if (!dictionary.contains(word))
      return 0;
    int length = word.length();
    switch (length) {
      case 3:
      case 4:
        return 1;
      case 5:
        return 2;
      case 6:
        return 3;
      case 7:
        return 5;
    }
    if (length >= 8)
      return 11;
    return 0;
  }

  /**
   * Reads in a dictionary of words and the board to be used.
   * Prints out all valid words for the given board using the given dictionary.
   * @param args not used
   */
  public static void main(String[] args) {
    In in = new In("09_boggle/test/dictionary-common.txt");
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard("09_boggle/test/board4x4.txt");

    System.out.println(board);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}
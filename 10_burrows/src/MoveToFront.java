import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.HashMap;

/**
 * Converts a given text file in which sequences of the same character occur
 * near each other many times, into a text file in which certain characters
 * appear much more frequently than others.
 * The main idea of move-to-front encoding is to maintain an ordered sequence of
 * the characters in the alphabet by repeatedly reading a character from the
 * input message; printing the position in the sequence in which that character
 * appears; and moving that character to the front of the sequence.
 */
public class MoveToFront {

  /**
   * Applies move-to-front encoding, reading from standard input and writing to
   * standard output.
   * Maintains an ordered sequence of the 256 extended ASCII characters.
   * Initializes the sequence by making the ith character in the sequence equal
   * to the ith extended ASCII character.
   * Reads each 8-bit character c from standard input, one at a time; outputs the
   * 8-bit index in the sequence where c appears; and moves c to the front.
   */
  public static void encode() {
    HashMap<Character, Integer> charMap = new HashMap<>();
    HashMap<Integer, Character> indexMap = new HashMap<>();
    for (int i = 0; i < 256; i++) {
      charMap.put((char) i, i);
      indexMap.put(i, (char) i);
    }
    char currentChar;
    char tempChar;
    int tempIndex;
    int currentCharIndex;

    while (!BinaryStdIn.isEmpty()) {
      currentChar = BinaryStdIn.readChar();
      currentCharIndex = charMap.get(currentChar);
      BinaryStdOut.write((char) currentCharIndex);

      for (int i = 1; i < currentCharIndex + 1; i++) {
        tempChar = indexMap.get(currentCharIndex - i);
        tempIndex = charMap.get(tempChar);
        charMap.replace(tempChar, tempIndex + 1);
        indexMap.replace(tempIndex + 1, tempChar);
      }
      charMap.replace(currentChar, 0);
      indexMap.replace(0, currentChar);
    }
    BinaryStdOut.close();
  }

  /**
   * Applies move-to-front decoding, reading from standard input and writing to
   * standard output.
   * Initializes an ordered sequence of 256 characters, where extended ASCII
   * character i appears ith in the sequence.
   * Reads each 8-bit character i from standard input one at a time;
   * writes the ith character in the sequence; and moves that character to the front.
   */
  public static void decode() {
    HashMap<Character, Integer> charMap = new HashMap<>();
    HashMap<Integer, Character> indexMap = new HashMap<>();
    for (int i = 0; i < 256; i++) {
      charMap.put((char) i, i);
      indexMap.put(i, (char) i);
    }
    char currentChar;
    char tempChar;
    int tempIndex;
    int currentCharIndex;

    while (!BinaryStdIn.isEmpty()) {
      currentCharIndex = BinaryStdIn.readChar();
      currentChar = indexMap.get(currentCharIndex);
      BinaryStdOut.write(currentChar);

      for (int i = 1; i < currentCharIndex + 1; i++) {
        tempChar = indexMap.get(currentCharIndex - i);
        tempIndex = charMap.get(tempChar);
        charMap.replace(tempChar, tempIndex + 1);
        indexMap.replace(tempIndex + 1, tempChar);
      }
      charMap.replace(currentChar, 0);
      indexMap.replace(0, currentChar);
    }
    BinaryStdOut.close();
  }

  /**
   * Encodes or decodes characters of a text file.
   *
   * To run:
   * java-algs4 MoveToFront - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
   * java-algs4 MoveToFront - < abra.txt | java-algs4 MoveToFront +
   *
   * @param args "-" to apply move-to-front encoding
   *             "+" to apply move-to-front decoding
   */
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      encode();
    } else if (args[0].equals("+")) {
      decode();
    }
  }
}
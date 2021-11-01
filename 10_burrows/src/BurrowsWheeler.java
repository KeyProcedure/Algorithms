import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.HashMap;

/**
 * Dependencies: CircularSuffixArray.java
 *
 * Implements the Burrows–Wheeler data compression algorithm.
 * Transforms text into a form that is more amenable for compression, by rearranging
 * the characters in the input so that there are lots of clusters with repeated characters,
 * but in such a way that it is still possible to recover the original input.
 * Given a typical English text file, transforms it into a text file in which
 * sequences of the same character occur near each other many times.
 */
public class BurrowsWheeler {

  /**
   * Applies Burrows-Wheeler transform.
   * Reads from standard input and writing to standard output.
   */
  public static void transform() {
    StringBuilder inputString = new StringBuilder();
    while (!BinaryStdIn.isEmpty())
      inputString.append(BinaryStdIn.readString());

    CircularSuffixArray suffixArray = new CircularSuffixArray(inputString.toString());
    StringBuilder outputString = new StringBuilder();
    int index;
    int first = -1;

    for (int i = 0; i < suffixArray.length(); i++) {
      index = suffixArray.length() - 1 + suffixArray.index(i);
      if (index >= inputString.length())
        index -= inputString.length();
      else if (index == suffixArray.length() - 1)
        first = i;
      outputString.append(inputString.charAt(index));
    }
    BinaryStdOut.write(first);
    BinaryStdOut.write(outputString.toString());
    BinaryStdOut.close();
  }


  /**
   * Applies Burrows-Wheeler inverse transform.
   * Reads from standard input and writing to standard output.
   */
  public static void inverseTransform() {
    HashMap<String, Queue<Integer>> tMap = new HashMap<>();
    StringBuilder inputString = new StringBuilder();
    int first = BinaryStdIn.readInt();

    while (!BinaryStdIn.isEmpty())
      inputString.append(BinaryStdIn.readString());

    for (int i = 0; i < inputString.length(); i++) {
      if (tMap.get(String.valueOf(inputString.charAt(i))) == null) {
        Queue<Integer> indexQueue = new Queue<>();
        indexQueue.enqueue(i);
        tMap.put(String.valueOf(inputString.charAt(i)), indexQueue);
      }
      else {
        tMap.get(String.valueOf(inputString.charAt(i))).enqueue(i);
      }
    }

    String[] firstCol = countSort(inputString);

    int[] next = new int[firstCol.length];
    for (int i = 0; i < firstCol.length; i++) {
      next[i] = tMap.get(firstCol[i]).dequeue();
    }

    StringBuilder rebuiltSb = new StringBuilder(firstCol.length);
    rebuiltSb.append(firstCol[first]);
    int newFirst = next[first];
    for (int i = 0; i < firstCol.length - 1; i++) {
      rebuiltSb.append(firstCol[newFirst]);
      newFirst = next[newFirst];
    }

    BinaryStdOut.write(rebuiltSb.toString());
    BinaryStdOut.close();
  }

  /**
   * Radix sorts the input string.
   * @param inputString string to sort
   * @return sorted array characters
   */
  private static String[] countSort(StringBuilder inputString) {
    int radix = 256;
    int n = inputString.length();
    String[] aux = new String[n];
    int[] count = new int[radix + 1];

    for (int i = 0; i < n; i++)
      count[(int) inputString.charAt(i) + 1]++;
    for (int r = 0; r < radix; r++)
      count[r + 1] += count[r];
    for (int i = 0; i < n; i++)
      aux[count[(int) inputString.charAt(i)]++] = String.valueOf(inputString.charAt(i));
    return aux;
  }

  /**
   * Transforms text by applying Burrows-Wheeler transform and inverse transform.
   *
   * To run:
   * java-algs4 BurrowsWheeler - < abra.txt | java-algs4 BurrowsWheeler +
   *
   * @param args "-" to apply Burrows-Wheeler transform
   *             "+" to apply Burrows-Wheeler inverse transform
   */
  public static void main(String[] args) {
    if (args[0].equals("-"))
      transform();
    else if (args[0].equals("+"))
      inverseTransform();
  }
}
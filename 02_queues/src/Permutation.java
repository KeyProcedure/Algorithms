import edu.princeton.cs.algs4.In;
import java.util.Iterator;

/**
 * Dependencies: RandomizedQueue.java
 *
 * Reads a sequence of strings from a file; and prints exactly k of them,
 * uniformly at random. Prints each item from the sequence at most once.
 */
public class Permutation {

  public static void main(String[] args) {

    // number of strings to output, default value is 10
    int numStrings = 10;
    final String fileName = "02_queues/test/mediumTale.txt"; // input test file
    In in = new In(fileName);

    RandomizedQueue<String> randomizedQ = new RandomizedQueue<>();

    while (!in.isEmpty()) {
      randomizedQ.enqueue(in.readString());
    }

    Iterator<String> iterator = randomizedQ.iterator();

    while (iterator.hasNext() && numStrings > 0) {
      System.out.println(iterator.next());
      numStrings--;
    }
  }
}
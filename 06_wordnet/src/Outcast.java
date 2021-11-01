import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Dependencies: WordNet.java
 *
 * Given a list of WordNet nouns x1, x2, ..., xn, finds which noun is the least
 * related to the others.
 * Identifies an outcast by computing the sum of the distances between each noun
 * and every other one:
 * di = distance(xi, x1) + distance(xi, x2) + ... + distance(xi, xn)
 * and returns a noun xt for which dt is maximum.
 */
public class Outcast {
  private final WordNet wordNet;

  /**
   * Initializes the local wordNet object to the input wordNet.
   * Assumes that argument to contain only valid wordnet nouns
   * (and that it contains at least two nouns).
   * @param wordnet wordNet object
   */
  public Outcast(WordNet wordnet) {
    this.wordNet = wordnet;
  }

  /**
   * Searches an array of nouns to find an outcast.
   * @param nouns array of nouns to search
   * @return outcast found
   */
  public String outcast(String[] nouns) {
    int arraySize = nouns.length;
    int index = 0;
    int count = 0;
    Integer[] distanceArray = new Integer[arraySize];

    for (int i = 0; i < arraySize; i++) {
      distanceArray[i] = 0;

      for (int j = 0; j < arraySize; j++) {
        if (i != j)
          distanceArray[i] += wordNet.distance(nouns[i], nouns[j]);
      }

      if (distanceArray[i] > count) {
        index = i;
        count = distanceArray[i];
      }
    }
    return nouns[index];
  }

  /**
   * Takes the name of a synset file, the name of a hypernym file,
   * followed by the name of an outcast file, and prints the outcast.
   * @param args not used
   */
  public static void main(String[] args) {
    final String synsetFile = "06_wordnet/test/synsets.txt";     // input test file
    final String hypernymFile = "06_wordnet/test/hypernyms.txt"; // input test file
    final String outcastFile = "06_wordnet/test/outcast5.txt";   // input test file

    WordNet wordnet = new WordNet(synsetFile, hypernymFile);
    Outcast outcast = new Outcast(wordnet);

    In in = new In(outcastFile);
    String[] nouns = in.readAllStrings();
    StdOut.println(outcastFile + ": " + outcast.outcast(nouns));
  }
}
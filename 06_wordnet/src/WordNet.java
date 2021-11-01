import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import java.util.HashMap;

/**
 * Dependencies: SAP.java
 *
 * Implements a rooted digraph in which each vertex v is an integer that represents
 * a synset (a set of synonyms), and each directed edge v→w represents that w is
 * a hypernym (more general synset) of v.
 * The digraph is acyclic and has one vertex, the root, that is an ancestor of every other vertex
 */
public class WordNet {
  private final SAP sapClass;
  private final Digraph digraph;
  private final HashMap<Integer, Bag<String>> synsetsMap;
  private final HashMap<String, Bag<Integer>> nounsMap;
  private int numOfSynsets = 0;

  /**
   * Initializes the data structures, reads in the files, and checks for cycles.
   * @param synsets file containing synsets
   * @param hypernyms file containing hypernyms
   * @throws IllegalArgumentException if either file name is null
   * @throws IllegalArgumentException if a cycle is detected
   */
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null)
      throw new IllegalArgumentException("The file name of synsets is not valid.");
    if (hypernyms == null)
      throw new IllegalArgumentException("The file name of hypernyms is not valid.");

    synsetsMap = new HashMap<>();
    nounsMap = new HashMap<>();
    readSynsets(synsets);
    digraph = new Digraph(numOfSynsets);
    readHypernyms(hypernyms);

    DirectedCycle cycle = new DirectedCycle(digraph);
    if (cycle.hasCycle())
      throw new IllegalArgumentException("A cycle has been detected in the digraph.");

    sapClass = new SAP(digraph);
  }

  /**
   * Reads in synsets and their corresponding IDs from the file, stores data in
   * hash maps.
   * @param fileName name of file to be read
   */
  private void readSynsets(String fileName) {
    In inputFile = new In(fileName);
    String line;
    int synsetId;

    while (inputFile.hasNextLine()) {
      line = inputFile.readLine();
      String[] stringArray = line.split(",");
      synsetId = Integer.parseInt(stringArray[0]);
      Bag<String> bagOfNouns = new Bag<>();

      for (String noun : stringArray[1].split(" ")) {
        bagOfNouns.add(noun);
        if (nounsMap.containsKey(noun))
          nounsMap.get(noun).add(synsetId);
        else {
          Bag<Integer> valuesBag = new Bag<>();
          valuesBag.add(synsetId);
          nounsMap.put(noun, valuesBag);
        }
      }
      synsetsMap.put(synsetId, bagOfNouns);
      numOfSynsets++;
    }
  }

  /**
   * Reads in hypernym IDs from a file, creates synset edges of the digraph,
   * verifies that a single root exists.
   * @param fileName name of file to be read
   * @throws IllegalArgumentException if the digraph does not contain exactly
   * one root
   */
  private void readHypernyms(String fileName) {
    In inputFile = new In(fileName);
    String line;
    int synsetId;
    int root = 0;

    while (inputFile.hasNextLine()) {
      line = inputFile.readLine();
      String[] stringArray = line.split(",");
      synsetId = Integer.parseInt(stringArray[0]);
      Bag<Integer> valuesBag = new Bag<>();

      for (int i = 1; i < stringArray.length; i++) {
        valuesBag.add(Integer.valueOf(stringArray[i]));
        digraph.addEdge(synsetId, Integer.parseInt(stringArray[i]));
      }
      if (valuesBag.isEmpty())
        root++;
    }
    if (root != 1)
      throw new IllegalArgumentException("The digraph does not contain exactly 1 root.");
  }

  /**
   * Creates a stack of all the nouns in the hash map.
   * @return all WordNet nouns
   */
  public Iterable<String> nouns() {
    Stack<String> nounStack = new Stack<>();

    for (String noun : nounsMap.keySet())
      nounStack.push(noun);

    return nounStack;
  }

  /**
   * Verifies that the word is a WordNet noun.
   * @param word String to verify
   * @return true if the word exists in the map, false otherwise
   * @throws IllegalArgumentException if the word does not exist in the digraph
   */
  public boolean isNoun(String word) {
    if (nounsMap.get(word) == null)
      throw new IllegalArgumentException(word + " is not a WordNet noun.");
    return nounsMap.get(word) != null;
  }

  /**
   * Calculates the distance (using the shortest ancestral path) between nounA and nounB.
   * @param nounA a noun
   * @param nounB a noun
   * @return distance between the two nouns
   */
  public int distance(String nounA, String nounB) {
    isNoun(nounA);
    isNoun(nounB);
    return (sapClass.length(nounsMap.get(nounA), nounsMap.get(nounB)));
  }

  /**
   * Finds a synset (second field of synsets.txt) that is the common ancestor of
   * nounA and nounB in the shortest ancestral path.
   * @param nounA a noun
   * @param nounB a noun
   * @return synset that is the common ancestor of nounA and nounB
   */
  public String sap(String nounA, String nounB) {
    isNoun(nounA);
    isNoun(nounB);
    StringBuilder synset = new StringBuilder();
    int ancestor = sapClass.ancestor(nounsMap.get(nounA), nounsMap.get(nounB));

    if (ancestor == -1)
      return "No Common Ancestor";

    for (String noun : synsetsMap.get(ancestor))
      synset.append(noun).append(" ");

    return (synset.toString());
  }
}
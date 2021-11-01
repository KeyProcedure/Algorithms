import edu.princeton.cs.algs4.Queue;

/**
 * An implementation of an R-way trie.
 * Stores a string symbol table for words found in the english dictionary,
 * with string keys and integer values.
 */
public class TrieST26<Integer> {
  private static final int R = 26;        // A-Z
  private Node root;                      // root of trie
  private int numKeys;                    // number of keys in trie

  /**
   * Inner R-way trie node class
   */
  private static class Node {
    private Object val;
    private Node[] next = new Node[R];
  }

  /**
   * Initializes an empty string trie.
   */
  public TrieST26() {}

  /**
   * Inserts the key-value pair into the trie, overwriting the old value
   * with the new value if the key is already in the trie.
   * If the value is null, effectively deletes the key from the trie.
   * @param key string to be inserted
   * @param val integer value associated with key
   * @throws IllegalArgumentException if the key is null
   */
  public void put(String key, Integer val) {
    if (key == null)
      throw new IllegalArgumentException("Key has not been initialized.");
    if (val == null)
      delete(key);
    else
      root = put(root, key, val, 0);
  }

  /**
   * Recursively inserts key-value pairs into the trie.
   * Each node implicitly contains the value of one character, based on the
   * link (each node contains 26 links representing A-Z) from its parent's node.
   * @param xNode root node
   * @param key string to be inserted
   * @param val integer value associated with key
   * @param charIndex character index
   * @return root node
   */
  private Node put(Node xNode, String key, Integer val, int charIndex) {
    if (xNode == null)
      xNode = new Node();
    if (charIndex == key.length()) {
      if (xNode.val == null)
        numKeys++;
      xNode.val = val;
      return xNode;
    }
    char c = key.charAt(charIndex);

    // char - 65 converts ascii to letters A-Z
    xNode.next[c - 65] = put(xNode.next[c - 65], key, val, charIndex + 1);
    return xNode;
  }


  /**
   * Determines whether the trie contains a word with the prefix specified.
   * @param prefix string prefix to validate
   * @return true if the prefix exists in the trie, false otherwise
   */
  public boolean hasPrefix(String prefix) {
    Node x = get(root, prefix, 0);
    Queue<String> results = new Queue<>();
    findPrefix(x, new StringBuilder(prefix), results);
    return !results.isEmpty();
  }

  /**
   * Recursively traverses the trie searching for words that contain the
   * specified prefix.
   * @param xNode root node
   * @param prefix prefix to validate
   * @param results a word containing the prefix found in the trie
   */
  private void findPrefix(Node xNode, StringBuilder prefix, Queue<String> results) {
    if (xNode == null)
      return;

    if (xNode.val != null)
      results.enqueue(prefix.toString());

    // for each character A-Z
    for (char c = 65; c <= 90; c++) {

      //prefix found
      if (!results.isEmpty())
        return;

      // add a letter to the prefix, continue the search for a valid word
      prefix.append(c);
      findPrefix(xNode.next[c - 65], prefix, results);
      prefix.deleteCharAt(prefix.length() - 1);
    }
  }

  /**
   * Determines whether this trie contains the given key.
   * @param key string to find
   * @return true if this trie contains the key, false otherwise
   * @throws IllegalArgumentException if the key is null
   */
  public boolean contains(String key) {
    if (key == null)
      throw new IllegalArgumentException("Key has not been initialized.");
    return get(key) != null;
  }

  /**
   * Retrieves the value associated with the given key.
   * @param key string to retrieve
   * @return the value associated with the given key if the key is in the trie,
   *     null if the key is not in the trie
   * @throws IllegalArgumentException if the key is null
   */
  public Object get(String key) {
    if (key == null)
      throw new IllegalArgumentException("Key has not been initialized.");
    Node xNode = get(root, key, 0);
    if (xNode == null)
      return null;
    return xNode.val;
  }

  /**
   * Recursively searches the trie for the key.
   * @param xNode root node
   * @param key string to retrieve
   * @param charIndex character index
   * @return node containing the value if found, null otherwise
   */
  private Node get(Node xNode, String key, int charIndex) {
    if (xNode == null)
      return null;
    if (charIndex == key.length())
      return xNode;
    char c = key.charAt(charIndex);
    return get(xNode.next[c - 65], key, charIndex + 1);
  }

  /**
   * Retrieves the size of the trie.
   * @return number of keys in the trie
   */
  public int size() {
    return numKeys;
  }

  /**
   * Verifies that the trie is empty.
   * @return true if the trie is empty, false otherwise
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Retrieves all keys in the trie as an Iterable.
   * To iterate over all the keys in the trie named st,
   * use the foreach notation: for (Key key : st.keys()).
   * @return all keys in the trie as an Iterable
   */
  public Iterable<String> keys() {
    return keysWithPrefix("");
  }

  /**
   * Retrieves all keys that have a matching prefix with the prefix specified.
   * @param prefix prefix of string to validate and match
   * @return all keys in the trie with matching prefixes, as an Iterable
   */
  public Iterable<String> keysWithPrefix(String prefix) {
    Queue<String> results = new Queue<String>();
    Node xNode = get(root, prefix, 0);
    collect(xNode, new StringBuilder(prefix), results);
    return results;
  }

  /**
   * Retrieves all the strings that match the specified prefix.
   * @param xNode root node
   * @param prefix prefix of string to match
   * @param results queue containing the strings with matched prefixes
   */
  private void collect(Node xNode, StringBuilder prefix, Queue<String> results) {
    if (xNode == null)
      return;

    if (xNode.val != null)
      results.enqueue(prefix.toString());

    for (char c = 65; c <= 90; c++) {
      prefix.append(c);
      collect(xNode.next[c - 65], prefix, results);
      prefix.deleteCharAt(prefix.length() - 1);
    }
  }

  /**
   * Returns all the keys in the trie that match the pattern,
   * where . symbol is treated as a wildcard character.
   * @param pattern string to match
   * @return an iterable containing all the keys in the trie that match the pattern
   */
  public Iterable<String> keysThatMatch(String pattern) {
    Queue<String> results = new Queue<String>();
    collect(root, new StringBuilder(), pattern, results);
    return results;
  }

  /**
   * Recursively retrieves all the strings that match the specified prefix and the pattern.
   * @param xNode root node
   * @param prefix prefix of string to match
   * @param pattern pattern to match
   * @param results queue containing the strings with matched prefixes
   */
  private void collect(Node xNode, StringBuilder prefix, String pattern, Queue<String> results) {
    if (xNode == null)
      return;
    int charIndex = prefix.length();
    if (charIndex == pattern.length() && xNode.val != null)
      results.enqueue(prefix.toString());
    if (charIndex == pattern.length())
      return;
    char c = pattern.charAt(charIndex);

    if (c == '.') {
      for (char ch = 0; ch < R; ch++) {
        prefix.append(ch);
        collect(xNode.next[ch], prefix, pattern, results);
        prefix.deleteCharAt(prefix.length() - 1);
      }
    }
    else {
      prefix.append(c);
      collect(xNode.next[c - 65], prefix, pattern, results);
      prefix.deleteCharAt(prefix.length() - 1);
    }
  }

  /**
   * Retrieves the string in the trie that is the longest prefix of the query string.
   * @param query query string
   * @return string in the trie that is the longest prefix of the query string,
   * or null if no such string exists.
   * @throws IllegalArgumentException if query is null
   */
  public String longestPrefixOf(String query) {
    if (query == null)
      throw new IllegalArgumentException("Query string has not been initialized.");
    int length = longestPrefixOf(root, query, 0, -1);
    if (length == -1)
      return null;
    else
      return query.substring(0, length);
  }

  /**
   * Recursively searches and retrieves the length of the longest string key in
   * the sub-trie, rooted at xNode that is a prefix of the query string,
   * assuming the first index character match, and a prefix match of given length
   * is already found.
   * @param xNode root node
   * @param query string to match
   * @param charIndex character index
   * @param length length of the prefix
   * @return length of the longest prefix matching the query string,-1 if no match
   */
  private int longestPrefixOf(Node xNode, String query, int charIndex, int length) {
    if (xNode == null)
      return length;
    if (xNode.val != null)
      length = charIndex;
    if (charIndex == query.length())
      return length;
    char c = query.charAt(charIndex);
    return longestPrefixOf(xNode.next[c - 65], query, charIndex + 1, length);
  }

  /**
   * Removes the key from the set if the key is present.
   * @param key string to remove
   * @throws IllegalArgumentException if the key is null
   */
  public void delete(String key) {
    if (key == null)
      throw new IllegalArgumentException("Key has not been initialized.");
    root = delete(root, key, 0);
  }

  /**
   * Recursively searches the trie and deletes the string specified.
   * @param xNode root node
   * @param key string to remove
   * @param charIndex character index
   * @return root node
   */
  private Node delete(Node xNode, String key, int charIndex) {
    if (xNode == null)
      return null;
    if (charIndex == key.length()) {
      if (xNode.val != null)
        numKeys--;
      xNode.val = null;
    }
    else {
      char c = key.charAt(charIndex);
      xNode.next[c - 65] = delete(xNode.next[c - 65], key, charIndex + 1);
    }

    // remove sub-trie rooted at xNode if it is completely empty
    if (xNode.val != null)
      return xNode;
    for (int c = 0; c < R; c++)
      if (xNode.next[c] != null)
        return xNode;
    return null;
  }
}
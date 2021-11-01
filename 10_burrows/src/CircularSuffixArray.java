/**
 * Dependencies: Quick3Str.java
 *
 * Implements a circular suffix array, which describes the abstraction of a
 * sorted array of the n circular suffixes of a string of length n.
 */
public class CircularSuffixArray {
  private Integer[] indexArray;

  /**
   * Creates a circular suffix array of the string specified.
   * @param originalString string used to create suffix array
   * @throws IllegalArgumentException if the input string is null
   */
  public CircularSuffixArray(String originalString) {
    if (originalString == null)
      throw new IllegalArgumentException("String has not been initialized.");

    indexArray = new Integer[originalString.length()];
    for (int i = 0; i < indexArray.length; i++) {
      indexArray[i] = i;
    }
    Quick3Str.sort(originalString, indexArray);
  }

  /**
   * Retrieves the length of the string.
   * @return length of the original string
   */
  public int length() {
    return indexArray.length;
  }

  /**
   * Retrieves the index of ith sorted suffix.
   * index[11] = 2 means that the 2nd original suffix appears 11th in the sorted order.
   * @param i index to retrieve
   * @return index suffix array where the original string appears
   */
  public int index(int i) {
    if (i < 0 || i > indexArray.length - 1)
      throw new IllegalArgumentException("Index is out of bounds.");

    return indexArray[i];
  }

  /**
   * Creates a sorted circular suffix array of the string specified.
   * @param args not used
   */
  public static void main(String[] args) {
    String originalString = "ABRACADABRA!";
    CircularSuffixArray circ = new CircularSuffixArray(originalString);
    System.out.println("Length: " + circ.length());
    System.out.println(circ.index(11));
  }
}
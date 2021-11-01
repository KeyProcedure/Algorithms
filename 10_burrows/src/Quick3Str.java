/**
 * Implements 3-way string quicksort, cutting off to insertion sort for short
 * substrings.
 */
public class Quick3Str {
  private static final int CUTOFF = 15;   // cutoff to insertion sort

  /**
   * Do not instantiate.
   */
  private Quick3Str() {
  }

  /**
   * Rearranges the array of strings in ascending order.
   * @param originalString original string
   * @param indexArray character index array of the string
   */
  public static void sort(String originalString, Integer[] indexArray) {
    sort(originalString, 0, indexArray.length - 1, 0, indexArray);
  }

  /**
   * Recursively performs a 3-way string quicksort a[lo..hi].
   * @param originalString original string
   * @param low low index
   * @param high high index
   * @param charIndex index of character
   * @param indexArray character index array of the string
   */
  private static void sort(String originalString, int low, int high, int charIndex,
                           Integer[] indexArray) {

    // cutoff to insertion sort for small subarray
    if (high <= low + CUTOFF) {
      insertion(originalString, low, high, charIndex, indexArray);
      return;
    }
    int lessThan = low, greaterThan = high;
    int vIndex = charAt(originalString, low, charIndex, indexArray);
    int i = low + 1;

    while (i <= greaterThan) {
      int tIndex = charAt(originalString, i, charIndex, indexArray);
      if (tIndex < vIndex)
        exchange(lessThan++, i++, indexArray);
      else if (tIndex > vIndex)
        exchange(i, greaterThan--, indexArray);
      else
        i++;
    }

    // sorts 3 sub-arrays recursively
    sort(originalString, low, lessThan - 1, charIndex, indexArray);
    if (vIndex >= 0)
      sort(originalString, lessThan, greaterThan, charIndex + 1, indexArray);
    sort(originalString, greaterThan + 1, high, charIndex, indexArray);
  }

  /**
   * Retrieves the nth character of the string.
   * @param originalString original string
   * @param stringIndex index of the string
   * @param charIndex index of the character
   * @param indexArray character index array of the string
   * @return nth character of the string, -1 if n = length of string
   */
  private static int charAt(String originalString, int stringIndex, int charIndex,
                            Integer[] indexArray) {
    assert charIndex >= 0 && charIndex <= originalString.length();

    if (charIndex == originalString.length())
      return -1;

    int index = charIndex + indexArray[stringIndex];

    if (index >= originalString.length())
      index -= originalString.length();

    return originalString.charAt(index);
  }

  /**
   * Sorts from a[lo] to a[hi], starting at the charIndex character.
   * @param originalString original string
   * @param low low index
   * @param high high index
   * @param charIndex index of the character
   * @param indexArray character index array of the string
   */
  private static void insertion(String originalString, int low, int high, int charIndex,
                                Integer[] indexArray) {
    for (int i = low; i <= high; i++)
      for (int j = i; j > low && less(originalString, j, j - 1, charIndex, indexArray); j--)
        exchange(j, j - 1, indexArray);
  }

  /**
   * Exchanges a[i] and a[j].
   * @param i index of character to exchange
   * @param j index of character to exchange
   * @param indexArray character index array of the string
   */
  private static void exchange(int i, int j, Integer[] indexArray) {
    int tempInt = indexArray[i];
    indexArray[i] = indexArray[j];
    indexArray[j] = tempInt;
  }

  /**
   * Determines if v is less than w, starting at character charIndex.
   * @param originalString original string
   * @param vIndex index to compare
   * @param wIndex index to compare
   * @param charIndex index of character
   * @param indexArray character index array of the string
   * @return true if the string is less than the other, false otherwise
   */
  private static boolean less(String originalString, int vIndex, int wIndex, int charIndex,
                              Integer[] indexArray) {
    for (int i = charIndex; i < originalString.length(); i++) {
      if (insertionCharAt(originalString, vIndex, i, indexArray) < insertionCharAt(
              originalString, wIndex, i, indexArray))
        return true;

      if (insertionCharAt(originalString, vIndex, i, indexArray) > insertionCharAt(
              originalString, wIndex, i, indexArray))
        return false;
    }
    return false;
  }

  /**
   * Retrieves the character at the index specified of the original string.
   * @param originalString original string
   * @param stringIndex index of the string
   * @param charIndex index of the character
   * @param indexArray character index array of the string
   * @return character at the specified index of the original string
   */
  private static char insertionCharAt(String originalString, int stringIndex, int charIndex,
                                      Integer[] indexArray) {
    int index = charIndex + indexArray[stringIndex];
    if (index >= originalString.length())
      index -= originalString.length();
    return originalString.charAt(index);
  }
}
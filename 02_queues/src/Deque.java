import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Implements a double-ended queue (deque) that supports adding and removing items
 * from either the front or the back of the data structure.
 * @param <Item> object type
 */
public class Deque<Item> implements Iterable<Item> {
  private int modCount = 0; // for iterator concurrency testing
  private int size;
  private final Node baseNode;

  /**
   * Inner node class.
   */
  private class Node {
    Item item;
    Node next;
    Node prev;
  }

  /**
   * Constructs an empty deque, creating a base node, setting next and prev
   * to the base node.
   */
  public Deque() {
    baseNode = new Node();
    baseNode.item = null;
    baseNode.next = baseNode;
    baseNode.prev = baseNode;
    size = 0;
    modCount++;
  }

  /**
   * Checks whether the deque is empty.
   * @return true if the deque is empty, false otherwise
   */
  public boolean isEmpty() {
    return baseNode.next.equals(baseNode);
  }

  /**
   * Retrieves the number of items in the deque.
   * @return size of the deque
   */
  public int size() {
    return size;
  }

  /**
   * Creates a new node, sets the value of the node to the generic item,
   * adds the node to the front of the deque, and increments the size variable.
   * @param item item to be added to the deque
   * @throws IllegalArgumentException if item is null
   */
  public void addFirst(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Item being added to the deque has not been initialized.");

    Node newNode = new Node();
    newNode.item = item;
    newNode.next = baseNode.next;
    baseNode.next = newNode;
    newNode.prev = baseNode;

    if (size == 0)
      baseNode.prev = newNode;
    else
      newNode.next.prev = newNode;

    size++;
    modCount++;
  }

  /**
   * Creates a new node, sets the value of the node to the generic item,
   * adds the node to the back of the deque, and increments the size variable.
   * @param item item to be added to the deque
   * @throws IllegalArgumentException if item is null
   */
  public void addLast(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Item being added to the deque has not been initialized.");

    Node newNode = new Node();
    newNode.item = item;

    if (size == 0)
      baseNode.next = newNode;
    else
      baseNode.prev.next = newNode;

    newNode.prev = baseNode.prev;
    baseNode.prev = newNode;
    newNode.next = baseNode;

    size++;
    modCount++;
  }

  /**
   * Removes the node located at the front of the deque, returns the item of
   * that node, and decrements the size variable.
   * @return item of the removed node
   * @throws NoSuchElementException if deque is empty
   */
  public Item removeFirst() {
    if (isEmpty())
      throw new NoSuchElementException("Deque is empty.");

    Item item = baseNode.next.item;
    baseNode.next.item = null;
    baseNode.next.next.prev = baseNode;
    baseNode.next = baseNode.next.next;

    if (size == 1)
      baseNode.prev = baseNode;

    size--;
    modCount++;
    return item;
  }

  /**
   * Removes the node located at the back of the deque, returns the item of
   * that node, and decrements the size variable.
   * @return item of the removed node
   * @throws NoSuchElementException if deque is empty
   */
  public Item removeLast() {
    if (isEmpty())
      throw new NoSuchElementException("Deque is empty.");

    Item item = baseNode.prev.item;
    baseNode.prev.item = null;
    baseNode.prev.prev.next = baseNode;
    baseNode.prev = baseNode.prev.prev;

    if (size == 1)
      baseNode.next = baseNode;

    size--;
    modCount++;
    return item;
  }

  /**
   * Retrieves an iterator, used to traverse the deque from front to back.
   * @return iterator
   */
  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  /**
   * Inner iterator class, implements the hasNext, next, and remove methods.
   */
  private class ListIterator implements Iterator<Item> {
    protected int mIterModCount = modCount; // for ConcurrentModificationException
    private Node current = baseNode;

    /**
     * Checks whether the deque has another node.
     * @return true if the deque contains another node
     */
    public boolean hasNext() {
      return !current.next.equals(baseNode);
    }

    /**
     * If a next node exists, retrieves the item of the next node and sets the
     * current node to the next node.
     * @return item of the next node
     * @throws ConcurrentModificationException if deque is concurrently modified
     * @throws NoSuchElementException if deque is empty
     */
    public Item next() {
      if (mIterModCount != modCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("Deque is empty.");

      Item item = current.next.item;
      current = current.next;
      return item;
    }

    /**
     * Unsupported function.
     * @throws UnsupportedOperationException if this method is called
     */
    public void remove() {
      throw new UnsupportedOperationException("Remove is an unsupported function.");
    }
  }
}
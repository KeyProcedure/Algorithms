import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Implements a data structure similar to a stack or queue in which an item is
 * removed uniformly at random.
 * @param <Item> object type
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
  private int modCount = 0; // for iterator concurrency testing
  private Item[] randomQ;
  private int size = 0;

  /**
   * Constructs an empty queue.
   */
  public RandomizedQueue() {
    randomQ = (Item[]) new Object[1];
    modCount++;
  }

  /**
   * Checks whether the queue is empty.
   * @return true if the queue is empty, false otherwise
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Retrieves the number of items on the queue.
   * @return the size of the queue
   */
  public int size() {
    return size;
  }

  /**
   * Adds an item to the queue, dynamically resizes the queue as needed.
   * @param item item to be added to the queue
   * @throws IllegalArgumentException if item is null
   */
  public void enqueue(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Item being added to the queue has not been initialized.");

    if (size >= randomQ.length - 1)
      resize(2 * randomQ.length);

    randomQ[size++] = item;
    modCount++;
  }

  /**
   * Removes and returns an item from the queue selected at random, dynamically
   * resizes the queue as needed.
   * @return the item dequeued
   * @throws NoSuchElementException if queue is empty
   */
  public Item dequeue() {
    if (size == 0)
      throw new NoSuchElementException("Queue is empty.");

    int index = getNextRandom();
    Item item = randomQ[index];
    randomQ[index] = null;
    size--;

    if (size > 0 && size == randomQ.length / 4)
      resize(randomQ.length / 2);

    modCount++;
    return item;
  }

  /**
   * Creates a random index within the non-null range of elements in the
   * queue.
   * @return random index of a non-null element
   */
  private int getNextRandom() {
    int index = StdRandom.uniform(0, randomQ.length);

    while (randomQ[index] == null)
      index = StdRandom.uniform(randomQ.length);

    return index;
  }

  /**
   * Creates a resized queue, copies non-null elements of the original queue.
   * @param capacity capacity of the resized queue
   */
  private void resize(int capacity) {
    Item[] copy = (Item[]) new Object[capacity];

    for (int i = 0, j = 0; i < randomQ.length && j < size; i++) {
      if (randomQ[i] != null)
        copy[j++] = randomQ[i];
    }

    randomQ = copy;
    modCount++;
  }

  /**
   * Retrieves a random item from the queue, without removing the item.
   * @return item randomly selected from the queue
   * @throws NoSuchElementException if queue is empty
   */
  public Item sample() {
    if (size == 0)
      throw new NoSuchElementException("Queue is empty.");

    return randomQ[getNextRandom()];
  }

  /**
   * Retrieves an independent iterator used to traverse over the items in random order.
   * @return independent iterator
   */
  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  /**
   * Inner iterator class, implements the hasNext, next, and remove methods.
   * Used to randomly iterate through the queue.
   */
  private class ListIterator implements Iterator<Item> {
    protected int mIterModCount = modCount; // for ConcurrentModificationException
    private int nIndex;
    private final Item[] newRandomQ;

    /**
     * Shuffles a copy of the queue.
     */
    public ListIterator() {
      newRandomQ = randomQ;
      nIndex = size;
      StdRandom.shuffle(newRandomQ, 0, size);
    }

    /**
     * Verifies that the queue has another element.
     * @return true if another element exists in the queue, false otherwise
     */
    public boolean hasNext() {
      return nIndex > 0;
    }

    /**
     * If another element exists in the queue, decrements the queue element counter,
     * and returns an element.
     * @return a random element from the queue
     * @throws ConcurrentModificationException if queue is concurrently modified
     * @throws NoSuchElementException if queue is empty
     */
    public Item next() {
      if (mIterModCount != modCount)
        throw new ConcurrentModificationException("Concurrent modification detected.");
      if (!hasNext())
        throw new NoSuchElementException("Queue is empty.");

      nIndex--;
      return newRandomQ[nIndex];
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
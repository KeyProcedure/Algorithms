import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Implements a weighted quick-union (disjoint-sets) data type to solve a
 * percolation system by finding a full site on the bottom row.
 * A full site is defined as being an open site that can be connected
 * to an open site in the top row via a chain of neighboring open sites.
 */
public class Percolation {
  private final Integer[] grid;
  private final WeightedQuickUnionUF unionFind;
  private final WeightedQuickUnionUF unionFindVirtual;
  private int openSiteCount;
  private final int gridLength;

  /**
   * Creates an n-by-n grid, with all sites initially blocked.
   * @param n grid dimension
   * @throws IllegalArgumentException n is <= 0
   */
  public Percolation(int n) {
    if (n <= 0)
      throw new IllegalArgumentException("The grid dimension, n, is <= 0.");

    unionFind = new WeightedQuickUnionUF(n * n);
    unionFindVirtual = new WeightedQuickUnionUF(n * n);

    grid = new Integer[n * n];
    openSiteCount = 0;
    gridLength = n;

    /*
     * Merges the sites on the top row, stored in unionFind.
     * Merges the sites on the top row and bottom row, stored in unionFindVirtual,
     * used to prevent percolation backwash (sites connected to the bottom row
     * displayed as being full, although they don't directly connect to the top row).
     */
    for (int i = 1; i < n; i++) {
      unionFind.union(xyTo1D(1, i), xyTo1D(1, i + 1));
      unionFindVirtual.union(xyTo1D(1, i), xyTo1D(1, i + 1));
      unionFindVirtual.union(xyTo1D(n, i), xyTo1D(n, i + 1));
    }
  }

  /**
   * Maps a 2-dimensional (row, column) pair to a 1-dimensional union find object index.
   * @param row row index of pair
   * @param col column index of pair
   * @return 1-dimensional index
   */
  private int xyTo1D(int row, int col) {
    return (row - 1) * gridLength + (col - 1);
  }

  /**
   * Validates a 2-dimensional pair of indices, valid indices are [1,n].
   * @param row row index of pair
   * @param col column index of pair
   * @throws IllegalArgumentException if the row or col index is not within the
   * range of the grid
   */
  private void validIndices(int row, int col) {
    if (row - 1 < 0 || row - 1 >= gridLength)
      throw new IllegalArgumentException("The row index is not within the range of the grid.");
    if (col - 1 < 0 || col - 1 >= gridLength)
      throw new IllegalArgumentException("The column index is not within the range of the grid.");
  }

  /**
   * Opens the site (row, col) if it is not open already.
   * @param row row index of site
   * @param col column index of site
   */
  public void open(int row, int col) {
    validIndices(row, col);
    int index = xyTo1D(row, col);

    if (grid[index] != null)
      return;

    grid[index] = index;
    openSiteCount++;

    if (row + 1 <= gridLength && isOpen(row + 1, col)) {
      unionFind.union(xyTo1D(row, col), xyTo1D(row + 1, col));
      unionFindVirtual.union(xyTo1D(row, col), xyTo1D(row + 1, col));
    }
    if (row - 1 > 0 && isOpen(row - 1, col)) {
      unionFind.union(xyTo1D(row, col), xyTo1D(row - 1, col));
      unionFindVirtual.union(xyTo1D(row, col), xyTo1D(row - 1, col));
    }
    if (col + 1 <= gridLength && isOpen(row, col + 1)) {
      unionFind.union(xyTo1D(row, col), xyTo1D(row, col + 1));
      unionFindVirtual.union(xyTo1D(row, col), xyTo1D(row, col + 1));
    }
    if (col - 1 > 0 && isOpen(row, col - 1)) {
      unionFind.union(xyTo1D(row, col), xyTo1D(row, col - 1));
      unionFindVirtual.union(xyTo1D(row, col), xyTo1D(row, col - 1));
    }
  }

  /**
   * Checks whether the site (row, col) is open.
   * @param row row index of site
   * @param col col index of site
   * @return true if the site is open, false otherwise
   */
  public boolean isOpen(int row, int col) {
    validIndices(row, col);
    int index = xyTo1D(row, col);
    return grid[index] != null;
  }

  /**
   * Checks whether the site (row, col) is full (an open site that can be connected
   * to an open site in the top row via a chain of neighboring open sites).
   * @param row row index of site
   * @param col col index of site
   * @return true if the site is full, false otherwise
   */
  public boolean isFull(int row, int col) {
    if (!isOpen(row, col))
      return false;

    int siteRoot = unionFind.find(xyTo1D(row, col));
    return siteRoot == unionFind.find(xyTo1D(1, 1));
  }

  /**
   * Retrieves the number of open sites.
   * @return number of open sites
   */
  public int numberOfOpenSites() {
    return openSiteCount;
  }

  /**
   * Checks whether the system percolates by verifying that a grid site on the
   * top row belongs to the same set as a grid site on the bottom row.
   * @return true if the system percolation, false otherwise
   */
  public boolean percolates() {
    if (gridLength == 1 && !isOpen(1, 1))
      return false;

    return unionFindVirtual.find(xyTo1D(1, 1)) ==
           unionFindVirtual.find(xyTo1D(gridLength, 1));
  }
}
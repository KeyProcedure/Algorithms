import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Dependencies: Percolation.java
 *
 * Estimates the value of the percolation threshold via a Monte Carlo simulation.
 * The fraction of sites that are opened when the system percolates provides an
 * estimate of the threshold.
 */
public class PercolationStats {
  private static final double CONFIDENCE_95 = 1.96;
  private final double[] openSiteCountArr;
  private final int trialCount;

  /**
   * Performs independent trials on an n-by-n grid.
   * @param n grid dimension
   * @param trials number of independent computational experiments
   * @throws IllegalArgumentException if the grid dimension is <= 0
   * @throws IllegalArgumentException if the number of trials to run is <= 0
   */
  public PercolationStats(int n, int trials) {
    if (n <= 0)
      throw new IllegalArgumentException("The grid dimension, n, is <= 0.");
    if (trials <= 0)
      throw new IllegalArgumentException("The number of trials to run is <= 0.");

    openSiteCountArr = new double[trials];
    trialCount = trials;

    for (int i = 0; i < trials; i++) {
      Percolation perc = new Percolation(n);

      // randomly open sites on the grid until the system percolates
      while (!perc.percolates()) {
        perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
      }

      // calculate the percolation threshold for each trial
      openSiteCountArr[i] = perc.numberOfOpenSites() / (1.0 * (n * n));
    }
  }

  /**
   * Calculates the sample mean of the percolation threshold.
   * @return sample mean
   */
  public double mean() {
    return StdStats.mean(openSiteCountArr);
  }

  /**
   * Calculates the sample standard deviation of the percolation threshold.
   * @return sample standard deviation
   */
  public double stddev() {
    return StdStats.stddev(openSiteCountArr);
  }

  /**
   * Calculates the low endpoint of the 95% confidence interval.
   * @return low endpoint of the confidence interval
   */
  public double confidenceLo() {
    return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialCount));
  }

  /**
   * Calculates the high endpoint of the 95% confidence interval.
   * @return high endpoint of the confidence interval
   */
  public double confidenceHi() {
    return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialCount));
  }

  /**
   * Uses the two values, n (grid dimension) and numOfTrials (number of trials to run),
   * to solve random percolation models and provide the sample mean, sample standard
   * deviation, and the 95% confidence interval for the percolation threshold.
   * @param args not used
   */
  public static void main(String[] args) {
    int n = 10;
    int numOfTrials = 30;
    PercolationStats stats = new PercolationStats(n, numOfTrials);
    System.out.println("mean                    " + "= " + stats.mean());
    System.out.println("stddev                  " + "= " + stats.stddev());
    System.out.println("95% confidence interval = [" + stats.confidenceLo()
                    + ", " + stats.confidenceHi() + "]");
  }
}
# Project Overview
A percolation system is modeled using an n-by-n grid of sites.\
This program implements a weighted quick-union (disjoint-sets) data type to estimate the value of the percolation threshold by randomly opening sites on the grid, and determining
when the system percolates.\
System percolation occurs when an open site on the bottom row exists that can be connected to an open site in the top row 
via a chain of neighboring (left, right, up, down) open sites.


Detailed project description and requirements are included in percolation_specification.pdf


# Brief Description of Class Files:

src/Percolation.java
- Solves the percolation system by finding a chain of open sites in the grid.

src/PercolationStats.java
- Estimates the value of the percolation threshold via a Monte Carlo simulation.
- Prints the statistics of the percolation threshold.

src/PercolationVisualizer.java
- Main application.
- Reads a file containing a series of sites to open, creates a visual grid representing the percolation system.
- Provided by course instructors.

src/InteractivePercolationVisualizer.java
- Creates an interactive visual grid representing the percolation system, opens sites based on mouse clicks.
- Provided by course instructors.

# Project Overview
This program implements feature detection and pattern recognition to recognize line patterns in a given set of points.

Detailed project description and requirements are included in collinear_specification.pdf


# Brief Description of Class Files:

src/FastCollinearPoints.java
- Main application.
- Sorts points based on their slope value relative to a single point, searches to find sets of collinear points.
- Prints and draws the line segments found.

src/BruteCollinearPoints.java
- Implements a brute force algorithm to fine collinear points.

src/Point.java
- A data type for points in the plane.

src/LineSegment.java
- A data type for Line segments in the plane.
- Provided by course instructors.

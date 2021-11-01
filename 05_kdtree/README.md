# Project Overview
This program implements a red-black BST to represent a set of points in the unit square.\
This implementation supports efficient range search to find all the points contained in a query rectangle, 
and nearest-neighbor search to find a closest point to a query point.

Detailed project description and requirements are included in kdtree_specification.pdf


# Brief Description of Class Files:

src/PointSET.java
- Brute force implementation of a red–black BST that represents a set of points in the unit square.

src/KdTree.java
- 2d-tree implementation of a red-black BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.

src/KdTreeVisualizer.java
- Main application.
- Adds the points that the user clicks in the standard draw window to a kd-tree and draws the resulting kd-tree.
- Provided by course instructors.

src/KdTreeGenerator.java
- Creates n random points in the unit square and prints to standard output.
- Provided by course instructors.

src/NearestNeighborVisualizer.java
- Reads points from a file and draws to standard draw. 
- Highlights the closest point to the mouse.
- Provided by course instructors.

src/RangeSearchVisualizer.java
- Reads points from a file and draws to standard draw. 
- Draws all the points in the rectangle the user selects by dragging the mouse.
- Provided by course instructors.

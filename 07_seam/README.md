# Project Overview
This program implements the principle of an edge weighted directed acyclic graph to perform seam-carving on image files.\
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time.\
It accomplishes this in an efficient manner by processing the image's pixels, calculating the energy of the pixels by implementing a dual-gradient energy function which
assigns an energy level to a pixel by computing the gradient of a pixel and its adjacent pixels, and identifying and removing a series of adjacent pixels, a seam, 
which compute to the minimum total energy.

Detailed project description and requirements are included in seam_specification.pdf


# Brief Description of Class Files:

src/SeamCarver.java
- Computes the energy values of each pixel in a picture, finds a horizontal or vertical seam of adjacent pixels containing the least amount of energy,
then removes the seam from the picture.

src/ShortestPath.java
- Computes the shortest path through the energy matrix, selecting a series of adjacent pixels with the least amount of cumulative energy.

src/Pixel.java
- A representation of a single pixel.

src/ResizeDemo.java
- Main application.
- Performs a series of seam-carving operations on an image, then displays the original image and the resized image, and prints the time elapsed to the screen.
- Provided by course instructors.

src/ShowSeams.java
- Displays 3 images, the original image, the image with an overlayed vertical seam, and the image with an overlayed horizontal seam.
- Provided by course instructors.

src/ShowEnergy.java
- Displays 2 images, the original image and the energy values of each pixel in the image in a grayscale.
- Provided by course instructors.

src/PrintEnergy.java
- Prints the energy of each pixel in the image.
- Provided by course instructors.

src/PrintSeams.java
- Prints the energy of each pixel in the image, the vertical seam, and a horizontal seam.
- Provided by course instructors.

src/SCUtility.java
- Utility functions used to test SeamCarver.java.
- Provided by course instructors.

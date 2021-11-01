# Project Overview
This program implements the Burrows–Wheeler data compression algorithm by performing a Burrows–Wheeler transform, move-to-front encoding, and Huffman compression.

Detailed project description and requirements are included in burrows_specification.pdf


# Brief Description of Class Files:

src/BurrowsWheeler.java
- Main application.
- Implements the Burrows–Wheeler data compression algorithm.

src/CircularSuffixArray.java
- Implements a circular suffix array, which describes the abstraction of a sorted array of the n circular suffixes of a string of length n.

src/MoveToFront.java
- Converts a given text file in which sequences of the same character occur near each other many times, into a text file in which certain characters appear much more frequently than others.

src/Quick3Str.java
- Implements 3-way string quicksort, cutting off to insertion sort for short substrings.

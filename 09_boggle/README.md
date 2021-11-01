# Project Overview
This program implements a 26-way trie to efficiently find all valid words in a given Boggle board by traversing through the tiles while searching for prefix matches.

Detailed project description and requirements are included in boggle_specification.pdf


# Brief Description of Class Files:

src/BoggleSolver.java
- Main application.
- Implements a 26-way trie to find all valid words in a given Boggle board, using a given dictionary.

src/BoggleBoard.java
- A representation of a Boggle board.

src/TrieST26.java
- An implementation of an R-way trie.
- Stores a string symbol table for words found in the english dictionary, with string keys and integer values.

src/BoggleGame.java
- GUI for the boggle solver.
- Provided by course instructors.

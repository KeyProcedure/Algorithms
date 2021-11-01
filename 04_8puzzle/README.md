# Project Overview
The 8-puzzle is a sliding puzzle that is played on a 3-by-3 grid with 8 square tiles labeled 1 through 8, plus a blank square.\
This program rearranges the tiles of the 8-puzzle board so that they are in row-major order, using as few moves as possible.

Detailed project description and requirements are included in 8puzzle_specification.pdf


# Brief Description of Class Files:

src/Solver.java
- Main application.
- Implements an A* search algorithm using a priority queue to solve a slider puzzle in the minimum number of moves.
- Prints the sequence of moves used to solve the puzzle.

src/Board.java
- A representation of an n-by-n board with sliding tiles.

src/PuzzleChecker.java
- Solves the slider puzzle, and prints the least amount of moves required to solve the puzzle.
- Provided by course instructors.

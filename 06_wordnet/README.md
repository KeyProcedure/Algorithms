# Project Overview
This program implements a directed digraph to efficiently store and search the words and relationships of a semantic lexicon for the English language, which have been grouped 
into sets of synonyms called synsets.

Detailed project description and requirements are included in wordnet_specification.pdf


# Brief Description of Class Files:

src/SAP.java
- Main application.
- Implements a directed digraph to find the shortest ancestral path between two vertices.

src/Outcast.java
- Prints the noun least related to other nouns by computing the sum of distances between each noun and every other one.

src/WordNet.java
- Implements a rooted acyclic digraph to store sets of synonyms and the degrees of relation among one another.

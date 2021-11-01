import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Implements a digraph to find an ancestral path between two vertices v and w.
 * Finds a directed path from v to a common ancestor x, together with a directed path
 * from w to the same ancestor x.
 * A shortest ancestral path is an ancestral path of minimum total length.
 */
public class SAP {
  private final Digraph digraph;
  private int length;
  private int shortestAncestor;

  /**
   * Deep copies a digraph.
   * @param digraph digraph to search
   * @throws IllegalArgumentException if the digraph is null
   */
  public SAP(Digraph digraph) {
    if (digraph == null)
      throw new IllegalArgumentException("Digraph has not been initialized.");

    this.digraph = new Digraph(digraph);
    length = Integer.MAX_VALUE;
    shortestAncestor = -1;
  }

  /**
   * Retrieves the length of the shortest ancestral path between v and w
   * @param v a vertex
   * @param w a vertex
   * @return length of the of SAP, -1 if no path exists
   */
  public int length(int v, int w) {
    sendVerticesToSP(v, w);
    if (shortestAncestor == -1)
      return -1;
    return length;
  }

  /**
   * Finds a common ancestor of two vertices that participates in a shortest ancestral path.
   * @param v a vertex
   * @param w a vertex
   * @return a common ancestor, -1 if no path exists
   */
  public int ancestor(int v, int w) {
    sendVerticesToSP(v, w);
    return shortestAncestor;
  }

  /**
   * Creates two stacks for vertex v and w, sends the stacks to sourcePath to
   * find the shortest common ancestral path.
   * @param v a vertex
   * @param w a vertex
   */
  private void sendVerticesToSP(int v, int w) {
    validateVertex(v);
    validateVertex(w);
    Stack<Integer> vStack = new Stack<>();
    Stack<Integer> wStack = new Stack<>();
    vStack.push(v);
    wStack.push(w);
    sourcePath(vStack, wStack);
  }

  /**
   * Validates vertex in the digraph.
   * @param v vertex to validate
   * @throws IllegalArgumentException if the vertex v is out of range
   */
  private void validateVertex(int v) {
    int maxLength = digraph.V();
    if (v < 0 || v >= maxLength)
      throw new IllegalArgumentException("Vertex v is outside of the digraph's range.");
  }

  /**
   * Finds the shortest ancestral path between any vertex in vStack and any
   * vertex in wStack.
   * @param vStack stack of vertices
   * @param wStack stack of vertices
   * @return shortest ancestral path, -1 if no such path
   */
  public int length(Iterable<Integer> vStack, Iterable<Integer> wStack) {
    validateVertices(vStack);
    validateVertices(wStack);
    sourcePath(vStack, wStack);
    if (shortestAncestor == -1)
      return -1;
    return length;
  }

  /**
   * Finds the common ancestor that participates in the shortest ancestral path
   * between any vertex in vStack and any vertex in wStack.
   * @param vStack stack of vertices
   * @param wStack stack of vertices
   * @return common ancestor
   */
  public int ancestor(Iterable<Integer> vStack, Iterable<Integer> wStack) {
    validateVertices(vStack);
    validateVertices(wStack);
    sourcePath(vStack, wStack);
    return shortestAncestor;
  }

  /**
   * Validates a stack of vertices.
   * @param vertices stack to be validated
   * @throws IllegalArgumentException if the vertex stack is null
   * @throws IllegalArgumentException if a vertex in the stack is null
   */
  private void validateVertices(Iterable<Integer> vertices) {
    if (vertices == null)
      throw new IllegalArgumentException("The stack of vertices has not been initialized.");

    for (Integer v : vertices) {
      if (v == null)
        throw new IllegalArgumentException("A vertex in the stack of vertices has not been initialized.");
      validateVertex(v);
    }
  }

  /**
   * Finds the common ancestor that participates in the shortest ancestral path
   * between any vertex in vStack and any vertex in wStack, and the length of that path.
   * @param vStack stack of vertices
   * @param wStack stack of vertices
   */
  private void sourcePath(Iterable<Integer> vStack, Iterable<Integer> wStack) {
    int vertexDistance;
    length = Integer.MAX_VALUE;
    shortestAncestor = -1;

    if (!vStack.iterator().hasNext() || !wStack.iterator().hasNext())
      return;

    BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, vStack);
    BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, wStack);

    for (int vertex = 0; vertex < digraph.V(); vertex++) {
      if (pathV.hasPathTo(vertex) && pathW.hasPathTo(vertex)) {
        vertexDistance = pathV.distTo(vertex) + pathW.distTo(vertex);
        if (vertexDistance < length) {
          length = vertexDistance;
          shortestAncestor = vertex;
        }
      }
    }
  }

  /**
   * Constructs a digraph, reads in vertex pairs, and prints the length of the shortest ancestral
   * path between the two vertices and a common ancestor that participates in that path.
   * @param args not used
   */
  public static void main(String[] args) {
    final String fileName = "06_wordnet/test/digraph1.txt"; // input test file
    In in = new In(fileName);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);

    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}
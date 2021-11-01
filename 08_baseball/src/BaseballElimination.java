import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;

/**
 * Implements the Ford–Fulkerson maximum flow algorithm to solve the baseball
 * elimination problem, determining which teams have been mathematically
 * eliminated from winning their division.
 */
public class BaseballElimination {
  private final HashMap<String, int[]> teamMap;
  private int[][] schedule;
  private FlowNetwork network;
  private FordFulkerson fordFulk;
  private final HashMap<Integer, String> teamIndexMap;
  private int[][] vertexToTeams;

  /**
   * Creates a baseball division from the given file.
   * @param filename name of the input file
   */
  public BaseballElimination(String filename) {
    teamMap = new HashMap<>();
    teamIndexMap = new HashMap<>();
    readFile(filename);
  }

  /**
   * Parses a text file and organizes the information of a division of teams,
   * their wins, losses, games left to play, and the schedule of games to be played
   * into a hashmap.
   * @param filename name of the input file
   */
  private void readFile(String filename) {
    In inputFile = new In(filename);
    String line;
    int numberOfTeams = Integer.parseInt(inputFile.readLine());
    schedule = new int[numberOfTeams][numberOfTeams];
    String[] teamStats = new String[4];
    int teamIndex = 0;
    int row = 0;
    int index;

    while (inputFile.hasNextLine()) {
      line = inputFile.readLine();
      index = 0;

      for (String word : line.split(" ")) {
        if (!word.equals("") && index < 4) {
          teamStats[index] = word;
          index++;
        }
        else if (!word.equals("")) {
          schedule[row][index - 4] = Integer.parseInt(word);
          index++;
        }
      }
      row++;

      // key: teamName
      // value[0]: wins
      // value[1]: losses
      // value[2]: games remaining
      // value[3]: teamIndex
      // value[4+i]: schedule
      teamMap.put(teamStats[0], new int[]{
              Integer.parseInt(teamStats[1]), Integer.parseInt(teamStats[2]),
              Integer.parseInt(teamStats[3]), teamIndex
      });

      // organizes a team's index and their name
      teamIndexMap.put(teamIndex, teamStats[0]);
      teamIndex++;
    }
  }

  /**
   * Retrieves the number of teams.
   * @return number of team
   */
  public int numberOfTeams() {
    return schedule.length;
  }

  /**
   * Creates a stack containing the names of all the teams.
   * @return stack of team names
   */
  public Iterable<String> teams() {
    Stack<String> teams = new Stack<>();
    for (String teamName : teamMap.keySet()) {
      teams.push(teamName);
    }
    return teams;
  }

  /**
   * Retrieves the number of wins for a given team.
   * @param team name of the team
   * @return number of wins
   */
  public int wins(String team) {
    teamExists(team);
    return teamMap.get(team)[0];
  }

  /**
   * Retrieves the number of losses for a given team.
   * @param team name of the team
   * @return number of losses
   */
  public int losses(String team) {
    teamExists(team);
    return teamMap.get(team)[1];
  }

  /**
   * Retrieves the number of remaining games for a given team.
   * @param team name of the team
   * @return number of games remaining
   */
  public int remaining(String team) {
    teamExists(team);
    return teamMap.get(team)[2];
  }

  /**
   * Retrieves the number of remaining games between team1 and team2.
   * @param team1 name of the team
   * @param team2 name of the team
   * @return number of remaining games between the two teams
   */
  public int against(String team1, String team2) {
    teamExists(team1);
    teamExists(team2);
    return schedule[teamMap.get(team1)[3]][teamMap.get(team2)[3]];
  }

  /**
   * Verifies that a team exists in the hashmap.
   * @param team name of the team to search for
   * @throws IllegalArgumentException if the team does not exist in the team map
   */
  private void teamExists(String team){
    if (teamMap.get(team) == null)
      throw new IllegalArgumentException(team + " does not exist in the division.");
  }

  /**
   * Adds the games left to be played, the team vertices, and the potential number of
   * games that each team can win to a flow network.
   * @param team name of the team
   */
  private void createFlowNetwork(String team) {
    int teamIndex = teamMap.get(team)[3];
    int numberOfTeams = schedule.length;
    int numberOfGames = (((numberOfTeams - 1) * (numberOfTeams - 1) + (numberOfTeams - 1)) / 2)
            - teamIndex - (numberOfTeams - 1 - teamIndex);
    int numberOfVertices = numberOfGames + numberOfTeams + 1;
    vertexToTeams = new int[numberOfGames][2];
    network = new FlowNetwork(numberOfVertices);
    int vertexIndex = 1;
    int highestPotentialScore = teamMap.get(team)[0] + teamMap.get(team)[2];
    int capacity;
    int teamVertex = numberOfGames + 1;

    for (int i = 0; i < numberOfTeams; i++) {
      if (i == teamIndex)
        continue;

      capacity = highestPotentialScore - teamMap.get(teamIndexMap.get(i))[0];

      if (capacity < 0)
        capacity = 0;

      network.addEdge(new FlowEdge(teamVertex++, numberOfGames + numberOfTeams, capacity));

      for (int j = i + 1; j < numberOfTeams; j++) {
        if (j == teamIndex)
          continue;

        network.addEdge(new FlowEdge(0, vertexIndex, schedule[i][j]));
        vertexToTeams[vertexIndex - 1][0] = i;
        vertexToTeams[vertexIndex - 1][1] = j;

        if (i < teamIndex)
          network.addEdge(new FlowEdge(vertexIndex, numberOfGames + i + 1, Double.POSITIVE_INFINITY));
        else
          network.addEdge(new FlowEdge(vertexIndex, numberOfGames + i, Double.POSITIVE_INFINITY));

        if (j < teamIndex)
          network.addEdge(new FlowEdge(vertexIndex, numberOfGames + j + 1, Double.POSITIVE_INFINITY));
         else
          network.addEdge(new FlowEdge(vertexIndex, numberOfGames + j, Double.POSITIVE_INFINITY));

        vertexIndex++;
      }
    }

    fordFulk = new FordFulkerson(network, 0, numberOfVertices - 1);
  }

  /**
   * Checks if a team is eliminated by considering the two cases, trivial and
   * non-trivial elimination.
   * @param team name of the team
   * @return true if the team has been eliminated, false otherwise
   */
  public boolean isEliminated(String team) {
    teamExists(team);
    if (trivialElimination(team) != null)
      return true;
    else
      return nontrivialElimination(team);
  }

  /**
   * Determines if the maximum number of games a team can win is less than the
   * number of wins of every other team.
   * @param team name of the team
   * @return name of the team that has eliminated the team being queried,
   * null if not eliminated
   */
  private String trivialElimination(String team) {
    int highestPotentialScore = teamMap.get(team)[0] + teamMap.get(team)[2];
    for (String teamName : teamMap.keySet()) {
      if (teamMap.get(teamName)[0] > highestPotentialScore)
        return teamName;
    }
    return null;
  }

  /**
   * Determines the minimum cut of the flow network to determine whether a given
   * team is mathematically eliminated.
   * @param team name of the team
   * @return true if the team been eliminated, false otherwise
   */
  private boolean nontrivialElimination(String team) {
    createFlowNetwork(team);
    for (FlowEdge edge : network.adj(0)) {
      if (edge.capacity() != edge.flow())
        return true;
    }
    return false;
  }

  /**
   * Finds a subset consisting of other teams in the division by choosing the team
   * vertices on the source side of a min s-t cut in the baseball elimination network.
   * @param team name of the team
   * @return subset of teams that eliminates given team, null if not eliminated
   */
  public Iterable<String> certificateOfElimination(String team) {
    teamExists(team);
    Stack<String> teamSubset = new Stack<>();
    String trivialOutput = trivialElimination(team);
    int edgeTo;

    if (trivialOutput != null) {
      teamSubset.push(trivialOutput);
      return teamSubset;
    }
    if (!nontrivialElimination(team))
      return null;

    HashMap<String, Integer> teamSubsetMap = new HashMap<>();
    for (FlowEdge edge : network.adj(0)) {
      if (fordFulk.inCut(edge.to())) {
        edgeTo = edge.to() - 1;
        if (teamSubsetMap.get(teamIndexMap.get(vertexToTeams[edgeTo][0])) == null) {
          teamSubsetMap.put(teamIndexMap.get(vertexToTeams[edgeTo][0]), vertexToTeams[edge.to()][0]);
          teamSubset.push(teamIndexMap.get(vertexToTeams[edgeTo][0]));
        }
        if (teamSubsetMap.get(teamIndexMap.get(vertexToTeams[edgeTo][1])) == null) {
          teamSubsetMap.put(teamIndexMap.get(vertexToTeams[edgeTo][1]), vertexToTeams[edge.to()][0]);
          teamSubset.push(teamIndexMap.get(vertexToTeams[edgeTo][1]));
        }
      }
    }
    return teamSubset;
  }

  /**
   * Reads in a sports division from an input file and prints whether each team
   * is mathematically eliminated and a certificate of elimination for each team
   * that is eliminated.
   * @param args not used
   */
  public static void main(String[] args) {
    final String fileName = "08_baseball/test/teams5.txt";  // input test file
    BaseballElimination division = new BaseballElimination(fileName);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      }
      else
        StdOut.println(team + " is not eliminated.");
    }
  }
}
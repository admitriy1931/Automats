package algorithms;

import automaton.SynchronizedAutomaton;
import com.google.common.collect.HashBasedTable;

import java.util.*;
import java.util.Map.Entry;

public class GreedySyncWordFinding {
    public static boolean isAutomatonSynchronized(SynchronizedAutomaton syncAut)
        throws CloneNotSupportedException {
        if (syncAut.vertices.size() < 2)
            return false;

        SynchronizedAutomaton clonedSyncAut = syncAut.clone();
        addTwoElementVertices(clonedSyncAut);
        Map<String, List<String>> dijkstraResults = new HashMap<>();

        for (String vertex : clonedSyncAut.twoElementVertices) {
            List<String> currentRoute =
                new ArrayList<>(Dijkstra.dijkstraAlg(clonedSyncAut, vertex).entrySet().iterator().next().getValue());
            if (currentRoute.size() != 0)
                dijkstraResults.put(vertex, currentRoute);
        }

        return dijkstraResults.size() == clonedSyncAut.twoElementVertices.size();
    }

    public static void addTwoElementVertices(SynchronizedAutomaton syncAut) {
        List<String> vertices = new ArrayList<>(syncAut.vertices);

        for (int i = 0; i < vertices.size(); i++) {
            String vertex1 = vertices.get(i);

            for (int j = i + 1; j < vertices.size(); j++) {
                String vertex2 = vertices.get(j);
                HashBasedTable<String, String, String> vertexRow = HashBasedTable.create();
                String vertexName = String.join("", vertex1, vertex2);
                syncAut.twoElementVertices.add(vertexName);

                for (String letter : syncAut.letters) {
                    String fromJump1 = syncAut.getJumpByVertexAndLetter(vertex1, letter);
                    String fromJump2 = syncAut.getJumpByVertexAndLetter(vertex2, letter);

                    if (Objects.equals(fromJump1, fromJump2))
                        vertexRow.put(vertexName, letter, fromJump1);
                    else {
                        if (fromJump1.compareTo(fromJump2) > 0) {
                            String fromJumpTemp = fromJump1;
                            fromJump1 = fromJump2;
                            fromJump2 = fromJumpTemp;
                        }

                        vertexRow.put(vertexName, letter, String.join("", fromJump1, fromJump2));
                    }
                }

                syncAut.addVertex(vertexName, vertexRow);
            }
        }
    }

    public static SynchronizedAutomaton addCombinedElementVertices(SynchronizedAutomaton syncAut)
        throws CloneNotSupportedException {
        SynchronizedAutomaton clonedSyncAut = syncAut.clone();
        List<String> rawVertices = new ArrayList<>(clonedSyncAut.vertices);
        List<String> vertices = new ArrayList<>();

        for (String rawVertex : rawVertices)
            if (rawVertex.length() == 1)
                vertices.add(rawVertex);

        getAllCombinedVertices(clonedSyncAut, vertices);

        for (String vertex : clonedSyncAut.combinedElementVertices) {
            HashBasedTable<String, String, String> vertexRow = HashBasedTable.create();

            for (String letter : clonedSyncAut.letters) {
                Set<String> statesAfterJump = new HashSet<>();

                for (int i = 0; i < vertex.length(); i++) {
                    String fromJump = clonedSyncAut.getJumpByVertexAndLetter(String.valueOf(vertex.charAt(i)), letter);
                    statesAfterJump.add(fromJump);
                }

                List<String> sortedStates = new ArrayList<>(statesAfterJump);
                Collections.sort(sortedStates);
                vertexRow.put(vertex, letter, String.join("", sortedStates));
            }

            clonedSyncAut.addVertex(vertex, vertexRow);
        }

        return clonedSyncAut.clone();
    }

    public static void greedyWordFindingAlg(SynchronizedAutomaton syncAut) {
        Set<String> combinedVerticesSet = new HashSet<>(syncAut.twoElementVertices);
        Map<String, List<String>> dijkstraResults = new HashMap<>();
        StringBuilder syncWord = new StringBuilder();

        for (String vertex : combinedVerticesSet)
            dijkstraResults.put(vertex, Dijkstra.dijkstraAlg(syncAut, vertex).entrySet().iterator().next().getValue());

        while (!dijkstraResults.isEmpty()) {
            Set<String> statesAfterJumps = getStatesJumps(syncAut, dijkstraResults, syncWord);

            updateDijkstraResults(dijkstraResults, statesAfterJumps);
        }

        syncAut.syncWord = syncWord.toString();
    }

    public static SynchronizedAutomaton greedyShortestWordFindingAlg(SynchronizedAutomaton syncAut)
        throws CloneNotSupportedException {
        Set<String> combinedVerticesSet = new HashSet<>(syncAut.combinedElementVertices);
        Map<String, List<String>> dijkstraResult = new HashMap<>();
        Set<String> twoElementVerticesSet = new HashSet<>();
        StringBuilder shortestSyncWord = new StringBuilder();

        for (String vertex : combinedVerticesSet)
            dijkstraResult.put(vertex, Dijkstra.dijkstraAlg(syncAut, vertex).entrySet().iterator().next().getValue());

        while (!dijkstraResult.isEmpty()) {
            Set<String> statesAfterJumps = getStatesJumps(syncAut, dijkstraResult, shortestSyncWord);

            for (String vertex : statesAfterJumps)
                if (vertex.length() == 2)
                    twoElementVerticesSet.add(vertex);

            updateDijkstraResults(dijkstraResult, statesAfterJumps);
        }

        List<String> twoElementVertices = new ArrayList<>(syncAut.twoElementVertices);
        String syncWord = syncAut.syncWord;

        SynchronizedAutomaton twoElementSyncAut = syncAut.clone();
        twoElementSyncAut.twoElementVertices = new ArrayList<>(twoElementVerticesSet);
        greedyWordFindingAlg(twoElementSyncAut);
        shortestSyncWord.append(twoElementSyncAut.syncWord);

        SynchronizedAutomaton clonedSyncAut = twoElementSyncAut.clone();
        clonedSyncAut.twoElementVertices = new ArrayList<>(twoElementVertices);
        clonedSyncAut.syncWord = syncWord;
        String strShortestSyncWord = shortestSyncWord.toString();

        if (!strShortestSyncWord.isEmpty()) clonedSyncAut.shortestSyncWord = strShortestSyncWord;
        else clonedSyncAut.shortestSyncWord = syncWord;

        return clonedSyncAut.clone();
    }

    private static Set<String> getStatesJumps
        (SynchronizedAutomaton syncAut, Map<String, List<String>> dijkstraResults, StringBuilder syncWord) {
        String currentVertex = "";
        int minDistance = 32767;

        for (Entry<String, List<String>> entrySet : dijkstraResults.entrySet())
            if (entrySet.getValue().size() < minDistance) {
                minDistance = entrySet.getValue().size();
                currentVertex = entrySet.getKey();
            }

        syncWord.append(String.join("", dijkstraResults.get(currentVertex)));

        Set<String> statesAfterJumps = new HashSet<>();
        for (Entry<String, List<String>> entrySet : dijkstraResults.entrySet()) {
            String vertex = entrySet.getKey();

            for (String letter : dijkstraResults.get(currentVertex))
                vertex = syncAut.getJumpByVertexAndLetter(vertex, letter);

            statesAfterJumps.add(vertex);
        }

        return statesAfterJumps;
    }

    private static void updateDijkstraResults
        (Map<String, List<String>> dijkstraResults, Set<String> statesAfterJumps) {
        Set<Entry<String, List<String>>> combinedVerticesEntry = new HashSet<>(dijkstraResults.entrySet());
        dijkstraResults.clear();

        for (Entry<String, List<String>> entrySet : combinedVerticesEntry)
            if (statesAfterJumps.contains(entrySet.getKey()))
                dijkstraResults.put(entrySet.getKey(), entrySet.getValue());
    }

    private static void getAllCombinedVertices(SynchronizedAutomaton syncAut, List<String> vertices) {
        for (int i = 0; i < (1 << vertices.size()); i++) {
            StringBuilder vertexName = new StringBuilder();

            for (int j = 0; j < vertices.size(); j++)
                if ((i & (1 << j)) != 0)
                    vertexName.append(vertices.get(j));

            if (vertexName.length() >= 3)
                syncAut.combinedElementVertices.add(vertexName.toString());
        }
    }
}

package algorithms;

import java.util.*;
import java.util.Map.*;

import automaton.SynchronizedAutomaton;

public class Dijkstra {
    public static Map<String, List<String>> dijkstraAlg
        (SynchronizedAutomaton syncAut, String startVertex) {
        Set<String> verticesSet = new HashSet<>(syncAut.vertices);
        Map<String, Integer> distanceFromStartVertex = new HashMap<>();
        Map<String, String> previousVertex = new HashMap<>();
        Map<String, Map<String, Integer>> edgeWeights = new HashMap<>();

        initWeights(syncAut, edgeWeights);

        verticesSet.remove(startVertex);
        for (String currentVertex : verticesSet) {
            distanceFromStartVertex.put(currentVertex, edgeWeights.get(startVertex).get(currentVertex));
            previousVertex.put(currentVertex, startVertex);
        }
        for (int i = 1; i < syncAut.vertices.size(); i++) {
            String minDistanceVertex = findMinDistance(verticesSet, distanceFromStartVertex);
            if (Objects.equals(minDistanceVertex, "-1"))
                continue;
            verticesSet.remove(minDistanceVertex);

            for (String currentVertex : verticesSet)
                if (ifNewBetterThanOld(distanceFromStartVertex, edgeWeights, minDistanceVertex, currentVertex)) {

                    distanceFromStartVertex.put
                        (currentVertex,
                            distanceFromStartVertex.get(minDistanceVertex) +
                                edgeWeights.get(minDistanceVertex).get(currentVertex));

                    previousVertex.put(currentVertex, minDistanceVertex);

                }
        }

        String finishVertex = getFinishVertex(distanceFromStartVertex);

        return Map.of(finishVertex, findRoute(syncAut, previousVertex, startVertex, finishVertex));
    }

    private static void initWeights
        (SynchronizedAutomaton syncAut, Map<String, Map<String, Integer>> edgeWeights) {
        for (String firstVertex : syncAut.vertices) {
            Map<String, Integer> currentEdgesWeights = new HashMap<>();
            Collection<String> allJumpsByVertex = syncAut.getAllJumpsByVertex(firstVertex);

            for (String secondVertex : syncAut.vertices) {
                if (allJumpsByVertex.contains(secondVertex) && !Objects.equals(firstVertex, secondVertex))
                    currentEdgesWeights.put(secondVertex, 1);
                else
                    currentEdgesWeights.put(secondVertex, 32767);
            }

            edgeWeights.put(firstVertex, currentEdgesWeights);
        }
    }

    private static String findMinDistance(Set<String> verticesSet, Map<String, Integer> startVertexDistance) {
        int minDistance = 32767;
        String vertex = "-1";
        for (String currentVertex : verticesSet) {
            if (startVertexDistance.get(currentVertex) < minDistance) {
                minDistance = startVertexDistance.get(currentVertex);
                vertex = currentVertex;
            }
        }
        return vertex;
    }

    private static boolean ifNewBetterThanOld
        (Map<String, Integer> distanceFromStartVertex,
         Map<String, Map<String, Integer>> edgeWeights,
         String minDistanceVertex, String currentVertex) {

        return distanceFromStartVertex.get(minDistanceVertex) + edgeWeights.get(minDistanceVertex).get(currentVertex)
            < distanceFromStartVertex.get(currentVertex);
    }

    private static String getFinishVertex(Map<String, Integer> distanceFromStartVertex) {
        Map<String, Integer> oneElementVerticesDistance = new HashMap<>();
        for (Entry<String, Integer> entrySet : distanceFromStartVertex.entrySet())
            if (entrySet.getKey().length() == 1)
                oneElementVerticesDistance.put(entrySet.getKey(), entrySet.getValue());

        int minDistance = 32767;
        String finishVertex = "";
        for (Entry<String, Integer> entrySet : oneElementVerticesDistance.entrySet())
            if (entrySet.getValue() < minDistance) {
                finishVertex = entrySet.getKey();
                minDistance = entrySet.getValue();
            }
        return finishVertex;
    }

    private static List<String> findRoute
        (SynchronizedAutomaton syncAut, Map<String, String> previousVertex, String startVertex, String finishVertex) {
        if (finishVertex.isEmpty())
            return new ArrayList<>();

        List<String> resultRoute = new ArrayList<>();
        List<String> letters = syncAut.letters;

        for (String letter : letters)
            if (Objects.equals(syncAut.getJumpByVertexAndLetter(previousVertex.get(finishVertex), letter),
                finishVertex)) {
                resultRoute.add(letter);
                break;
            }

        String currentVertex = finishVertex;
        while (!Objects.equals(previousVertex.get(currentVertex), startVertex)) {
            currentVertex = previousVertex.get(currentVertex);

            for (String letter : letters)
                if (Objects.equals(
                    syncAut.getJumpByVertexAndLetter(previousVertex.get(currentVertex), letter),
                    currentVertex)) {
                    resultRoute.add(letter);
                    break;
                }
        }
        Collections.reverse(resultRoute);

        return resultRoute;
    }
}

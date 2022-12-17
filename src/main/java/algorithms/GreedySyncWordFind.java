package algorithms;

import automaton.SynchronizedAutomaton;
import com.google.common.collect.HashBasedTable;

import java.util.*;

public class GreedySyncWordFind {
    private GreedySyncWordFind() {}

    public static SynchronizedAutomaton AddTwoElementVertices (SynchronizedAutomaton synchronizedAutomaton) throws CloneNotSupportedException {
        SynchronizedAutomaton syncAut = synchronizedAutomaton.clone();
        List<String> vertices = new ArrayList<>(syncAut.vertices);
        for (int i = 0; i < vertices.size(); i++) {
            String vertex1 = vertices.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                String vertex2 = vertices.get(j);
                HashBasedTable<String, String, String> vertexRow = HashBasedTable.create();
                String vertexName = String.join(", ", vertex1 , vertex2);
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
                        vertexRow.put(vertexName, letter, String.join(", ", fromJump1, fromJump2));
                    }
                }
                syncAut.addVertex(vertexName, vertexRow);
            }
        }
        return syncAut;
    }

    public static String FindFirstSyncWordLetter (SynchronizedAutomaton syncAut) {
        for (String twoElementVertex : syncAut.twoElementVertices) {
            Collection<String> jumps = syncAut.getAllJumpsByVertex(twoElementVertex);
            for (String jump : jumps)
                if (jump.length() == 1)
                    for (String letter : syncAut.letters)
                        if (jump.equals(syncAut.getJumpByVertexAndLetter(twoElementVertex, letter)))
                            return letter;
        }
        throw new IllegalArgumentException("Automaton is not synchronizable");
    }

    public static String FindSyncWord (SynchronizedAutomaton synchronizedAutomaton) throws CloneNotSupportedException {
        SynchronizedAutomaton syncAut = AddTwoElementVertices(synchronizedAutomaton);
        String w1 = FindFirstSyncWordLetter(syncAut);
        List<String> redTEVertices = syncAut.twoElementVertices;
        HashSet<String> iterRedTEVertices = new HashSet<>();
        for (String vertex : redTEVertices) {
            String jump = syncAut.getJumpByVertexAndLetter(vertex, w1);
            if (jump.length() != 1)
                iterRedTEVertices.add(jump);
        }
        for (String iterRedTEVertex : iterRedTEVertices) {
            System.out.println(iterRedTEVertex + "|");
        }
        return w1;
    }
}

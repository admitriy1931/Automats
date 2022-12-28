package automaton;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Automaton implements Cloneable, Serializable {
    public Boolean isFinalized;
    public HashBasedTable<String, String, String> jumpTable;
    public List<String> vertices;
    public List<String> letters;
    public String startVertex;
    public List<String> finalVertices;
    public static final String NON_EXISTING_VERTEX = "This vertex doesn't exist";
    public static final String NON_EXISTING_LETTER = "This letter doesn't exist";

    public Automaton(Boolean isFinalized, HashBasedTable<String, String, String> jumpTable,
                     String startVertex, List<String> finalVertices) {
        this.isFinalized = isFinalized;
        this.jumpTable = jumpTable;
        this.vertices = Lists.newArrayList(jumpTable.rowKeySet());
        this.letters = Lists.newArrayList(jumpTable.columnKeySet());
        this.startVertex = startVertex;
        this.finalVertices = finalVertices;
    }

    public Collection<String> getAllJumpsByVertex(String vertex) {
        if (!vertices.contains(vertex)) throw new IllegalArgumentException(NON_EXISTING_VERTEX);
        return jumpTable.row(vertex).values();
    }

    public Collection<String> getAllJumpsByLetter(String letter) {
        if (!letters.contains(letter)) throw new IllegalArgumentException(NON_EXISTING_LETTER);
        return jumpTable.column(letter).values();
    }

    public String getJumpByVertexAndLetter(String vertex, String letter) {
        if (!vertices.contains(vertex)) throw new IllegalArgumentException(NON_EXISTING_VERTEX);
        else if (!letters.contains(letter)) throw new IllegalArgumentException(NON_EXISTING_LETTER);
        return jumpTable.get(vertex, letter);
    }

    public void addVertex(String vertexName, HashBasedTable<String, String, String> vertexRow) {
        if (vertexRow.rowKeySet().size() != 1 || vertexRow.columnKeySet().size() != letters.size())
            throw new IllegalArgumentException("incorrect vertex row given");
        jumpTable.putAll(vertexRow);
        vertices.add(vertexName);
    }

    public void removeVertex(String vertex) {
        if (!vertices.contains(vertex)) throw new IllegalArgumentException(NON_EXISTING_VERTEX);
        finalVertices.remove(vertex);
        jumpTable.row(vertex).clear();
        vertices.remove(vertex);
    }

    public int getVertexStatus(String vertex) {
        if (!vertices.contains(vertex)) throw new IllegalArgumentException(NON_EXISTING_VERTEX);
        if (startVertex.equals(vertex)) return -1;
        if (finalVertices.contains(vertex)) return 1;
        return 0;
    }

    public boolean isVertexStock(String vertex) {
        if (!vertices.contains(vertex)) throw new IllegalArgumentException(NON_EXISTING_VERTEX);
        List<String> distinctJumps = getAllJumpsByVertex(vertex).stream().distinct().collect(Collectors.toList());
        return distinctJumps.size() == 1 && distinctJumps.contains(vertex) && !finalVertices.contains(vertex);
    }

    @Override
    public Automaton clone() throws CloneNotSupportedException {
        Automaton automaton = (Automaton) super.clone();

        automaton.jumpTable = HashBasedTable.create(jumpTable);
        automaton.vertices = new ArrayList<>(vertices);
        automaton.letters = new ArrayList<>(letters);
        automaton.finalVertices = new ArrayList<>(finalVertices);

        return automaton;
    }
}

package automat;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.List;

public class Automat {
    public Boolean isFinalised;
    public HashBasedTable<String, String, String> jumpTable;
    public List<String> vertexes;
    public List<String> letters;
    public String startVertex;
    public List<String> finalVertexes;
    private static final String VERTEX_NOT_EXISTING = "This vertex doesn't exist";
    private static final String LETTER_NOT_EXISTING = "This letter doesn't exist";

    public Automat(Boolean isFinalised, HashBasedTable<String, String, String> jumpTable,
                   String startVertex, List<String> finalVertexes) {
        this.isFinalised = isFinalised;
        this.jumpTable = jumpTable;
        this.vertexes = Lists.newArrayList(jumpTable.rowKeySet());
        this.letters = Lists.newArrayList(jumpTable.columnKeySet());
        this.startVertex = startVertex;
        this.finalVertexes = finalVertexes;
    }

    public Collection<String> getAllJumpsByVertex(String vertex) {
        if (!vertexes.contains(vertex)) throw new IllegalArgumentException(VERTEX_NOT_EXISTING);
        return jumpTable.row(vertex).values();
    }

    public Collection<String> getAllJumpsByLetter(String letter) {
        if (!letters.contains(letter)) throw new IllegalArgumentException(LETTER_NOT_EXISTING);
        return jumpTable.column(letter).values();
    }

    public String getJumpByVertexAndLetter(String vertex, String letter) {
        if (!vertexes.contains(vertex)) throw new IllegalArgumentException(VERTEX_NOT_EXISTING);
        else if (!letters.contains(letter)) throw new IllegalArgumentException(LETTER_NOT_EXISTING);
        return jumpTable.get(vertex, letter);
    }

    public void addVertex(Table<String, String, String> vertexRow) {
        if (vertexRow.rowKeySet().size() != 1 || vertexRow.columnKeySet().size() != vertexes.size())
            throw new IllegalArgumentException("incorrect vertex row given");
        jumpTable.putAll(vertexRow);
        vertexes.add(String.valueOf(vertexRow.columnKeySet()));
    }

    public void removeVertex(String vertex) {
        if (!vertexes.contains(vertex)) throw new IllegalArgumentException(VERTEX_NOT_EXISTING);
        finalVertexes.remove(vertex);
        jumpTable.row(vertex).clear();
        vertexes.remove(vertex);
    }
}

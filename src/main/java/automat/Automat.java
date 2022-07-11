package automat;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Automat {
    public Boolean isFinalised;
    public HashBasedTable<String, String, String> jumpTable;
    public List<String> vertexes;
    public List<String> letters;
    public String startVertex;
    public List<String> finalVertexes;
    private static final String VERTEX_NOT_EXISTING = "This vertex doesn't exist";
    private static final String LETTER_NOT_EXISTING = "This letter doesn't exist";

    public Automat(Boolean isFinalised, HashBasedTable<String, String, String> jumpTable, String startVertex, List<String> finalVertexes) {
        this.isFinalised = isFinalised;
        this.jumpTable = jumpTable;
        this.vertexes = Lists.newArrayList(jumpTable.rowKeySet());
        this.letters = Lists.newArrayList(jumpTable.columnKeySet());
        this.startVertex = startVertex;
        this.finalVertexes = finalVertexes;
    }

    public Collection<String> getAllJumpsByVertex(String vertex) {
        if (!this.vertexes.contains(vertex)) {
            throw new IllegalArgumentException("This vertex doesn't exist");
        } else {
            return this.jumpTable.row(vertex).values();
        }
    }

    public Collection<String> getAllJumpsByLetter(String letter) {
        if (!this.letters.contains(letter)) {
            throw new IllegalArgumentException("This letter doesn't exist");
        } else {
            return this.jumpTable.column(letter).values();
        }
    }

    public String getJumpByVertexAndLetter(String vertex, String letter) {
        if (!this.vertexes.contains(vertex)) {
            throw new IllegalArgumentException("This vertex doesn't exist");
        } else if (!this.letters.contains(letter)) {
            throw new IllegalArgumentException("This letter doesn't exist");
        } else {
            return (String)this.jumpTable.get(vertex, letter);
        }
    }

    public void addVertex(Table<String, String, String> vertexRow) {
        if (vertexRow.rowKeySet().size() == 1 && vertexRow.columnKeySet().size() == this.vertexes.size()) {
            this.jumpTable.putAll(vertexRow);
            this.vertexes.add(String.valueOf(vertexRow.columnKeySet()));
        } else {
            throw new IllegalArgumentException("incorrect vertex row given");
        }
    }

    public void removeVertex(String vertex) {
        if (!this.vertexes.contains(vertex)) {
            throw new IllegalArgumentException("This vertex doesn't exist");
        } else {
            this.jumpTable.row(vertex).clear();
            this.vertexes.remove(vertex);
        }
    }
}

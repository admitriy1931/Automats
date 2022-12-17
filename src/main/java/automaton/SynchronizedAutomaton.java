package automaton;

import com.google.common.collect.HashBasedTable;

import java.io.Serializable;
import java.util.List;

public class SynchronizedAutomaton extends Automaton implements Cloneable, Serializable{
    public String syncVertex;
    public List<String> shortestSyncWord;
    public List<String> twoElementVertices;
    public SynchronizedAutomaton(Boolean isFinalized, HashBasedTable<String, String, String> jumpTable,
                                 String startVertex, List<String> finalVertices, String syncVertex,
                                 List<String> shortestSyncWord, List<String> twoElementVertices) {
        super(isFinalized, jumpTable, startVertex, finalVertices);
        this.syncVertex = syncVertex;
        this.shortestSyncWord = shortestSyncWord;
        this.twoElementVertices = twoElementVertices;
    }

    @Override
    public SynchronizedAutomaton clone() throws CloneNotSupportedException {
        return (SynchronizedAutomaton) super.clone();
    }
}

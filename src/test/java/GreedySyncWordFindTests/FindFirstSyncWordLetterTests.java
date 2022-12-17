package GreedySyncWordFindTests;

import algorithms.GreedySyncWordFind;
import automaton.SynchronizedAutomaton;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FindFirstSyncWordLetterTests {
    @Test
    public void FindFirstSyncWordLetter1 () throws CloneNotSupportedException {
        HashBasedTable<String, String, String> table = HashBasedTable.create();
        table.put("0", "a", "5");
        table.put("1", "a", "6");
        table.put("2", "a", "0");
        table.put("3", "a", "3");
        table.put("4", "a", "6");
        table.put("5", "a", "3");
        table.put("6", "a", "3");

        table.put("0", "b", "2");
        table.put("1", "b", "2");
        table.put("2", "b", "4");
        table.put("3", "b", "5");
        table.put("4", "b", "2");
        table.put("5", "b", "0");
        table.put("6", "b", "1");
        SynchronizedAutomaton initial = new SynchronizedAutomaton(false, table, "0", null,
                null, null, new ArrayList<>());
        SynchronizedAutomaton result = GreedySyncWordFind.AddTwoElementVertices(initial);
        Assertions.assertEquals("b", GreedySyncWordFind.FindFirstSyncWordLetter(result));
    }
}


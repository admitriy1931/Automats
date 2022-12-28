package GreedySyncWordFindingTests;

import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import algorithms.GreedySyncWordFinding;
import automaton.SynchronizedAutomaton;

public class IsAutomatonSynchronizedTests {
    @Test
    public void SimpleTest() throws CloneNotSupportedException {
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

        jumpTable.put("0", "a", "0");
        jumpTable.put("1", "a", "1");

        jumpTable.put("0", "b", "1");
        jumpTable.put("1", "b", "0");

        SynchronizedAutomaton syncAut = new SynchronizedAutomaton
            (jumpTable, new ArrayList<>(), new ArrayList<>(), null, null);

        Assertions.assertFalse(GreedySyncWordFinding.isAutomatonSynchronized(syncAut));
    }
}

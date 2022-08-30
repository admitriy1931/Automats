package IsomorphismTests;

import algorithms.Isomorphism;
import automaton.Automaton;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class EndOfWordTest {
    @Test
    public void simpleTest1(){
        var aut = makeAut1(0);
        var expected = new ArrayList<String>();
        expected.add("a");
        expected.add("b");
        expected.add("");

        var actual = Isomorphism.getEndOfWord("1", "", aut);

        Assertions.assertEquals(actual, expected);

        actual = Isomorphism.getEndOfWord("2", "", aut);
        expected.clear();
        expected.add("a");
        expected.add("");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void simpleTest2(){
        var aut = makeAut2(0);

        var expected = new ArrayList<String>();
        expected.add("b");
        expected.add("a");
        expected.add("a");
        expected.add("a");
        expected.add("");

        var actual = Isomorphism.getEndOfWord("1", "", aut);

        Assertions.assertEquals(expected, actual);
    }

    public static Automaton makeAut1(Integer shift){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

        var v1 = String.valueOf(1 + shift);
        var v2 = String.valueOf(2 + shift);
        var v3 = String.valueOf(3 + shift);
        var v4 = String.valueOf(4 + shift);

        jumpTable.put(v1, "a", v4);
        jumpTable.put(v1, "b", v2);

        jumpTable.put(v2, "a", v3);
        jumpTable.put(v2, "b", v4);

        jumpTable.put(v3, "a", v4);
        jumpTable.put(v3, "b", v4);

        jumpTable.put(v4, "a", v4);
        jumpTable.put(v4, "b", v4);

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add(v3);

        return new Automaton(false, jumpTable, v1, finalVertexes);
    }

    public static Automaton makeAut2(Integer shift){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

        var v1 = String.valueOf(1 + shift);
        var v2 = String.valueOf(2 + shift);
        var v3 = String.valueOf(3 + shift);
        var v4 = String.valueOf(4 + shift);
        var v5 = String.valueOf(5 + shift);
        var v6 = String.valueOf(6 + shift);
        var v7 = String.valueOf(7 + shift);

        jumpTable.put(v1, "a", v2);
        jumpTable.put(v1, "b", v3);
        jumpTable.put(v1, "c", v4);

        jumpTable.put(v2, "a", v3);
        jumpTable.put(v2, "b", v4);
        jumpTable.put(v2, "c", v5);

        jumpTable.put(v3, "a", v5);
        jumpTable.put(v3, "b", v7);
        jumpTable.put(v3, "c", v7);

        jumpTable.put(v4, "a", v7);
        jumpTable.put(v4, "b", v7);
        jumpTable.put(v4, "c", v7);

        jumpTable.put(v5, "a", v7);
        jumpTable.put(v5, "b", v6);
        jumpTable.put(v5, "c", v7);

        jumpTable.put(v6, "a", v7);
        jumpTable.put(v6, "b", v7);
        jumpTable.put(v6, "c", v7);

        jumpTable.put(v7, "a", v7);
        jumpTable.put(v7, "b", v7);
        jumpTable.put(v7, "c", v7);

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add(v6);

        return new Automaton(false, jumpTable, v1, finalVertexes);
    }
}

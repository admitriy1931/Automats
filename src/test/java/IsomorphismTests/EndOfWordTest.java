package IsomorphismTests;

import algorithms.Isomorphism;
import automat.Automat;
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
        var aut = makeAut2();

        var expected = new ArrayList<String>();
        expected.add("b");
        expected.add("a");
        expected.add("a");
        expected.add("a");
        expected.add("");

        var actual = Isomorphism.getEndOfWord("1", "", aut);

        Assertions.assertEquals(expected, actual);
    }

    public static Automat makeAut1(Integer shift){
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

        return new Automat(false, jumpTable, v1, finalVertexes);
    }

    public static Automat makeAut2(){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

        jumpTable.put("1", "a", "2");
        jumpTable.put("1", "b", "3");
        jumpTable.put("1", "c", "4");

        jumpTable.put("2", "a", "3");
        jumpTable.put("2", "b", "4");
        jumpTable.put("2", "c", "5");

        jumpTable.put("3", "a", "5");
        jumpTable.put("3", "b", "7");
        jumpTable.put("3", "c", "7");

        jumpTable.put("4", "a", "7");
        jumpTable.put("4", "b", "7");
        jumpTable.put("4", "c", "7");

        jumpTable.put("5", "a", "7");
        jumpTable.put("5", "b", "6");
        jumpTable.put("5", "c", "7");

        jumpTable.put("6", "a", "7");
        jumpTable.put("6", "b", "7");
        jumpTable.put("6", "c", "7");

        jumpTable.put("7", "a", "7");
        jumpTable.put("7", "b", "7");
        jumpTable.put("7", "c", "7");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("6");

        return new Automat(false, jumpTable, "1", finalVertexes);
    }
}

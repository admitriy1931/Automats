package IsomorphismTests;

import algorithms.Isomorphism;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class EndOfWordTest {
    @Test
    public void simpleTest(){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

        jumpTable.put("1", "a", "2");
        jumpTable.put("1", "b", "4");

        jumpTable.put("2", "a", "3");
        jumpTable.put("2", "b", "4");

        jumpTable.put("3", "a", "4");
        jumpTable.put("3", "b", "4");

        jumpTable.put("4", "a", "4");
        jumpTable.put("4", "b", "4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut = new Automat(false, jumpTable, "1", finalVertexes);
        var expected = new ArrayList<String>();
        expected.add("3");
        expected.add("2");
        expected.add("1");

        var word = Isomorphism.getEndOfWord("1", "", aut);

        Assertions.assertEquals(word, expected);
    }
}

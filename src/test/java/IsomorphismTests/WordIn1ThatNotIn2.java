package IsomorphismTests;

import algorithms.Isomorphism;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class WordIn1ThatNotIn2 {
    @BeforeEach
    public void clear(){
        Isomorphism.clear();
    }

    @Test
    public void noWordTest(){
        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = EndOfWordTest.makeAut1(1);

        var actual = Isomorphism.findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, "");

        Assertions.assertEquals(0, actual.size());

        clear();
        actual = Isomorphism.findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, "");

        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void emptyWordTest(){
        var aut1 = EndOfWordTest.makeAut1(0);
        aut1.finalVertexes.add("1");
        var aut2 = EndOfWordTest.makeAut1(0);

        var actual = Isomorphism.findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, "");

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("", actual.get(0));

        clear();
        actual = Isomorphism.findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, "");

        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void shortWordTest(){
        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = EndOfWordTest.makeAut1(0);
        aut2.finalVertexes.add("2");

        var actual = Isomorphism.findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, "");

        Assertions.assertEquals(0, actual.size());

        clear();
        actual = Isomorphism.findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, "");

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("b", actual.get(0));
    }

    @Test
    public void cycleTest(){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();
        jumpTable.put("1", "a", "4");
        jumpTable.put("1", "b", "2");

        jumpTable.put("2", "a", "1");
        jumpTable.put("2", "b", "3");

        jumpTable.put("3", "a", "4");
        jumpTable.put("3", "b", "4");
        jumpTable.put("4", "a", "4");
        jumpTable.put("4", "b", "4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = new Automat(false, jumpTable, "1", finalVertexes);

        var actual = Isomorphism.findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, "");
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("a", actual.get(0));
        Assertions.assertEquals("b", actual.get(1));

        clear();
        actual = Isomorphism.findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, "");
        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals("b", actual.get(0));
        Assertions.assertEquals("b", actual.get(1));
        Assertions.assertEquals("a", actual.get(2));
        Assertions.assertEquals("b", actual.get(3));
    }
}

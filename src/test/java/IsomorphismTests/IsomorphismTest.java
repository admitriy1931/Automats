package IsomorphismTests;

import algorithms.Isomorphism;
import automaton.Automaton;
import automaton.IsomorphismResult;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class IsomorphismTest {
    public IsomorphismResult automatsAreIsomorphic(Automaton aut1, Automaton aut2){
        try {
            return Isomorphism.automatsAreIsomorphic(aut1, aut2);
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void shiftedAutomatTest(){
        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = EndOfWordTest.makeAut1(1);

        var expectedAssociations = new HashMap<String, String>();
        expectedAssociations.put("1", "2");
        expectedAssociations.put("2", "3");
        expectedAssociations.put("3", "4");
        expectedAssociations.put("4", "5");
        var expected = new IsomorphismResult(null, null, expectedAssociations);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void emptyWordTest(){
        var aut1 = EndOfWordTest.makeAut1(0);
        aut1.finalVertices.add("1");
        var aut2 = EndOfWordTest.makeAut1(0);

        var expected = new IsomorphismResult("", null, null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult(null, "", null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void differentFinalVertexesTest(){
        var aut1 = EndOfWordTest.makeAut1(0);
        aut1.finalVertices.add("2");
        var aut2 = EndOfWordTest.makeAut1(0);

        var expected = new IsomorphismResult("b", null, null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult(null, "b", null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void differentStocksTest(){
        var jumpTable = makeTable(
                "1a4\n" + "1b2\n" +
                "2a2\n" + "2b2\n" +
                "3a4\n" + "3b4\n" +
                "4a4\n" + "4b4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = new Automaton(false, jumpTable, "1", finalVertexes);

        var expected = new IsomorphismResult("ba", null, null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult(null, "ba", null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void differentCyclesTest(){
        var jumpTable = makeTable(
                "1a4\n" + "1b2\n" +
                "2a3\n" + "2b1\n" +
                "3a4\n" + "3b4\n" +
                "4a4\n" + "4b4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = new Automaton(false, jumpTable, "1", finalVertexes);

        var expected = new IsomorphismResult(null, "bbba", null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult("bbba", null, null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
   }

   @Test
   public void differentCycles2Test(){
        var jumpTable = makeTable(
                "1a4\n" + "1b2\n" +
                "2a1\n" + "2b3\n" +
                "3a4\n" + "3b4\n" +
                "4a4\n" + "4b4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = new Automaton(false, jumpTable, "1", finalVertexes);

        var expected = new IsomorphismResult("ba", "babb", null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult("babb", "ba", null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
   }

    @Test
    public void notAdductedAutomatsTest(){
        var jumpTable1 = makeTable(
                "1a2\n" + "1b2\n" +
                "2a3\n" + "2b3\n" +
                "3a3\n" + "3b3");

        var finalVertexes1 = new ArrayList<String>();
        finalVertexes1.add("2");

        var aut1 = new Automaton(false, jumpTable1, "1", finalVertexes1);

        var jumpTable2 = makeTable(
                "1a3\n" + "1b2\n" +
                "2a4\n" + "2b4\n" +
                "3a4\n" + "3b4\n" +
                "4a4\n" + "4b4");

        var finalVertexes2 = new ArrayList<String>();
        finalVertexes2.add("2");
        finalVertexes2.add("3");

        var aut2 = new Automaton(false, jumpTable2, "1", finalVertexes2);

        var expectedAssociations = new HashMap<String, String>();
        expectedAssociations.put("1", "1");
        expectedAssociations.put("2", "2, 3");
        expectedAssociations.put("3", "4");
        var expected = new IsomorphismResult(null, null, expectedAssociations);

        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bigShiftedAutomats(){
        var aut1 = EndOfWordTest.makeAut2(0);
        var aut2 = EndOfWordTest.makeAut2(10);

        var expectedAssociations = new HashMap<String, String>();
        expectedAssociations.put("1", "11");
        expectedAssociations.put("2", "12");
        expectedAssociations.put("3", "13");
        expectedAssociations.put("5", "15");
        expectedAssociations.put("6", "16");
        expectedAssociations.put("4, 7", "14, 17");
        var expected = new IsomorphismResult(null, null, expectedAssociations);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void differentVertexesSameLanguageTest(){
        var jumpTable1 = makeTable(
                "1a2\n" + "1b6\n" +
                "2a6\n" + "2b3\n" +
                "3a4\n" + "3b6\n" +
                "4a6\n" + "4b5\n" +
                "5a4\n" + "5b6\n" +
                "6a6\n" + "6b6");
        var finalVertexes1 = new ArrayList<String>();
        finalVertexes1.add("2");
        finalVertexes1.add("4");
        var aut1 = new Automaton(false, jumpTable1, "1", finalVertexes1);

        var jumpTable2 = makeTable(
                "1a2\n" + "1b3\n" +
                "2a3\n" + "2b1\n" +
                "3a3\n" + "3b3");
        var finalVertexes2 = new ArrayList<String>();
        finalVertexes2.add("2");
        var aut2 = new Automaton(false, jumpTable2, "1", finalVertexes2);

        var expectedAssociations = new HashMap<String, String>();
        expectedAssociations.put("1, 3, 5", "1");
        expectedAssociations.put("2, 4", "2");
        expectedAssociations.put("6", "3");
        var expected = new IsomorphismResult(null, null, expectedAssociations);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected ,actual);
    }

    public static HashBasedTable<String, String, String> makeTable(String data){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();
        for (var row : data.split("\n")) {
            var splittedRow = row.split("");
            jumpTable.put(splittedRow[0], splittedRow[1], splittedRow[2]);
        }
        return jumpTable;
    }
}

package IsomorphismTests;

import algorithms.Isomorphism;
import automat.Automat;
import automat.IsomorphismResult;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class IsomorphismTest {
    public IsomorphismResult automatsAreIsomorphic(Automat aut1, Automat aut2){
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
        aut1.finalVertexes.add("1");
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
        aut1.finalVertexes.add("2");
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
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();
        jumpTable.put("1", "a", "4");
        jumpTable.put("1", "b", "2");

        jumpTable.put("2", "a", "2");
        jumpTable.put("2", "b", "2");

        jumpTable.put("3", "a", "4");
        jumpTable.put("3", "b", "4");
        jumpTable.put("4", "a", "4");
        jumpTable.put("4", "b", "4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = new Automat(false, jumpTable, "1", finalVertexes);

        var expected = new IsomorphismResult("ba", null, null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult(null, "ba", null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void differentCyclesTest(){
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();
        jumpTable.put("1", "a", "4");
        jumpTable.put("1", "b", "2");

        jumpTable.put("2", "a", "3");
        jumpTable.put("2", "b", "1");

        jumpTable.put("3", "a", "4");
        jumpTable.put("3", "b", "4");
        jumpTable.put("4", "a", "4");
        jumpTable.put("4", "b", "4");

        var finalVertexes = new ArrayList<String>();
        finalVertexes.add("3");

        var aut1 = EndOfWordTest.makeAut1(0);
        var aut2 = new Automat(false, jumpTable, "1", finalVertexes);

        var expected = new IsomorphismResult(null, "bbba", null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult("bbba", null, null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
   }

   public void differentCycles2Test(){
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

        var expected = new IsomorphismResult("ba", "babb", null);
        var actual = automatsAreIsomorphic(aut1, aut2);
        Assertions.assertEquals(expected, actual);

        expected = new IsomorphismResult("babb", "ba", null);
        actual = automatsAreIsomorphic(aut2, aut1);
        Assertions.assertEquals(expected, actual);
   }

    @Test
    public void notAdductedAutomatsTest(){
        HashBasedTable<String, String, String> jumpTable1 = HashBasedTable.create();

        jumpTable1.put("1", "a", "2");
        jumpTable1.put("1", "b", "2");
        jumpTable1.put("2", "a", "3");
        jumpTable1.put("2", "b", "3");
        jumpTable1.put("3", "a", "3");
        jumpTable1.put("3", "b", "3");

        var finalVertexes1 = new ArrayList<String>();
        finalVertexes1.add("2");

        var aut1 = new Automat(false, jumpTable1, "1", finalVertexes1);

        HashBasedTable<String, String, String> jumpTable2 = HashBasedTable.create();

        jumpTable2.put("1", "a", "3");
        jumpTable2.put("1", "b", "2");
        jumpTable2.put("2", "a", "4");
        jumpTable2.put("2", "b", "4");
        jumpTable2.put("3", "a", "4");
        jumpTable2.put("3", "b", "4");
        jumpTable2.put("4", "a", "4");
        jumpTable2.put("4", "b", "4");

        var finalVertexes2 = new ArrayList<String>();
        finalVertexes2.add("2");
        finalVertexes2.add("3");

        var aut2 = new Automat(false, jumpTable2, "1", finalVertexes2);

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
}

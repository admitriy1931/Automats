package AdductionTests;

import algorithms.Adduction;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReachabilityTest {
    @Test
    public void reachabilityTest1(){
        HashBasedTable<String, String, String> first = HashBasedTable.create();
        first.put("0", "a", "5");
        first.put("1", "a", "6");
        first.put("2", "a", "0");
        first.put("3", "a", "3");
        first.put("4", "a", "6");
        first.put("5", "a", "3");
        first.put("6", "a", "3");

        first.put("0", "b", "2");
        first.put("1", "b", "2");
        first.put("2", "b", "4");
        first.put("3", "b", "5");
        first.put("4", "b", "2");
        first.put("5", "b", "0");
        first.put("6", "b", "1");

        List<String> firstFinal = Lists.newArrayList("4", "5", "6");

        Automat result = Adduction.excludeUnreachableVertexes(new Automat(false, first, "0", firstFinal));
        Automat expected = new Automat(false, first, "0", firstFinal);

        Assertions.assertEquals(expected.jumpTable, result.jumpTable);
    }

    @Test
    public void reachabilityTest2(){
        HashBasedTable<String, String, String> first = HashBasedTable.create();
        first.put("0", "a", "1");
        first.put("1", "a", "1");
        first.put("2", "a", "1");
        first.put("3", "a", "1");
        first.put("4", "a", "5");
        first.put("5", "a", "1");
        first.put("6", "a", "4");

        first.put("0", "b", "3");
        first.put("1", "b", "0");
        first.put("2", "b", "2");
        first.put("3", "b", "2");
        first.put("4", "b", "2");
        first.put("5", "b", "0");
        first.put("6", "b", "5");

        List<String> firstFinal = Lists.newArrayList("2", "3", "4");
        Automat result = Adduction.excludeUnreachableVertexes(new Automat(false, first, "0", firstFinal));

        first.row("4").clear();
        first.row("5").clear();
        first.row("6").clear();

        Automat expected = new Automat(false, first, "0", firstFinal);

        Assertions.assertEquals(expected.jumpTable, result.jumpTable);
    }
}

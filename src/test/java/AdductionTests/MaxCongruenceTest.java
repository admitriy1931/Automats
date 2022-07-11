package AdductionTests;

import algorithms.Adduction;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MaxCongruenceTest {
    @Test
    public void maxCongruenceTest1 () {
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

        List<List<String>> result = Adduction.buildMaxCongruence(new Automat(
                false, first, "0", firstFinal));

        List<List<String>> expected = Lists.newArrayList();
        expected.add(Lists.newArrayList("0", "1"));
        expected.add(Lists.newArrayList("2"));
        expected.add(Lists.newArrayList("3"));
        expected.add(Lists.newArrayList("4"));
        expected.add(Lists.newArrayList("5", "6"));

        Assertions.assertTrue(expected.containsAll(result));
    }

    @Test
    public void maxCongruenceTest2 () {
        HashBasedTable<String, String, String> first = HashBasedTable.create();
        first.put("0", "a", "1");
        first.put("1", "a", "1");
        first.put("2", "a", "1");
        first.put("3", "a", "1");

        first.put("0", "b", "3");
        first.put("1", "b", "0");
        first.put("2", "b", "2");
        first.put("3", "b", "2");

        List<String> firstFinal = Lists.newArrayList("2", "3");

        List<List<String>> result = Adduction.buildMaxCongruence(new Automat(
                false, first, "0", firstFinal));

        List<List<String>> expected = Lists.newArrayList();
        expected.add(Lists.newArrayList("0"));
        expected.add(Lists.newArrayList("1"));
        expected.add(Lists.newArrayList("2", "3"));

        Assertions.assertTrue(expected.containsAll(result));
    }
}

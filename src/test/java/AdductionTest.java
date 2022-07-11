import algorithms.Adduction;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class AdductionTest {
    @Test
    public void reachabilityTest(){
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

        Set<String> firstFinal = Sets.newHashSet("4", "5", "6");

        HashBasedTable<String, String, String> second = HashBasedTable.create();
        second.put("0", "a", "5");
        second.put("1", "a", "6");
        second.put("2", "a", "0");
        second.put("3", "a", "3");
        second.put("4", "a", "6");
        second.put("5", "a", "3");
        second.put("6", "a", "3");

        second.put("0", "b", "2");
        second.put("1", "b", "2");
        second.put("2", "b", "4");
        second.put("3", "b", "5");
        second.put("4", "b", "2");
        second.put("5", "b", "0");
        second.put("6", "b", "1");

        Automat expected = new Automat(false, second, "0", firstFinal);
        Automat result = Adduction.findReachableVertexes(new Automat(false, first, "0", firstFinal));

        System.out.println(result.jumpTable);
        System.out.println(expected.jumpTable);
        Assertions.assertEquals(result.jumpTable, expected.jumpTable);
    }
}

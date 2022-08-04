package AdductionTests;

import algorithms.Adduction;
import automat.Automat;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class buildAdductedTest {
    @Test
    public void buildAdductedTest1 () {
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

        Automat result = null;
        Automat tested = new Automat(
                false, first, "0", firstFinal);
        try {
            result = Adduction.buildAdductedAutomat(tested);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println(tested.jumpTable);
        System.out.println(result.jumpTable);

        HashBasedTable<String, String, String> expected = HashBasedTable.create();
        expected.put("0, 1", "a", "5, 6");
        expected.put("2", "a", "0, 1");
        expected.put("3", "a", "3");
        expected.put("4", "a", "5, 6");
        expected.put("5, 6", "a", "3");

        expected.put("0, 1", "b", "2");
        expected.put("2", "b", "4");
        expected.put("3", "b", "5, 6");
        expected.put("4", "b", "2");
        expected.put("5, 6", "b", "0, 1");

        List<String> expectedFinalVertexes = Lists.newArrayList("4", "5, 6");

        assert result != null;
        Assertions.assertEquals("0, 1", result.startVertex);
        Assertions.assertEquals(expectedFinalVertexes, result.finalVertexes);
        Assertions.assertEquals(expected, result.jumpTable);
    }
}

package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.RegexpExeption;

public class EmptyWordTest {
    public Boolean standardTest(String regexp){
        try {
            return RegExprBuild.allowEmptyWord(regexp);
        }
        catch (RegexpExeption regexpExeption){
            return null;
        }
    }

    @Test
    public void letterWithIterationPositiveTest(){
        var actual = standardTest("a*");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void sumWithIterationPositiveTest(){
        var actual = standardTest("a + b* + c");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);

        actual = standardTest("a + (b+c)* + c");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void concatWithIterationPositiveTest(){
        var actual = standardTest("a*b*c*");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);

        actual = standardTest("a*(bc)*c*");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void concatWithIterationNegativeTest(){
        var actual = standardTest("a*bc*");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);

        actual = standardTest("a*(bc)c*");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void letterWithIterationNegativeTest(){
        var actual = standardTest("a");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void sumWithIterationNegativeTest(){
        var actual = standardTest("a + b + c");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);

        actual = standardTest("a + (b+c) + c");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }
}

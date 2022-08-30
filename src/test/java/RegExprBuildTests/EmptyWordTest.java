package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.RegexpException;

public class EmptyWordTest {
    private Boolean standardTest(String regexp){
        try {
            return RegExprBuild.allowEmptyWord(regexp);
        }
        catch (RegexpException regexpExeption){
            return null;
        }
    }

    private void standardPositiveTest(String regexp){
        var actual = standardTest(regexp);

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    private void standardNegativeTest(String regexp){
        var actual = standardTest(regexp);

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void letterWithIterationPositiveTest(){
        standardPositiveTest("a*");
    }

    @Test
    public void sumWithIterationPositiveTest(){
        standardPositiveTest("a + b* + c");
        standardPositiveTest("a + (b+c)* + c");
    }

    @Test
    public void concatWithIterationPositiveTest(){
        standardPositiveTest("a*b*c*");
        standardPositiveTest("a*(bc)*c*");
    }

    @Test
    public void concatWithIterationNegativeTest(){
        standardNegativeTest("a*bc*");
        standardNegativeTest("a*(bc)c*");
    }

    @Test
    public void letterWithIterationNegativeTest(){
        standardNegativeTest("a");
    }

    @Test
    public void sumWithIterationNegativeTest(){
        standardNegativeTest("a + b + c");
        standardNegativeTest("a + (b+c) + c");
    }

    @Test
    public void complexPositiveTest(){
        standardPositiveTest("a + bb + (c+c)(c+c) + (d*d* + dd)(dd + d*d*)");
    }

    @Test
    public void bigNegativeTest1(){
        standardNegativeTest("c + (dd+d)(d)");
    }

    @Test
    public void emptyWordSymbolSumTest(){
        standardPositiveTest(String.format("a + %s", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolSumMultiplyTest(){
        standardPositiveTest(String.format("a + bc + %s", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolMultiplySumTest(){
        standardNegativeTest(String.format("a(bc + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolIterationSumTest(){
        standardPositiveTest(String.format("a*(c + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolSumSumConcatTest(){
        standardPositiveTest(String.format(
                "(a + %s)(b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExamTest(){
        standardPositiveTest(String.format("b*(ab*ab*a + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam1Test(){
        standardPositiveTest(String.format(
                "b(ba)*(b(b + %s) + a) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam2Test(){
        standardPositiveTest(String.format(
                "b(ba)*(b(b + %s) + a) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam3Test(){
        standardNegativeTest(String.format(
                "b*a(b(b + %s) + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam4Test(){
        standardPositiveTest(String.format(
                "ab*a(b + %s) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam5Test(){
        standardNegativeTest(String.format("(ab + ba)*a(a + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam6Test(){
        standardPositiveTest(String.format(
                "a(aa + %s) + b + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam7Test(){
        standardNegativeTest(String.format("(bb + a)b + b(a + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam8Test(){
        standardPositiveTest(String.format(
                "(a + b)(b(a + b + %s) + a) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam9Test(){
        var s = RegExprBuild.emptyWordSymbol;
        standardPositiveTest(String.format("a(b(b + %s) + %s) + %s", s, s, s));
    }

    @Test
    public void emptyWordSymbolExam10Test(){
        standardPositiveTest(String.format(
                "(baa)*(ba(ba* + %s) + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam11Test(){
        standardNegativeTest(String.format("ab* + b(a + b + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam12Test(){
        standardNegativeTest(String.format("ab*(a + %s) + ba*(b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam13Test(){
        standardNegativeTest(String.format("a*b(a(a + %s) + b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam14Test(){
        standardNegativeTest(String.format("(bb + a)(a + %s)", RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam15Test(){
        standardPositiveTest(String.format("(ab + ba)*(a(a + %s) + b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam16Test(){
        standardNegativeTest(String.format("(a + b)((a + b)(a + %s) + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam17Test(){
        standardPositiveTest(String.format("(ba)*(bb(a + b + %s) + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
    }

    @Test
    public void emptyWordSymbolExam18Test(){
        var s = RegExprBuild.emptyWordSymbol;
        standardPositiveTest(String.format("b((a + b)(a + b + %s) + %s) + %s", s, s, s));
    }

    @Test
    public void emptyWordSymbolExam19Test(){
        standardNegativeTest("(ab + ba)*(a + bb)");
    }

    @Test
    public void emptyWordSymbolExam20Test(){
        standardPositiveTest(String.format("(baa)*(b + ab* + %s)", RegExprBuild.emptyWordSymbol));
    }
}

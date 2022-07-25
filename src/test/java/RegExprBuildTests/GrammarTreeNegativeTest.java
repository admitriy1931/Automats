package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.RegexpExeption;

public class GrammarTreeNegativeTest {
    private void standardTest(String regexp, String expectedText, Integer expectedPosition){
        var thrown = Assertions.assertThrows(
                RegexpExeption.class,
                () -> RegExprBuild.makeGrammarTree(regexp));

        Assertions.assertEquals(expectedText, thrown.getText());
        Assertions.assertEquals(expectedPosition, thrown.getWrongSymbolPosition());
    }

    @Test
    public void noClosingBracket1Test(){
        standardTest("(", "Нет закрывающей скобки", 0);
    }

    @Test
    public void wrongSymbolTest(){
        standardTest("a +& b", "Неизвестный символ", 3);
    }

    @Test
    public void noClosingBracket2Test(){
        standardTest("((a + b)", "Нет закрывающей скобки", 7);
    }

    @Test
    public void noOpeningBracket1Test(){
        standardTest(")", "Нет открывающейся скобки", 0);
    }

    @Test
    public void noOpeningBracket2Test(){
        standardTest("a + b)", "Нет открывающейся скобки", 5);
    }

    @Test
    public void noLeftOperand1Test(){
        standardTest("+b", "Нет левого операнда для +", 0);
    }

    @Test
    public void noLeftOperand2Test(){
        standardTest("a(+b)", "Нет левого операнда для +", 2);
    }

    @Test
    public void noLeftOperand3Test(){
        standardTest("(+)", "Нет левого операнда для +", 1);
    }

    @Test
    public void noOperand2Test(){
        standardTest("a+", "Нет правого операнда для +", 1);
    }

    @Test
    public void emptyBracketsTest(){
        standardTest("a()", "Пустые скобки", 2);
    }

    @Test
    public void wrongIterationUsage1Test(){
        standardTest("(*)", "Неправильное использование итерации", 1);
    }

    @Test
    public void wrongIterationUsage2Test(){
        standardTest("*)", "Неправильное использование итерации", 0);
    }

    @Test
    public void doublePlusTest(){
        standardTest("a++b", "Нет левого операнда для +", 2);
    }

    @Test
    public void noRightOperandTest(){
        standardTest("(a+)+b", "Нет правого операнда для +", 2);
    }
}

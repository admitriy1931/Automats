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
    public void noClosingBracketTest(){
        standardTest("(", "Нет закрывающей скобки", 0);
        standardTest("(a", "Нет закрывающей скобки", 1);
        standardTest("((", "Нет закрывающей скобки", 1);

        standardTest("a(", "Нет закрывающей скобки", 1);
        standardTest("((a + b)", "Нет закрывающей скобки", 7);
    }

    @Test
    public void noOpeningBracketTest(){
        standardTest(")", "Нет открывающейся скобки", 0);
        standardTest(")+", "Нет открывающейся скобки", 0);
        standardTest(")a", "Нет открывающейся скобки", 0);
        standardTest(")*", "Нет открывающейся скобки", 0);
        standardTest(")(", "Нет открывающейся скобки", 0);
        standardTest("))", "Нет открывающейся скобки", 0);
        standardTest(")_", "Нет открывающейся скобки", 0);

        standardTest("a)", "Нет открывающейся скобки", 1);
        standardTest("a + b)", "Нет открывающейся скобки", 5);
    }

    @Test
    public void wrongSymbolTest(){
        standardTest("a +& b", "Неизвестный символ", 3);
    }

    @Test
    public void noLeftOperandTest(){
        standardTest("+b", "Нет левого операнда для +", 0);
        standardTest("a(+b)", "Нет левого операнда для +", 2);
        standardTest("(+)", "Нет левого операнда для +", 1);
        standardTest("a++b", "Нет левого операнда для +", 2);

        standardTest("++", "Нет левого операнда для +", 0);
        standardTest("+*", "Нет левого операнда для +", 0);
        standardTest("+(", "Нет левого операнда для +", 0);
        standardTest("+)", "Нет левого операнда для +", 0);
    }

    @Test
    public void noRightOperandTest(){
        standardTest("a+", "Нет правого операнда для +", 1);
        standardTest("(a+)+b", "Нет правого операнда для +", 2);
    }

    @Test
    public void emptyBracketsTest(){
        standardTest("a()", "Пустые скобки", 2);
        standardTest("((()))", "Пустые скобки", 3);
        standardTest("((()a))", "Пустые скобки", 3);
        standardTest("((()*))", "Пустые скобки", 3);
    }

    @Test
    public void wrongIterationUsageTest(){
        standardTest("(*)", "Неправильное использование итерации", 1);
        standardTest("*)", "Неправильное использование итерации", 0);
        standardTest("(*", "Неправильное использование итерации", 1);

        standardTest("*+", "Неправильное использование итерации", 0);
        standardTest("**", "Неправильное использование итерации", 0);
        standardTest("*(", "Неправильное использование итерации", 0);
    }
}

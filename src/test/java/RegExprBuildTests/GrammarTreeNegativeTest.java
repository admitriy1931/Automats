package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.RegexpException;

public class GrammarTreeNegativeTest {
    private void standardTest(String regexp, String expectedText, Integer expectedPosition){
        var thrown  = Assertions.assertThrows(
                RegexpException.class,
                () -> RegExprBuild.makeGrammarTree(regexp));

        Assertions.assertEquals(expectedText, thrown.getText());
        Assertions.assertEquals(expectedPosition, thrown.getWrongSymbolPosition());
    }

    @Test
    public void treeForEmptyStringTest(){
        standardTest("", "Пустой ввод", -1);
    }

    @Test
    public void noClosingBracketTest(){
        standardTest("(", "Нет закрывающей скобки", 0);
        standardTest("(a", "Нет закрывающей скобки", 1);
        standardTest("((", "Нет закрывающей скобки", 1);

        standardTest("a(", "Нет закрывающей скобки", 1);
        standardTest("((a + b)", "Нет закрывающей скобки", 7);
        standardTest("+(", "Нет закрывающей скобки", 1);
        standardTest("(*", "Нет закрывающей скобки", 1);
        standardTest("*(", "Нет закрывающей скобки", 1);
    }

    @Test
    public void noOpeningBracketTest(){
        standardTest(")", "Нет открывающей скобки", 0);
        standardTest(")+", "Нет открывающей скобки", 0);
        standardTest(")a", "Нет открывающей скобки", 0);
        standardTest(")*", "Нет открывающей скобки", 0);
        standardTest(")(", "Нет открывающей скобки", 0);
        standardTest("))", "Нет открывающей скобки", 0);
        standardTest(")_", "Нет открывающей скобки", 0);

        standardTest("a)", "Нет открывающей скобки", 1);
        standardTest("a + b)", "Нет открывающей скобки", 5);
        standardTest("+)", "Нет открывающей скобки", 1);
        standardTest("*)", "Нет открывающей скобки", 1);
    }

    @Test
    public void wrongSymbolTest(){
        standardTest("a +& b", "Неизвестный символ, &", 3);
    }

    @Test
    public void noOperandTest(){
        standardTest("+b", "Не хватает операнда", 0);
        standardTest("a(+b)", "Не хватает операнда", -1);
        standardTest("(+)", "Не хватает операнда", 2);
        standardTest("a++b", "Не хватает операнда", 2);
        standardTest("++", "Не хватает операнда", 1);
        standardTest("a+", "Не хватает операнда", 1);
        standardTest("(a+)+b", "Не хватает операнда", 3);
    }

    @Test
    public void emptyBracketsTest(){
        standardTest("((()))", "Пустые скобки", 3);
        standardTest("a()", "Пустые скобки", 2);
        standardTest("((()a))", "Пустые скобки", 3);
        standardTest("((()*))", "Пустые скобки", 3);
    }

    @Test
    public void wrongIterationUsageTest(){
        standardTest("+*", "Неправильное использование итерации", 1);
        standardTest("*+", "Неправильное использование итерации", 0);
        standardTest("**", "Неправильное использование итерации", 0);
        standardTest("(*)", "Неправильное использование итерации", 1);
    }
}

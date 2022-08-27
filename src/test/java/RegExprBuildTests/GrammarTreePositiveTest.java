package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.GrammarTree;

public class GrammarTreePositiveTest {
    private void standardTest(String regexp, GrammarTree expected){
        var actual = RegExprBuild.makeGrammarTreeOrNull(regexp);

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.subTreesAreEqual(expected));
    }

    @Test
    public void treeForEmptyStringTest(){
        standardTest("", new GrammarTree(""));
    }

    @Test
    public void treeForLetterIsRootTest(){
        standardTest("a", new GrammarTree("a"));
    }

    @Test
    public void letterLetterConcatTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("ab", expected);
        standardTest("a(b)", expected);
        standardTest("(a)b", expected);
        standardTest("(a)(b)", expected);

    }

    @Test
    public void letterConcatConcat1Test(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("b"));

        standardTest("abb", expected);
    }

    @Test
    public void letterConcatConcat2Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("bb"));

        standardTest("(a)(bb)", expected);
        standardTest("(a)(b(b))", expected);
        standardTest("(a)((b)(b))", expected);
        standardTest("(a)(b)(b)", expected);
    }

    @Test
    public void letterConcatConcat3Test(){
        var expected = new GrammarTree("конкатенация");
        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(new GrammarTree("b"));

        standardTest("ab(b)", expected);
        standardTest("(ab)b", expected);
    }

    @Test
    public void letterSumConcatTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));

        standardTest("a (b+c)", expected);
        standardTest("(a) (b+c)", expected);
        standardTest("a ((b)+(c))", expected);
        standardTest("(a) ((b)+(c))", expected);
    }

    @Test
    public void concatConcatConcat1Test(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));
        expected.add(new GrammarTree("d"));

        standardTest("abcd", expected);
        standardTest("a(bc)d", expected);
    }

    @Test
    public void concatConcatConcat2Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("cd"));

        standardTest("ab (cd)", expected);
        standardTest("(ab) (cd)", expected);
        standardTest("((a)b) (c(d))", expected);
    }

    @Test
    public void concatConcatConcat3Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(new GrammarTree("c"));
        expected.add(new GrammarTree("d"));

        standardTest("(ab) cd", expected);
    }

    @Test
    public void concatSumConcat2Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("ab (c+d)", expected);
        standardTest("(ab) (c+d)", expected);
        standardTest("((a)(b)) ((c)+(d))", expected);
        standardTest("ab ((c)+(d))", expected);
    }

    @Test
    public void concatSumConcat3Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b(c+d)"));

        standardTest("(a) (b)((c)+(d))", expected);
    }

    @Test
    public void sumLetterConcatTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));
        expected.add(new GrammarTree("a"));

        standardTest("(b+c) a", expected);
        standardTest("(b+c) (a)", expected);
        standardTest("((b)+(c)) a", expected);
        standardTest("((b)+(c)) (a)", expected);
    }

    @Test
    public void sumConcatConcat1Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("(c+d) ab", expected);
        standardTest("((c)+(d)) ab", expected);
    }

    @Test
    public void sumConcatConcat2Test(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));

        standardTest("(c+d) (ab)", expected);
        standardTest("((c)+(d)) (a)(b)", expected);
        standardTest("((c)+(d)) ((a)(b))", expected);
    }

    @Test
    public void sumSumConcatTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a+b"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("(a+b) (c+d)", expected);
        standardTest("(a+b) ((c)+(d))", expected);
        standardTest("((a)+(b)) ((c)+(d))", expected);
    }

    @Test
    public void letterLetterSumTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("a+b", expected);
        standardTest("a+(b)", expected);
        standardTest("(a)+b", expected);
        standardTest("(a)+(b)", expected);

    }

    @Test
    public void letterConcatSum1Test(){
        var expected = new GrammarTree("+");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("bc"));

        standardTest("a+bc", expected);
        standardTest("a+b(c)", expected);
        standardTest("(a) + (b)(c)", expected);
        standardTest("(a) + (bc)", expected);
        standardTest("(a) + (b(c))", expected);
        standardTest("(a) + ((b)(c))", expected);
    }

    @Test
    public void letterConcatSum2Test(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(new GrammarTree("c"));

        standardTest("(ab) + c", expected);
        standardTest("ab + c", expected);
    }

    @Test
    public void letterSumSumTest(){
        var expected = new GrammarTree("+");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));

        standardTest("a + (b+c)", expected);
        standardTest("(a) + (b+c)", expected);
        standardTest("a + ((b)+(c))", expected);
        standardTest("(a) + ((b)+(c))", expected);
    }

    @Test
    public void concatConcatSumTest(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("cd"));

        standardTest("ab + cd", expected);
        standardTest("ab + (cd)", expected);
        standardTest("(ab) + cd", expected);
        standardTest("(ab) + (cd)", expected);
        standardTest("((a)b) + (c(d))", expected);
    }

    @Test
    public void concatSumSumTest(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("ab + (c+d)", expected);
        standardTest("(ab) + (c+d)", expected);
        standardTest("ab + ((c)+(d))", expected);
        standardTest("(a)(b) + ((c)+(d))", expected);
        standardTest("((a)(b)) + ((c)+(d))", expected);
    }

    @Test
    public void sumLetterSumTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));
        expected.add(new GrammarTree("a"));

        standardTest("(b+c) a", expected);
        standardTest("(b+c) (a)", expected);
        standardTest("((b)+(c)) a", expected);
        standardTest("((b)+(c)) (a)", expected);
    }

    @Test
    public void sumSumSumTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a+b"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("(a+b) (c+d)", expected);
        standardTest("(a+b) ((c)+(d))", expected);
        standardTest("((a)+(b)) ((c)+(d))", expected);
    }

    @Test
    public void wikiExamplePart1Test(){
        var expected = new GrammarTree("конкатенация");

        var child = RegExprBuild.makeGrammarTreeOrNull("ab");
        Assertions.assertNotNull(child);
        child.iterationAvailable = true;

        expected.add(new GrammarTree("a"));
        expected.add(child);
        expected.iterationAvailable = true;

        standardTest("(a(ab)*)*", expected);
    }

    @Test
    public void wikiExamplePart2Test(){
        var expected = RegExprBuild.makeGrammarTreeOrNull("ba");
        Assertions.assertNotNull(expected);
        expected.iterationAvailable = true;

        standardTest("(ba)*", expected);
    }

    @Test
    public void wikiExampleTest(){
        var tree = new GrammarTree("+");

        tree.add(RegExprBuild.makeGrammarTreeOrNull("(a(ab)*)*"));
        tree.add(RegExprBuild.makeGrammarTreeOrNull("(ba)*"));

        standardTest("(a(ab)*)* + (ba)*", tree);
    }

    @Test
    public void plusAndBracketsTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));
        expected.add(new GrammarTree("d"));

        standardTest("a + (b + c) + d", expected);
        standardTest("a + b + c + d", expected);
    }

    @Test
    public void plusIterationTest(){
        var expected = RegExprBuild.makeGrammarTreeOrNull("b+c");
        Assertions.assertNotNull(expected);
        expected.iterationAvailable = true;

        standardTest("(b + c)*", expected);
    }

    @Test
    public void plusIteration2Test(){
        var expected = RegExprBuild.makeGrammarTreeOrNull("a+b+c");
        Assertions.assertNotNull(expected);
        expected.iterationAvailable = true;

        standardTest("(a + b + c)*", expected);
    }

    @Test
    public void plusMultiplyIterationTest(){
        var expected = RegExprBuild.makeGrammarTreeOrNull("a+b+cd");
        Assertions.assertNotNull(expected);
        expected.iterationAvailable = true;

        standardTest("(a + b + cd)*", expected);
    }

    @Test
    public void plusMultiplyIteration2Test(){
        var expected = RegExprBuild.makeGrammarTreeOrNull("a+(b+c)");
        Assertions.assertNotNull(expected);
        expected.iterationAvailable = true;

        standardTest("(a + (b + c))*", expected);
    }

    @Test
    public void plusWithBracketsAndIterationTest(){
        var expected = new GrammarTree("+");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("(b+c)*"));

        standardTest("a + (b + c)*", expected);

        expected.add(new GrammarTree("d"));

        standardTest("a + (b + c)* + d", expected);
    }

    @Test
    public void multiplyBracketsIterationTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("(bc)*"));
        expected.add(new GrammarTree("d"));

        standardTest("a(bc)*d", expected);
    }

    @Test
    public void letterIteration(){
        var expected = new GrammarTree("a");
        expected.iterationAvailable = true;

        standardTest("a*", expected);
        standardTest("(a)*", expected);
    }

    @Test
    public void letterLetterIterationConcat(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b*"));

        standardTest("ab*", expected);
        standardTest("a(b)*", expected);
    }

    @Test
    public void complexTest(){
        var expected = new GrammarTree("+");

        var child = RegExprBuild.makeGrammarTreeOrNull("(d+e)f");
        Assertions.assertNotNull(child);
        child.iterationAvailable = true;

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("bc*"));
        expected.add(child);

        standardTest("a + bc* + ((d+e)f)*", expected);
    }

    @Test
    public void doubleIterationTest(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a*"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("a*"));

        standardTest("a** + (a*)*", expected);
    }

    @Test
    public void complexProdTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a+b"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c + de"));

        standardTest("(a + b)(c + de)", expected);
    }

    @Test
    public void bigTest1(){
        var expected = new GrammarTree("+");

        var child = new GrammarTree("конкатенация");
        child.add(RegExprBuild.makeGrammarTreeOrNull("dd+d"));
        child.add(new GrammarTree("d"));

        expected.add(new GrammarTree("c"));
        expected.add(child);

        standardTest("c + (dd+d)(d)", expected);
    }

    @Test
    public void bigTest2(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("(d+d)(e+e)"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("f+f"));

        standardTest("(d+d)(e+e)+(f+f)", expected);
    }

    @Test
    public void bigTest3(){
        var expected = new GrammarTree("+");

        var child = new GrammarTree("конкатенация");
        child.add(RegExprBuild.makeGrammarTreeOrNull("(d+d)(e+e)+(f+f)"));
        child.add(new GrammarTree("d"));

        expected.add(new GrammarTree("c"));
        expected.add(child);

        standardTest("(c+((d+d)(e+e)+(f+f))(d))", expected);
    }

    @Test
    public void bigTest4(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+((d+d)(e+e)+(f+f))(d)"));
        expected.add(new GrammarTree("g"));

        standardTest("(c+((d+d)(e+e)+(f+f))(d)) + g", expected);
    }

    @Test
    public void bigTest5(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("(c+((d+d)(e+e)+(f+f))(d)) + g"));
        expected.add(new GrammarTree("h"));

        standardTest("((c+((d+d)(e+e)+(f+f))(d)) + g) + h", expected);
    }

    @Test
    public void manyBrackets(){
        var expected = new GrammarTree("+");

        var child = new GrammarTree("+");
        child.add(RegExprBuild.makeGrammarTreeOrNull("a+b"));
        child.add(RegExprBuild.makeGrammarTreeOrNull("cd"));

        expected.add(child);
        expected.add(new GrammarTree("e"));

        standardTest("( (( ((a) + b) + cd )) + e)", expected);
    }

    @Test
    public void Exam2Test(){
        var expected = new GrammarTree("+");
        expected.add(RegExprBuild.makeGrammarTreeOrNull("a(aa + -)"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("-"));

        standardTest("a(aa + -) + b + -", expected);
    }

    @Test
    public void Exam2modifiedTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("a(aa + -)"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("-"));

        standardTest("a + a(aa + -) + b + -", expected);
    }

    @Test
    public void Exam2modified2Test(){
        var expected = new GrammarTree("+");
        expected.add(RegExprBuild.makeGrammarTreeOrNull("a(a(aa + -))"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("-"));

        standardTest("a(a(aa + -)) + b + -", expected);
    }
}

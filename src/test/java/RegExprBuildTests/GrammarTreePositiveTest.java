package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.GrammarTree;

public class GrammarTreePositiveTest {
    private static final String s = RegExprBuild.emptyWordSymbol;

    private void standardTest(String regexp, GrammarTree expected){
        var actual = RegExprBuild.makeGrammarTreeOrNull(regexp);

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.subTreesAreEqual(expected));
    }

    @Test
    public void treeForLetterIsRootTest(){
        standardTest("a", new GrammarTree("a"));
    }

    @Test
    public void letterLetterConcatTest(){
        var expected = new GrammarTree(RegExprBuild.CON);
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("ab", expected);
        standardTest("a(b)", expected);
        standardTest("(a)b", expected);
        standardTest("(a)(b)", expected);
    }

    @Test
    public void letterConcatConcat1Test(){
        var expected = new GrammarTree(RegExprBuild.CON);
        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(new GrammarTree("b"));

        standardTest("abb", expected);
    }

    @Test
    public void letterConcatConcat2Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("bb"));

        standardTest("(a)(bb)", expected);
        standardTest("(a)(b(b))", expected);
        standardTest("(a)((b)(b))", expected);
    }

    @Test
    public void letterConcatConcat3Test(){
        var expected = new GrammarTree(RegExprBuild.CON);
        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(new GrammarTree("b"));

        standardTest("(a)(b)(b)", expected);
        standardTest("ab(b)", expected);
        standardTest("(ab)b", expected);
    }

    @Test
    public void letterSumConcatTest(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));

        standardTest("a (b+c)", expected);
        standardTest("(a) (b+c)", expected);
        standardTest("a ((b)+(c))", expected);
        standardTest("(a) ((b)+(c))", expected);
    }

    @Test
    public void concatConcatConcat1Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("abc"));
        expected.add(new GrammarTree("d"));

        standardTest("abcd", expected);
    }

    @Test
    public void concatConcatConcat2Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a(bc)"));
        expected.add(new GrammarTree("d"));

        standardTest("a(bc)d", expected);
    }

    @Test
    public void concatConcatConcat3Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("cd"));

        standardTest("ab (cd)", expected);
        standardTest("(ab) (cd)", expected);
        standardTest("((a)b) (c(d))", expected);
    }

    @Test
    public void concatConcatConcat4Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("(ab)c"));
        expected.add(new GrammarTree("d"));

        standardTest("(ab) cd", expected);
    }

    @Test
    public void concatSumConcat2Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("ab (c+d)", expected);
        standardTest("(ab) (c+d)", expected);
        standardTest("((a)(b)) ((c)+(d))", expected);
        standardTest("ab ((c)+(d))", expected);
    }

    @Test
    public void concatSumConcat3Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("(a) (b)((c)+(d))", expected);
    }

    @Test
    public void sumLetterConcatTest(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));
        expected.add(new GrammarTree("a"));

        standardTest("(b+c) a", expected);
        standardTest("(b+c) (a)", expected);
        standardTest("((b)+(c)) a", expected);
        standardTest("((b)+(c)) (a)", expected);
    }

    @Test
    public void sumConcatConcat1Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("(c+d)a"));
        expected.add(new GrammarTree("b"));

        standardTest("(c+d) ab", expected);
        standardTest("((c)+(d)) ab", expected);
        standardTest("((c)+(d)) (a)(b)", expected);
    }

    @Test
    public void sumConcatConcat2Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("ab"));

        standardTest("(c+d) (ab)", expected);
        standardTest("((c)+(d)) ((a)(b))", expected);
    }

    @Test
    public void sumSumConcatTest(){
        var expected = new GrammarTree(RegExprBuild.CON);

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
        var expected = new GrammarTree(RegExprBuild.CON);
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b+c"));
        expected.add(new GrammarTree("a"));

        standardTest("(b+c) a", expected);
        standardTest("(b+c) (a)", expected);
        standardTest("((b)+(c)) a", expected);
        standardTest("((b)+(c)) (a)", expected);
    }

    @Test
    public void sumSumSumTest(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a+b"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c+d"));

        standardTest("(a+b) (c+d)", expected);
        standardTest("(a+b) ((c)+(d))", expected);
        standardTest("((a)+(b)) ((c)+(d))", expected);
    }

    @Test
    public void wikiExamplePart1Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

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
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("(a(ab)*)*"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("(ba)*"));

        standardTest("(a(ab)*)* + (ba)*", expected);
    }

    @Test
    public void plusAndBrackets1Test(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a + b + c"));
        expected.add(new GrammarTree("d"));

        standardTest("a + b + c + d", expected);
    }

    @Test
    public void plusAndBrackets2Test(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a + (b + c)"));
        expected.add(new GrammarTree("d"));

        standardTest("a + (b + c) + d", expected);
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
    public void plusWithBracketsAndIteration1Test(){
        var expected = new GrammarTree("+");

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("(b+c)*"));

        standardTest("a + (b + c)*", expected);
    }

    @Test
    public void plusWithBracketsAndIteration2Test(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a + (b + c)*"));
        expected.add(new GrammarTree("d"));

        standardTest("a + (b + c)* + d", expected);
    }

    @Test
    public void multiplyBracketsIterationTest(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a(bc)*"));
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
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("b*"));

        standardTest("ab*", expected);
        standardTest("a(b)*", expected);
    }

    @Test
    public void complex1Test(){
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("d+e"));
        expected.add(new GrammarTree("f"));
        expected.iterationAvailable = true;

        standardTest("((d+e)f)*", expected);
    }

    @Test
    public void complex2Test(){
        var expected = new GrammarTree("+");

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a + bc*"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("((d+e)f)*"));

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
        var expected = new GrammarTree(RegExprBuild.CON);

        expected.add(RegExprBuild.makeGrammarTreeOrNull("a+b"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("c + de"));

        standardTest("(a + b)(c + de)", expected);
    }

    @Test
    public void bigTest1(){
        var expected = new GrammarTree("+");

        var child = new GrammarTree(RegExprBuild.CON);
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

        var child = new GrammarTree(RegExprBuild.CON);
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
    public void emptyWordTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree(s));
        standardTest("a + " + s, expected);
    }

    @Test
    public void Exam2smallTest(){
        var expected = new GrammarTree(RegExprBuild.CON);
        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull("aa + " + s));
        standardTest(String.format("a(aa + %s)", s), expected);
    }

    @Test
    public void Exam2Test(){
        var expected = new GrammarTree("+");
        expected.add(RegExprBuild.makeGrammarTreeOrNull(String.format("a(aa + %s) + b", s)));
        expected.add(new GrammarTree(s));

        standardTest(String.format("a(aa + %s) + b + %s", s, s), expected);
    }

    @Test
    public void letterConcatSumTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull(String.format("a(aa + %s)", s)));

        standardTest(String.format("a + a(aa + %s)", s), expected);
    }

    @Test
    public void twoLettersAndConcatSumTest(){
        var expected = new GrammarTree("+");
        expected.add(RegExprBuild.makeGrammarTreeOrNull(String.format("a + a(aa + %s)", s)));
        expected.add(new GrammarTree("b"));

        standardTest(String.format("a + a(aa + %s) + b", s), expected);
    }

    @Test
    public void Exam2modifiedTest(){
        var expected = new GrammarTree("+");
        expected.add(RegExprBuild.makeGrammarTreeOrNull(String.format("a + a(aa + %s) + b", s)));
        expected.add(new GrammarTree(s));

        standardTest(String.format("a + a(aa + %s) + b + %s", s, s), expected);
    }

    @Test
    public void threeConcatTest(){
        var expected = new GrammarTree(RegExprBuild.CON);
        expected.add(new GrammarTree("a"));
        expected.add(RegExprBuild.makeGrammarTreeOrNull(String.format("a(aa + %s)", s)));

        standardTest(String.format("a(a(aa + %s))", s), expected);
    }

    @Test
    public void Exam2modified2Test(){
        var expected = new GrammarTree("+");
        expected.add(RegExprBuild.makeGrammarTreeOrNull(String.format("a(a(aa + %s)) + b", s)));
        expected.add(new GrammarTree(s));

        standardTest(String.format("a(a(aa + %s)) + b + %s", s, s), expected);
    }
}

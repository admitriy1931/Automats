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

        actual = RegExprBuild.makeGrammarTreeOrNull("(" + regexp + ")");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.subTreesAreEqual(expected));
    }

    @Test
    public void simple1Test(){
        var tree = new GrammarTree("+");

        var child1 = new GrammarTree("конкатенация");
        child1.add(new GrammarTree("a"));
        child1.add(new GrammarTree("b"));
        child1.add(new GrammarTree("c"));

        var child2 = new GrammarTree("конкатенация");
        child2.add(new GrammarTree("a"));
        child2.add(new GrammarTree("b"));
        child2.add(new GrammarTree("c"));

        tree.add(child1);
        tree.add(child2);

        standardTest("abc + abc", tree);
    }

    @Test
    public void wikiExampleTest(){
        var tree = new GrammarTree("+");

        var child1 = new GrammarTree("конкатенация");

        var grandChild = new GrammarTree("конкатенация");
        grandChild.add(new GrammarTree("a"));
        grandChild.add(new GrammarTree("b"));
        grandChild.iterationAvailable = true;

        child1.add(new GrammarTree("a"));
        child1.add(grandChild);
        child1.iterationAvailable = true;

        var child2 = new GrammarTree("конкатенация");
        child2.add(new GrammarTree("b"));
        child2.add(new GrammarTree("a"));
        child2.iterationAvailable = true;

        tree.add(child1);
        tree.add(child2);

        standardTest("(a(ab)*)* + (ba)*", tree);
    }

    @Test
    public void treeForEmptyStringTest(){
        var actual = RegExprBuild.makeGrammarTreeOrNull("");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.subTreesAreEqual(new GrammarTree("")));
    }

    @Test
    public void treeForLetterIsRootTest(){
        standardTest("a", new GrammarTree("a"));
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
    public void plusWithBracketsAndIterationTest(){
        var expected = new GrammarTree("+");
        var child1 = new GrammarTree("a");
        var child2 = new GrammarTree("+");
        child2.add(new GrammarTree("b"));
        child2.add(new GrammarTree("c"));
        child2.iterationAvailable = true;
        expected.add(child1);
        expected.add(child2);

        standardTest("a + (b + c)*", expected);

        expected.add(new GrammarTree("d"));

        standardTest("a + (b + c)* + d", expected);
    }

    @Test
    public void plusMultiplyWithoutBracketsTest(){
        var expected = new GrammarTree("+");
        var child = new GrammarTree("конкатенация");
        child.add(new GrammarTree("b"));
        child.add(new GrammarTree("c"));
        expected.add(new GrammarTree("a"));
        expected.add(child);

        standardTest("a + bc", expected);
    }

    @Test
    public void multiplyBracketsTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));
        expected.add(new GrammarTree("d"));

        standardTest("a(bc)d", expected);
        standardTest("abcd", expected);
    }

    @Test
    public void multiplyBracketsIterationTest(){
        var expected = new GrammarTree("конкатенация");

        expected.add(new GrammarTree("a"));

        var child = new GrammarTree("конкатенация");
        child.add(new GrammarTree("b"));
        child.add(new GrammarTree("c"));
        child.iterationAvailable = true;

        expected.add(child);
        expected.add(new GrammarTree("d"));

        standardTest("a(bc)*d", expected);
    }

    @Test
    public void plusMultiplyWithBracketsTest(){
        var expected = new GrammarTree("конкатенация");
        var child = new GrammarTree("+");
        child.add(new GrammarTree("a"));
        child.add(new GrammarTree("b"));
        expected.add(child);
        expected.add(new GrammarTree("c"));

        standardTest("(a + b)c", expected);
    }

    @Test
    public void complexTest(){
        var expected = new GrammarTree("+");
        var child1 = new GrammarTree("a");

        var child2 = new GrammarTree("конкатенация");
        child2.add(new GrammarTree("b"));
        var c = new GrammarTree("c");
        c.iterationAvailable = true;
        child2.add(c);

        var child3 = new GrammarTree("конкатенация");
        var grandChild = new GrammarTree("+");
        grandChild.add(new GrammarTree("d"));
        grandChild.add(new GrammarTree("e"));
        child3.add(grandChild);
        child3.add(new GrammarTree("f"));
        child3.iterationAvailable = true;

        expected.add(child1);
        expected.add(child2);
        expected.add(child3);

        standardTest("a + bc* + ((d+e)f)*", expected);
    }

    @Test
    public void doubleIterationTest(){
        var expected = new GrammarTree("+");

        var child1 = new GrammarTree("a");
        child1.iterationAvailable = true;
        var child2 = new GrammarTree("a");
        child2.iterationAvailable = true;

        expected.add(child1);
        expected.add(child2);

//        standardTest("a** + (a*)*", expected);
        standardTest("a** + (a*)b", expected);
    }

    @Test
    public void complexProdTest(){
        var expected = new GrammarTree("конкатенация");

        var child1 = new GrammarTree("+");
        child1.add(new GrammarTree("a"));
        child1.add(new GrammarTree("b"));

        var child2 = new GrammarTree("+");
        child2.add(new GrammarTree("c"));
        var grandChild = new GrammarTree("конкатенация");
        grandChild.add(new GrammarTree("d"));
        grandChild.add(new GrammarTree("e"));
        child2.add(grandChild);

        expected.add(child1);
        expected.add(child2);

        standardTest("(a + b)(c + de)", expected);
    }

    @Test
    public void letterInBracketsTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("a(b)", expected);
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
    public void letterConcatConcatTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("b"));

        standardTest("abb", expected);
        standardTest("ab(b)", expected);
        standardTest("(a)(bb)", expected);
        standardTest("(a)(b(b))", expected);
        standardTest("(ab)b", expected);
        standardTest("(a)(b)(b)", expected);
        standardTest("(a)((b)(b))", expected);
    }

    @Test
    public void letterSumConcatTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        var child = new GrammarTree("+");
        child.add(new GrammarTree("b"));
        child.add(new GrammarTree("c"));
        expected.add(child);

        standardTest("a (b+c)", expected);
        standardTest("(a) (b+c)", expected);
        standardTest("a ((b)+(c))", expected);
        standardTest("(a) ((b)+(c))", expected);
    }

    @Test
    public void concatConcatConcatTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));
        expected.add(new GrammarTree("d"));

        standardTest("abcd", expected);
        standardTest("ab (cd)", expected);
        standardTest("(ab) cd", expected);
        standardTest("(ab) (cd)", expected);
        standardTest("((a)b) (c(d))", expected);
    }

    @Test
    public void concatSumConcatTest(){
        var expected = new GrammarTree("конкатенация");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        var child = new GrammarTree("+");
        child.add(new GrammarTree("c"));
        child.add(new GrammarTree("d"));
        expected.add(child);

        standardTest("ab (c+d)", expected);
        standardTest("(ab) (c+d)", expected);
        standardTest("ab ((c)+(d))", expected);
        standardTest("(a)(b) ((c)+(d))", expected);
        standardTest("((a)(b)) ((c)+(d))", expected);
    }

    @Test
    public void sumLetterConcatTest(){
        var expected = new GrammarTree("конкатенация");
        var child = new GrammarTree("+");
        child.add(new GrammarTree("b"));
        child.add(new GrammarTree("c"));
        expected.add(child);
        expected.add(new GrammarTree("a"));

        standardTest("(b+c) a", expected);
        standardTest("(b+c) (a)", expected);
        standardTest("((b)+(c)) a", expected);
//        standardTest("((b)+(c)) (a)", expected);
    }

    @Test
    public void sumConcatConcatTest(){
        var expected = new GrammarTree("конкатенация");
        var child = new GrammarTree("+");
        child.add(new GrammarTree("c"));
        child.add(new GrammarTree("d"));
        expected.add(child);
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("(c+d) ab", expected);
        standardTest("(c+d) (ab)", expected);
        standardTest("((c)+(d)) ab", expected);
        standardTest("((c)+(d)) (a)(b)", expected);
        standardTest("((c)+(d)) ((a)(b))", expected);
    }

    @Test
    public void sumSumConcatTest(){
        var expected = new GrammarTree("конкатенация");

        var child1 = new GrammarTree("+");
        child1.add(new GrammarTree("a"));
        child1.add(new GrammarTree("b"));

        var child2 = new GrammarTree("+");
        child2.add(new GrammarTree("c"));
        child2.add(new GrammarTree("d"));

        expected.add(child1);
        expected.add(child2);

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
    public void letterConcatSumTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));

        standardTest("a+b+c", expected);
        standardTest("a+b+(c)", expected);
        standardTest("(a) + (b+c)", expected);
        standardTest("(a) + (b+(c))", expected);
        standardTest("(a+b) + c", expected);
        standardTest("(a) + (b) + (c)", expected);
        standardTest("(a) + ((b)+(c))", expected);
    }

    @Test
    public void letterSumSumTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));

        standardTest("a + (b+c)", expected);
        standardTest("(a) + (b+c)", expected);
        standardTest("a + ((b)+(c))", expected);
        standardTest("(a) + ((b)+(c))", expected);
    }

    @Test
    public void concatConcatSumTest(){
        var expected = new GrammarTree("+");
        var child1 = new GrammarTree("конкатенация");
        child1.add(new GrammarTree("a"));
        child1.add(new GrammarTree("b"));
        var child2 = new GrammarTree("конкатенация");
        child2.add(new GrammarTree("c"));
        child2.add(new GrammarTree("d"));
        expected.add(child1);
        expected.add(child2);

        standardTest("ab + cd", expected);
        standardTest("ab + (cd)", expected);
        standardTest("(ab) + cd", expected);
        standardTest("(ab) + (cd)", expected);
        standardTest("((a)b) + (c(d))", expected);
    }

    @Test
    public void concatSumSumTest(){
        var expected = new GrammarTree("+");

        var child = new GrammarTree("конкатенация");
        child.add(new GrammarTree("a"));
        child.add(new GrammarTree("b"));

        expected.add(child);
        expected.add(new GrammarTree("c"));
        expected.add(new GrammarTree("d"));

        standardTest("ab + (c+d)", expected);
        standardTest("(ab) + (c+d)", expected);
        standardTest("ab + ((c)+(d))", expected);
        standardTest("(a)(b) + ((c)+(d))", expected);
        standardTest("((a)(b)) + ((c)+(d))", expected);
    }

    @Test
    public void sumLetterSumTest(){
        var expected = new GrammarTree("конкатенация");
        var child = new GrammarTree("+");
        child.add(new GrammarTree("b"));
        child.add(new GrammarTree("c"));
        expected.add(child);
        expected.add(new GrammarTree("a"));

        standardTest("(b+c) a", expected);
        standardTest("(b+c) (a)", expected);
        standardTest("((b)+(c)) a", expected);
        standardTest("((b)+(c)) (a)", expected);
    }

    @Test
    public void sumConcatSumTest(){
        var expected = new GrammarTree("конкатенация");
        var child = new GrammarTree("+");
        child.add(new GrammarTree("c"));
        child.add(new GrammarTree("d"));
        expected.add(child);
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));

        standardTest("(c+d) ab", expected);
        standardTest("(c+d) (ab)", expected);
        standardTest("((c)+(d)) ab", expected);
        standardTest("((c)+(d)) (a)(b)", expected);
        standardTest("((c)+(d)) ((a)(b))", expected);
    }

    @Test
    public void sumSumSumTest(){
        var expected = new GrammarTree("конкатенация");

        var child1 = new GrammarTree("+");
        child1.add(new GrammarTree("a"));
        child1.add(new GrammarTree("b"));

        var child2 = new GrammarTree("+");
        child2.add(new GrammarTree("c"));
        child2.add(new GrammarTree("d"));

        expected.add(child1);
        expected.add(child2);

        standardTest("(a+b) (c+d)", expected);
        standardTest("(a+b) ((c)+(d))", expected);
        standardTest("((a)+(b)) ((c)+(d))", expected);
    }
}

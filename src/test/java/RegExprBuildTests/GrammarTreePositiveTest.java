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

        standardTest("(abc + abc)", tree);
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
        standardTest("", new GrammarTree(""));
    }

    @Test
    public void treeForLetterIsRootTest(){
        standardTest("a", new GrammarTree("a"));
    }

    @Test
    public void plusWithoutBracketsTest(){
        var expected = new GrammarTree("+");
        expected.add(new GrammarTree("a"));
        expected.add(new GrammarTree("b"));
        expected.add(new GrammarTree("c"));

        standardTest("a + b + c", expected);
    }

    @Test
    public void plusWithBracketsTest(){
        var expected = new GrammarTree("+");
        var child1 = new GrammarTree("a");
        var child2 = new GrammarTree("+");
        child2.add(new GrammarTree("b"));
        child2.add(new GrammarTree("c"));
        expected.add(child1);
        expected.add(child2);

        standardTest("(a + (b + c))", expected);
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

        standardTest("a** + (a*)*", expected);
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
}

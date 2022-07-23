package algorithms;

import regexp.GrammarTree;
import regexp.GrammarTreeException;

import java.util.ArrayList;
import java.util.Objects;

public class RegExprBuild {
    public static Boolean isCorrect(String regexp){
        return makeGrammarTreeOrNull(regexp) != null;
    }

    public static GrammarTree makeGrammarTreeOrNull(String regexp){
        try {
            return makeGrammarTree(regexp);
        }
        catch (GrammarTreeException e){
            return null;
        }
    }

    //TODO: убрать знак для конкатенации и использовать * для итерации
    public static GrammarTree makeGrammarTree(String regexp) throws GrammarTreeException{
        return completeRawTree(makeRawTree(regexp));
    }

    private static GrammarTree makeRawTree(String regexp) throws GrammarTreeException{
        var tree = new GrammarTree("");
        var bracketsCount = 0;
        var implicitMultiply = false;

        for (var i = 0; i < regexp.length(); i++){
            var symbol = regexp.charAt(i);
            if (Character.isWhitespace(symbol))
                continue;

            switch (symbol){
                case '(':
                    if (implicitMultiply)
                        tree.value = "конкатенация";
                    bracketsCount += 1;
                    tree = getNewChild(tree);

                    implicitMultiply = false;
                    break;
                case ')':
                    if (--bracketsCount < 0)
                        throw new GrammarTreeException("Нет открывающейся скобки", i);
                    if (getPreviousSymbol(i, regexp) == '(')
                        throw new GrammarTreeException("Пустые скобки", i);

                    if (tree.value.isEmpty()){
                        if (tree.children.size() == 0)
                            throw new GrammarTreeException(
                                    "Нет правого операнда для " + tree.parent.value,
                                    tree.parent.position);
                        var child = tree.children.get(0);
                        ChangeLastChild(tree.parent, child);
                        child.parent = tree.parent;
                        tree = child;
                    }
                    while (!tree.value.isEmpty() && tree.parent != null)
                        tree = tree.parent;

                    implicitMultiply = true;
                    break;
                case '+':
                    if (tree.children.isEmpty())
                        throw new GrammarTreeException(
                                "Нет левого операнда для " + symbol, i);
                    if (tree.parent != null
                            && Objects.equals(Character.toString(symbol), tree.parent.value)){
                        if (tree.value.isEmpty()){
                            var child = tree.children.get(0);
                            tree.value = child.value;
                            tree.position = child.position;
                            tree.children = new ArrayList<>();

                            tree = getNewChild(tree.parent);
                        }
                        else{
                            tree = tree.parent;
                            tree = getNewChild(tree);
                        }
                    }
                    else{
                        tree.value = Character.toString(symbol);
                        tree.position = i;
                        tree = getNewChild(tree);
                    }
                    implicitMultiply = false;
                    break;
                case '*':
                    if (tree.children.isEmpty())
                        throw new GrammarTreeException(
                                "Неправильное использование итерации", i);
                    tree.children.get(tree.children.size()-1).iterationAvailable = true;
                    implicitMultiply = true;
                    break;
                default:
                    if (implicitMultiply)
                        tree.value = "конкатенация";
                    var child = new GrammarTree(Character.toString(symbol));
                    child.position = i;
                    tree.add(child);

                    implicitMultiply = true;
            }
        }
        if (bracketsCount != 0)
            throw new GrammarTreeException(
                    "Нет закрывающей скобки", regexp.length()-1);

        return tree;
    }

    private static GrammarTree completeRawTree(GrammarTree tree) throws GrammarTreeException{
        if (tree.value.isEmpty()){
            if (!tree.children.isEmpty()){
                var child = tree.children.get(0);
                if (tree.parent != null)
                    ChangeLastChild(tree.parent, child);
                child.parent = tree.parent;
                tree = child;
            }
            else if (tree.parent != null && !tree.parent.value.isEmpty())
                throw new GrammarTreeException(
                        "Нет правого операнда для " + tree.parent.value,
                        tree.parent.position);
        }
        while (tree.parent != null)
            tree = tree.parent;

        return tree;
    }

    private static Character getPreviousSymbol(Integer currentPosition, String regexp){
        var j = currentPosition-1;
        while (j > 0 && Character.isWhitespace(regexp.charAt(j))){
            j--;
        }
        return regexp.charAt(j);
    }

    private static GrammarTree getNewChild(GrammarTree tree){
        var child = new GrammarTree("");
        tree.add(child);
        return child;
    }

    private static void ChangeLastChild(GrammarTree tree, GrammarTree child){
        tree.children.set(tree.children.size()-1, child);
    }
}

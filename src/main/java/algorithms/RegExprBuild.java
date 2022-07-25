package algorithms;

import regexp.GrammarTree;
import regexp.RegexpExeption;

import java.util.*;

public class RegExprBuild {
    public static Boolean isCorrect(String regexp){
        return makeGrammarTreeOrNull(regexp) != null;
    }

    public static GrammarTree makeGrammarTreeOrNull(String regexp){
        try {
            return makeGrammarTree(regexp);
        }
        catch (RegexpExeption e){
            return null;
        }
    }

    public static GrammarTree makeGrammarTree(String regexp) throws RegexpExeption {
        return completeRawTree(makeRawTree(regexp));
    }

    private static GrammarTree makeRawTree(String regexp) throws RegexpExeption {
        var tree = new GrammarTree("");
        var bracketsCount = 0;
        var implicitMultiply = false;

        for (var i = 0; i < regexp.length(); i++){
            var symbol = regexp.charAt(i);
            if (Character.isWhitespace(symbol))
                continue;

            switch (symbol){
                case '(':
                    if (implicitMultiply){
                        tree.value = "конкатенация";
                        tree = getNewChild(tree);
                    }

                    bracketsCount += 1;
                    tree = getNewChild(tree);

                    implicitMultiply = false;
                    break;
                case ')':
                    if (--bracketsCount < 0)
                        throw new RegexpExeption("Нет открывающейся скобки", i);
                    if (getPreviousSymbol(i, regexp) == '(')
                        throw new RegexpExeption("Пустые скобки", i);

                    if (tree.value.isEmpty()){
                        if (tree.children.size() == 0)
                            throw new RegexpExeption(
                                    "Нет правого операнда для " + tree.parent.value,
                                    tree.parent.position);
                        if (tree.parent != null){
                            var child = tree.children.get(0);
                            ChangeLastChild(tree.parent, child);
                            child.parent = tree.parent;
                            tree = child;
                        }
                    }
                    while (!tree.value.isEmpty() && tree.parent != null)
                        tree = tree.parent;

                    implicitMultiply = true;
                    break;
                case '+':
                    if (tree.children.isEmpty())
                        throw new RegexpExeption(
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
                        var val = Character.toString(symbol);
                        if (!Objects.equals(tree.value, "")
                                && !Objects.equals(tree.value, val)){
                            if (tree.parent == null){
                                var parent = new GrammarTree("");
                                parent.add(tree);
                            }
                            tree = tree.parent;
                        }
                        tree.value = val;
                        tree.position = i;
                        tree = getNewChild(tree);
                    }
                    implicitMultiply = false;
                    break;
                case '*':
                    if (tree.children.isEmpty())
                        throw new RegexpExeption(
                                "Неправильное использование итерации", i);
                    tree.children.get(tree.children.size()-1).iterationAvailable = true;
                    implicitMultiply = true;
                    break;
                default:
                    if (!Character.isLetterOrDigit(symbol))
                        throw new RegexpExeption("Неизвестный символ", i);
                    if (implicitMultiply)
                        tree.value = "конкатенация";
                    var child = new GrammarTree(Character.toString(symbol));
                    child.position = i;
                    tree.add(child);

                    implicitMultiply = true;
            }
        }
        if (bracketsCount != 0)
            throw new RegexpExeption(
                    "Нет закрывающей скобки", regexp.length()-1);

        return tree;
    }

    private static GrammarTree completeRawTree(GrammarTree tree) throws RegexpExeption {
        if (tree.value.isEmpty()){
            if (!tree.children.isEmpty()){
                var child = tree.children.get(0);
                if (tree.parent != null)
                    ChangeLastChild(tree.parent, child);
                child.parent = tree.parent;
                tree = child;
            }
            else if (tree.parent != null && !tree.parent.value.isEmpty())
                throw new RegexpExeption(
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

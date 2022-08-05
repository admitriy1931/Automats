package algorithms;

import regexp.GrammarTree;
import regexp.RegexpExeption;

import java.util.*;

public class RegExprBuild {
    public static Boolean allowEmptyWord(String regexp) throws RegexpExeption {
        var tree = makeGrammarTree(regexp);
        if (tree.iterationAvailable)
            return true;
        if (Objects.equals(tree.value, "+")){
            for (var child: tree.children)
                if (child.iterationAvailable)
                    return true;
        }
        if (Objects.equals(tree.value, "конкатенация")){
            for (var child: tree.children)
                if (!child.iterationAvailable)
                    return false;
            return true;
        }
        return false;
    }

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
                        tree = implicitMultiplyHandler(tree);
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
                    var flag = tree.value.isEmpty();
                    if (tree.value.isEmpty()){
                        if (tree.children.size() == 0)
                            throw new RegexpExeption(
                                    "Нет правого операнда для " + tree.parent.value,
                                    tree.parent.position);
                        if (tree.parent != null){
                            var child = tree.children.get(0);
                            if (Objects.equals(child.value, tree.parent.value) && !child.iterationAvailable){
                                tree = moveChildrenToParent(tree, child);
                            }
                            else{
                                ChangeLastChild(tree.parent, child);
                                tree = child;
                                if (tree.parent.value.isEmpty() && tree.parent.parent != null){
                                    var parent = tree;
                                    while (parent.parent != null && parent.parent.value.isEmpty())
                                        parent = parent.parent;
                                    if (parent.parent != null && Objects.equals(parent.parent.value, "+")){
                                        ChangeLastChild(tree.parent.parent, child);
                                    }
                                }
                            }
                        }
                    }
                    while (!tree.value.isEmpty() && tree.parent != null)
                        tree = tree.parent;

                    if (!flag){
                        if (tree.parent != null){
                            var child = tree.children.get(0);
                            if (Objects.equals(child.value, tree.parent.value) && !child.iterationAvailable){
                                tree = moveChildrenToParent(tree, child);
                            }
                            else{
                                ChangeLastChild(tree.parent, child);
                                tree = child;
                            }
                        }
                    }

                    implicitMultiply = true;
                    break;
                case '+':
                    if (tree.children.isEmpty())
                        throw new RegexpExeption(
                                "Нет левого операнда для " + symbol, i);
                    if (tree.parent != null
                            && Objects.equals("+", tree.parent.value)){
                        if (tree.value.isEmpty()){
                            var child = tree.children.get(0);
                            if (Objects.equals(child.value, "+") && !child.iterationAvailable){
                                moveChildrenToParent(tree, child);
                            }
                            else{
                                tree.value = child.value;
                                tree.position = child.position;
                                tree.children = child.children;
                                tree.iterationAvailable = child.iterationAvailable;
                            }
                            tree = getNewChild(tree.parent);
                        }
                        else{
                            tree = tree.parent;
                            tree = getNewChild(tree);
                        }
                    }
                    else{
                        var val = Character.toString(symbol);
                        if (!tree.value.isEmpty()
                                && !Objects.equals(tree.value, val)){
                            if (tree.parent == null){
                                var parent = new GrammarTree("");
                                parent.add(tree);
                            }
                            tree = tree.parent;
                        }
                        else if (tree.value.isEmpty()){
                            var child = tree.children.get(0);
                            if (Objects.equals(child.value, "+") && !child.iterationAvailable){
                                moveChildrenToParent(child, child);
                            }
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
                    if (implicitMultiply){
                        tree = implicitMultiplyHandler(tree);
                        tree.value = Character.toString(symbol);
                        tree.position = i;
                        tree = tree.parent;
                    }
                    else{
                        var child = new GrammarTree(Character.toString(symbol));
                        child.position = i;
                        tree.add(child);
                    }

                    implicitMultiply = true;
            }
        }
        if (bracketsCount != 0)
            throw new RegexpExeption(
                    "Нет закрывающей скобки", regexp.length()-1);


        if (tree.parent != null && tree.children.size() == 1){
            var child = tree.children.get(0);
            if (tree.value.isEmpty()
                    && Objects.equals(child.value, tree.parent.value)
                    && !child.iterationAvailable){
                tree = moveChildrenToParent(tree, child);
            }
        }

        return tree;
    }

    private static GrammarTree implicitMultiplyHandler(GrammarTree tree){
        if (tree.parent != null
                && Objects.equals("конкатенация", tree.parent.value)){
            if (tree.value.isEmpty()){
                var child = tree.children.get(0);
                if (Objects.equals(child.value, "конкатенация") && !child.iterationAvailable){
                    moveChildrenToParent(tree, child);
                }
                else{
                    tree.value = child.value;
                    tree.position = child.position;
                    tree.children = child.children;
                    tree.iterationAvailable = child.iterationAvailable;
                }
            }
            tree = getNewChild(tree.parent);
        }
        else {
            if (tree.value.isEmpty()){
                var child = tree.children.get(0);
                if (Objects.equals(child.value, "конкатенация") && !child.iterationAvailable){
                    if (tree.parent != null){
                        ChangeLastChild(tree.parent, child);
                    }
                    child.parent = tree.parent;
                    tree.children = new ArrayList<>();
                    tree.parent = null;

                    tree = child;
                }
            }
            tree.value = "конкатенация";
            tree = getNewChild(tree);
        }

        return tree;
    }

    private static GrammarTree moveChildrenToParent(GrammarTree tree, GrammarTree child){
        ChangeLastChild(tree.parent, child.children.get(0));
        for (var j = 1; j < child.children.size(); j++){
            tree.parent.add(child.children.get(j));
        }
        return tree.parent;
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
        return (j < 0) ? null : regexp.charAt(j);
    }

    private static GrammarTree getNewChild(GrammarTree tree){
        var child = new GrammarTree("");
        tree.add(child);
        return child;
    }

    private static void ChangeLastChild(GrammarTree tree, GrammarTree child){
        tree.children.set(tree.children.size()-1, child);
        child.parent = tree;
    }

    private static void upChild(){

    }
}

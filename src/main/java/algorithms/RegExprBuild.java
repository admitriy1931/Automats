package algorithms;

import regexp.GrammarTree;
import regexp.RegexpExeption;

import java.util.*;

public class RegExprBuild {
    public static Boolean allowEmptyWord(String regexp) throws RegexpExeption {
        return allowEmptyWord(makeGrammarTree(regexp));
    }

    private static Boolean allowEmptyWord(GrammarTree tree){
        if (tree.iterationAvailable)
            return true;
        if (Objects.equals(tree.value, "+")){
            for (var child: tree.children)
                if (allowEmptyWord(child))
                    return true;
        }
        if (Objects.equals(tree.value, "конкатенация")){
            for (var child: tree.children)
                if (!allowEmptyWord(child))
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

    private static GrammarTree tryInsertNode(GrammarTree tree){
        if (!tree.value.isEmpty() && (tree.parent == null || !tree.parent.value.isEmpty())){
            if (tree.parent == null){
                new GrammarTree("конкатенация").add(tree);
                tree = tree.parent;
            }
            else{
                var parent = new GrammarTree("конкатенация");
                tree.parent = changeLastChild(tree.parent, parent);
                parent.add(tree);
                tree = parent;
            }
        }
        return tree;
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
                        tree = tryInsertNode(tree);
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
                    var prev = getPreviousSymbol(i, regexp);
                    if (prev == null || prev == '(')
                        throw new RegexpExeption("Пустые скобки", i);

                    if (tree.value.isEmpty()){
                        if (tree.children.size() == 0)
                            throw new RegexpExeption("Нет правого операнда для " + tree.parent.value,
                                    tree.parent.position);
                        if (tree.parent != null)
                            tree = changeLastChild(tree.parent, tree.children.get(0));
                    }
                    tree = Objects.equals(tree.value, "+") || Objects.equals(tree.value, "конкатенация")
                            ? tree.parent : getEmptyAncestorOrParent(tree);

                    implicitMultiply = true;
                    break;
                case '+':
                    if (tree.children.isEmpty())
                        throw new RegexpExeption("Нет левого операнда для +", i);
                    if (tree.parent != null && Objects.equals("+", tree.parent.value)){
                        if (tree.value.isEmpty())
                            processEmptyNode(tree, "+");
                        tree = getNewChild(tree.parent);
                    }
                    else{
                        if (tree.parent != null && Objects.equals(tree.parent.value, "конкатенация")
                                && tree.value.isEmpty()){
                            changeLastChild(tree.parent, tree.children.get(0));
                            tree = tree.parent;
                        }
                        if (!Objects.equals(tree.value, "") && !Objects.equals(tree.value, "+")){
                            if (tree.parent == null)
                                new GrammarTree("").add(tree);
                            tree = tree.parent;
                        }
                        tree.value = "+";
                        tree.position = i;
                        tree = getNewChild(tree);
                    }
                    implicitMultiply = false;
                    break;
                case '*':
                    if (tree.children.isEmpty())
                        throw new RegexpExeption("Неправильное использование итерации", i);
                    var childTree = tree.children.get(tree.children.size()-1);
                    if ((Objects.equals(childTree.value, "+") || Objects.equals(childTree.value, "конкатенация"))
                            && !tree.value.isEmpty())
                        tree.iterationAvailable = true;
                    else
                        childTree.iterationAvailable = true;

                    implicitMultiply = true;
                    break;
                default:
                    if (!Character.isLetterOrDigit(symbol))
                        throw new RegexpExeption("Неизвестный символ", i);
                    if (implicitMultiply){
                        if (tree.parent != null && Objects.equals(tree.parent.value, "конкатенация")){
                            if (tree.value.isEmpty())
                                processEmptyNode(tree, "конкатенация");
                            tree = getNewChild(tree.parent);
                        }
                        else
                            tree.value = "конкатенация";
                    }
                    var child = new GrammarTree(Character.toString(symbol));
                    child.position = i;
                    tree.add(child);

                    implicitMultiply = true;
            }
        }
        if (bracketsCount != 0)
            throw new RegexpExeption("Нет закрывающей скобки", regexp.length()-1);

        while (!tree.value.isEmpty() && tree.parent != null)
            tree = tree.parent;
        return tree;
    }

    private static void processEmptyNode(GrammarTree tree, String processedOperation){
        var child = tree.children.get(0);
        if (Objects.equals(child.value, processedOperation) && !child.iterationAvailable){
            changeLastChild(tree.parent, child.children.get(0));
            for (var j = 1; j < child.children.size(); j++)
                tree.parent.add(child.children.get(j));
        }
        else
            changeNode(tree, child);
    }

    private static GrammarTree getEmptyAncestorOrParent(GrammarTree tree){
        var parent = tree;
        while (!parent.value.isEmpty() && parent.parent != null)
            parent = parent.parent;
        return (parent.parent != null || parent.value.isEmpty())
                ? parent : tree.parent;
    }

    private static GrammarTree completeRawTree(GrammarTree tree) throws RegexpExeption {
        if (tree.value.isEmpty()){
            if (!tree.children.isEmpty()){
                var child = tree.children.get(0);
                if (tree.parent != null)
                    changeLastChild(tree.parent, child);
                child.parent = tree.parent;
                tree = child;
            }
            else if (tree.parent != null && !tree.parent.value.isEmpty())
                throw new RegexpExeption("Нет правого операнда для " + tree.parent.value,
                        tree.parent.position);
        }
        while (tree.parent != null)
            tree = tree.parent;

        while (tree.value.isEmpty()){
            if (tree.children.size() == 0)
                return tree;
            var child = tree.children.get(0);
            child.parent = null;
            tree = child;
        }

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

    private static GrammarTree changeLastChild(GrammarTree tree, GrammarTree child){
        tree.children.set(tree.children.size()-1, child);
        child.parent = tree;

        return child;
    }

    private static void changeNode(GrammarTree oldNode, GrammarTree newNode){
        oldNode.value = newNode.value;
        oldNode.position = newNode.position;
        oldNode.children = newNode.children;
        oldNode.iterationAvailable = newNode.iterationAvailable;
    }
}

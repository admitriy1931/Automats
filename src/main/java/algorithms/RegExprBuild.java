package algorithms;

import regexp.GrammarTree;
import regexp.LinearisedSymbol;
import regexp.RegexpException;

import java.util.*;


public class RegExprBuild {
    public static final String CON = "con";
    public static String emptyWordSymbol = "λ";

    public static Boolean allowEmptyWord(String regexp) throws RegexpException {
        return allowEmptyWord(makeGrammarTree(regexp));
    }

    private static Boolean allowEmptyWord(GrammarTree tree){
        if (tree.iterationAvailable || tree.canBeEmpty){
            return true;
        }
        if (Objects.equals(tree.value, emptyWordSymbol)){
            return true;
        }
        if (Objects.equals(tree.value, "+")){
            for (var child: tree.children)
                if (allowEmptyWord(child))
                    return true;
        }
        if (Objects.equals(tree.value, CON)){
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
        catch (RegexpException e){
            return null;
        }
    }

    private static HashMap<String, Integer> priorities = new HashMap<>() {{
        put("+", 1);
        put("con", 2);
    }};

    public static GrammarTree makeGrammarTree(String regexp) throws RegexpException {
        for (var i = 0; i < regexp.length(); i++){
            if (regexp.charAt(i) != ')')
                continue;
            var j = i-1;
            while (j > 0 && Character.isWhitespace(regexp.charAt(j)))
                j--;
            if (j < 0)
                continue;

            if (regexp.charAt(j) == '(')
                throw new RegexpException("Пустые скобки", i);
        }
        var rpn = makeReversePolishNotation(regexp);
        return makeTreeFromRPN(rpn);
    }

    private static GrammarTree makeTreeFromRPN(List<LinearisedSymbol> rpn) throws RegexpException {
        if (rpn.size() == 0)
            throw new RegexpException("Пустой ввод", -1);
        var current = new GrammarTree("");
        var trees = new Stack<GrammarTree>();

        for (var i = 0; i < rpn.size(); i++){
            var symbol = rpn.get(i);

            if (priorities.containsKey(symbol.getStringValue())){
                current = new GrammarTree(symbol.getStringValue());
                current.position = i;
                if (trees.size() < 2)
                    throw new RegexpException("Не хватает операнда", symbol.getNumber());

                var previous = trees.pop();
                current.add(trees.pop());
                current.add(previous);
                trees.add(current);
            }
            else if (Objects.equals(symbol.getStringValue(), "*")){
                if (trees.size() == 0)
                    throw new RegexpException("Неправильное использование итерации", symbol.getNumber());
                trees.lastElement().iterationAvailable = true;
            }
            else{
                current = new GrammarTree(symbol.getStringValue());
                current.position = i;
                trees.add(current);
            }
        }

        if (trees.size() != 1)
            throw new RegexpException("неправильное выражение", -1);
        return trees.pop();
    }

    private static List<LinearisedSymbol> makeReversePolishNotation(String regexp) throws RegexpException {
        var result = new ArrayList<LinearisedSymbol>();
        var stack = new Stack<LinearisedSymbol>();
        var implicitMultiply = false;
        for (var i = 0; i < regexp.length(); i++){
            var symbol = regexp.charAt(i);
            switch (symbol){
                case '(':
                    if (implicitMultiply)
                        handleImplicitMultiply(stack, result);
                    stack.add(new LinearisedSymbol(Character.toString(symbol), i));
                    implicitMultiply = false;
                    break;
                case ')':
                    while (stack.size() != 0 && !stack.lastElement().getStringValue().equals("("))
                        result.add(new LinearisedSymbol(stack.pop().getStringValue(), i));
                    if (stack.size() == 0)
                        throw new RegexpException("Нет открывающей скобки", i);
                    stack.pop();
                    implicitMultiply = true;
                    break;
                case '+':
                    while (stack.size() != 0 && priorities.containsKey(stack.lastElement().getStringValue())
                            && priorities.get(stack.lastElement().getStringValue()) >= priorities.get("+"))
                        result.add(new LinearisedSymbol(stack.pop().getStringValue(), i));
                    stack.add(new LinearisedSymbol(Character.toString(symbol), i));
                    implicitMultiply = false;
                    break;
                case '*':
                    result.add(new LinearisedSymbol(Character.toString(symbol), i));
                    break;
                case ' ':
                    continue;
                default:
                    if (!Character.isLetterOrDigit(symbol)
                            && !Objects.equals(emptyWordSymbol, Character.toString(symbol)))
                        throw new RegexpException(String.format("Неизвестный символ, %c", symbol), i);
                    if (implicitMultiply)
                        handleImplicitMultiply(stack, result);
                    result.add(new LinearisedSymbol(Character.toString(symbol), i));
                    implicitMultiply = true;
                    break;
            }
        }
        while (stack.size() != 0){
            var s = stack.pop();
            if (s.getStringValue().equals("("))
                throw new RegexpException("Нет закрывающей скобки", regexp.length()-1);
            result.add(new LinearisedSymbol(s.getStringValue(), s.getNumber()));
        }
        return result;
    }

    private static void handleImplicitMultiply(Stack<LinearisedSymbol> stack, List<LinearisedSymbol> result){
        while (stack.size() != 0 && priorities.containsKey(stack.lastElement().getStringValue())
                && priorities.get(stack.lastElement().getStringValue()) >= priorities.get(CON)){
            var s = stack.pop();
            result.add(new LinearisedSymbol(s.getStringValue(), s.getNumber()));
        }
        stack.add(new LinearisedSymbol(CON, -1));
    }
}

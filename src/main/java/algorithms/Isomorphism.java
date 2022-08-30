package algorithms;

import automat.Automat;
import automat.IsomorphismResult;
import com.google.common.collect.Lists;

import java.util.*;

public class Isomorphism {
    private Isomorphism() {}

    static HashMap<String, String> associations = new HashMap<>();

    public static void clear(){
        associations.clear();
    }

    public static IsomorphismResult automatsAreIsomorphic(Automat aut1, Automat aut2) throws CloneNotSupportedException {
        aut1 = Adduction.buildAdductedAutomat(aut1);
        aut2 = Adduction.buildAdductedAutomat(aut2);
        clear();
        var wordIn2ThatNotIn1 = Lists.reverse(
                findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, ""));
        clear();
        var wordIn1ThatNotIn2 = Lists.reverse(
                findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, ""));

        return new IsomorphismResult(
                (wordIn1ThatNotIn2.size() != 0 ? String.join("", wordIn1ThatNotIn2) : null),
                (wordIn2ThatNotIn1.size() != 0 ? String.join("", wordIn2ThatNotIn1) : null),
                (wordIn1ThatNotIn2.size() == 0 && wordIn2ThatNotIn1.size() == 0 ? associations : null));
    }

    public static List<String> getEndOfWord(String currentVertex, String prevLetter, Automat aut){
        return getEndOfWord(currentVertex, prevLetter, aut, new HashSet<>());
    }

    public static List<String> getEndOfWord(String currentVertex, String prevLetter,
                                            Automat aut, HashSet<String> visited){
        visited.add(currentVertex);
        if (aut.finalVertexes.contains(currentVertex)){
            var result = new ArrayList<String>();
            result.add(prevLetter);
            return result;
        }
        for (var letter : aut.letters) {
            var q = aut.getJumpByVertexAndLetter(currentVertex, letter);
            if (visited.contains(q) || aut.isVertexStock(q)){
                continue;
            }
            visited.add(q);
            var result = getEndOfWord(q, letter, aut, visited);
            if (result != null){
                result.add(prevLetter);
                return result;
            }
        }
        return null;
    }

    public static List<String> findWordIn1ThatNotIn2(
            Automat aut1, Automat aut2, String u, String v, String prevLetter){
        return findWordIn1ThatNotIn2(aut1, aut2, u, v, prevLetter, new HashMap<>());
    }

    public static List<String> findWordIn1ThatNotIn2(
            Automat aut1, Automat aut2, String u, String v, String prevLetter, HashMap<String, Boolean> prevVisited){
        var visited = (HashMap<String, Boolean>)prevVisited.clone();

        visited.put(u, true);

        if (aut1.finalVertexes.contains(u) != aut2.finalVertexes.contains(v)){
            if (aut1.finalVertexes.contains(u)){
                var result = new ArrayList<String>();
                result.add(prevLetter);
                return result;
            }
        }
        if (!associations.containsKey(u))
            associations.put(u, v);

        for (var letter : aut1.letters) {
            var q1 = aut1.getJumpByVertexAndLetter(u, letter);
            if (!aut2.letters.contains(letter) && !aut1.isVertexStock(q1)) {
                var result = getEndOfWord(q1, letter, aut1);
                result.add(prevLetter); // null не вылетит если q1 не сток, а мы это проверили
                return result;
            }
            else if (!aut2.letters.contains(letter)){
                continue;
            }
            var q2 = aut2.getJumpByVertexAndLetter(v, letter);
            if (!aut1.isVertexStock(q1)){
                if (aut2.isVertexStock(q2)){
                    var result = getEndOfWord(q1, letter, aut1);
                    result.add(prevLetter); // null не вылетит если q1 не сток, а мы это проверили
                    return result;
                }
                if (visited.containsKey(q1)){
                    if (!Objects.equals(q2, associations.get(q1))){
                        var result = getEndOfWord(q1, letter, aut1);
                        result.add(prevLetter);
                        return result;
                    }
                }
                else{
                    var result = findWordIn1ThatNotIn2(aut1, aut2, q1, q2, letter, visited);
                    if (result.size() != 0){
                        result.add(prevLetter);
                        return result;
                    }
                }
            }
            else{
                if (!associations.containsKey(q1))
                    associations.put(q1, q2);
            }
        }
        return new ArrayList<>();
    }
}

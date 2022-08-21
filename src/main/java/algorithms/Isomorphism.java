package algorithms;

import automat.Automat;
import automat.IsomorphismResult;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Isomorphism {
    private Isomorphism() {}

    static HashMap<String, Boolean> visited = new HashMap<>();
    static HashMap<String, String> associations = new HashMap<>();

    public static IsomorphismResult dfsIsomorphismCheck (Automat aut1, Automat aut2) {
        var wordIn2ThatNotIn1 = Lists.reverse(
                findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, ""));
        visited.clear();
        associations.clear();
        var wordIn1ThatNotIn2 = Lists.reverse(
                findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, ""));

        return new IsomorphismResult(
                (wordIn1ThatNotIn2.size() != 0 ? String.join("", wordIn1ThatNotIn2) : null),
                (wordIn2ThatNotIn1.size() != 0 ? String.join("", wordIn2ThatNotIn1) : null),
                (wordIn1ThatNotIn2.size() == 0 && wordIn2ThatNotIn1.size() == 0 ? associations : null));
    }

    public static List<String> getEndOfWord(String currentVertex, String prevLetter, Automat aut){
        if (aut.finalVertexes.contains(currentVertex)){
            var result = new ArrayList<String>();
            result.add(prevLetter);
            return result;
        }
        for (var letter : aut.letters) {
            var q = aut.getJumpByVertexAndLetter(currentVertex, letter);
            if (aut.isVertexStock(q)){
                continue;
            }
            var result = getEndOfWord(q, letter, aut);
            if (result != null){
                result.add(prevLetter);
                return result;
            }
        }
        return null;
    }

    public static List<String> findWordIn1ThatNotIn2(Automat aut1, Automat aut2, String u, String v, String prevLetter){
        visited.put(u, true);

        if (aut1.finalVertexes.contains(u) == aut2.finalVertexes.contains(v)){
            var result = new ArrayList<String>();
            result.add(prevLetter);
            return result;
        }
        associations.put(u, v);

        for (var letter : aut1.letters) {
            var q1 = aut1.getJumpByVertexAndLetter(u, letter);
            var q2 = aut2.getJumpByVertexAndLetter(u, letter);
            if (!aut1.isVertexStock(q1)){
                if (aut2.isVertexStock(q2)){
                    var result = getEndOfWord(q1, letter, aut1);
                    result.add(prevLetter); // null не вылетит если q1 не сток, а мы это проверили
                    return result;
                }
                if (visited.get(q1)){
                    if (!Objects.equals(q2, associations.get(q1))){
                        var result = getEndOfWord(q1, letter, aut1);
                        result.add(prevLetter);
                        return result;
                    }
                }
                else{
                    var result = findWordIn1ThatNotIn2(aut1, aut2, q1, q2, letter);
                    if (result.size() != 0){
                        result.add(prevLetter);
                        return result;
                    }
                }
            }
            else{
                associations.put(q1, q2);
            }
        }
        return new ArrayList<>();
    }
}

package automat;

import java.util.HashMap;
import java.util.Objects;

public class IsomorphismResult {
    public String wordIn1ThatNotIn2;
    public String wordIn2ThatNotIn1;
    public HashMap<String, String> associations;
    public Boolean areIsomorphic;

    public IsomorphismResult(String wordIn1ThatNotIn2, String wordIn2ThatNotIn1, HashMap<String, String> associations){
        this.wordIn1ThatNotIn2 = wordIn1ThatNotIn2;
        this.wordIn2ThatNotIn1 = wordIn2ThatNotIn1;
        this.associations = associations;
        this.areIsomorphic = wordIn1ThatNotIn2 == null && wordIn2ThatNotIn1 == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsomorphismResult that = (IsomorphismResult) o;
        return Objects.equals(wordIn1ThatNotIn2, that.wordIn1ThatNotIn2) && Objects.equals(wordIn2ThatNotIn1, that.wordIn2ThatNotIn1) && Objects.equals(associations, that.associations) && Objects.equals(areIsomorphic, that.areIsomorphic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordIn1ThatNotIn2, wordIn2ThatNotIn1, associations, areIsomorphic);
    }
}

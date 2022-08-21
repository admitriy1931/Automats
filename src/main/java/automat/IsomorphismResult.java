package automat;

import java.util.HashMap;

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
}

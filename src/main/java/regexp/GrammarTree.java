package regexp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GrammarTree {
    public String value;
    public Integer position = -1;
    public GrammarTree parent;
    public List<GrammarTree> children = new ArrayList<>();
    public Boolean iterationAvailable = false;

    public GrammarTree(String value){
        this.value = value;
    }

    public void add(GrammarTree child){
        this.children.add(child);
        child.parent = this;
    }

    public Boolean subTreesAreEqual(GrammarTree other){
        if (!Objects.equals(this.value, other.value)) return false;
        if (this.children.size() != other.children.size()) return false;
        if (this.iterationAvailable != other.iterationAvailable) return false;
        for (var i = 0; i < this.children.size(); i++){
            if (!this.children.get(i).subTreesAreEqual(other.children.get(i)))
                return false;
        }
        return true;
    }
}

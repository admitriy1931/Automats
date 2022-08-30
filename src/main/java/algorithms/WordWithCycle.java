package algorithms;

import java.util.ArrayList;
import java.util.List;

public class WordWithCycle {
    public List<String> start;
    public List<String> cycle;
    public List<String> end;

    public WordWithCycle(List<String> start, List<String> cycle, List<String> end){
        this.start = start;
        this.cycle = cycle;
        this.end = end;
    }

    public WordWithCycle(List<String> start){
        this.start = start;
        this.cycle = new ArrayList<>();
        this.end = new ArrayList<>();
    }
}

package algorithms;

import automat.Automat;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adduction {
    private Adduction() {
    }

    public static Automat findReachableVertexes(Automat automat) {
        Set<String> reachedVertexes = Sets.newHashSet();
        reachedVertexes.add(automat.startVertex);
        boolean running = true;

        while(running) {
            Set<String> iteratedReachedVertexes = Sets.newHashSet();
            reachedVertexes.forEach(x -> iteratedReachedVertexes.addAll(automat.getAllJumpsByVertex(x)));
            iteratedReachedVertexes.addAll(reachedVertexes);
            if (iteratedReachedVertexes.equals(reachedVertexes)) running = false;
            reachedVertexes = iteratedReachedVertexes;
        }

        Set<String> notReachedVertexes = Sets.difference(new HashSet<>(automat.vertexes), reachedVertexes);
        notReachedVertexes.forEach(automat::removeVertex);
        return automat;
    }

    public static Automat findEquivalentVertexes(Automat automat) {
        List<List<String>> eqClasses = Lists.newArrayList();
        eqClasses.add(automat.finalVertexes);
        eqClasses.add(Lists.newArrayList(Sets.difference(new HashSet<>(automat.vertexes), new HashSet<>(automat.finalVertexes))));
        int notSplitCounter = 0;

        do {
            List<String > pickedClass = eqClasses.get(0);
            List<List<String>> dividedClass = Lists.newArrayList();
            List<String> hashes = Lists.newArrayList();
            for (String letter : pickedClass) {
                StringBuilder hash = new StringBuilder();
                for (int i = 0; i < eqClasses.size(); i++) {
                    if ((eqClasses.get(i)).contains(letter)) {
                        hash.append(i);
                    }
                }
                hashes.add(hash.toString());
            }

            //divide into separate classes based on dividedClass


            if (dividedClass.size() == 1) {
                eqClasses.add(eqClasses.remove(0));
                notSplitCounter++;
            } else {
                eqClasses.remove(0);
                eqClasses.addAll(dividedClass);
                notSplitCounter = 0;
            }
        } while(notSplitCounter != eqClasses.size());

        return null;
    }
}

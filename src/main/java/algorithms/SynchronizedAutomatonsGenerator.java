package algorithms;

import com.google.common.collect.HashBasedTable;

import java.util.*;

import automaton.SynchronizedAutomaton;

public class SynchronizedAutomatonsGenerator {
    /**
     * Генерирует заданное количество синхронизируемых автоматов
     *
     * @param automatonsNumber количество автоматов
     * @param verticesNumber   количество вершин (для данной заадчи оптимальное количество - 3 или 4)
     * @param lettersNumber    количество букв (для данной задачи оптимальное количество - 2 или 3)
     * @return список синхронизируемых автоматов
     */
    public static List<SynchronizedAutomaton> synchronizedAutomatonsGenerator
    (int automatonsNumber, int verticesNumber, int lettersNumber) throws CloneNotSupportedException {
        if (verticesNumber < 2)
            return new ArrayList<>();

        List<SynchronizedAutomaton> syncAutsList = new ArrayList<>();
        List<String> letters = new ArrayList<>(List.of("a", "b", "c", "d"));
        Random randomInt = new Random(System.currentTimeMillis());

        int counter = 0;
        while (counter != automatonsNumber) {
            HashBasedTable<String, String, String> currentTable = HashBasedTable.create();

            for (int i = 1; i <= verticesNumber; i++)
                for (int j = 0; j < lettersNumber; j++)
                    currentTable.put(String.valueOf(i), letters.get(j),
                        String.valueOf(1 + randomInt.nextInt(verticesNumber)));

            SynchronizedAutomaton currentSyncAut = new SynchronizedAutomaton
                (currentTable, new ArrayList<>(), new ArrayList<>(), null, null);

            if (GreedySyncWordFinding.isAutomatonSynchronized(currentSyncAut)) {
                GreedySyncWordFinding.addTwoElementVertices(currentSyncAut);
                GreedySyncWordFinding.greedyWordFindingAlg(currentSyncAut);

                currentSyncAut = GreedySyncWordFinding.addCombinedElementVertices(currentSyncAut);
                currentSyncAut = GreedySyncWordFinding.greedyShortestWordFindingAlg(currentSyncAut);

                syncAutsList.add(currentSyncAut);
                counter += 1;
            }
        }
        return syncAutsList;
    }
}

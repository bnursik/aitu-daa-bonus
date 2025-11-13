package app;

import logic.MSTUtils;
import model.Edge;
import model.Graph;
import visual.GraphRenderer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Graph g = SampleGraphs.createSampleGraph();

        // 1. Build MST
        List<Edge> mst = MSTUtils.buildMST(g);

        System.out.println("Original MST edges:");
        double totalWeight = 0;
        for (Edge e : mst) {
            System.out.println("  " + e);
            totalWeight += e.w;
        }
        System.out.println("Total MST weight = " + totalWeight);
        System.out.println();

        // 2. RANDOMLY choose an edge to remove from the MST
        Random random = new Random(); // you can pass a seed e.g. new Random(42) for reproducible runs
        int removeIndex = random.nextInt(mst.size());
        Edge removed = mst.get(removeIndex);

        System.out.println("Randomly selected edge index in MST: " + removeIndex);
        System.out.println("Removed edge from MST: " + removed);

        // 3. Components after removal
        int[] comp = MSTUtils.computeComponents(g.n, mst, removed);

        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int v = 0; v < comp.length; v++) {
            groups.computeIfAbsent(comp[v], k -> new ArrayList<>()).add(v);
        }

        System.out.println("Components after removal:");
        for (Map.Entry<Integer, List<Integer>> entry : groups.entrySet()) {
            System.out.println("  Component " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        // 4. Find replacement edge (lightest edge connecting different components)
        Edge replacement = MSTUtils.findReplacementEdge(g, comp);
        if (replacement == null) {
            System.out.println("No replacement edge found (graph disconnected).");
        } else {
            System.out.println("Replacement edge chosen: " + replacement);
        }

        // 5. New MST = MST - removed + replacement
        List<Edge> newMST = new ArrayList<>(mst);
        newMST.remove(removed);
        if (replacement != null) {
            newMST.add(replacement);
        }

        double newTotalWeight = 0;
        System.out.println("\nNew MST edges:");
        for (Edge e : newMST) {
            System.out.println("  " + e);
            newTotalWeight += e.w;
        }
        System.out.println("New MST total weight = " + newTotalWeight);

        // 6. Save all stages as PNG in ./output
        Path outDir = Paths.get("output");
        int width = 800;
        int height = 600;

        // Stage 1: original graph, no MST
        GraphRenderer.saveStageImage(
                g,
                Collections.emptyList(),
                null,
                null,
                "Original Graph",
                outDir,
                "1_original.png",
                width,
                height);

        // Stage 2: MST
        GraphRenderer.saveStageImage(
                g,
                mst,
                null,
                null,
                "MST",
                outDir,
                "2_mst.png",
                width,
                height);

        // Stage 3: MST after random removal
        List<Edge> mstAfterRemoval = new ArrayList<>(mst);
        mstAfterRemoval.remove(removed);
        GraphRenderer.saveStageImage(
                g,
                mstAfterRemoval,
                removed,
                null,
                "After Removal",
                outDir,
                "3_after_removal.png",
                width,
                height);

        // Stage 4: new MST with replacement
        GraphRenderer.saveStageImage(
                g,
                newMST,
                removed,
                replacement,
                "New MST with Replacement",
                outDir,
                "4_new_mst.png",
                width,
                height);

        // Optional GUI window for the final stage
        GraphRenderer.showInWindow(
                g,
                newMST,
                removed,
                replacement,
                "Final MST",
                width,
                height);
    }
}

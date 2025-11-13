package logic;

import model.Edge;
import model.Graph;

import java.util.*;

public class MSTUtils {

    public static List<Edge> buildMST(Graph g) {
        List<Edge> sorted = new ArrayList<>(g.edges);
        sorted.sort(Comparator.comparingDouble(e -> e.w));

        DSU dsu = new DSU(g.n);
        List<Edge> mst = new ArrayList<>();

        for (Edge e : sorted) {
            if (dsu.union(e.u, e.v)) {
                mst.add(e);
            }
        }
        return mst;
    }

    public static int[] computeComponents(int n, List<Edge> mst, Edge removed) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++)
            adj.add(new ArrayList<>());

        for (Edge e : mst) {
            if (e.equals(removed))
                continue;
            adj.get(e.u).add(e.v);
            adj.get(e.v).add(e.u);
        }

        int[] comp = new int[n];
        Arrays.fill(comp, -1);
        int compId = 0;

        for (int i = 0; i < n; i++) {
            if (comp[i] != -1)
                continue;
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(i);
            comp[i] = compId;

            while (!stack.isEmpty()) {
                int v = stack.pop();
                for (int to : adj.get(v)) {
                    if (comp[to] == -1) {
                        comp[to] = compId;
                        stack.push(to);
                    }
                }
            }
            compId++;
        }
        return comp;
    }

    public static Edge findReplacementEdge(Graph g, int[] comp) {
        Edge best = null;
        for (Edge e : g.edges) {
            int cu = comp[e.u];
            int cv = comp[e.v];
            if (cu != cv) {
                if (best == null || e.w < best.w) {
                    best = e;
                }
            }
        }
        return best;
    }
}

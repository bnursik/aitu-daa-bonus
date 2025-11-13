package model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public final int n; 
    public final List<Edge> edges = new ArrayList<>();

    public Graph(int n) {
        this.n = n;
    }

    public void addEdge(int u, int v, double w) {
        edges.add(new Edge(u, v, w));
    }
}

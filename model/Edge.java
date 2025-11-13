package model;

import java.util.Objects;

public class Edge {
    public final int u;
    public final int v;
    public final double w;

    public Edge(int u, int v, double w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Edge))
            return false;
        Edge edge = (Edge) o;
        boolean same = (u == edge.u && v == edge.v);
        boolean reversed = (u == edge.v && v == edge.u);
        return Double.compare(edge.w, w) == 0 && (same || reversed);
    }

    @Override
    public int hashCode() {
        int a = Math.min(u, v);
        int b = Math.max(u, v);
        return Objects.hash(a, b, w);
    }

    @Override
    public String toString() {
        return "(" + u + " - " + v + ", w=" + w + ")";
    }
}

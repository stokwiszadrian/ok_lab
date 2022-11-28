package main;

import graph.Edge;
import graph.Graph;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Edge[] edges = new Edge[] {
                new Edge(0, 1, 6),
                new Edge(1, 2, 12),
                new Edge(2, 3, 2),
                new Edge(3, 4, 9),
                new Edge(4, 5, 14),
                new Edge(5, 6, 9),
                new Edge(6, 0, 5),
                new Edge(2, 4, 4),
                new Edge(4, 6, 7),
                new Edge(6, 3, 7)
        };
        Graph g = new Graph(edges.length, 7, edges);
        g.applyKruskal();
    }
}
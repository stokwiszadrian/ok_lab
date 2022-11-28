package main;

import graph.Edge;
import graph.Graph;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Edge[] edges = new Edge[] {
                new Edge(0, 1, 7),
                new Edge(1, 2, 8),
                new Edge(2, 4, 5),
                new Edge(4, 6, 9),
                new Edge(5, 6, 11),
                new Edge(3, 5, 6),
                new Edge(0, 3, 5),
                new Edge(1, 3, 9),
                new Edge(1, 4, 7),
                new Edge(3, 4, 15),
                new Edge(4, 5, 8),
        };
        Graph g = new Graph(edges.length, 7, edges);
        g.applyKruskal();
    }
}
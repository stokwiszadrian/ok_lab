package main;

import graph.Edge;
import graph.Graph;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        ArrayList<Edge> edges = new ArrayList<>(List.of(new Edge[]{
                new Edge(0, 1, 2),
                new Edge(0, 2, 6),
                new Edge(0, 3, 8),
                new Edge(0, 4, 4),
                new Edge(1, 2, 6),
                new Edge(1, 3, 7),
                new Edge(1, 4, 3),
                new Edge(2, 3, 8),
                new Edge(2, 4, 5),
                new Edge(3, 4, 9),
        }));
        Graph g = new Graph(edges.size(), 5, edges);
//        Graph g = new Graph();
        g.christofides();
    }
}
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
                new Edge(0, 1, 3),
                new Edge(1, 2, 5),
                new Edge(2, 3, 5),
                new Edge(3, 4, 9),
                new Edge(4, 5, 6),
                new Edge(5, 0, 4),
                new Edge(1, 5, 8),
                new Edge(2, 5, 14),
                new Edge(2, 4, 10),
        }));
        Graph g = new Graph(edges.size(), 6, edges);
//        Graph g = new Graph();
        g.CPP();
    }
}
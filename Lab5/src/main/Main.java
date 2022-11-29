package main;

import graph.Edge;
import graph.Graph;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
//        ArrayList<Edge> edges = new ArrayList<>(List.of(new Edge[]{
//                new Edge(0, 1, 3),
//                new Edge(1, 3, 1),
//                new Edge(3, 5, 1),
//                new Edge(1, 5, 6),
//                new Edge(4, 5, 4),
//                new Edge(0, 2, 1),
//                new Edge(2, 4, 2),
//                new Edge(0, 4, 5),
//        }));
//        Graph g = new Graph(edges.size(), 6, edges);
        Graph g = new Graph();
        g.CPP();
    }
}
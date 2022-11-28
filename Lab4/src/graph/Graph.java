package graph;

import java.util.*;

public class Graph {

    int edges;
    int vertices;
    Edge[] edgeArray;

    public Graph(int edges, int vertices) {
        this.edges = edges;
        this.vertices = vertices;
        edgeArray = new Edge[this.edges];
        for (int i = 0; i < edges; i++) {
            edgeArray[i] = new Edge();
        }
    }

    public Graph(int edges, int vertices, Edge[] edgeArray) {
        this.edges = edges;
        this.vertices = vertices;
        this.edgeArray = edgeArray;
    }
    public void applyKruskal() {

        // Inicjalizacja finalResult - tablicy przechowującej końcowe MST
        Edge[] finalResult = new Edge[vertices];
        int newEdge = 0;
        int index = 0;
        for (index = 0; index < vertices; ++index)
            finalResult[index] = new Edge();

        // sortowanie krawędzi
        Arrays.sort(edgeArray);

        // Tworzymy podzbiory dla każdego wierzchołka
        Subset[] subsetArray = new Subset[vertices];

        for (index = 0; index < vertices; ++index)
            subsetArray[index] = new Subset();

        for (int vertex = 0; vertex < vertices; ++vertex) {
            subsetArray[vertex].parent = vertex;
            subsetArray[vertex].value = 0;
        }
        index = 0;

        // Iterujemy dopóki nie znajdziemy wszystkich krawędzi
        while (newEdge < vertices - 1) {
            // tworzenie instancji Edge dla nowej krawędzi
            Edge nextEdge = edgeArray[index++];

            int nextsrc = findSetOfElement(subsetArray, nextEdge.src);
            int nextdest = findSetOfElement(subsetArray, nextEdge.dest);

            // Jeśli krawędź nie tworzy cyklu, to dodajemy ją do rozwiązanie
            if (nextsrc != nextdest) {
                finalResult[newEdge++] = nextEdge;
                performUnion(subsetArray, nextsrc, nextdest);
            }
        }

        // Wypisanie wyniku

        for (index = 0; index < newEdge; ++index)
            System.out.println(finalResult[index].src + " - " + finalResult[index].dest + ": " + finalResult[index].weight);
    }

    // create findSetOfElement() method to get set of an element  
    private int findSetOfElement(Subset[] subsetArray, int i) {
        if (subsetArray[i].parent != i)
            subsetArray[i].parent = findSetOfElement(subsetArray, subsetArray[i].parent);
        return subsetArray[i].parent;
    }

    // metoda łącząca zbiory
    private void performUnion(Subset[] subsetArray, int srcRoot, int destRoot) {

        int nextsrcRoot = findSetOfElement(subsetArray, srcRoot);
        int nextdestRoot = findSetOfElement(subsetArray, destRoot);

        if (subsetArray[nextsrcRoot].value < subsetArray[nextdestRoot].value)
            subsetArray[nextsrcRoot].parent = nextdestRoot;
        else if (subsetArray[nextsrcRoot].value > subsetArray[nextdestRoot].value)
            subsetArray[nextdestRoot].parent = nextsrcRoot;
        else {
            subsetArray[nextdestRoot].parent = nextsrcRoot;
            subsetArray[nextsrcRoot].value++;
        }


    }
}
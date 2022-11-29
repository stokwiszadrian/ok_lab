package graph;

import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.util.*;

public class Graph {

    public int edges;
    int vertices;
    ArrayList<Edge> edgeArray;

    public int[] dist;

    public PriorityQueue<Integer> pq;

    public int[] shortest;
    public HashSet<Integer> settled;
    public Graph(int edges, int vertices) {
        this.edges = edges;
        this.vertices = vertices;
        edgeArray = new ArrayList<>();
        for (int i = 0; i < edges; i++) {
            edgeArray.add(new Edge());
        }
        dist = new int[vertices];
        pq = new PriorityQueue<Integer>(vertices);
        settled = new HashSet<>();
        shortest = new int[vertices];
    }

    public Graph(int edges, int vertices, ArrayList<Edge> edgeArray) {
        this.edges = edges;
        this.vertices = vertices;
        this.edgeArray = edgeArray;
        dist = new int[vertices];
        pq = new PriorityQueue<>(vertices);
        settled = new HashSet<>();
        shortest = new int[vertices];
    }

    public boolean applyFleury(int v, ArrayList<Edge> edgesLeft) {
        ArrayList<Edge> edgesFrom = edgesFromV(v, edgesLeft);
//        System.out.println("EDGES FROM: " + edgesFrom.size());
//        System.out.println(edgesLeft);
        if (edgesLeft.size() == 0) return true;
        if (edgesFrom.size() == 1) {
            Edge e = edgesFrom.get(0);
//            System.out.println(e.src + " " +  e.dest + " " + edgesLeft + " == 1");
            edgesLeft.remove(e);
//            System.out.println(e.src + " " +  e.dest + " " + edgesLeft + " == 1 AFTER");
            System.out.println(e.src + " - " + e.dest + ": " + e.weight);
            applyFleury(e.dest, edgesLeft);
        }
        else if (edgesFrom.size() > 1) {
            for (Edge e : edgesFrom) {
                int count1 = dfs(e.src);
                edgeArray.remove(e);
                int count2 = dfs(e.src);
                edgeArray.add(e);
                if (count1 <= count2) {
//                    System.out.println(e.src + " " +  e.dest + " " + edgesLeft + " > 2");
                    edgesLeft.remove(e);
//                    System.out.println(e.src + " " +  e.dest + " " + edgesLeft + " > 2 AFTER");
                    System.out.println(e.src + " - " + e.dest + ": " + e.weight);
                    applyFleury(e.dest, edgesLeft);
                    break;
                }
                else {
//                    System.out.println("Rozspójnia");
                }
            }
        }
        else {
            System.out.println("No edges from x!");
            return false;
        }
        return false;
    }

    public void nonEulerian() {
        Set<Integer> oddVertices = new LinkedHashSet<>();
        for (int v = 0; v < vertices; v++) {
            System.out.println(edgesFromV(v, edgeArray).size());
            if (edgesFromV(v, edgeArray).size() % 2 != 0) {
                oddVertices.add(v);
            }
        }
        ArrayList<List<List<Integer>>> pairings = new ArrayList<List<List<Integer>>>();
        generatePairs(oddVertices, new ArrayList<List<Integer>>(), pairings);

        int minSum = Integer.MAX_VALUE;
        ArrayList<List<Integer>> minResult = new ArrayList<>();
        ArrayList<List<Edge>> minPaths = new ArrayList<>();
        for (List<List<Integer>> result : pairings)
        {
//            System.out.println(result);
            int curSum = 0;
            ArrayList<List<Edge>> paths = new ArrayList<>();
            for (List<Integer> pair : result) {
                System.out.println("PAIR: " + pair.get(0) + "  " + pair.get(1));
                this.dijkstra(pair.get(0));
                curSum += dist[pair.get(1)];
                ArrayList<Edge> path = new ArrayList<>();
//                for (int j=0; j < edges; j++) {
//                    System.out.println("SHORTEST: " + shortest[j] + " " + shortest [j+1]);
//                    Edge e = getEdge(shortest[j], shortest[j+1]);
//                    path.add(e);
//                    if (shortest[j+1] == pair.get(0)) {
//                        paths.add(path);
//                        break;
//                    }
//                }
                int j = pair.get(1);
                for (int i : shortest) {
                    System.out.println(i + " <-");
                }
                while (j != pair.get(0)) {
                    int next = shortest[j];
                    System.out.println("SHORTEST: " + next + " " + shortest[next]);
                    Edge e = getEdge(next, shortest[next]);
                    path.add(e);
                    j = next;
                }
                Edge lastEdge = getEdge(pair.get(0), j);
                System.out.println("LAST EDGE: " + lastEdge.src + " " + lastEdge.dest + " " + lastEdge.weight);
                path.add(lastEdge);
                paths.add(path);
            }
            for (int i : dist) {
                System.out.println("dist: "+i);
            }
            System.out.println(curSum);
            if (curSum < minSum) {
                minSum = curSum;
                minResult = (ArrayList<List<Integer>>) result;
                minPaths = paths;
            }
        }

//        System.out.println("MIN RESULT, MIN SUM:");
//        System.out.println(minResult + "  " + minSum);;
        ArrayList<Edge> newEdges = new ArrayList<>();
        for (List<Edge> p : minPaths) {
            newEdges.addAll(p);
            for (Edge e : p) {
                System.out.println("EDGE" + e.src + e.dest + e.weight);
            }
        }
        edgeArray.addAll(newEdges);
        System.out.println(edgeArray);
        ArrayList<Edge> edgesLeft = new ArrayList<>();
        edgesLeft.addAll(edgeArray);
        applyFleury(0, edgesLeft);
    }

    private void generatePairs(Set<Integer> set, List<List<Integer>> currentResults, List<List<List<Integer>>> results) {
        if (set.size() < 2)
        {
            results.add(new ArrayList<List<Integer>>(currentResults));
            return;
        }
        List<Integer> list = new ArrayList<Integer>(set);
        Integer first = list.remove(0);
        for (int i=0; i<list.size(); i++)
        {
            Integer second = list.get(i);
            Set<Integer> nextSet = new LinkedHashSet<Integer>(list);
            nextSet.remove(second);

            List<Integer> pair = Arrays.asList(first, second);
            currentResults.add(pair);
            generatePairs(nextSet, currentResults, results);
            currentResults.remove(pair);
        }
    }

    public void dijkstra( int src)
    {
        dist = new int[vertices];
        pq = new PriorityQueue<>(vertices);
        settled = new HashSet<>();
        shortest = new int[vertices];
        for (int i = 0; i < vertices; i++)
            dist[i] = Integer.MAX_VALUE;

        // Add source node to the priority queue
        pq.add(src);

        // Distance to the source is 0
        dist[src] = 0;

        while (settled.size() != vertices) {

            // Terminating condition check when
            // the priority queue is empty, return
            if (pq.isEmpty())
                return;

            // Removing the minimum distance node
            // from the priority queue
            int u = (int) pq.remove();

            // Adding the node whose distance is
            // finalized
            if (settled.contains(u))

                // Continue keyword skips execution for
                // following check
                continue;

            // We don't have to call e_Neighbors(u)
            // if u is already present in the settled set.
            settled.add(u);

            e_Neighbours(u);
        }
    }

    private void e_Neighbours(int u)
    {

        int edgeDistance = -1;
        int newDistance = -1;

        // All the neighbors of v
        for (int i = 0; i < edgesFromV(u, edgeArray).size(); i++) {
            Edge e = edgesFromV(u, edgeArray).get(i);

            int node = e.src;
            if (node == u) {
                node = e.dest;
            }

            // If current node hasn't already been processed
            if (!settled.contains(node)) {
                edgeDistance = e.weight;
                newDistance = dist[u] + edgeDistance;

                // If new distance is cheaper in cost
                if (newDistance < dist[node]){
                    dist[node] = newDistance;
                    shortest[node] = u;
                }

                // Add the current node to the queue
                pq.add(node);
            }
        }
    }

    public Edge getEdge(int src, int dest) {
        for (Edge e : edgeArray) {
            if ((e.src == src && e.dest == dest) || (e.src == dest && e.dest == src)) return new Edge(e.src, e.dest, e.weight);
        }
        return new Edge(0, 0, 1);
    }

//    public int calcPath(int src, int dest) {
//        dijkstra(src);
//
//    }

    public ArrayList<Edge> edgesFromV(int v, ArrayList<Edge> edgesLeft) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Edge e : edgesLeft) {
            if (e.src == v || e.dest == v) {
                edges.add(e);
            }
        }
        return edges;
    }

    public int dfs(int v) {
        ArrayList<Integer> treelist = new ArrayList<>();
        visit(treelist, v);
        return treelist.size();
    }

    public void visit(ArrayList<Integer> treelist, int v) {
//        ArrayList<Edge> edges = new ArrayList<>(List.of(edgeArray));
        for (int i = 0; i < vertices; i++) {
            if (!treelist.contains(i) && checkEdge(v, i)) {
                treelist.add(i);
                visit(treelist, i);
            }
        }

    }
    public boolean checkEdge(int src, int dest) {
        for (Edge e : edgeArray) {
            if ((e.src == src && e.dest == dest) || (e.src == dest && e.dest == src)) return true;
        }
        return false;
    }
//    public void applyKruskal() {
//
//        // Inicjalizacja finalResult - tablicy przechowującej końcowe MST
//        Edge[] finalResult = new Edge[vertices];
//        int newEdge = 0;
//        int index = 0;
//        for (index = 0; index < vertices; ++index)
//            finalResult[index] = new Edge();
//
//        // sortowanie krawędzi
//        Arrays.sort(edgeArray);
//
//        // Tworzymy podzbiory dla każdego wierzchołka
//        Subset[] subsetArray = new Subset[vertices];
//
//        for (index = 0; index < vertices; ++index)
//            subsetArray[index] = new Subset();
//
//        for (int vertex = 0; vertex < vertices; ++vertex) {
//            subsetArray[vertex].parent = vertex;
//            subsetArray[vertex].value = 0;
//        }
//        index = 0;
//
//        // Iterujemy dopóki nie znajdziemy wszystkich krawędzi
//        while (newEdge < vertices - 1) {
//            // tworzenie instancji Edge dla nowej krawędzi
//            Edge nextEdge = edgeArray[index++];
//
//            int nextsrc = findSetOfElement(subsetArray, nextEdge.src);
//            int nextdest = findSetOfElement(subsetArray, nextEdge.dest);
//
//            // Jeśli krawędź nie tworzy cyklu, to dodajemy ją do rozwiązanie
//            if (nextsrc != nextdest) {
//                finalResult[newEdge++] = nextEdge;
//                performUnion(subsetArray, nextsrc, nextdest);
//            }
//        }
//
//        // Wypisanie wyniku
//
//        for (index = 0; index < newEdge; ++index)
//            System.out.println(finalResult[index].src + " - " + finalResult[index].dest + ": " + finalResult[index].weight);
//    }
//
//    // create findSetOfElement() method to get set of an element
//    private int findSetOfElement(Subset[] subsetArray, int i) {
//        if (subsetArray[i].parent != i)
//            subsetArray[i].parent = findSetOfElement(subsetArray, subsetArray[i].parent);
//        return subsetArray[i].parent;
//    }

    // metoda łącząca zbiory
//    private void performUnion(Subset[] subsetArray, int srcRoot, int destRoot) {
//
//        int nextsrcRoot = findSetOfElement(subsetArray, srcRoot);
//        int nextdestRoot = findSetOfElement(subsetArray, destRoot);
//
//        if (subsetArray[nextsrcRoot].value < subsetArray[nextdestRoot].value)
//            subsetArray[nextsrcRoot].parent = nextdestRoot;
//        else if (subsetArray[nextsrcRoot].value > subsetArray[nextdestRoot].value)
//            subsetArray[nextdestRoot].parent = nextsrcRoot;
//        else {
//            subsetArray[nextdestRoot].parent = nextsrcRoot;
//            subsetArray[nextsrcRoot].value++;
//        }
//
//
//    }
}
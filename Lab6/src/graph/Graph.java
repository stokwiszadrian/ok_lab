package graph;

import java.lang.reflect.Array;
import java.util.*;

public class Graph {

    public int edges;
    int vertices;
    ArrayList<Edge> edgeArray;

    public int[] dist;

    public PriorityQueue<Node> pq;

    public int[] shortest;
    public HashSet<Integer> settled;
    public Graph() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj ilość wierzchołków: ");
        this.vertices = Integer.parseInt(scanner.next());
        System.out.println("Podaj ilość krawędzi: ");
        this.edges = Integer.parseInt(scanner.next());
        edgeArray = new ArrayList<>();
        for (int i = 0; i < edges; i++) {
            System.out.println(i + ") Podaj wierzchołek początkowy: ");
            int src = Integer.parseInt(scanner.next());
            System.out.println(i + ") Podaj wierzchołek końcowy: ");
            int dest = Integer.parseInt(scanner.next());
            System.out.println(i + ") Podaj wagę krawędzi: ");
            int weight = Integer.parseInt(scanner.next());
            if ((0 > src  || src >= vertices) || (0 > dest || dest >= vertices)) {
                throw new Exception("Niepoprawna wartość wierzchołka");
            }
            else edgeArray.add(new Edge(src, dest, weight));
        }
        dist = new int[vertices];
        settled = new HashSet<>();
        shortest = new int[vertices];
    }

    public Graph(int edges, int vertices, ArrayList<Edge> edgeArray) {
        this.edges = edges;
        this.vertices = vertices;
        this.edgeArray = edgeArray;
        dist = new int[vertices];
        settled = new HashSet<>();
        shortest = new int[vertices];
    }

    public void christofides() {
        // tworzenie minimalnego drzewa rozpinającego
        Graph mst = new Graph(vertices - 1, vertices, this.applyKruskal());
        System.out.println("MST EDGEARRAY:");
        for (int index = 0; index < mst.edgeArray.size(); ++index)
            System.out.println(mst.edgeArray.get(index).src + " - " + mst.edgeArray.get(index).dest + ": " + mst.edgeArray.get(index).weight);
        System.out.println("CPP:");
//        mst.CPP();
        // tworzenie zbioru wierzchołków o nieparzystym stopniu w mst
        Set<Integer> oddVertices = new LinkedHashSet<>();
        System.out.println("EDGE ARRAY SIZE: " + mst.edgeArray);
        for (int v = 0; v < vertices; v++) {
            if (edgesFromV(v, mst.edgeArray).size() % 2 != 0) {
                oddVertices.add(v);
                System.out.println("ODD EDGE: " + v);
            }
        }

        ArrayList<Edge> subgraphEdges = new ArrayList<>();
        ArrayList<Integer> oddVerticesList = new ArrayList<>(oddVertices.stream().toList());
        System.out.println("SIZE: " + oddVertices.size());
        for (int v = 0; v < oddVertices.size() - 1; v++) {
            for (int w = v + 1; w < oddVertices.size(); w++) {
                System.out.println(oddVerticesList.get(v) + "  :  " + oddVerticesList.get(w));
                Edge subEdge = getEdge(oddVerticesList.get(v), oddVerticesList.get(w));
                subgraphEdges.add(subEdge);
            }
        }
        for (int index = 0; index < subgraphEdges.size(); ++index)
            System.out.println(subgraphEdges.get(index).src + " - " + subgraphEdges.get(index).dest + ": " + subgraphEdges.get(index).weight);

        Graph subgraph = new Graph(subgraphEdges.size(), vertices, subgraphEdges);

        ArrayList<List<List<Integer>>> pairings = new ArrayList<List<List<Integer>>>();
        generatePairs(oddVertices, new ArrayList<List<Integer>>(), pairings);
//
//        // znalezienie zbioru par wierchołków o najmniejszej wadze
//
        int minSum = Integer.MAX_VALUE;
        ArrayList<List<Edge>> minPaths = new ArrayList<>();

        for (List<List<Integer>> result : pairings) {

            int curSum = 0;
            ArrayList<List<Edge>> paths = new ArrayList<>();
            for (List<Integer> pair : result) {
                subgraph.dijkstra(pair.get(0));
                curSum += subgraph.dist[pair.get(1)];
                ArrayList<Edge> path = new ArrayList<>();
                int j = pair.get(1);

                while (j != pair.get(0)) {
                    int next = subgraph.shortest[j];
                    Edge e = getEdge(j, next);
                    path.add(e);
                    j = next;
                }

                paths.add(path);
            }

            if (curSum < minSum) {
                minSum = curSum;
                minPaths = paths;
            }
        }

        ArrayList<Edge> newEdges = new ArrayList<>();
        for (List<Edge> p : minPaths) {
            newEdges.addAll(p);
        }
        mst.edgeArray.addAll(newEdges);
        ArrayList<Edge> edgesLeft = new ArrayList<>(mst.edgeArray);

        System.out.println("MST AFTER FLEURY: ");
        ArrayList<Integer> visitedVertices = new ArrayList<>();
        mst.applyFleury(0, edgesLeft, visitedVertices);

    }


    public boolean printCheck(ArrayList<Integer> visitedVertices, ArrayList<Edge> edgesLeft, int dest) {
        return (edgesLeft.isEmpty() || !visitedVertices.contains(dest));
    }
    public boolean applyFleury(int v, ArrayList<Edge> edgesLeft, ArrayList<Integer> visitedVertices) {
        ArrayList<Edge> edgesFrom = edgesFromV(v, edgesLeft);
        if (edgesLeft.size() == 0) return true;
        if (edgesFrom.size() == 1) {

            // Jeśli jest 1 krawędź, usuwamy ją i przechodzimy dalej

            Edge e = edgesFrom.get(0);
            edgesLeft.remove(e);
            System.out.println("CHECK == 1: " + e.src + " : " + e.dest + "  " + visitedVertices);
            if (e.dest != v) {
                 if (printCheck(visitedVertices, edgesLeft, e.dest))
                     System.out.println("|\nv\n" + e.dest);
                 else
                     System.out.println("|\nv");
                visitedVertices.add(e.src);
                visitedVertices.add(e.dest);
                applyFleury(e.dest, edgesLeft, visitedVertices);
            }
            else {
                if (printCheck(visitedVertices, edgesLeft, e.src))
                    System.out.println("|\nv\n" + e.src);
                else
                    System.out.println("|\nv");
                visitedVertices.add(e.src);
                visitedVertices.add(e.dest);
                applyFleury(e.src, edgesLeft, visitedVertices);
            }
        }
        else if (edgesFrom.size() > 1) {
            for (Edge e : edgesFrom) {

                // Sprawdzamy za pomocą DFS, czy usunięcie danej krawędzi rozspójnia graf

                int count1 = dfs(e.src);
                edgeArray.remove(e);
                int count2 = dfs(e.src);
                edgeArray.add(e);
                if (count1 <= count2) {
                    edgesLeft.remove(e);
                    System.out.println("CHECK > 1: " + e.src + " : " + e.dest + "  " + visitedVertices);
                    if (e.dest != v) {
                        if (edgesLeft.size() == edgeArray.size() - 1) {
                            System.out.println("POCZĄTEK");
                            System.out.println(e.src + "\n|\nv\n" + e.dest);
                        }
                        else if (printCheck(visitedVertices, edgesLeft, e.dest))
                            System.out.println("|\nv\n" + e.dest);
                        else
                            System.out.println("|\nv");
                        visitedVertices.add(e.src);
                        visitedVertices.add(e.dest);
                        applyFleury(e.dest, edgesLeft, visitedVertices);
                    }
                    else {
                        if (printCheck(visitedVertices, edgesLeft, e.src))
                            System.out.println("|\nv\n" + e.src);
                        else
                            System.out.println("|\nv");
                        visitedVertices.add(e.src);
                        visitedVertices.add(e.dest);
                        applyFleury(e.src, edgesLeft, visitedVertices);
                    }
                    break;
                }
                else {
//                     System.out.println("Rozspójnia");
                }
            }
        }
        else {
            return false;
        }
        return false;
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

    public void dijkstra(int src)
    {
        // inicjalizacja zmiennych
        dist = new int[vertices];
        pq = new PriorityQueue<>(vertices);
        settled = new HashSet<>();
        shortest = new int[vertices];
        for (int i = 0; i < vertices; i++)
            dist[i] = Integer.MAX_VALUE;

        pq.add(new graph.Node(src, 0));

        dist[src] = 0;

        while (settled.size() != vertices) {

            // Zakończenie pętli, jeżeli kolejka jest pusta
            if (pq.isEmpty())
                return;

            // Usuwany jest node o najmniejszym koszcie
            int u = pq.remove().node;

            // Pominięcie wierzchołka, jeżeli znajduje się już w zbiorze wierzchołków odwiedzonych
            if (settled.contains(u))
                continue;

            settled.add(u);

            e_Neighbours(u);
        }
    }

    private void e_Neighbours(int u)
    {

        int edgeDistance = -1;
        int newDistance = -1;

        // Wykonaj dla wszystkich sąsiadów wierzchołka u
        for (int i = 0; i < edgesFromV(u, edgeArray).size(); i++) {
            Edge e = edgesFromV(u, edgeArray).get(i);

            // Zamiana wierzchołḱa, aby był on tym końcowym

            int v = e.src;
            if (v == u) {
                v = e.dest;
            }

            Node node = new Node(v, e.weight);

            // Wykonaj, jeśli obecny node nie został obsłużony
            if (!settled.contains(node.node)) {
                edgeDistance = node.cost;
                newDistance = dist[u] + edgeDistance;

                // Wykonaj, jeżeli nowy dystans jest mniejszy od obecnego
                if (newDistance < dist[node.node]){
                    dist[node.node] = newDistance;
                    shortest[node.node] = u;
                }

                // Dodaj obecny node do kolejki
                pq.add(new Node(node.node, dist[node.node]));
            }
        }
    }

    public Edge getEdge(int src, int dest) {
        for (Edge e : edgeArray) {
            if ((e.src == src && e.dest == dest) || (e.src == dest && e.dest == src)) return new Edge(e.src, e.dest, e.weight);
        }
        return new Edge(0, 0, 1);
    }

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

    public ArrayList<Edge> applyKruskal() {

        // Inicjalizacja finalResult - tablicy przechowującej końcowe MST
        ArrayList<Edge> finalResult = new ArrayList<>();
        int newEdge = 0;
        int index = 0;
        for (index = 0; index < vertices; ++index)
            finalResult.add(index, new Edge());

        // sortowanie krawędzi
//        Arrays.sort(edgeArray);
        Collections.sort(edgeArray);
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
            Edge nextEdge = edgeArray.get(index++);

            int nextsrc = findSetOfElement(subsetArray, nextEdge.src);
            int nextdest = findSetOfElement(subsetArray, nextEdge.dest);

            // Jeśli krawędź nie tworzy cyklu, to dodajemy ją do rozwiązanie
            if (nextsrc != nextdest) {
                finalResult.set(newEdge++, nextEdge);
                performUnion(subsetArray, nextsrc, nextdest);
            }
        }

        finalResult.remove(newEdge);

        // Wypisanie wyniku
        for (index = 0; index < newEdge; ++index)
            System.out.println(finalResult.get(index).src + " - " + finalResult.get(index).dest + ": " + finalResult.get(index).weight);
        return finalResult;
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
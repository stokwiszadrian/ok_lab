package graph;

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

    public void CPP() {
        int odd = 0;
        ArrayList<Integer> oddV = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            int deg = edgesFromV(i, edgeArray).size();
            if ( deg % 2 != 0 ) {
                odd += 1;
                oddV.add(i);
            }
        }
        if ( odd == 0 ) {
            applyFleury(0, edgeArray);
        }
        else if ( odd == 2) {
            semiEulerian(oddV.get(0), oddV.get(1));
        }
        else {
            nonEulerian();
        }

    }

    public boolean applyFleury(int v, ArrayList<Edge> edgesLeft) {
        ArrayList<Edge> edgesFrom = edgesFromV(v, edgesLeft);
        if (edgesLeft.size() == 0) return true;
        if (edgesFrom.size() == 1) {

            // Jeśli jest 1 krawędź, usuwamy ją i przechodzimy dalej

            Edge e = edgesFrom.get(0);
            edgesLeft.remove(e);
            if (e.dest != v) {
                 System.out.println(e.src + " - " + e.dest + ": " + e.weight);
                applyFleury(e.dest, edgesLeft);
            }
            else {
                 System.out.println(e.dest + " - " + e.src + ": " + e.weight);
                applyFleury(e.src, edgesLeft);
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
                    if (e.dest != v) {
                         System.out.println(e.src + " - " + e.dest + ": " + e.weight);
                        applyFleury(e.dest, edgesLeft);
                    }
                    else {
                         System.out.println(e.dest + " - " + e.src + ": " + e.weight);
                        applyFleury(e.src, edgesLeft);
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

    public void semiEulerian(int v1, int v2) {

        // uzywamy algorytmu Dijkstry by znaleźć ścieżkę między nieparzystymi wierzchołkami
        ArrayList<Edge> edgesLeft = new ArrayList<>(edgeArray);
        this.dijkstra(v1);

        // wypisujemy ścieżkę Eulerowską, zaczynając od nieparzystego wierzchołka
        applyFleury(v1, edgesLeft);

        // wypisujemy ścieżkę "powrotną"
        ArrayList<Edge> path = new ArrayList<>();
        int j = v2;
        while (j != v1) {
            int next = shortest[j];
            Edge e = getEdge(j, next);
            path.add(e);
            j = next;
        }
        for (Edge e : path) {
             System.out.println(e.src + " - " + e.dest + ": " + e.weight);
        }
    }

    public void nonEulerian() {
        Set<Integer> oddVertices = new LinkedHashSet<>();
        for (int v = 0; v < vertices; v++) {
            if (edgesFromV(v, edgeArray).size() % 2 != 0) {
                oddVertices.add(v);
            }
        }

        // generowanie par wierchołków o nieparzystym stopniu

        ArrayList<List<List<Integer>>> pairings = new ArrayList<List<List<Integer>>>();
        generatePairs(oddVertices, new ArrayList<List<Integer>>(), pairings);

        // znalezienie zbioru par wierchołków o najmniejszej wadze

        int minSum = Integer.MAX_VALUE;
        ArrayList<List<Edge>> minPaths = new ArrayList<>();

        for (List<List<Integer>> result : pairings) {

            int curSum = 0;
            ArrayList<List<Edge>> paths = new ArrayList<>();
            for (List<Integer> pair : result) {
                this.dijkstra(pair.get(0));
                curSum += dist[pair.get(1)];
                ArrayList<Edge> path = new ArrayList<>();
                int j = pair.get(1);

                while (j != pair.get(0)) {
                    int next = shortest[j];
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

        // dodawanie nowych krawędzi do pozostałych krawędzi grafu
        // w ten sposób otrzymujemy graf eulerowski

        ArrayList<Edge> newEdges = new ArrayList<>();
        for (List<Edge> p : minPaths) {
            newEdges.addAll(p);
        }
        edgeArray.addAll(newEdges);
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
}
package graph;

public class Edge implements Comparable<Edge> {
    public int src;
    public int dest;
    public int weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public Edge() {}


    public int compareTo(Edge edge) {
        return this.weight - edge.weight;
    }
}

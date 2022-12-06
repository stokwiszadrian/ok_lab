package graph;

class Node implements Comparable<Node> {
    public int node;
    public int cost;
    public Node() { } //empty constructor

    public Node(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }
    @Override
    public int compareTo(Node node2)
    {
        if (this.cost < node2.cost)
            return -1;
        if (this.cost > node2.cost)
            return 1;
        return 0;
    }

}
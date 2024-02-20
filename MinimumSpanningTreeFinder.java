package Question3;

import java.util.ArrayList;
import java.util.PriorityQueue;

class Edge implements Comparable<Edge> {
    int source, destination, weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

class Graph {
    ArrayList<ArrayList<Edge>> adjacencyList;

    public Graph(int vertices) {
        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjacencyList.get(source).add(new Edge(source, destination, weight));
        adjacencyList.get(destination).add(new Edge(destination, source, weight)); // Undirected graph
    }

    public ArrayList<Edge> findMinimumSpanningTree() {
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        int[] parent = new int[adjacencyList.size()];
        int[] rank = new int[adjacencyList.size()];

        // Initialize each vertex to be its own parent initially
        for (int i = 0; i < adjacencyList.size(); i++) {
            parent[i] = i;
        }

        // Add all edges to the priority queue (min heap)
        for (int i = 0; i < adjacencyList.size(); i++) {
            for (Edge edge : adjacencyList.get(i)) {
                priorityQueue.add(edge);
            }
        }

        ArrayList<Edge> minimumSpanningTree = new ArrayList<>();

        // Process edges until V-1 edges (all vertices connected)
        while (!priorityQueue.isEmpty() && minimumSpanningTree.size() < adjacencyList.size() - 1) {
            Edge edge = priorityQueue.poll();
            int sourceParent = find(parent, edge.source);
            int destinationParent = find(parent, edge.destination);

            // Check for cycle formation
            if (sourceParent != destinationParent) {
                minimumSpanningTree.add(edge);
                union(parent, rank, sourceParent, destinationParent);
            }
        }

        return minimumSpanningTree;
    }

    int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        }
        return find(parent, parent[i]);
    }

    void union(int[] parent, int[] rank, int sourceParent, int destinationParent) {
        int sourceRoot = find(parent, sourceParent);
        int destinationRoot = find(parent, destinationParent);

        // Attach smaller tree under root of larger tree
        if (rank[sourceRoot] < rank[destinationRoot]) {
            parent[sourceRoot] = destinationRoot;
        } else if (rank[sourceRoot] > rank[destinationRoot]) {
            parent[destinationRoot] = sourceRoot;
        } else {
            parent[destinationRoot] = sourceRoot;
            rank[sourceRoot]++;
        }
    }
}

public class MinimumSpanningTreeFinder {
    public static void main(String[] args) {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 2, 4);
        graph.addEdge(2, 3, 8);

        ArrayList<Edge> minimumSpanningTree = graph.findMinimumSpanningTree();

        System.out.println("Minimum Spanning Tree Edges:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println(edge.source + " - " + edge.destination + " (" + edge.weight + ")");
        }
    }
}

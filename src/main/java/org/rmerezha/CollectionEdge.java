package org.rmerezha;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollectionEdge {
    private final int V;
    private final List<List<Edge>> adjList;

    CollectionEdge(int V) {
        this.V = V;
        adjList = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    void addEdge(int src, int dest, double weight) {
        adjList.get(src).add(new Edge(src, dest, weight));
        adjList.get(dest).add(new Edge(dest, src, weight)); // Для неорієнтованого графа
    }

    void removeEdge(int src, int dest) {
        adjList.get(src).removeIf(edge -> edge.getDest() == dest);
        adjList.get(dest).removeIf(edge -> edge.getSrc() == src);
    }

    List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> list : adjList) {
            for (Edge edge : list) {
                if (edge.getSrc() < edge.getDest()) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    int getV() {
        return V;
    }
}

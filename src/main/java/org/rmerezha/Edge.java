package org.rmerezha;


public class Edge implements Comparable<Edge> {

    private int src;
    private int dest;
    private double weight;

    public Edge(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
               "src=" + (src + 1)+
               ", dest=" + (dest + 1) +
               ", weight=" + weight +
               '}';
    }
}

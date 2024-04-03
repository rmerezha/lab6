package org.rmerezha;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public final class Graph {


    private final int variant;
    private final int n3;
    private final int n4;
    private double[][] matrix;

    public Graph(int variant) {
        String s = ((Integer) variant).toString();

        if (s.length() != 4) {
            throw new IllegalArgumentException();
        }

        this.variant = variant;

        String[] arr = s.split("");
        n3 = Integer.parseInt(arr[2]);
        n4 = Integer.parseInt(arr[3]);
    }


    public double generateK() {
        return 1.0 - n3 * 0.02 - n4 * 0.005 - 0.25;
    }


    public void generateMatrix(int size) {
        Random random = new Random(variant);

        matrix = new double[size][size];

        forEach(e -> random.nextDouble(2.0));

    }

    public static void main(String[] args) {
        Graph graph = new Graph(3414);
        graph.generatedAdjacencyMatrix(11);
    }


    public double[][] generatedAdjacencyMatrix(int size) {
        generateMatrix(size);
        double k = generateK();

        multiplication(k);

        forEach(e -> e >= 1 ? 1.0 : 0.0);

        return matrix;
    }

    public void forEach(Function<Double, Double> fun) {

        int size = matrix.length;

        for (int i = 0; i < size * size; i++) {
            int q = i % size;
            int w = i / size;

            matrix[q][w] = fun.apply(matrix[q][w]);
        }
    }


    public void multiplication(double k) {

        forEach(e -> e * k);

    }

    public void printMatrix() {
        Arrays.stream(matrix).forEach(e -> System.out.println(Arrays.toString(e)));
    }


}

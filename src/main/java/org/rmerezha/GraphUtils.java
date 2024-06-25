package org.rmerezha;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class GraphUtils {

    private static final int VARIANT = 3414;
    private static final int SIZE = 11;

    private GraphUtils() { }

    public static double[][] createWeightedGraph(double[][] adjacencyMatrix) {
        double[][] weightMatrix = generatedW(adjacencyMatrix);
        int n = adjacencyMatrix.length;

        double[][] weightedGraph = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    weightedGraph[i][j] = weightMatrix[i][j];
                } else {
                    weightedGraph[i][j] = 0;
                }
            }
        }

        return weightedGraph;
    }

    private static double[][] generatedW(double[][] a) {
        double[][] b = generatedB();
        double[][] c = generatedC(b, a);
        double[][] d = generatedD(c);
        double[][] h = generatedH(d);
        double[][] tr = generatedTr();
        double[][] w = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j <= i; j++) {
                w[i][j] = (d[i][j] + h[i][j]*tr[i][j])*c[i][j];
                w[j][i] = w[i][j];
            }
        }
        return w;
    }

    private static double[][] generatedB() {
        Random random = new Random(VARIANT);
        double[][] matrix = new double[SIZE][SIZE];
        forEach(matrix, e -> random.nextDouble(2.0));
        return matrix;
    }

    private static double[][] generatedC(double[][] bMatrix, double[][] aMatrix) {
        var cMatrix = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            int q = i % SIZE;
            int w = i / SIZE;
            cMatrix[q][w] = Math.ceil(bMatrix[q][w] * 100 * aMatrix[q][w]);
        }
        return cMatrix;
    }

    private static double[][] generatedD(double[][] cMatrix) {
        var dMatrix = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            int q = i % SIZE;
            int w = i / SIZE;
            if (cMatrix[q][w] == 0) {
                dMatrix[q][w] = 0;
            } else {
                dMatrix[q][w] = 1;
            }
        }
        return dMatrix;
    }

    private static double[][] generatedH(double[][] dMatrix) {
        var hMatrix = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            int q = i % SIZE;
            int w = i / SIZE;
            if (dMatrix[q][w] != dMatrix[w][q]) {
                hMatrix[q][w] = 1;
            } else {
                hMatrix[q][w] = 0;
            }
        }
        return hMatrix;
    }

    private static double[][] generatedTr() {
        double[][] matrix = new double[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = i; j < SIZE; j++) {
                matrix[i][j] = 1;
            }
        }

        return matrix;
    }








    private static void forEach(double[][] matrix, Function<Double, Double> fun) {
        for (int i = 0; i < SIZE * SIZE; i++) {
            int q = i % SIZE;
            int w = i / SIZE;
            matrix[q][w] = fun.apply(matrix[q][w]);
        }
    }












}

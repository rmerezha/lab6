package org.rmerezha;

import java.util.ArrayList;
import java.util.List;

public class EdgeMapper {

    public static List<Edge> mapToEdges(double[][] weightedMatrix) {
        List<Edge> edges = new ArrayList<>();
        int n = weightedMatrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (weightedMatrix[i][j] > 0) { // Для ненулевых весов
                    edges.add(new Edge(i, j, weightedMatrix[i][j]));
                }
            }
        }

        return edges;
    }
}
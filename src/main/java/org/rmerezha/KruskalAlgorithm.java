package org.rmerezha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KruskalAlgorithm {

    public static List<Edge> kruskalMST(CollectionEdge collectionEdge) {
        List<Edge> mst = new LinkedList<>();
        List<Edge> edges = collectionEdge.getAllEdges();
        Collections.sort(edges);

        DisjointSet ds = new DisjointSet(collectionEdge.getV());

        for (Edge edge : edges) {
            int rootSrc = ds.find(edge.getSrc());
            int rootDest = ds.find(edge.getDest());

            if (rootSrc != rootDest) {
                mst.add(edge);
                ds.union(rootSrc, rootDest);
            }
        }

        return mst;
    }
}

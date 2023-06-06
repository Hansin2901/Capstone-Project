package Graphs;

import java.util.*;

public class GraphGen {
    public static void main(String args[]) {
        // List<Edge> edges = Arrays.asList(new Edge(0, 1), new Edge(1, 2), new Edge(2,
        // 4), new Edge(4, 1),new Edge(3, 2), new Edge(2, 3), new Edge(3, 5), new
        // Edge(5, 4), new Edge(1, 1), new Edge(4,5), new Edge(0,3));
        // List<Edge> edges = Arrays.asList( new Edge(2, 8), new Edge(3, 8),
        //         new Edge(3, 10), new Edge(4, 6), new Edge(5, 9), new Edge(6, 2), new Edge(1, 7), new Edge(9, 3));
        // boolean[] colour={false,true,true,true,true,true,false,false,false,false,false,false};
        List<Edge> edges = Arrays.asList(new Edge(1, 4), new Edge(2, 4), new Edge(2, 5), new Edge(3, 5),
        new Edge(3, 6));
        boolean[] colour={false,true,true,true,false,false,false,false};
        Graph graph = new Graph(edges,colour);
        graph.getBestMatching();
        // System.out.println(graph.adjList);
        // System.out.println(graph.augmentingPath);
    }
}

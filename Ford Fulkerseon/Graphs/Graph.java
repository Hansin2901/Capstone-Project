package Graphs;

import java.util.*;

public class Graph {
    List<List<Integer>> adjList = new ArrayList<>();
    List<List<Integer>> augmentingPath = new ArrayList<>();
    int noOfNodes;
    boolean[] setB;
    boolean[] matched;

    public Graph(List<Edge> edges, boolean[] setB) {
        int n = 0;
        // finding the max number of nodes in graph
        for (Edge e : edges) {
            n = Integer.max(n, Integer.max(e.s, e.d));// compares current max with the new edges start and end node
        }
        noOfNodes = n+1;//this is for the extra 
        for (int i = 0; i <= n; i++) {
            adjList.add(i, new ArrayList<>());
        }
        for (Edge current : edges) {
            adjList.get(current.s).add(current.d);// gets list of starting node and adds destination node to it
        }
        this.setB = setB;
        this.matched = new boolean[noOfNodes];
        Arrays.fill(matched, false);
        assignFreeNodes();

    }

    public void showGraph() {
        int s = 0;
        int n = adjList.size();// determines the size of the adjacency list
        while (s < n) {
            for (int d : adjList.get(s)) {
                System.out.print("Adjacency List for the graph is:");
                System.out.print("(" + s + " -- > " + d + ")\t");
            }
            System.out.println();
            s++;
        }
    }

    public void findAugmentationPath(int start, int destination, int number) {
        boolean visited[] = new boolean[number];
        boolean alreadyInPath[] = new boolean[number ];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited[start] = true;
        visited[destination] = true;
        queue.add(start);
        int[] travelPath = new int[number + 1];
        while (queue.size() != 0) {
            int currentNode = queue.poll();
            Iterator<Integer> i = adjList.get(currentNode).listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (n == destination) {
                    List<Integer> path = new ArrayList<>();
                    while (currentNode != start) {
                        path.add(currentNode);
                        System.out.println(path);
                        alreadyInPath[currentNode] = true;
                        for (int a : adjList.get(currentNode)) {
                            if (!alreadyInPath[a] && a!=destination) {
                                visited[a] = false;
                            }
                        }
                        currentNode = travelPath[currentNode];

                    }
                    augmentingPath.add(path);
                    System.out.println(augmentingPath);
                }
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                    travelPath[n] = currentNode;
                    continue;
                }

            }

        }

    }

    public void assignFreeNodes() {
        adjList.get(0).clear();
        // adjList.get(noOfNodes ).clear();
        for (int i = 1; i < noOfNodes; i++) {
            if (!matched[i]) {
                if (setB[i]) {
                    adjList.get(0).add(i);//adding the path 0 to free node
                } else {
                    // adjList.get(noOfNodes).add(i);
                    adjList.get(i).add(noOfNodes);//adding path freenode to destination node

                }
            }
        }
    }

    public void getBestMatching() {
        findAugmentationPath(0, noOfNodes , noOfNodes + 1);
        while (augmentingPath.size() != 0  ) {
            System.out.println("-------"+augmentingPath+"---------");
            for (int i = 0; i < augmentingPath.size(); i++) {
                List<Integer>temp=augmentingPath.get(i);
                for (int j = 0; j < (temp.size() - 1); j++) {
                    int node2 = augmentingPath.get(i).get(j);
                    int node1 = augmentingPath.get(i).get(j + 1);
                    matched[node2] = true;
                    matched[node1] = true;
                    adjList.get(node1).remove(Integer.valueOf(node2));
                    adjList.get(node2).remove(Integer.valueOf(11));
                    adjList.get(node2).add(node1);
                    
                }
            }
            assignFreeNodes();
            augmentingPath = new ArrayList<>();
            findAugmentationPath(0, noOfNodes , noOfNodes + 1);
        }
    }

}

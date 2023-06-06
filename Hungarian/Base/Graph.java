package Base;

import java.util.*;
import java.lang.Math;
public class Graph {
    // List<List<Integer>> adjMatrix = new ArrayList<>();
    int matched=0;
    List<Integer> augmentingPath;
    int[][] adjMatrix;
    List<Integer> B;
    List<Integer> A;
    int[] matchedA;
    boolean[] isMatchedB;
    int[][] matching;
    int[][] cost;
    List<Vertex> vertices;
    int totalSize;
    int[][] slack;
    int V;
    public Graph(int[][] cost, int[][] matching) {
        this.cost=cost;
        this.matching = matching;
        B=new ArrayList<>();
        A=new ArrayList<>();
        vertices=new ArrayList<Vertex>();
        // vertices.add(new Vertex());
        for(int i=0;i<cost.length+cost[0].length+2;i++){
            vertices.add(new Vertex());
        }
        //generating vertices 
        int j=1;
        for(int i=0;i<cost.length;i++){
            B.add(j);
            j++;
        }
        for(int i=0;i<cost[0].length;i++){
            A.add(j);
            j++;
        }
        //setting matching status to define graph
        
        isMatchedB=new boolean[B.size()];
        matchedA=new int[A.size()];
        Arrays.fill(matchedA, -1);
        Arrays.fill(isMatchedB, false);
        for(int i=0;i<matching.length;i++){
            isMatchedB[matching[i][0]]=true;
            matchedA[matching[i][1]]=matching[i][0];
        }
        totalSize=A.size()+B.size()+2;
        adjMatrix=new int[totalSize][totalSize];
        for (int[] row : adjMatrix){
            Arrays.fill(row, -1);
        }
        // for (int i = 0; i <= totalSize; i++) {
        //     adjMatrix.add(new ArrayList<>(Collections.nCopies(totalSize, -1)));
        // }
        slack=new int[B.size()][A.size()];
        for (int[] row : slack){
            Arrays.fill(row, 0);
        }
        V=totalSize;
        augmentingPath=new ArrayList<>();
        
       
    }
        
    
    public void constructGraph() {
        for (int[] row : adjMatrix){
            Arrays.fill(row, -1);
        }
        for(int i=0;i<B.size();i++){
            if(isMatchedB[i]){
                adjMatrix[0][B.get(i)]=-1;
            }
            else{
                adjMatrix[0][B.get(i)]=0;
            }
        }
        for(int i=0;i<A.size();i++){
            if(matchedA[i]<0){
                adjMatrix[A.get(i)][totalSize-1]=0;
            }
            else{
                adjMatrix[A.get(i)][totalSize-1]=-1;
            }
        }
        for(int i=0;i<B.size();i++){
            for(int j=0;j<A.size();j++){
                if(matchedA[j]==i){
                    adjMatrix[A.get(j)][B.get(i)]=slack[i][j];
                }
                else{
                    adjMatrix[B.get(i)][A.get(j)]=slack[i][j];
                }
            }
        }

        
    }
    public void genSlack() {
        for(int i=0;i<B.size();i++){
            for(int j=0;j<A.size();j++){
                slack[i][j]=cost[i][j]-vertices.get(B.get(i)).y+vertices.get(A.get(j)).y;
            }
        }
        
    }
    void dijkstra(int graph[][], int src)
    {
        int dist[] = new int[V]; 
        Boolean sptSet[] = new Boolean[V];
        int fromNode[]=new int[V];
        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }
        dist[src] = 0;
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, sptSet);
            sptSet[u] = true;
            for (int v = 0; v < V; v++)
                if (!sptSet[v] && graph[u][v] >= 0
                    && dist[u] != Integer.MAX_VALUE
                    && dist[u] + graph[u][v] < dist[v]){
                    dist[v] = dist[u] + graph[u][v];
                    fromNode[v]=u;
                    }

        }
  
        // print the constructed distance array
        for(int i =0;i<totalSize;i++){
            vertices.get(i).shortestDist=dist[i];
        }
        // printSolution(dist);
        int i=totalSize-1;
        while(fromNode[i]!=0){
            augmentingPath.add(fromNode[i]);
            i=fromNode[i];
        }
    }
    int minDistance(int dist[], Boolean sptSet[])
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;
  
        for (int v = 0; v < V; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
  
        return min_index;
    }
    void printSolution(int dist[])
    {
        System.out.println(
            "Vertex \t\t Distance from Source");
        for (int i = 0; i < V; i++)
            System.out.println(i + " \t\t " + dist[i]);
    }
    public void showMat(int[][] a) {
        for(int[] i:a){
            for(int j:i){
                System.out.print(j+" ");
            }
            System.out.println();
        }
        
    }
    public void updateY(){
        for(int i=1;i<totalSize-1;i++){
            if(vertices.get(i).shortestDist<vertices.get(totalSize-1).shortestDist){
                vertices.get(i).y+=vertices.get(totalSize-1).shortestDist -vertices.get(i).shortestDist;
            }
        }
    }
    public void updateMatching() {
        for(int i=0;i<augmentingPath.size();i+=2){
            matchedA[augmentingPath.get(i)-B.size()-1]=augmentingPath.get(i+1)-1;
            isMatchedB[augmentingPath.get(i+1)-1]=true;
        }
        matched=0;
        for(int i=0;i<matchedA.length;i++){
            System.out.print(matchedA[i]+" ");
            if(matchedA[i]>=0){
                matched+=1;
            }

        }
        System.out.println();
    }
    public void hungarian() {
        while(matched<A.size()){
            genSlack();
            constructGraph();
            dijkstra(adjMatrix,0);
            System.out.println(augmentingPath);
            updateMatching();
            updateY();
        }
        debug();
    }
    public void debug() {
        int C=0;
        int Y=0;
        for(int i=0;i<matchedA.length;i++){
            C+=cost[matchedA[i]][i];
            Y=Y+vertices.get(B.get(matchedA[i])).y-vertices.get(A.get(i)).y;
        }
        System.out.println("cost: "+C+" Y: "+Y);
    }
    public static void main(String args[]) {
        // int[][] cost={{1,3,10,9},{4,8,4,2},{7,6,3,6},{7,4,5,8}};
        int[][] cost=new int[1000][1000];
        int max=1000;
        int min=1;

        for (int i = 0; i < cost.length; i++) {
            for (int j = 0; j < cost[i].length; j++) {
               cost[i][j] = (int)(Math.random()*(max-min+1)+min);
                
            }
         }
        int[][] matching= new int[0][0];
        Graph a=new Graph(cost,matching);
        // System.out.println(a.adjMatrix);
        // a.showMat(a.adjMatrix);
        // a.genSlack();
        // a.constructGraph();
        // a.dijkstra(a.adjMatrix,0);
        // System.out.println(a.augmentingPath);
        // a.updateMatching();
        // a.updateY();
        a.hungarian();

    }
}

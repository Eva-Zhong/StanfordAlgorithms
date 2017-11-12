/**
 * Created by zhongyifan on 4/11/2017.
 * In this programming problem and the next you'll code up the clustering algorithm from lecture for computing a max-spacing
 * k-clustering.

 This file describes a distance function (equivalently, a complete graph with edge costs). It has the following format:

 [number_of_nodes]

 [edge 1 node 1] [edge 1 node 2] [edge 1 cost]

 [edge 2 node 1] [edge 2 node 2] [edge 2 cost]

 ...

 There is one edge (i,j) for each choice of 1≤i<j≤n, where n is the number of nodes.

 For example, the third line of the file is "1 3 5250", indicating that the distance between nodes 1 and 3 (equivalently,
 the cost of the edge (1,3)) is 5250. You can assume that distances are positive, but you should NOT assume that they are
 distinct.

 Your task in this problem is to run the clustering algorithm from lecture on this data set, where the target number k of
 clusters is set to 4. What is the maximum spacing of a 4-clustering?
 */

import java.util.*;
import java.io.*;

public class MaxClustering {
    public Graph g;
    private Edge<Integer>[] edges;
    private Vertex<Integer>[] vertices;
    public int size;

    public static void main(String[] args) {
        Graph g = new Graph("largetest3.txt");
        System.out.println(findCluster(g));

    }

     public static int findCluster(Graph g) {

        Comparator<Edge> edgeComparator = new EdgeComparator();
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(edgeComparator);

        for (Edge e: g.edges) {
            pq.add(e);
        }

        int k = g.adj.size();
        System.out.println(k);

        int[] parents = new int[k+1];
        for (int i = 1; i <= k; i++) {
            parents[i] = -1;
        }

        while (pq.size() != 0) {
            Edge e = pq.poll();
            int l1 = (int) e.label1;
            int l2 = (int) e.label2;
            System.out.println(l1 + ", " + l2);

            if (g.isCycle(parents, l1, l2) == false) {
                g.union(parents, l1, l2);
                k--;
            }

            if (k == 4) {
                while (pq.size() != 0) {
                    Edge curE = pq.poll();
                    int la = (int) curE.label1;
                    int lb = (int) curE.label2;
                    if (g.isCycle(parents, la, lb) == false) {
                        int spacing = curE.getWeight();
                        return spacing;
                    }
                }
            }

        }


        return 0;
    }

    static class Graph {
        // The idx of each item in the list is the label of the vertex;
        // adj[i] stores all the edges of the vertex with label i+1;

        public List<ArrayList<Edge>> adj = new ArrayList<ArrayList<Edge>>();
        public List<Edge<Integer>> edges = new ArrayList<Edge<Integer>>();
        public ArrayList<Vertex<Integer>> vertices = new ArrayList<Vertex<Integer>>();

        public Graph(String inputfilename) {
            File f = new File(inputfilename);

            try {
                Scanner sc = new Scanner(f);
                int size = sc.nextInt();
                System.out.println("Size: " + size);
//                this.adj = new ArrayList<ArrayList<Edge>>(size);

                for (int i = 0; i < size; i++) {
                    adj.add(i, new ArrayList<Edge>());
                }

                while(sc.hasNextLine()) {
                    int l1 = sc.nextInt();
                    int l2 = sc.nextInt();
                    int weight = sc.nextInt();
                    Edge newE = new Edge(l1, l2, weight);
                    this.adj.get(l1-1).add(newE);
                    this.adj.get(l2-1).add(newE);
                    this.edges.add(newE);
                }
            }

            catch (FileNotFoundException e) {
                System.out.println("File not found.");
            }

        }

//        public void makeSet(T x) {
//
//        }

        // The integers here represents the indices of each item in the Graph;
        // The item does not necessarily need to be integers
        public void union(int[] parents, int x, int y) {
            int l1 = findSet(parents, x);

            int l2 = findSet(parents, y);
            // The parent of the leader of U1 is the leader of U2;
            parents[l1] = l2;

        }

        public int findSet(int[] parents, int x) {

            int cur = parents[x];
            System.out.println(cur);
            if (cur == -1) {return x;}
            else {
                return findSet(parents, parents[x]);
            }
        }

        public boolean isCycle(int[] parents, int x, int y) {

            int l1 = findSet(parents, x);
            int l2 = findSet(parents, y);

            if (l1 == l2) {
                return true;
            } else {
                return false;
            }
        }

    }

    static class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            if (e1.weight > e2.weight) {
                return 1;
            } else if (e1.weight == e2.weight) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    static class Edge<T> {
        //Instance variables
        private T label1;
        private T label2;
        private int weight;

        //Constructor takes all the necessary formation to store an edge weight
        public Edge(T first, T second, int number) {
            this.label1 = first;
            this.label2 = second;
            this.weight = number;
        }

        //Returns the label of the first vertex on the edge
        public T getLabel1() {
            return this.label1;
        }

        //Returns the label of the second vertex on the edge
        public T getLabel2() {
            return this.label2;
        }

        //Returns the weight between the two given vertices
        public int getWeight() {
            return this.weight;
        }
    }

    static class Vertex<T> {
        private T label;
        private List<Vertex<T>> neighborList;

        /*******
         * distance and path are instance variables that will come
         * in handy when running graph algorithms. You can interact
         * with them using the getters and setters, but they aren't
         * automatically set to anything specific
         ********/
        private double distance;
        private List<Vertex<T>> path;

        /****************
         * Constructor for Vertex class, pass it the label
         ************/
        public Vertex(T lb) {
            this.label = lb;
            this.path = new ArrayList<Vertex<T>>();
            this.neighborList = new ArrayList<Vertex<T>>();
        }

        /***********
         * Returns the label used for this vertex
         ***********/
        public T getLabel() {
            return this.label;
        }

        /******************
         * Gets a list of this Vertex's neighbors
         *******************/
        public List<Vertex<T>> getNeighbors() {
            return this.neighborList;
        }

        /********************
         * Add a neighbor to the vertex
         *******************/
        public void addNeighbor(Vertex<T> n) {
            if(!this.neighborList.contains(n)) {
                this.neighborList.add(n);
            }
        }

        /**************
         * Returns this Vertex as a string
         ******************/
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.label.toString());
            if(distance != Double.POSITIVE_INFINITY) {
                sb.append(String.format("(%2f)", distance));
            }
            return sb.toString();
        }

        /****************
         * Get this vertex's distance
         *****************/
        public double getDistance() {
            return this.distance;
        }

        /****************
         * Set this vertex's distance
         *****************/
        public void setDistance(double d) {
            this.distance = d;
        }

        /*********************
         * Get this vertex's path list
         *******************/
        public List<Vertex<T>> getPath() {
            return this.path;
        }

        /************************
         * Set this vertex's path list
         ***********************/
        public void setPath(List<Vertex<T>> p) {
            this.path = p;
        }

        public boolean equals(Object ob) {
            if(!(ob instanceof Vertex)) { return false; }

            return this.label.equals(((Vertex<T>)ob).getLabel());
        }
    }

}

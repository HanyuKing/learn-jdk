package leetcode;

import scala.concurrent.java8.FuturesConvertersImpl;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/2 10:02
 * @Description
 * @Version 1.0
 **/
public class P2325 {
    public static void main(String[] args) {
        // System.out.println(new Solution2325().decodeMessage("the quick brown fox jumps over the lazy dog", "vkbs bs t suepuv"));

        // case5:
//        int[] ans = new Solution2325().shortestAlternatingPaths(3, new int[][]{
//                {0, 1},
//                {0, 2}
//        }, new int[][]{
//                {1, 0}
//        });
//        // case4:
//        int[] ans = new Solution2325().shortestAlternatingPaths(3, new int[][]{
//                {0, 1},
//        }, new int[][]{
//                {1, 2}
//        });

//        // case3:
//        int[] ans = new Solution2325().shortestAlternatingPaths(3, new int[][]{
//                {1, 0},
//        }, new int[][]{
//                {2, 1}
//        });
        // case2:
//        int[] ans = new Solution2325().shortestAlternatingPaths(3, new int[][]{
//                {0, 1},
//        }, new int[][]{
//                {2, 1}
//        });
//        // case1:
//        int[] ans = new Solution2325().shortestAlternatingPaths(5, new int[][]{
//                {0, 1},
//                {1, 2},
//                {2, 3},
//                {3, 4},
//        }, new int[][]{
//                {1, 2},
//                {2, 3},
//                {3, 1},
//        });

        int[] ans = new Solution2325().shortestAlternatingPaths(6, new int[][]{
                // {2,2},{0,1},{0,3},{0,0},{0,4},{2,1},{2,0},{1,4},{3,4}
                {4,1},{3,5},{5,2},{1,4},{4,2},{0,0},{2,0},{1,1}
        }, new int[][]{
                // {1,3},{0,0},{0,3},{4,2},{1,0}
                {5,5},{5,0},{4,4},{0,3},{1,0}
        });

        // [0,-1,4,1,-1,2]

        for (int i = 0; i < ans.length; i++) {
            System.out.print(ans[i] + " ");
        }
        System.out.println();
    }

}
class Solution2325 {
    public String decodeMessage(String key, String message) {
        char[] chars = new char[26];
        int index = 0;
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == ' ') {
                continue;
            }
            if (chars[key.charAt(i) - 'a'] == 0) {
                chars[key.charAt(i) - 'a'] = (char)(97 + index++);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == ' ') {
                sb.append(' ');
            } else {
                sb.append(chars[message.charAt(i) - 'a']);
            }
        }
        return sb.toString();
    }

    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        Graph graph = new Graph();
        for (int i = 0; i < redEdges.length; i++) {
            int[] edge = redEdges[i];
            Node nodeFrom = null;
            Node nodeTo = null;
            if ((nodeFrom = graph.nodeMap.get(edge[0])) == null) {
                nodeFrom = new Node(edge[0]);
                graph.nodeMap.put(edge[0], nodeFrom);
            }
            if ((nodeTo = graph.nodeMap.get(edge[1])) == null) {
                nodeTo = new Node(edge[1]);
                graph.nodeMap.put(edge[1], nodeTo);
            }
            Edge edgeObj = new Edge(0, edge[0], edge[1]);
            List<Edge> outEdges = nodeFrom.outMap.getOrDefault(nodeTo, new ArrayList<>());
            List<Edge> inEdges = nodeFrom.inMap.getOrDefault(nodeTo, new ArrayList<>());
            inEdges.add(edgeObj);
            outEdges.add(edgeObj);
            nodeFrom.outMap.put(nodeTo, outEdges);
            nodeTo.inMap.put(nodeFrom, inEdges);
        }

        for (int i = 0; i < blueEdges.length; i++) {
            int[] edge = blueEdges[i];
            Node nodeFrom = null;
            Node nodeTo = null;
            if ((nodeFrom = graph.nodeMap.get(edge[0])) == null) {
                nodeFrom = new Node(edge[0]);
                graph.nodeMap.put(edge[0], nodeFrom);
            }
            if ((nodeTo = graph.nodeMap.get(edge[1])) == null) {
                nodeTo = new Node(edge[1]);
                graph.nodeMap.put(edge[1], nodeTo);
            }
            Edge edgeObj = new Edge(1, edge[0], edge[1]);
            List<Edge> outEdges = nodeFrom.outMap.getOrDefault(nodeTo, new ArrayList<>());
            List<Edge> inEdges = nodeFrom.inMap.getOrDefault(nodeTo, new ArrayList<>());
            inEdges.add(edgeObj);
            outEdges.add(edgeObj);
            nodeFrom.outMap.put(nodeTo, outEdges);
            nodeTo.inMap.put(nodeFrom, inEdges);
        }

        Map<Integer, Integer> shortestPathMap = new HashMap<>();
        Node zeroNode = graph.nodeMap.get(0);
        if (zeroNode != null) {
            shortestPathMap = zeroNode.findShortestPath();
        }

        int[] answers = new int[n];
        answers[0] = 0;

        for (int i = 1; i < n; i++) {
            answers[i] = shortestPathMap.getOrDefault(i, -1);
        }

        return answers;
    }

    class Node {
        public int nodeNo;
        public Map<Node, List<Edge>> inMap = new HashMap<>();
        public Map<Node, List<Edge>> outMap = new HashMap<>();
        public Node(int nodeNo) {
            this.nodeNo = nodeNo;
        }

        public Map<Integer, Integer> findShortestPath() {
            Map<Integer, Integer> shortestPathMap = new HashMap<>();

            for (Map.Entry<Node, List<Edge>> entry : outMap.entrySet()) {
                Node outNode = entry.getKey();
                List<Edge> outEdges = entry.getValue();
                if (outNode.nodeNo == this.nodeNo) {
                    shortestPathMap.put(outNode.nodeNo, 0);
                } else {
                    shortestPathMap.put(outNode.nodeNo, 1);
                    Set<String> usedPath = new HashSet<>();
                    deepFindShortestPath(outNode, outNode, outEdges.get(0), 1, shortestPathMap, usedPath);
                    if (outEdges.size() > 1) {
                        usedPath = new HashSet<>();
                        deepFindShortestPath(outNode, outNode, outEdges.get(1), 1, shortestPathMap, usedPath);
                    }
                }
            }

            return shortestPathMap;
        }

        private void deepFindShortestPath(Node startNode, Node currNode, Edge inEdge, int pathLong,  Map<Integer, Integer> shortestPathMap, Set<String> usedPath) {
            for (Map.Entry<Node, List<Edge>> entry : currNode.outMap.entrySet()) {
                Node outNode = entry.getKey();
                List<Edge> outEdges = entry.getValue();

                Edge selectedEdge = outEdges.size() == 1 ? outEdges.get(0) : outEdges.get(0).color == inEdge.color ? outEdges.get(1) : outEdges.get(0);
                if (selectedEdge.color == inEdge.color) {
                    continue;
                }
                if (outNode.nodeNo == currNode.nodeNo) {
                    continue;
                }
                String path = currNode.nodeNo + ":" + selectedEdge.toNodeNo + ":" + selectedEdge.color;
                if (usedPath.contains(path)) {
                    // circle
                    return;
                }
                usedPath.add(path);

                shortestPathMap.put(outNode.nodeNo, Math.min(pathLong + 1, shortestPathMap.getOrDefault(outNode.nodeNo, Integer.MAX_VALUE)));

                deepFindShortestPath(startNode, outNode, selectedEdge, pathLong + 1, shortestPathMap, usedPath);
            }
            return ;
        }
    }

    class Edge {
        public int color; // 0-red, 1-blue
        public int fromNodeNo;
        public int toNodeNo;
        public Edge(int color, int fromNodeNo, int toNodeNo) {
            this.color = color;
            this.fromNodeNo = fromNodeNo;
            this.toNodeNo = toNodeNo;
        }
    }

    class Graph {
        public Map<Integer, Node> nodeMap = new TreeMap<>();

    }

}

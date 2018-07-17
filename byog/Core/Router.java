//package byog.Core;
//
//import byog.TileEngine.TETile;
//
//import java.util.*;
//
//public class Router {
//    /* Pathfinder */
//
//    public static List<Position> shortestPath(Position startPos,
//                                          Position endPos) {
//        PriorityQueue<TileComparable> fringe = new PriorityQueue<TileComparable>();
//        fringe.add(new TileComparable(startPos, 0));
//
//        HashMap<Position, Position> bestPath = new HashMap<Position, Position>();
//
//        // Track distance from start node to where we are in algorithm's iteration
//        HashMap<Position, Double> distTo = new HashMap<Position, Double>();
//        distTo.put(startPos, 0.0);
//
//        // A* algorithm
//        while(true) {
//            TileComparable v = fringe.peek();
//
//            // get adjacent tiles of curr positon
//            for(Position w: startPos.adj) {
//                double distFromStart = distTo.get(v.p) + distance(v.p, w);
//
//                if((distTo.get(w) == null) || distFromStart < distTo.get(w)) {
//                    distTo.put(w, distFromStart);
//                    bestPath.put(w, v.p);
//                    fringe.add(new TileComparable(w, distFromStart + distance(endPos, w)));
//                }
//            }
//            try {
//                fringe.remove();
//            } catch (NullPointerException e) {
//                System.out.println("No valid path!");
//            }
//
//            // THE EQUALS MAY ERROR!!!
//            if(v.p.equals(endPos)) {
//                break;
//            }
//        }
//        LinkedList<Position> optimalPath = new LinkedList<>();
//        optimalPath.addFirst(endPos);
//        Position previousNode = bestPath.get(endPos);
//        while (!previousNode.equals(startPos)) {
//            optimalPath.addFirst(previousNode);
//            previousNode = bestPath.get(previousNode);
//        }
//        optimalPath.addFirst(startPos);
//        return optimalPath;
//
//    }
//
//    private static class TileComparable implements Comparable<TileComparable> {
//        Position p;
//        double dist;
//
//        TileComparable(Position p, double dist) {
//            this.p = p;
//            this.dist = dist;
//        }
//
//        @Override
//        public int compareTo(TileComparable otherNode) {
//            if (this.dist > otherNode.dist) {
//                return 1;
//            } else if (this.dist == otherNode.dist) {
//                return 0;
//            } else {
//                return -1;
//            }
//        }
//    }
//
//    private static double distance(Position p1, Position p2) {
//        return Math.sqrt(Math.pow(p1.x - p2.x, 2) +  Math.pow(p1.y - p2.y, 2));
//    }
//    public static void main(String[] args) {
//
//    }
//}

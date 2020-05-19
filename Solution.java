import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    int maxStation = scan.nextInt();
    Graph graph = new Graph(maxStation);
    graph.maxStation = maxStation;
    for (int i = 0; i < maxStation; i++) {
      graph.vertex[i] = i + 1;
    }
    int noOfTrains = scan.nextInt();
    graph.waitTime = scan.nextInt();
    int[] input = new int[4];
    for (int j = 0; j < noOfTrains; j++) {
      for (int i = 0; i < 4; i++) {
        input[i] = scan.nextInt();
      }
      if (graph.map.get((int) input[0]) == null) {
        graph.map.put(graph.vertex[(int) input[0] - 1], new ArrayList<Edge>());
      }
     //System.out.println(graph.vertex[(int) input[1] - 1]);
      Edge edge = new Edge(input[0], (int) input[1] , input[2], input[3]);
      //ArrayList<Edge> listEdge = graph.map.get(graph.vertex[(int)input[0]-1].station)
      //System.out.println(graph.vertex[(int)input[0]-1]+ " "+edge.vertex+" "+ edge.startTime+" "+edge.endTime);
      graph.map.get(graph.vertex[(int) input[0] - 1]).add(edge);
    }

    for (Integer entry : graph.map.keySet()) {
//      System.out.println("Station: " + entry);
//      System.out.println(graph.map.get(entry).toString());
    }
    int tmin = graph.dijkstra();
    if(tmin >= 9999999){
      System.out.println("NO");
    }
    else{
      System.out.println("YES "+(int)tmin);
    }
  }
}


class Edge {
  int startVertex;
  int destVertex;
  int startTime;
  int endTime;

  Edge(int startVertex, int destVertex, int startTime, int endTime) {
    this.startVertex = startVertex;
    this.destVertex = destVertex;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}

class DjTable implements Comparable<DjTable> {
  int travelTime;
  int waitTime;
  int prevNode;
  boolean done;
  int sum;

  DjTable() {
    travelTime = 9999999;
    waitTime = 9999999;
    sum = travelTime + waitTime;
    done = false;
  }

  @Override
  public int compareTo(DjTable djTable) {
    if (this.sum > djTable.sum) {
      return -1;
    } else if (this.sum < djTable.sum) {
      return 1;
    } else
      return 0;
  }

}

class LeastSum implements Comparable<LeastSum> {
  int vertex;
  int sum;
  Edge edge;

  LeastSum(int vertex, int sum, Edge edge) {
    this.sum = sum;
    this.vertex = vertex;
    this.edge = edge;
  }

  @Override
  public int compareTo(LeastSum leastSum) {
    if (this.sum > leastSum.sum) {
      return 1;
    } else if (this.sum < leastSum.sum) {
      return -1;
    } else
      return 0;
  }
}

class Graph {
  int maxStation;
  int[] vertex;
  HashMap<Integer, List<Edge>> map = new HashMap<>();
  int waitTime;

  Graph(int maxStation) {
    this.maxStation = maxStation;
    this.vertex = new int[maxStation+1000];
  }

  int dijkstra() {
    HashMap<Integer, DjTable> djMap = new HashMap<>();
    PriorityQueue<LeastSum> priorityQueue = new PriorityQueue<>();

    for (int i = 0; i < maxStation; i++) {
      djMap.put(vertex[i], new DjTable());
    }
    int start = vertex[0];
//    int dest = vertex[maxStation - 1];

    //Set start node as 0
    djMap.get(start).travelTime = 0;
    djMap.get(start).waitTime = 0;
    djMap.get(start).sum = 0;
    djMap.get(start).done = true;

    for (Edge edge : map.get(start)) {
      if (this.waitTime >= edge.startTime) {
        djMap.get(edge.destVertex).travelTime = edge.endTime - edge.startTime;
        djMap.get(edge.destVertex).waitTime = edge.startTime;
        djMap.get(edge.destVertex).prevNode = edge.startVertex;
        djMap.get(edge.destVertex).sum = edge.endTime;
        priorityQueue.add(new LeastSum(edge.destVertex, edge.endTime, edge));
      }
      // take care of else if needed later
    }
    int cnt = 0;
    while (!priorityQueue.isEmpty()) {
      int setToDone = 0;
      LeastSum current = priorityQueue.poll();

//      for (Integer i : djMap.keySet()) {
//        if (i == current.vertex) {
//          break;
//        }
//        if (djMap.get(i).done) {
//          ++setToDone;
//        }
//      }
//      if (setToDone == current.vertex - 1) {
//        djMap.get(current.vertex).done = true;
//      }
//      System.out.println(++cnt);
//      if(cnt == 4){
//        System.out.println("stop");
//      }
      DjTable currentDj = djMap.get(current.vertex);
      currentDj.prevNode = current.vertex;
//      currentDj.travelTime =

      if (map.get(current.vertex) != null) {
        for (Edge edge : map.get(current.vertex)) {
          int wait = edge.startTime - current.edge.endTime;
          if (this.waitTime >= wait && wait >= 0) {
            if(edge.destVertex <= maxStation && edge.startVertex < maxStation) {
              djMap.get(edge.destVertex).travelTime = edge.endTime - edge.startTime;
              djMap.get(edge.destVertex).waitTime = wait;
              djMap.get(edge.destVertex).prevNode = edge.startVertex;
              djMap.get(edge.destVertex).sum = edge.endTime ;
              priorityQueue.add(new LeastSum(edge.destVertex, edge.endTime, edge));
            }
          }
          // take care of else if needed later
        }
      }
    }
    return djMap.get(maxStation).sum;
  }
}
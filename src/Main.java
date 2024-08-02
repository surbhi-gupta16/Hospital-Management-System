import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Pair{
    int first;
    int second;
    public Pair(int first, int second){
        this.first = first;
        this.second = second;
    }
}
public class Main {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            edges.add(new ArrayList<>());
        }
        edges.get(0).add(0);
        edges.get(0).add(1);
        edges.get(0).add(2);
        edges.get(1).add(0);
        edges.get(1).add(2);
        edges.get(1).add(7);
        edges.get(2).add(1);
        edges.get(2).add(2);
        edges.get(2).add(4);
        edges.get(3).add(3);
        edges.get(3).add(4);
        edges.get(3).add(-7);
        System.out.println(Arrays.toString(bellman_ford(5,edges,0)));
    }

    static int[] bellman_ford(int V, ArrayList<ArrayList<Integer>> edges, int S) {
        int[] dist = new int[V];
        for(int i=0;i<V;i++){
            dist[i] = 100000000;
        }
        dist[S] =0;
        //V-1 iteration
        for(int i=0;i<V;i++){
            for(int j=0;j<edges.size();j++){
                int u = edges.get(j).get(0);
                int v = edges.get(j).get(1);
                int wt = edges.get(j).get(2);
                if(dist[u]+wt < dist[v] && dist[u] != 100000000){
                    dist[v] = dist[u]+wt;
                }
            }
        }
        if(findNegCycle(edges,dist)){
            return new int[]{-1};
        }
        return dist;
    }
    static boolean findNegCycle(ArrayList<ArrayList<Integer>> edges,int[] dist){
        int n = dist.length;
        int[] dist2 = new int[n];
        for(int i=0;i<n;i++){
            dist2[i] = dist[i];
        }
        for(int j=0;j<edges.size();j++){
            int u = edges.get(j).get(0);
            int v = edges.get(j).get(1);
            int wt = edges.get(j).get(2);
            if(dist2[u]+wt < dist2[v]){
                dist2[v] = dist2[u]+wt;
            }
        }
        for(int i=0;i<n;i++){
            if(dist2[i] != dist[i]) return true;
        }
        return false;
    }
}
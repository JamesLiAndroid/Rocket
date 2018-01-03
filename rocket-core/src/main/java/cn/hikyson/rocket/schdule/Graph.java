package cn.hikyson.rocket.schdule;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * 有向无环图的拓扑排序算法
 * Created by kysonchao on 2017/12/26.
 */
public class Graph {
    //顶点数
    private int mVerticeCount;
    //邻接表
    private List<Integer>[] mAdj;

    public Graph(int verticeCount) {
        this.mVerticeCount = verticeCount;
        mAdj = new ArrayList[mVerticeCount];
        for (int i = 0; i < mVerticeCount; i++) {
            mAdj[i] = new ArrayList<Integer>();
        }
    }

    /**
     * 添加边
     *
     * @param u from
     * @param v to
     */
    public void addEdge(int u, int v) {
        mAdj[u].add(v);
    }

    /**
     * 拓扑排序
     */
    public Vector<Integer> topologicalSort() {
        int indegree[] = new int[mVerticeCount];
        for (int i = 0; i < mVerticeCount; i++) {
            ArrayList<Integer> temp = (ArrayList<Integer>) mAdj[i];
            for (int node : temp) {
                indegree[node]++;
            }
        }
        Queue<Integer> q = new LinkedList<Integer>();
        for (int i = 0; i < mVerticeCount; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }
        int cnt = 0;
        Vector<Integer> topOrder = new Vector<Integer>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topOrder.add(u);
            for (int node : mAdj[u]) {
                if (--indegree[node] == 0) {
                    q.add(node);
                }
            }
            cnt++;
        }
        if (cnt != mVerticeCount) {//检查是否有环
            throw new IllegalStateException("exists a cycle in the graph");
        }
        return topOrder;
    }
}

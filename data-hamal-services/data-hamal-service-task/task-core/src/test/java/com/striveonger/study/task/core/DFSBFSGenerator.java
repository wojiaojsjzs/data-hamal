package com.striveonger.study.task.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Mr.Lee
 * @description: 让ChatGPT写的...屁都不是, 输出结果都不对
 * @date 2023-05-18 10:02
 */
public class DFSBFSGenerator {

    private int V; // 顶点数量
    private ArrayList<ArrayList<Integer>> adj; // 邻接表

    public DFSBFSGenerator(int v) {
        V = v;
        adj = new ArrayList<ArrayList<Integer>>(v);
        for (int i = 0; i < v; ++i)
            adj.add(new ArrayList<Integer>());
    }

    // 添加边
    public void addEdge(int v, int w) {
        adj.get(v).add(w);
    }

    // DFS生成
    public void dfsGenerate(int start) {
        boolean[] visited = new boolean[V];
        dfsGenerateUtil(start, visited);
    }

    private void dfsGenerateUtil(int v, boolean[] visited) {
        visited[v] = true;
        System.out.print(v + " ");

        // 随机选择下一步要访问的顶点
        int next = -1;
        for (int i : adj.get(v)) {
            if (!visited[i]) {
                if (Math.random() < 0.5) {
                    next = i;
                    break;
                }
            }
        }

        // 如果没有未访问的邻接顶点，则使用BFS生成
        if (next == -1) {
            bfsGenerate(v, visited);
        } else {
            dfsGenerateUtil(next, visited);
        }
    }

    // BFS生成
    public void bfsGenerate(int start, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<Integer>();
        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            System.out.print(v + " ");

            // 随机访问邻接顶点
            for (int i : adj.get(v)) {
                if (!visited[i]) {
                    if (Math.random() < 0.5) {
                        visited[i] = true;
                        queue.add(i);
                    }
                }
            }
        }
    }

    // 测试函数
    public static void main(String[] args) {
        DFSBFSGenerator g = new DFSBFSGenerator(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 6);
        g.addEdge(4, 6);
        g.addEdge(5, 6);

        System.out.println("使用DFS和BFS生成的结果为：");
        g.dfsGenerate(0);
    }

}

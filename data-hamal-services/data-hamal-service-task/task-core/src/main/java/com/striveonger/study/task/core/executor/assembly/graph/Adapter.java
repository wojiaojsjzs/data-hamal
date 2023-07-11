package com.striveonger.study.task.core.executor.assembly.graph;

import java.util.Map;
import java.util.Set;

/**
 * @description: 邻接表的适配器
 * @author: Mr.Lee
 * @date: 2021-08-27 16:54
 */
public class Adapter<T> {

    public Graph<T> createGraph(Map<T, Set<T>> data) {
        Graph<T> graph = new Graph<>();
        for (Map.Entry<T, Set<T>> entry: data.entrySet()) {
            T from = entry.getKey();
            if (!graph.getNodes().containsKey(from)) {
                graph.getNodes().put(from, new Node<>(from));
            }
            Node<T> fromNode = graph.getNodes().get(from);
            for (T to : entry.getValue()) {
                if (!graph.getNodes().containsKey(to)) {
                    graph.getNodes().put(to, new Node<>(to));
                }
                Node<T> toNode = graph.getNodes().get(to);
                updateGraph(graph, fromNode, toNode);
            }
        }
        return graph;
    }

    public void updateGraph(Graph<T> graph, Node<T> fromNode, Node<T> toNode) {
        // 向from添加邻居节点
        fromNode.getNexts().add(toNode);

        // 设置from的直接边
        Edge<T> edge = new Edge<>(fromNode, toNode);
        fromNode.getEdges().add(edge);
        graph.getEdges().add(edge);

        // 更新入度与出度
        fromNode.addOut(1);
        toNode.addIn(1);
    }
}

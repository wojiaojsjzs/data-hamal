package com.striveonger.study.task.core.executor.assembly.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: Mr.Lee
 * @date: 2021-08-27 16:04
 */
public class Graph<T> {

    private Map<T, Node<T>> nodes;
    private Set<Edge<T>> edges;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }

    public Map<T, Node<T>> getNodes() {
        return nodes;
    }

    public void setNodes(Map<T, Node<T>> nodes) {
        this.nodes = nodes;
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge<T>> edges) {
        this.edges = edges;
    }
}
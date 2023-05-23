package com.striveonger.study.task.core.executor.assembly.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Mr.Lee
 * @date: 2021-08-27 15:54
 */
public class Node<T> {

    /**
     * 节点值
     */
    private T value;

    /**
     * 节点入度
     */
    private int in;

    /**
     * 节点出度
     */
    private int out;

    /**
     * 点的所有邻居
     */
    private List<Node<T>> nexts;

    /**
     * 点的所有边
     */
    private List<Edge<T>> edges;

    public Node(T value) {
        this.value = value;
        this.in = 0;
        this.out = 0;
        this.nexts = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public List<Node<T>> getNexts() {
        return nexts;
    }

    public Node<T> getNext(int idx) {
        return nexts.get(idx);
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public void addIn(int x) {
        this.in += x;
    }

    public void addOut(int x) {
        this.out += x;
    }


}

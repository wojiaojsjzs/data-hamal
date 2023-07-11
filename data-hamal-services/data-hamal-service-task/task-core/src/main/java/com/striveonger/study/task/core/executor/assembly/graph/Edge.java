package com.striveonger.study.task.core.executor.assembly.graph;

/**
 * @description:
 * @author: Mr.Lee
 * @date: 2021-08-27 15:58
 */
public class Edge<T> {

    /**
     * 起点
     */
    private Node<T> from;

    /**
     * 终点
     */
    private Node<T> to;

    public Edge(Node<T> from, Node<T> to) {
        this.from = from;
        this.to = to;
    }

    public Node<T> getFrom() {
        return from;
    }

    public Node<T> getTo() {
        return to;
    }
}

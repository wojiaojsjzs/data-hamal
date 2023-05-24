package com.striveonger.study.task.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.task.core.executor.assembly.graph.Adapter;
import com.striveonger.study.task.core.executor.assembly.graph.Graph;
import com.striveonger.study.task.core.executor.assembly.graph.Node;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @description: 自定义的拓扑排序
 * @author: Mr.Lee
 * @date: 2021-08-11 10:30
 */
public class CustomTopologySort {

    public static void main(String[] args) {
        // 准备图数据
        Adapter adapter = new Adapter();
        String json = """
            {
                "X1" : ["X2"],
                "X2" : ["X3"],
                "X3" : ["F"],
                "A1" : ["A2"],
            	"A2" : ["C"],
            	"B"  : ["C"],
            	"C"  : ["D", "E1", "F"],
            	"D"  : ["G"],
            	"E1" : ["E2"],
            	"E2" : ["G"],
            	"F"  : ["H"],
            	"G"  : ["I"],
            	"H"  : ["I"],
            	"I"  : ["J"],
            	"J"  : ["K"],
            	"K"  : []
            }
            """;

        Map<String, Set<String>> map = JacksonUtils.toObject(json, new TypeReference<Map<String, Set<String>>>() {});
        Graph<String> graph = adapter.createGraph(map);
        sort(graph);
    }

    private static void sort(Graph<String> graph) {
        // 必须是有向无环图哦~
        Queue<Node<String>> queue = new LinkedList<>();
        Map<Node<String>, Integer> nodeInMpa = new HashMap<>();
        Set<Node<String>> register = new HashSet<>();
        // 1. 将图中, 所有的节点及节点入度插入到Map中
        for (Node<String> node : graph.getNodes().values()) {
            nodeInMpa.put(node, node.getIn());
            // 2. 将入度为0的节点插入到队列中.
            if (node.getIn() == 0) {
                // 入队就注册
                register.add(node);
                queue.offer(node);
            }
        }

        // 这里是所有跟节点的起点
        List<Object> list = new ArrayList<>();
        while (!queue.isEmpty()) {
            Node<String> curr = queue.poll();
            if (curr.getOut() > 1) {
                // 多支路线
                Object res = bfs(curr, register, nodeInMpa);
                list.add(res);
            } else if (curr.getOut() == 1) {
                // 单支路线
                Object res = dfs(curr, register, nodeInMpa);
                list.add(res);
            } else {
                // 单独执行节点
                System.out.println(curr.getValue());
                list.add(curr.getValue());
            }

            // 更新队列
            if (queue.isEmpty()) {
                // 更新队列, 检查否有合并分支
                for (Map.Entry<Node<String>, Integer> entry : nodeInMpa.entrySet()) {
                    // 如果没有注册过, 且入度为0. 那肯定是合并分支
                    if (!register.contains(entry.getKey()) && entry.getValue() == 0) {
                        // 消除合并节点影响
                        Integer in = nodeInMpa.get(entry.getKey()) - 1;
                        nodeInMpa.put(entry.getKey(), in);
                        // 注册 合并节点
                        register.add(entry.getKey());
                        // 加入队列
                        queue.add(entry.getKey());
                    }
                }
            }
        }
        System.out.println(list);
    }

    private static Map<Object, Object> bfs(Node<String> node, Set<Node<String>> register, Map<Node<String>, Integer> nodeInMpa) {
        Queue<Node<String>> queue = new LinkedList<>();
        // 创建并行节点
        Map<Object, Object> res = new HashMap<>();
        // 使用 node节点
        System.out.println(node.getValue());
        res.put(node.getValue(), node.getValue());
        // 消除 node节点在图中的影响, 并注册邻居节点
        for (Node<String> curr : node.getNexts()) {
            // 消除影响
            Integer in = nodeInMpa.get(curr) - 1;
            nodeInMpa.put(curr, in);
            // 注册 node邻居节点
            register.add(curr);
            // 加入到队列中
            queue.offer(curr);
        }

        while (!queue.isEmpty()) {
            Node<String> curr = queue.poll();
            if (curr.getOut() > 1) {
                // 多支路线
                Object obj = bfs(curr, register, nodeInMpa);
                res.put(obj, obj);
            } else if (curr.getOut() == 1) {
                // 单支路线
                Object obj = dfs(curr, register, nodeInMpa);
                res.put(obj, obj);
            } else {
                // 单独执行节点
                System.out.println(curr.getValue());
                res.put(curr.getValue(), curr.getValue());
            }
            if (queue.isEmpty()) {
                // 更新队列, 检查否有合并分支
                for (Map.Entry<Node<String>, Integer> entry : nodeInMpa.entrySet()) {
                    // 如果没有注册过, 且入度为0. 那肯定是合并分支
                    if (!register.contains(entry.getKey()) && entry.getValue() == 0) {
                        // 消除合并节点影响
                        Integer in = nodeInMpa.get(entry.getKey()) - 1;
                        nodeInMpa.put(entry.getKey(), in);
                        // 注册 合并节点
                        register.add(entry.getKey());
                        // 加入队列
                        queue.add(entry.getKey());
                    }
                }
            }
        }
        return res;
    }

    private static List<Object> dfs(Node<String> node, Set<Node<String>> register, Map<Node<String>, Integer> nodeInMpa) {
        // 创建串行节点
        List<Object> res = new ArrayList<>();
        System.out.println(node.getValue());
        res.add(node.getValue());
        if (node.getOut() == 1) {
            Node<String> curr = node.getNexts().get(0);
            // 消除 node 在后继节点的影响
            Integer in = nodeInMpa.get(curr) - 1;
            nodeInMpa.put(curr, in);
            if (curr.getIn() == 1) {
                // 注册 node邻居节点
                register.add(curr);
                res.add(dfs(curr, register, nodeInMpa));
            }
        } else if (node.getOut() > 1) {
            // 说明有分支存在, 所以调用bfs
            // bfs 里, 已经处理了起始点, 所以, 这里就不用再加入了
            res.clear();
            res.add(bfs(node, register, nodeInMpa));
        }
        return res;
    }

}

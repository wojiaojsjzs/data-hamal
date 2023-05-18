package com.striveonger.study.task.core.executor.assembly;

import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.assembly.graph.Adapter;
import com.striveonger.study.task.core.executor.assembly.graph.Graph;
import com.striveonger.study.task.core.executor.assembly.graph.Node;
import com.striveonger.study.task.core.executor.flow.ParalleFlowExecutor;
import com.striveonger.study.task.core.executor.flow.SerialeFlowExecutor;
import com.striveonger.study.task.core.launch.TaskLaunch;
import com.striveonger.study.task.core.scope.Workbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.striveonger.study.task.core.exception.BuildTaskException.Type;
/**
 * @author Mr.Lee
 * @description: 执行器的拼装工具
 * @date 2023-05-17 16:59
 */
public class ExecutorAssembly {
    private final Logger log = LoggerFactory.getLogger(ExecutorAssembly.class);

    // TODO: 拼装顺序还是有问题...一会儿找个安静的时间, 重写一下dfs+bfs遍历图的算法吧(这个项目最好玩儿的, 就是这个了)

    private final Graph<Executor> graph;
    private final Workbench workbench;

    private ExecutorAssembly(Graph<Executor> graph, Workbench workbench) {
        this.graph = graph;
        this.workbench = workbench;
    }

    public Executor assembly() {
        Queue<Node<Executor>> queue = new LinkedList<>();
        Map<Node<Executor>, Integer> nodeInMpa = new HashMap<>();
        Set<Node<Executor>> register = new HashSet<>();

        // 应对图中有多起点的情况.
        List<Executor> parallelFlows = new ArrayList<>();
        // 应对多起点合并分支的情况.
        List<Executor> mergeFlows = new ArrayList<>();
        // 合并节点的标致位
        boolean isMerge = false;
        // 1. 将图中, 所有的节点及节点入度插入到Map中
        for (Node<Executor> node : graph.getNodes().values()) {
            nodeInMpa.put(node, node.getIn());
            // 2. 将入度为0的节点插入到队列中.
            if (node.getIn() == 0) {
                // 入队就注册
                register.add(node);
                queue.offer(node);
            }
        }
        while (!queue.isEmpty()) {
            Node<Executor> curr = queue.poll();
            if (curr.getOut() > 1) {
                // 多支路线
                Executor flow = generateParallelFlow(curr, register, nodeInMpa);
                if (isMerge) {
                    mergeFlows.add(flow);
                } else {
                    parallelFlows.add(flow);
                }
            } else if (curr.getOut() == 1) {
                // 单支路线
                Executor flow = generateSerialFlow(curr, register, nodeInMpa);
                if (isMerge) {
                    mergeFlows.add(flow);
                } else {
                    parallelFlows.add(flow);
                }
            } else {
                // 单独执行节点
                Executor sole = curr.getValue();
                if (isMerge) {
                    mergeFlows.add(sole);
                } else {
                    parallelFlows.add(sole);
                }
            }
            if (queue.isEmpty()) {
                isMerge = false;
                // 更新队列, 检查否有合并分支
                for (Map.Entry<Node<Executor>, Integer> entry : nodeInMpa.entrySet()) {
                    // 如果没有注册过, 且入度为0. 那肯定是合并分支
                    if (!register.contains(entry.getKey()) && entry.getValue() == 0) {
                        isMerge = true;
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

        // 创建一个并行的Flow
        ParalleFlowExecutor executor = new ParalleFlowExecutor();
        executor.setWorkbench(workbench);
        executor.push(parallelFlows);
        executor.push(mergeFlows);
        return executor;
    }

    /**
     * BFS 创建并行 Flow
     *
     * @param node
     * @param register
     * @param nodeInMpa
     * @return
     */
    private Executor generateParallelFlow(Node<Executor> node, Set<Node<Executor>> register, Map<Node<Executor>, Integer> nodeInMpa) {
        Queue<Node<Executor>> queue = new LinkedList<>();
        // 应对图中有多起点的情况.
        List<Executor> parallelFlows = new ArrayList<>();
        // 应对多起点合并分支的情况.
        List<Executor> mergeFlows = new ArrayList<>();
        // 合并节点的标致位
        boolean isMerge = false;
        // 创建并行节点的起点
        Executor start = node.getValue();
        // 消除 node节点在图中的影响, 并注册邻居节点
        for (Node<Executor> curr : node.getNexts()) {
            // 消除影响
            Integer in = nodeInMpa.get(curr) - 1;
            nodeInMpa.put(curr, in);
            // 注册 node邻居节点
            register.add(curr);
            // 加入到队列中
            queue.offer(curr);
        }

        while (!queue.isEmpty()) {
            Node<Executor> curr = queue.poll();
            if (curr.getOut() > 1) {
                // 多支路线
                Executor flow = generateParallelFlow(curr, register, nodeInMpa);
                if (isMerge) {
                    mergeFlows.add(flow);
                } else {
                    parallelFlows.add(flow);
                }
            } else if (curr.getOut() == 1) {
                // 单支路线
                Executor flow = generateSerialFlow(curr, register, nodeInMpa);
                if (isMerge) {
                    mergeFlows.add(flow);
                } else {
                    parallelFlows.add(flow);
                }
            } else {
                // 单独执行节点
                Executor sole = curr.getValue();
                if (isMerge) {
                    mergeFlows.add(sole);
                } else {
                    parallelFlows.add(sole);
                }
            }
            if (queue.isEmpty()) {
                isMerge = false;
                // 更新队列, 检查否有合并分支
                for (Map.Entry<Node<Executor>, Integer> entry : nodeInMpa.entrySet()) {
                    // 如果没有注册过, 且入度为0. 那肯定是合并分支
                    if (!register.contains(entry.getKey()) && entry.getValue() == 0) {
                        isMerge = true;
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
        // 创建一个并行的Flow
        ParalleFlowExecutor executor = new ParalleFlowExecutor();
        executor.setWorkbench(workbench);
        executor.push(parallelFlows);
        executor.push(mergeFlows);
        return executor;
    }


    /**
     * DFS 创建串行 Flow
     *
     * @param node
     * @param register
     * @param nodeInMpa
     * @return
     */
    private Executor generateSerialFlow(Node<Executor> node, Set<Node<Executor>> register, Map<Node<Executor>, Integer> nodeInMpa) {
        // 创建串行节点
        List<Executor> serialFlows = new ArrayList<>();
        serialFlows.add(node.getValue());
        if (node.getOut() == 1) {
            Node<Executor> curr = node.getNexts().get(0);
            // 消除 node 在后继节点的影响
            int in = nodeInMpa.get(curr) - 1;
            nodeInMpa.put(curr, in);
            if (in == 0 && curr.getOut() == 1) {
                // 注册 node邻居节点
                register.add(curr);
                serialFlows.add(generateSerialFlow(curr, register, nodeInMpa));
            }
        } else if (node.getOut() > 1) {
            // 说明有分支存在, 所以调用bfs
            // bfs 里, 已经处理了起始点, 所以, 这里就不用再加入了
            serialFlows.clear();
            serialFlows.add(generateParallelFlow(node, register, nodeInMpa));
        }
        SerialeFlowExecutor executor = new SerialeFlowExecutor();
        executor.setWorkbench(workbench);
        executor.push(serialFlows);
        return executor;
    }

    public static class Builder {
        private Graph<Executor> graph;
        private Workbench workbench;

        public static Builder builder() {
            return new Builder();
        }

        public Builder graph(Map<Executor, Set<Executor>> data) {
            Adapter<Executor> adapter = new Adapter<>();
            this.graph = adapter.createGraph(data);
            return this;
        }

        public Builder workbench(Workbench workbench) {
            this.workbench = workbench;
            return this;
        }

        public ExecutorAssembly build() {
            if (graph == null || workbench == null) {
                throw new BuildTaskException(Type.STEP, "missing the necessary 'graph' or 'workbench'...");
            }
            return new ExecutorAssembly(graph, workbench);
        }

    }

}

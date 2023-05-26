package com.striveonger.study.task.core.executor.assembly;

import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.EmptyExecutor;
import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.assembly.graph.Adapter;
import com.striveonger.study.task.core.executor.assembly.graph.Graph;
import com.striveonger.study.task.core.executor.assembly.graph.Node;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.executor.flow.FlowExecutor;
import com.striveonger.study.task.core.executor.flow.ParalleFlowExecutor;
import com.striveonger.study.task.core.executor.flow.SerialeFlowExecutor;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.striveonger.study.task.core.exception.BuildTaskException.Type;

/**
 * @author Mr.Lee
 * @description: 执行器的组装工具
 * @date 2023-05-17 16:59
 */
public class ExecutorAssembly {
    private final Logger log = LoggerFactory.getLogger(ExecutorAssembly.class);

    private final Graph<Executor> graph;
    private final Workbench workbench;

    // 实时的入度信息
    private final Map<Node<Executor>, Integer> intake = new HashMap<>();
    // 访问过的节点(防止重复访问)
    private final Set<Node<Executor>> register = new HashSet<>();

    private ExecutorAssembly(Graph<Executor> graph, Workbench workbench) {
        this.graph = graph;
        this.workbench = workbench;
    }

    /**
     * 开始组装
     */
    public Executor assembly() {
        // 1. 将图中, 所有的节点及节点入度保存到 intake
        Node<Executor> start = null;
        for (Node<Executor> node : graph.getNodes().values()) {
            intake.put(node, node.getIn());
            // 2. 寻找start节点
            if (node.getIn() == 0) {
                register.add(node);
                start = node;
            }
        }
        // 经过处理的图, 起点只有一个
        if (start == null || register.size() > 1) {
            throw new BuildTaskException(Type.STEP, "not found applicable start node...");
        }

        if (start.getOut() == 1) {
            return dfs(start);
        } else if (start.getOut() > 1) {
            return bfs(start);
        } else {
            // 只有一个执行节点的情况
            return start.getValue();
        }
    }

    /**
     * 多支路的情况
     *
     * @param node 分叉节点
     * @return
     */
    private FlowExecutor bfs(Node<Executor> node) {
        // 做为当前的"起点", 进入串行执行器, 等待合并各个分支
        FlowExecutor result = new SerialeFlowExecutor();
        result.setWorkbench(workbench);
        result.push(node.getValue());
        // 注册并消除对后继节点的影响
        register.add(node);
        clearImpact(node);
        // 需要合并的各支路
        List<Executable> list = new ArrayList<>();
        FlowExecutor paralle = new ParalleFlowExecutor();
        paralle.setWorkbench(workbench);
        // 处理支路
        boolean merge = false;
        Queue<Node<Executor>> queue = new LinkedList<>();
        node.getNexts().forEach(queue::offer);
        while (!queue.isEmpty()) {
            Node<Executor> next = queue.poll();
            if (next.getOut() > 1) {
                FlowExecutor flow = bfs(next);
                list.add(flow);
                // 判断是否需要合并
                if (merge) {
                    paralle.push(list);
                    list.clear();
                }
            } else if (next.getOut() == 1) {
                FlowExecutor flow = dfs(next);
                list.add(flow);
                // 判断是否需要合并
                if (merge) {
                    paralle.push(list);
                    list.clear();
                }
            } else {
                // 用来处理分支上的叶子节点
                // 注册节点, 叶子节点不需要消除影响的
                register.add(next);
                if (merge) {
                    //  A
                    //     \
                    //       C
                    //     /
                    //  B
                    // A, B 被加到queue中, 最终 C 会走这里
                    paralle.push(next.getValue());
                } else {
                    //      B -- C
                    //    /
                    //  A
                    //    \
                    //      D
                    // A 做为node进来, D 会走这里...
                    list.add(next.getValue());
                }
            }

            if (queue.isEmpty()) {
                merge = false;
                // 更新队列, 检查否有合并分支
                for (Map.Entry<Node<Executor>, Integer> entry : intake.entrySet()) {
                    // 如果没有注册过, 且入度为0. 那肯定是合并分支
                    if (!register.contains(entry.getKey()) && entry.getValue() == 0) {
                        merge = true;
                        // 加入队列
                        queue.offer(entry.getKey());
                    }
                }
            }
        }
        result.push(paralle);
        return result;
    }

    /**
     * 单支路的情况
     *
     * @param node currNode.out == 1 && nextNode.in < 2
     * @return [node, node]
     */
    private FlowExecutor dfs(Node<Executor> node) {
        if (node.getOut() > 1) {
            return bfs(node);
        } else {
            SerialeFlowExecutor flow = new SerialeFlowExecutor();
            flow.setWorkbench(workbench);
            // 无后继或单后继节点的情况
            flow.push(node.getValue());
            // 注册并消除影响
            register.add(node);
            clearImpact(node);
            // 判断是否要继续向下嗅探
            Node<Executor> next;
            if (node.getOut() == 1 && (next = node.getNext(0)).getIn() == 1) {
                // 当前节点出度为1, 并且后继节点的入度也为1 时, 就继续向下收集 (找到一条绳上的蚂蚱)

                // 方案一: 简化执行结构
                FlowExecutor nextFlows = dfs(next);
                List<Executable> list = mergeSerialeFlow(nextFlows);
                flow.push(list);

                // 方案二: 带层级执行, 会额外创建执行器
                // flow.push(dfs(next));
            }
            return flow;
        }
    }

    /**
     * 简化执行结构, 摊平层级
     *
     * @param flow
     * @return
     */
    private List<Executable> mergeSerialeFlow(FlowExecutor flow) {
        List<Executable> list = new ArrayList<>();
        for (Executable task : flow.getSubtasks()) {
            if (task instanceof SerialeFlowExecutor subTask) {
                list.addAll(mergeSerialeFlow(subTask));
            } else {
                list.add(task);
            }
        }
        return list;
    }

    /**
     * 消除node节点, 在图中的影响
     *
     * @param node 完成注册的节点
     */
    private void clearImpact(Node<Executor> node) {
        for (Node<Executor> next : node.getNexts()) {
            int in = intake.get(next) - 1;
            intake.put(next, in);
        }
    }

    public static class Builder {
        private final Adapter<Executor> adapter = new Adapter<>();
        private Workbench workbench;
        private Map<String, ExecutorExtraInfo> extras;
        private Map<String, Set<String>> topology;

        public static Builder builder() {
            return new Builder();
        }

        public Builder topology(Map<String, Set<String>> topology) {
            this.topology = topology;
            return this;
        }

        public Builder extras(Map<String, ExecutorExtraInfo> extras) {
            this.extras = extras;
            return this;
        }


        public Builder workbench(Workbench workbench) {
            this.workbench = workbench;
            return this;
        }

        public ExecutorAssembly build() {
            if (topology == null || extras == null) {
                throw new BuildTaskException(Type.STEP, "missing the necessary 'topology' or 'extras'...");
            }
            Map<Executor, Set<Executor>> data = new HashMap<>();
            for (Map.Entry<String, Set<String>> entry : topology.entrySet()) {
                ExecutorExtraInfo key = extras.get(entry.getKey());
                if (key == null) {
                    throw new BuildTaskException(Type.STEP, "topology key not match executor...");
                }
                Set<Executor> value = new HashSet<>();
                for (String s : entry.getValue()) {
                    ExecutorExtraInfo extra = extras.get(s);
                    if (extra == null) {
                        throw new BuildTaskException(Type.STEP, "topology key not match executor...");
                    }
                    value.add(extra.getExecutor());
                }
                data.put(key.getExecutor(), value);
            }

            Graph<Executor> graph = adapter.createGraph(data);
            if (graph == null || workbench == null) {
                throw new BuildTaskException(Type.STEP, "missing the necessary 'graph' or 'workbench'...");
            }
            // 处理起点
            replyMuchStart(graph);
            return new ExecutorAssembly(graph, workbench);
        }

        /**
         * 应对多起点的情况
         */
        private void replyMuchStart(Graph<Executor> graph) {
            Set<Node<Executor>> set = new HashSet<>();
            for (Node<Executor> node : graph.getNodes().values()) {
                if (node.getIn() == 0) {
                    set.add(node);
                }
            }
            if (set.size() > 1) {
                // 对于多起点的情况, 虚出一个起点来(能让代码更加统一)
                Executor from = new EmptyExecutor();
                Node<Executor> fromNode = new Node<>(from);
                graph.getNodes().put(from, fromNode);
                for (Node<Executor> toNode : set) {
                    adapter.updateGraph(graph, fromNode, toNode);
                }
            }
        }

    }

    // === debug start ===
    private String getDisplayName(Executor executor) {
        StepContext context = workbench.getContext().getStepContext(executor);
        return context.getDisplayName();
    }

    private List<String> getDisplayName(List<Executor> executors) {
        return executors.stream().map(this::getDisplayName).toList();
    }
    // === debug end ===
}

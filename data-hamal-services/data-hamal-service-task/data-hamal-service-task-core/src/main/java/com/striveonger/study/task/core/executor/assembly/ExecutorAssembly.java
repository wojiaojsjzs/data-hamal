package com.striveonger.study.task.core.executor.assembly;

import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.assembly.graph.Adapter;
import com.striveonger.study.task.core.executor.assembly.graph.Graph;
import com.striveonger.study.task.core.executor.assembly.graph.Node;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.executor.flow.FlowExecutor;
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

    // 当前的可访问的节点
    private final Queue<Node<Executor>> queue = new LinkedList<>();
    // 实时的入度信息
    private final Map<Node<Executor>, Integer> intake = new HashMap<>();
    // 访问过的节点(防止重复访问)
    private final Set<Node<Executor>> register = new HashSet<>();

    private ExecutorAssembly(Graph<Executor> graph, Workbench workbench) {
        this.graph = graph;
        this.workbench = workbench;
    }

    public FlowExecutor assembly() {

        // 1. 将图中, 所有的节点及节点入度插入到Map中
        for (Node<Executor> node : graph.getNodes().values()) {
            intake.put(node, node.getIn());
            // 2. 将入度为0的节点插入到队列中.
            if (node.getIn() == 0) {
                queue.offer(node);
                // 入队就注册
                register.add(node);
            }
        }

        while (!queue.isEmpty()) {
            Node<Executor> current = queue.poll();
            if (current.getOut() > 1) {
                // bfs

            } else if (current.getOut() == 1) {
                // dfs
                List<Executor> executors = dfs(current);
                SerialeFlowExecutor flow = new SerialeFlowExecutor();
                flow.setWorkbench(workbench);
                flow.push(executors);
                
                // === debug start ===
                log.debug("SerialeFlowExecutor: {}", getDisplayName(executors));
                // === debug end ===
            }

            if (queue.isEmpty()) {
                // 更新队列, 检查否有合并分支
                for (Map.Entry<Node<Executor>, Integer> entry : intake.entrySet()) {
                    // 如果没有注册过, 且入度为0. 那肯定是合并分支
                    if (!register.contains(entry.getKey()) && entry.getValue() == 0) {
                        // 加入队列
                        queue.offer(entry.getKey());
                        // 还是秉承着入队就注册
                        register.add(entry.getKey());
                    }
                }
            }
        }

        return null;
    }


    private List<Executor> dfs(Node<Executor> current) {
        List<Executor> list = new ArrayList<>();
        if (current.getOut() > 1) {
            // 多后继节点的情况

        } else {
            // 无后继或单后继节点的情况
            list.add(current.getValue());
            // 注册并消除影响
            register.add(current);
            clearImpact(current);
            // 判断是否要继续向下嗅探
            Node<Executor> next;
            if (current.getOut() == 1 && (next = current.getNext(0)).getIn() == 1) {
                // 当前节点出度为1, 并且后继节点的入度也为1 时, 就继续向下收集 (找到一条绳上的蚂蚱)
                list.addAll(dfs(next));
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

            Adapter<Executor> adapter = new Adapter<>();
            Graph<Executor> graph = adapter.createGraph(data);
            if (graph == null || workbench == null) {
                throw new BuildTaskException(Type.STEP, "missing the necessary 'graph' or 'workbench'...");
            }
            return new ExecutorAssembly(graph, workbench);
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

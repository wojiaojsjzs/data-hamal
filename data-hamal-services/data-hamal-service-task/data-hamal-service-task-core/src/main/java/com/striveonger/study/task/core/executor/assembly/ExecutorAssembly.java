package com.striveonger.study.task.core.executor.assembly;

import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.assembly.graph.Adapter;
import com.striveonger.study.task.core.executor.assembly.graph.Graph;
import com.striveonger.study.task.core.executor.assembly.graph.Node;
import com.striveonger.study.task.core.executor.flow.FlowExecutor;
import com.striveonger.study.task.core.listener.Listener;
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

    private final Graph<Executor> graph;
    private final Workbench workbench;

    private Queue<Node<Executor>> queue = new LinkedList<>();
    private final Map<Node<Executor>, Integer> intake = new HashMap<>();
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

        // 是否为多起点
        boolean muchStart = register.size() > 1;
        List<FlowExecutor> list = new ArrayList<>();


        while (!queue.isEmpty()) {
            Node<Executor> current = queue.poll();
            if (current.getOut() > 1) {
                // bfs
            } else if (current.getOut() == 1) {
                // dfs
                FlowExecutor executors = dfs(current);

            }
        }

        return null;
    }

    private FlowExecutor dfs(Node<Executor> current) {
        List<Executor> list = new ArrayList<>();
        if (current.getOut() < 2 && current.getIn() < 2) {
            list.add(current.getValue());

        }
        return null;

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

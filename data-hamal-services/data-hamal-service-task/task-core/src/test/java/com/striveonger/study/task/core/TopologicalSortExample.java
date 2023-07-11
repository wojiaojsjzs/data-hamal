package com.striveonger.study.task.core;


import com.fasterxml.jackson.core.type.TypeReference;
import com.striveonger.study.core.utils.JacksonUtils;

import java.util.*;

public class TopologicalSortExample {
    public static void main(String[] args) {
        String json = """
            {
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

        Map<String, List<String>> dependencyMap = JacksonUtils.toObject(json, new TypeReference<>() {});
        assert dependencyMap != null;
        List<String> executionOrder = topologicalSort(dependencyMap);

        System.out.println("Execution Order:");
        for (String task : executionOrder) {
            System.out.println(task);
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> dependencyMap) {
        List<String> executionOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (String task : dependencyMap.keySet()) {
            if (!visited.contains(task)) {
                visit(task, dependencyMap, visited, executionOrder);
            }
        }

        Collections.reverse(executionOrder);
        return executionOrder;
    }

    private static void visit(String task, Map<String, List<String>> dependencyMap, Set<String> visited, List<String> executionOrder) {
        visited.add(task);
        List<String> dependencies = dependencyMap.get(task);
        if (dependencies != null) {
            for (String dependency : dependencies) {
                if (!visited.contains(dependency)) {
                    visit(dependency, dependencyMap, visited, executionOrder);
                }
            }
        }
        executionOrder.add(task);
    }
}

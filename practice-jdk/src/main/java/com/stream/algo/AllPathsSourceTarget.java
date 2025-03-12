package com.stream.algo;

import java.util.ArrayList;
import java.util.List;

public class AllPathsSourceTarget {
    static List<List<Integer>> ans = new ArrayList<List<Integer>>();
    public static void dfs(int[][] graph, int now, List<Integer> path) {
        path.add(now);
        if (now == graph.length - 1) {
            ans.add(new ArrayList<>(path));
        }
        else {
            for (int i = 0; i < graph[now].length; i++) {
                dfs(graph, graph[now][i], path);
            }
        }
        path.remove((Object) now);
    }
    public static List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        dfs(graph, 0, new ArrayList<Integer>());
        return ans;
    }

    public static void main(String[] args) {
        allPathsSourceTarget(new int[][]{{1, 2}, {3}, {3}, {}});
        for (List<Integer> list : ans) {
            for (Integer integer : list) {
                System.out.print(integer + " ");
            }
            System.out.println();
        }
    }
}

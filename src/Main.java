import java.util.*;

class Edge {
    int u, v;
    Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }
}

class Business {
    int id, s, t, l, c1, c2, p;
    List<Integer> path;
    Business(int id, int s, int t, int l, int c1, int c2, int p, List<Integer> path) {
        this.id = id;
        this.s = s;
        this.t = t;
        this.l = l;
        this.c1 = c1;
        this.c2 = c2;
        this.p = p;
        this.path = path;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取输入数据
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[] maxChangeTimes = new int[n];
        for (int i = 0; i < n; i++) {
            maxChangeTimes[i] = scanner.nextInt();
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges.add(new Edge(u - 1, v - 1)); // 转换为0开始的编号
        }

        int b = scanner.nextInt();
        List<Business> businesses = new ArrayList<>();
        for (int i = 0; i < b; i++) {
            int Src = scanner.nextInt();
            int Snk = scanner.nextInt();
            int S = scanner.nextInt();
            int L = scanner.nextInt();
            int R = scanner.nextInt();
            int V = scanner.nextInt();
            List<Integer> path = new ArrayList<>();
            for (int j = 0; j < S; j++) {
                path.add(scanner.nextInt() - 1); // 转换为0开始的编号
            }
            businesses.add(new Business(i, Src - 1, Snk - 1, S, L, R, V, path));
        }

        int T = scanner.nextInt();
        List<List<Integer>> scenarios = new ArrayList<>();
        for (int i = 0; i < T; i++) {
            List<Integer> scenario = new ArrayList<>();
            while (true) {
                int e = scanner.nextInt();
                if (e == 0) break;
                scenario.add(e - 1); // 转换为0开始的编号
            }
            scenarios.add(scenario);

        }

        // 建立图的邻接表
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (Edge edge : edges) {
            graph.get(edge.u).add(edge.v);
            graph.get(edge.v).add(edge.u);
        }

        // 处理每个测试场景
        for (List<Integer> scenario : scenarios) {
            Set<Integer> brokenEdges = new HashSet<>(scenario);
            replanBusinesses(graph, edges, businesses, brokenEdges);
        }

        scanner.close();
    }

    private static boolean bfsFindPath(List<List<Integer>> graph, int s, int t, List<Integer> path, Set<Integer> brokenEdges) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> prev = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(s);
        visited.add(s);

        while (!queue.isEmpty()) {
            int u = queue.poll();

            if (u == t) {
                int current = t;
                while (current != s) {
                    path.add(current);
                    current = prev.get(current);
                }
                path.add(s);
                Collections.reverse(path);
                return true;
            }

            for (int v : graph.get(u)) {
                if (!visited.contains(v) && !brokenEdges.contains(v)) {
                    queue.add(v);
                    visited.add(v);
                    prev.put(v, u);
                }
            }
        }
        return false;
    }

    private static void replanBusinesses(List<List<Integer>> graph, List<Edge> edges, List<Business> businesses, Set<Integer> brokenEdges) {
        List<int[]> replannedBusinesses = new ArrayList<>();

        for (Business business : businesses) {
            List<Integer> newPath = new ArrayList<>();
            if (bfsFindPath(graph, business.s, business.t, newPath, brokenEdges)) {
                replannedBusinesses.add(new int[]{business.id, newPath.size() - 1});
                System.out.print(business.id + " " + (newPath.size() - 1));
                for (int i = 1; i < newPath.size(); i++) {
                    System.out.print(" " + (newPath.get(i - 1) + 1) + " " + (newPath.get(i) + 1));
                }
                System.out.println();
            }
        }
    }
}


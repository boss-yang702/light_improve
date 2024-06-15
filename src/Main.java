import java.util.*;

class Edge {//边
    int u, v;
    Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }
}

class Business {//业务
    int id, Src, Snk, S, L, R, V;
    List<Integer> path;
    Business(int id, int Src, int Snk, int S, int L, int R, int V, List<Integer> path) {
        this.id = id;
        this.Src = Src;
        this.Snk = Snk;
        this.S = S;
        this.L = L;
        this.R = R;
        this.V = V;
        this.path = path;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取输入数据
        int n = scanner.nextInt(); // 节点数
        int m = scanner.nextInt(); // 边数
        int[] maxChangeTimes = new int[n]; // 每个节点的变通道次数
        for (int i = 0; i < n; i++) {
            maxChangeTimes[i] = scanner.nextInt();
        }

        List<Edge> edges = new ArrayList<>(); // 编号ek表示edges(uk,vk) 0<=ek<=M-1
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
        for (int i = 0; i < T; i++) {
            List<Integer> scenario = new ArrayList<>();
            while (true) {
                int e = scanner.nextInt(); // 发生中断的边的编号
                if (e == 0) break;
                scenario.add(e - 1); // 转换为0开始的编号

                // 在每次读取故障边时，立即重新解析图并重新规划涉及的业务路径并输出结果
                Set<Integer> brokenEdges = new HashSet<>(scenario);
                List<List<Integer>> graph = buildGraph(n, edges, brokenEdges);
                replanBusinesses(graph, businesses);
            }
        }

        scanner.close();
    }

    private static List<List<Integer>> buildGraph(int n, List<Edge> edges, Set<Integer> brokenEdges) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.size(); i++) {
            if (!brokenEdges.contains(i)) {
                Edge edge = edges.get(i);
                graph.get(edge.u).add(edge.v);
                graph.get(edge.v).add(edge.u);
            }
        }
        return graph;
    }

    private static boolean bfsFindPath(List<List<Integer>> graph, int s, int t, List<Integer> path) {
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
                if (!visited.contains(v)) {
                    queue.add(v);
                    visited.add(v);
                    prev.put(v, u);
                }
            }
        }
        return false;
    }

    private static void replanBusinesses(List<List<Integer>> graph, List<Business> businesses) {
        for (Business business : businesses) {
            List<Integer> newPath = new ArrayList<>();
            if (bfsFindPath(graph, business.Src, business.Snk, newPath)) {
                System.out.print(business.id + " " + (newPath.size() - 1));
                for (int i = 1; i < newPath.size(); i++) {
                    System.out.print(" " + (newPath.get(i - 1) + 1) + " " + (newPath.get(i) + 1));
                }
                System.out.println();
            }
        }
    }
}

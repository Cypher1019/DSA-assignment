package Question5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a solution to find the impacted network devices
 * due to a power outage on a target device.
 */
public class ImpactedDevicesFinder {

    /**
     * Finds the impacted network devices due to a power outage on the target device.
     *
     * @param connections    The 2D array representing network connections between devices.
     * @param targetDevice   The device where the power failure occurred.
     * @return               The list of impacted network devices.
     */
    public static List<Integer> findImpactedDevices(int[][] connections, int targetDevice) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        // Build the graph and calculate in-degree of each node
        for (int[] connection : connections) {
            int from = connection[0];
            int to = connection[1];
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Perform DFS starting from the target device
        List<Integer> result = new ArrayList<>();
        dfs(graph, inDegree, targetDevice, targetDevice, result);

        return result;
    }

    /**
     * Performs depth-first search (DFS) to find impacted devices.
     *
     * @param graph       The graph representing network connections.
     * @param inDegree    The in-degree of each node in the graph.
     * @param node        The current node being explored.
     * @param target      The target device where the power failure occurred.
     * @param result      The list to store impacted devices.
     */
    private static void dfs(Map<Integer, List<Integer>> graph, Map<Integer, Integer> inDegree,
                            int node, int target, List<Integer> result) {
        // If the current node has no incoming edges other than from the target node,
        // add it to the result
        if (inDegree.getOrDefault(node, 0) == 1 && node != target) {
            result.add(node);
        }

        // Recursively explore the children of the current node
        if (graph.containsKey(node)) {
            for (int child : graph.get(node)) {
                dfs(graph, inDegree, child, target, result);
            }
        }
    }

    /**
     * Main method to execute the program.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        int[][] connections = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 6 }, { 2, 4 }, { 4, 6 }, { 4, 5 }, { 5, 7 } };
        int targetDevice = 4;

        List<Integer> impactedDevices = findImpactedDevices(connections, targetDevice);

        System.out.print("Impacted devices due to power failure on device " + targetDevice + ": {");
        for (int i = 0; i < impactedDevices.size(); i++) {
            System.out.print(impactedDevices.get(i));
            if (i < impactedDevices.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
}

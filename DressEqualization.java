package Question2;

public class DressEqualization {

   
    public static int calculateMinMoves(int[] machines) {
        int totalDresses = 0; // Total number of dresses in all machines
        for (int dresses : machines) {
            totalDresses += dresses;
        }

        int numMachines = machines.length; // Number of sewing machines
        if (totalDresses % numMachines != 0) {
            // If total number of dresses is not divisible by number of machines, equalization is not possible
            return -1;
        }

        int targetDresses = totalDresses / numMachines; // Target number of dresses per machine
        int minMoves = 0; // Minimum moves needed to equalize dresses
        int balance = 0; // Balance of dresses across machines

        // Traverse through each machine
        for (int dressCount : machines) {
            // Calculate the balance after processing this machine
            balance += dressCount - targetDresses;
            // Update the maximum imbalance found so far (minimum moves needed)
            minMoves = Math.max(minMoves, Math.max(Math.abs(balance), dressCount - targetDresses));
        }

        return minMoves;
    }

    public static void main(String[] args) {
        int[] machines = {2, 1, 3, 0, 2};
        int moves = calculateMinMoves(machines);
        System.out.println("Minimum moves required to equalize dresses: " + moves);
    }
}

package Question1;

import java.util.Arrays;

public class EngineConstruction {

    public static int calculateMinimumConstructionTime(int[] engineDurations, int splitTimeCost) {
        Arrays.sort(engineDurations);

        int totalEngines = engineDurations.length;
        int totalTime = 0;

        for (int i = 0; i < totalEngines; i++) {
            if (i == 0) {
                // First split, no cost incurred
                totalTime += engineDurations[i];
            } else {
                // Subsequent splits, incur the split cost
                totalTime += Math.min(splitTimeCost, engineDurations[i]);
            }
        }

        return totalTime;
    }

    public static void main(String[] args) {
        int[] engineDurations = {3, 4, 5, 2};
        int splitTimeCost = 2;
        System.out.println("Minimum time needed to construct all engines: " + calculateMinimumConstructionTime(engineDurations, splitTimeCost));
    }
}

package Question2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecretSharingSolver {

    /**
     * Finds the set of individuals who will eventually know the secret after all the possible secret-sharing intervals have occurred.
     *
     * @param numIndividuals    The total number of individuals.
     * @param secretIntervals   The intervals during which the secret can be shared.
     * @param firstIndividual   The individual who initially possesses the secret.
     * @return The list of individuals who eventually know the secret.
     */
    public static List<Integer> findPeopleWhoKnowSecret(int numIndividuals, int[][] secretIntervals, int firstIndividual) {
        Set<Integer> knowsSecret = new HashSet<>();
        knowsSecret.add(firstIndividual);

        boolean updated;
        do {
            updated = false;
            for (int[] interval : secretIntervals) {
                int start = interval[0];
                int end = interval[1];
                for (int i = start; i <= end; i++) {
                    if (knowsSecret.contains(i)) {
                        for (int j = start; j <= end; j++) {
                            if (!knowsSecret.contains(j)) {
                                knowsSecret.add(j);
                                updated = true;
                            }
                        }
                        break;
                    }
                }
            }
        } while (updated);

        return new ArrayList<>(knowsSecret);
    }

    public static void main(String[] args) {
        int numIndividuals = 5;
        int[][] secretIntervals = {{0, 2}, {1, 3}, {2, 4}};
        int firstIndividual = 0;

        List<Integer> result = findPeopleWhoKnowSecret(numIndividuals, secretIntervals, firstIndividual);
        System.out.println("People who eventually know the secret: " + result);
    }
}

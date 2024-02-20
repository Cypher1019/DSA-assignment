package Question1;

public class VenueThemeMinimumCostCalculator {
    // Function to calculate the minimum cost to decorate venues with different themes
    public int calculateMinimumCost(int[][] costMatrix) {
        // Check if the cost matrix is valid
        if (costMatrix == null || costMatrix.length == 0)
            return 0;

        // Number of venues
        int numOfVenues = costMatrix.length;
        // Number of themes
        int numOfThemes = costMatrix[0].length;
        // Dynamic programming table to store minimum costs
        int[][] dp = new int[numOfVenues][numOfThemes];

        // Initialize the first row of the dp table with the costs of decorating the first venue
        for (int theme = 0; theme < numOfThemes; theme++) {
            dp[0][theme] = costMatrix[0][theme];
        }

        // Dynamic programming to compute the minimum cost
        for (int venue = 1; venue < numOfVenues; venue++) {
            for (int theme = 0; theme < numOfThemes; theme++) {
                // Initialize the minimum cost for the current venue and theme
                dp[venue][theme] = Integer.MAX_VALUE;
                // Iterate over previous themes and choose the minimum cost
                for (int prevTheme = 0; prevTheme < numOfThemes; prevTheme++) {
                    // Ensure adjacent venues have different themes
                    if (theme != prevTheme) {
                        // Update the minimum cost for the current venue and theme
                        dp[venue][theme] = Math.min(dp[venue][theme], dp[venue - 1][prevTheme] + costMatrix[venue][theme]);
                    }
                }
            }
        }

        // Find the minimum cost from the last row of the dp table
        int resultMinimumCost = Integer.MAX_VALUE;
        for (int theme = 0; theme < numOfThemes; theme++) {
            resultMinimumCost = Math.min(resultMinimumCost, dp[numOfVenues - 1][theme]);
        }

        return resultMinimumCost;
    }

    public static void main(String[] args) {
        // Create an instance of the VenueThemeMinimumCostCalculator
        VenueThemeMinimumCostCalculator costCalculator = new VenueThemeMinimumCostCalculator();
        // Example cost matrix
        int[][] costMatrix = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        // Calculate and print the minimum cost
        System.out.println("Minimum cost: " + costCalculator.calculateMinimumCost(costMatrix));
    }
}

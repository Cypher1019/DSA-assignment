package Question5;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents an Ant Colony Optimization algorithm for solving the Traveling Salesman Problem.
 */
class AntColonyTSPMainr {
    private int[][] distanceMatrix;
    private int numAnts;
    private double[][] pheromoneMatrix;
    private double[][] probabilities;
    private int numCities;
    private int[] bestTour;
    private int bestTourLength;
    private double evaporationRate;
    private double alpha;
    private double beta;

    /**
     * Constructor to initialize the AntColonyTSPSolver.
     * 
     * @param distanceMatrix    The distance matrix representing distances between cities.
     * @param numAnts           The number of ants in the colony.
     * @param evaporationRate   The rate at which pheromones evaporate.
     * @param alpha             The alpha parameter controlling the influence of pheromones.
     * @param beta              The beta parameter controlling the influence of distances.
     */
    public AntColonyTSPMainr(int[][] distanceMatrix, int numAnts, double evaporationRate, double alpha, double beta) {
        this.distanceMatrix = distanceMatrix;
        this.numAnts = numAnts;
        this.evaporationRate = evaporationRate;
        this.alpha = alpha;
        this.beta = beta;
        this.numCities = distanceMatrix.length;
        this.pheromoneMatrix = new double[numCities][numCities];
        this.probabilities = new double[numCities][numCities];
        initializePheromones();
    }

    /**
     * Initializes the pheromone matrix with initial pheromone values.
     */
    private void initializePheromones() {
        double initialPheromone = 1.0 / numCities;
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j) {
                    pheromoneMatrix[i][j] = initialPheromone;
                }
            }
        }
    }

    /**
     * Solves the TSP problem using Ant Colony Optimization.
     * 
     * @param maxIterations The maximum number of iterations to run the algorithm.
     */
    public void solve(int maxIterations) {
        bestTourLength = Integer.MAX_VALUE;
        bestTour = new int[numCities];
        Random random = new Random();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (int ant = 0; ant < numAnts; ant++) {
                boolean[] visited = new boolean[numCities];
                int[] tour = new int[numCities];
                int currentCity = random.nextInt(numCities);
                tour[0] = currentCity;
                visited[currentCity] = true;

                for (int i = 1; i < numCities; i++) {
                    calculateProbabilities(currentCity, visited);
                    int nextCity = selectNextCity(currentCity);
                    tour[i] = nextCity;
                    visited[nextCity] = true;
                    currentCity = nextCity;
                }

                int tourLength = calculateTourLength(tour);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = tour;
                }
            }

            updatePheromones();
        }
    }

    /**
     * Calculates the probabilities of selecting each city as the next destination for an ant.
     * 
     * @param city      The current city.
     * @param visited   An array indicating which cities have been visited.
     */
    private void calculateProbabilities(int city, boolean[] visited) {
        double total = 0.0;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                probabilities[city][i] = Math.pow(pheromoneMatrix[city][i], alpha) *
                        Math.pow(1.0 / distanceMatrix[city][i], beta);
                total += probabilities[city][i];
            } else {
                probabilities[city][i] = 0.0;
            }
        }

        for (int i = 0; i < numCities; i++) {
            probabilities[city][i] /= total;
        }
    }

    /**
     * Selects the next city based on the calculated probabilities.
     * 
     * @param city  The current city.
     * @return      The index of the selected next city.
     */
    private int selectNextCity(int city) {
        double[] probabilities = this.probabilities[city];
        double r = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < numCities; i++) {
            cumulativeProbability += probabilities[i];
            if (r <= cumulativeProbability) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Updates the pheromone levels on the edges based on the best tour found.
     */
    private void updatePheromones() {
        // Evaporation
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] *= (1.0 - evaporationRate);
            }
        }
        // Add new pheromones
        for (int ant = 0; ant < numAnts; ant++) {
            for (int i = 0; i < numCities - 1; i++) {
                int city1 = bestTour[i];
                int city2 = bestTour[i + 1];
                pheromoneMatrix[city1][city2] += (1.0 / bestTourLength);
                pheromoneMatrix[city2][city1] += (1.0 / bestTourLength);
            }
        }
    }

    /**
     * Calculates the total length of a tour.
     * 
     * @param tour  The tour represented as an array of city indices.
     * @return      The total length of the tour.
     */
    private int calculateTourLength(int[] tour) {
        int length = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            length += distanceMatrix[tour[i]][tour[i + 1]];
        }
        length += distanceMatrix[tour[tour.length - 1]][tour[0]]; // Return to the starting city
        return length;
    }

    /**
     * Gets the length of the best tour found.
     * 
     * @return The length of the best tour.
     */
    public int getBestTourLength() {
        return bestTourLength;
    }

    /**
     * Gets the array representing the best tour found.
     * 
     * @return The array representing the best tour.
     */
    public int[] getBestTour() {
        return bestTour;
    }
}

/**
 * Main class to test the AntColonyTSPSolver.
 */
public class AntColonyTSPMain {
    public static void main(String[] args) {
        int[][] distanceMatrix = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        int numAnts = 5;
        double evaporationRate = 0.5;
        double alpha = 1.0;
        double beta = 2.0;

        AntColonyTSPMainr colony = new AntColonyTSPMainr(distanceMatrix, numAnts, evaporationRate, alpha, beta);
        colony.solve(1000); // Solve TSP with 1000 iterations

        int[] bestTour = colony.getBestTour();
        int bestTourLength = colony.getBestTourLength();

        System.out.println("Best tour: " + Arrays.toString(bestTour));
        System.out.println("Best tour length: " + bestTourLength);
    }
}

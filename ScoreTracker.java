package Question3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreTracker {
    private List<Double> scoreList;

    /**
     * Initializes a new ScoreTracker object.
     */
    public ScoreTracker() {
        scoreList = new ArrayList<>();
    }

    /**
     * Adds a new assignment score to the data stream.
     *
     * @param newScore The new assignment score to be added.
     */
    public void addScore(double newScore) {
        scoreList.add(newScore);
    }

    /**
     * Calculates the median of all the assignment scores in the data stream.
     * If the number of scores is even, the median is the average of the two middle scores.
     *
     * @return The median score.
     */
    public double getMedianScore() {
        List<Double> sortedScores = new ArrayList<>(scoreList);
        Collections.sort(sortedScores);
        int size = sortedScores.size();
        if (size == 0) {
            return 0; // No scores available
        } else if (size % 2 == 0) {
            // If number of scores is even
            int mid = size / 2;
            return (sortedScores.get(mid - 1) + sortedScores.get(mid)) / 2.0; // Average of two middle scores
        } else {
            // If number of scores is odd
            return sortedScores.get(size / 2); // Middle score
        }
    }

    public static void main(String[] args) {
        ScoreTracker scoreTracker = new ScoreTracker();
        scoreTracker.addScore(85.5); // Stream: [85.5]
        scoreTracker.addScore(92.3); // Stream: [85.5, 92.3]
        scoreTracker.addScore(77.8); // Stream: [85.5, 92.3, 77.8]
        scoreTracker.addScore(90.1); // Stream: [85.5, 92.3, 77.8, 90.1]
        double median1 = scoreTracker.getMedianScore(); // Output: 87.8
        System.out.println("Median 1: " + median1);

        scoreTracker.addScore(81.2); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2]
        scoreTracker.addScore(88.7); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2, 88.7]
        double median2 = scoreTracker.getMedianScore(); // Output: 87.1
        System.out.println("Median 2: " + median2);
    }
}

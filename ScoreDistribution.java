import java.util.*;

public class ScoreDistribution {
    public ArrayList<Double> scores = new ArrayList<>();
    public int numberOfScores = 0;

    public ScoreDistribution(ArrayList<Double> sco) {
        for (Double i : sco) {
            scores.add(i);
        }
        numberOfScores = scores.size();
    }

    public double mean() {
        double sum = 0;
        for (Double i : scores) {
            sum += i;
        }
        return sum / scores.size();
    }

    public double median() {
        ArrayList<Double> sortedScores = new ArrayList<>(scores);
        Collections.sort(sortedScores);
        int mid = sortedScores.size() / 2;
        if (sortedScores.size() % 2 == 0) {
            return (sortedScores.get(mid - 1) + sortedScores.get(mid)) / 2.0;
        } else {
            return sortedScores.get(mid);
        }
    }

    public double mode() {
        HashMap<Double, Integer> freqMap = new HashMap<>();
        for (Double score : scores) {
            freqMap.put(score, freqMap.getOrDefault(score, 0) + 1);
        }

        int maxFreq = 0;
        double mode = scores.get(0);
        for (Map.Entry<Double, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                mode = entry.getKey();
            }
        }
        return mode;
    }

    public double highestScore() {
        return Collections.max(scores);
    }

    public double lowestScore() {
        return Collections.min(scores);
    }

    public double standardDeviation() {
        double mean = mean();
        double sum = 0;
        for (Double score : scores) {
            sum += Math.pow(score - mean, 2);
        }
        return Math.sqrt(sum / scores.size());
    }

    public Map<String, Integer> gradeDistribution() {
        Map<String, Integer> grades = new HashMap<>();
        grades.put("A", 0);
        grades.put("B", 0);
        grades.put("C", 0);
        grades.put("D", 0);
        grades.put("F", 0);

        for (Double score : scores) {
            if (score >= 90) grades.put("A", grades.get("A") + 1);
            else if (score >= 80) grades.put("B", grades.get("B") + 1);
            else if (score >= 70) grades.put("C", grades.get("C") + 1);
            else if (score >= 60) grades.put("D", grades.get("D") + 1);
            else grades.put("F", grades.get("F") + 1);
        }

        return grades;
    }

    public int countAbove(double threshold) {
        int count = 0;
        for (Double score : scores) {
            if (score > threshold) {
                count++;
            }
        }
        return count;
    }

    public int countBelow(double threshold) {
        int count = 0;
        for (Double score : scores) {
            if (score < threshold) {
                count++;
            }
        }
        return count;
    }

    public double percentagePassing(double passingScore) {
        int passingCount = 0;
        for (Double score : scores) {
            if (score >= passingScore) {
                passingCount++;
            }
        }
        return (passingCount * 100.0) / scores.size();
    }
}
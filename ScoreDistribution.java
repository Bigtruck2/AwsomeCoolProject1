import java.util.ArrayList;
public class ScoreDistribution {
    public ArrayList<Integer> scores = new ArrayList<>();
    public int numberOfScores = 0;

    public ScoreDistribution(ArrayList<Integer> sco){
        for (Integer i : sco){
            scores.add(i);
        }
        numberOfScores = scores.size();
    }

    public int classMean(ArrayList<Integer> scores) {
        int sum = 0;
        for (Integer i : scores) {
            sum += i;
        }
        return sum / scores.size();
    }
}

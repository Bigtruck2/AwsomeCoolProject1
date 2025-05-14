import java.util.ArrayList;
public class ScoreDistribution {
    public ArrayList<Score> Scores = new ArrayList<>();
    public int numberOfScores = 0;

    public ScoreDistribution(Score[] sco){
        for (Score i : sco){
            Scores.add(i);
        }
        numberOfScores = Scores.size();
    }

    public int classMean(){
    }
}

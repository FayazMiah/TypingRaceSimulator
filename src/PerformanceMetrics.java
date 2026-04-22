import java.util.ArrayList;
import java.util.HashMap;

public class PerformanceMetrics {

    public static double getWPM(Typist typist, long unixTimeStart) {
        double progress = (double) typist.getProgress();
        long currentUnixTime = System.currentTimeMillis();
        double deltaT = (double) (currentUnixTime - unixTimeStart) / 60000.0;
        double WPM = (progress / deltaT) / 5; // division by 5 to get average word length, not characters
        return WPM;
    }

    public static int getAccuracyPercentage(Typist typist) {
        double numberOfMistypes = (double) typist.getNumberOfMistypes();
        double numberOfProgressions = (double) typist.getNumberOfProgressions();
        //System.out.println("PROGESSIONS: " + numberOfProgressions + " MISTYPES:" + numberOfMistypes);
        double total = numberOfProgressions + numberOfMistypes;
        if (total == 0.0) {
            return 100;
        }
        int p = (int) Math.round((numberOfProgressions / total) * 100);
        if (p > 100) {
            p = 100;
        }
        return p;
    }

    public static int getBurnoutCount(Typist typist) {
        return typist.getBurnoutCount();
    }


    public static int determinePosition(Typist typist, ArrayList<Typist> typists) {
        //
        int position = 1;
        for (Typist other : typists) {
            if (other.getProgress() > typist.getProgress()) {
                position++;
            }
        }
        return position;
    }

    public static HashMap<String, Double> createRaceData(Typist typist, ArrayList<Typist> typists) {
        //highestWPM refers to that independant race only, not their highest over all races
        Double highestWPM = typist.getRaceBestWPM();
        Double burnoutCount = (double) getBurnoutCount(typist);
        Double accPercent = (double) getAccuracyPercentage(typist);
        Double position = (double) determinePosition(typist, typists);
        HashMap<String, Double> data = new HashMap<>();
        data.put("WPMReached", highestWPM);
        data.put("PositionPlaced", position);
        data.put("BurnoutCount", burnoutCount);
        data.put("AdvanceAccuracy", accPercent);
        return data;
    }
}

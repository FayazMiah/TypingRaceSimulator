import java.util.*;
import javax.swing.Timer;

public class TypingRace {

    private Passage passage;
    private int passageLength;
    private ArrayList<Typist> typists;

    private static final double MISTYPE_BASE_CHANCE = 0.2;
    private static final int SLIDE_BACK_AMOUNT = 2;
    private static final int BURNOUT_DURATION = 3;

    private boolean autocorrect;
    private boolean caffeine;
    private boolean nightShift;

    public static ArrayList<HashMap<Typist, HashMap<String, Double>>> globalRaceData = new ArrayList<>();
    private HashMap<Typist, HashMap<String, Double>> raceData = new HashMap<>();
    private static HashMap<Typist, Double> bestWPMs = new HashMap<>();

    public String gameState = "RUNNING";

    private long startUnixTime;

    private Timer gameTimer;

    public TypingRace(Passage passage, boolean autocorrect, boolean caffeine, boolean nightShift) {
        this.passage = passage;
        this.passageLength = passage.getLength();

        this.autocorrect = autocorrect;
        this.caffeine = caffeine;
        this.nightShift = nightShift;

        this.typists = new ArrayList<>();
    }

    public void addTypist(Typist t) {
        if (typists.size() >= 6) return;
        typists.add(t);
    }

    //start race (gui orientated)
    public void startRace(RaceRenderer renderer) {

        this.startUnixTime = System.currentTimeMillis();

        // reset all typists and check for existing pb
        for (Typist t : typists) {
            t.resetToStart();

            Double wpm = bestWPMs.get(t);
            if (wpm == null) {
                bestWPMs.put(t, 0.0); //may not be needed (delete if so)
            }

        }

        // game loop (without yieldign)
        gameTimer = new Timer(120, e -> {

            if (gameState.equals("FINISHED") || gameTimer == null) {
                return;
            }

            // advance typists
            for (Typist t : typists) {
                advanceTypist(t);
            }
            Typist winner = null;

            for (Typist t : typists) {
                if (raceFinishedBy(t)) {
                    winner = t;
                    this.gameState = "FINISHED";
                    gameTimer.stop();
                    renderer.renderResults(this, typists, winner);
                    //System.out.println("FINISHED RACEsadadsf");
                    break;
                }
            }

            // render ui
            if (this.gameState.equals("RUNNING")) {
                renderer.render(this, typists, passage);
            }

            /*
            if (winner != null) {
                gameTimer.stop();
                return;
                //System.out.println("Winner: " + winner.getName());
            }*/

            for (Typist t : typists) { // put racedata of each typist into the TypingRace instance racedata
                HashMap<String, Double> data = PerformanceMetrics.createRaceData(t, typists);
                raceData.put(t, data);
            }
            globalRaceData.add(raceData); //also put it in the class to store over all races played.
        });

        //System.out.println("starting gametimer");
        gameTimer.start();
    }

    // attempt to progress typist
    private void advanceTypist(Typist t) {

        double effectiveAccuracy = EffectiveModifiers.calculateEffectiveAccuracy(t, caffeine, nightShift, passageLength);
        double effectiveMistypeChance = EffectiveModifiers.calculateEffectiveMistypeChance(t);
        double effectiveSlideBackMult = EffectiveModifiers.calculateEffectiveSlideBackMult(t, autocorrect);
        int extendedBurnoutDuration = EffectiveModifiers.calculateExtendedBurnoutDuration(t);

        t.effectiveAccuracy = effectiveAccuracy; //needed to prevent altering the typist attribute accuracy to keep it protected

        if (t.isBurntOut()) {
            t.recoverFromBurnout();
            return;
        }

        // advance progress
        if (Math.random() < effectiveAccuracy) {
            t.typeCharacter();
        }
        else if (Math.random() < (1 - effectiveAccuracy) * MISTYPE_BASE_CHANCE * effectiveMistypeChance) { // mistype

            //mark positions of mistypes in the hashset
            int pos = t.getProgress();
            t.addMistake(pos);
            t.slideBack((int) Math.round(SLIDE_BACK_AMOUNT * effectiveSlideBackMult));
        }

        // burnout
        if (Math.random() < 0.05 * effectiveAccuracy * effectiveAccuracy) {
            t.burnOut(BURNOUT_DURATION + extendedBurnoutDuration);
        }

        if (t.getProgress() > 2) {
            Double currentwpm = PerformanceMetrics.getWPM(t, this.startUnixTime);
            Double highestwpm = bestWPMs.get(t);
            double raceHighestwpm = t.getRaceBestWPM();
            if (highestwpm == null || currentwpm > highestwpm) {
                bestWPMs.put(t, currentwpm);
            }
            if (currentwpm > raceHighestwpm) {
                t.setRaceBestWPM(currentwpm);
            }
            //System.out.println("CURRENT HIGH:: " + highestwpm + "CURRENT: " + currentwpm);
        }


        //setRaceBestWPM(double wpm)
    }

    private boolean raceFinishedBy(Typist t) {
        return t.getProgress() >= passageLength;
    }

    // getters for UI
    public ArrayList<Typist> getTypists() {
        return typists;
    }

    public Passage getPassage() {
        return passage;
    }

    // other getters

    public long getStartTime() {
        return this.startUnixTime;
    }

    public HashMap<Typist, HashMap<String, Double>> getRaceData () {
        return this.raceData;
    }
    public void resetRace() {
        if (gameTimer != null) {
            gameTimer.stop();
            gameTimer = null;
        }
        this.gameState = "RUNNING";
        this.startUnixTime = 0;
        this.raceData.clear();

        for (Typist t : typists) {
            t.resetToStart();
            t.setRaceBestWPM(0);
            if (!bestWPMs.containsKey(t)) {
                bestWPMs.put(t, 0.0);
            }
        }

    }

}
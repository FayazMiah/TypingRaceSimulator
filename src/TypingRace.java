import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class TypingRace
{
    private Passage passage;
    private int passageLength;
    private ArrayList<Typist> typists;

    // game tuning
    private static final double MISTYPE_BASE_CHANCE = 0.2;
    private static final int SLIDE_BACK_AMOUNT = 2;
    private static final int BURNOUT_DURATION = 3;
    private boolean autocorrect = false;
    private boolean caffeine = false;
    private boolean nightShift = false;


    public TypingRace(Passage passage, boolean autocorrect, boolean caffeiene, boolean nightShift)
    {
        this.passage = passage;
        this.passageLength = passage.getLength();

        this.autocorrect = autocorrect;
        this.caffeine = caffeiene;
        this.nightShift = nightShift;

        typists = new ArrayList<>();
    }

    public static void main(String[] args) {
        TypingRace race = new TypingRace(PassageController.createPassage("short"), false, false, false);
        race.addTypist(new Typist('①', "TURBOFINGERS", 0.85));
        race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60));
        race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30));
        race.startRace();
    }

    // adds a typist max of 6)
    public void addTypist(Typist theTypist)
    {
        if (typists.size() >= 6)
        {
            System.out.println("Cannot add more than 6 typists.");
            return;
        }
        typists.add(theTypist);
    }

    //starts the race, only when there is atleast 2 players
    public void startRace()
    {
        if (typists.size() < 2) {
            System.out.println("Need at least 2 typists to start the race.");
            return;
        }

        boolean finished = false;
        Typist winner = null;

        // reset all typists
        for (Typist t : typists) {
            t.resetToStart();
            if (this.nightShift) {
                t.setAccuracy(t.getAccuracy() * 0.9); // reduce by 10%
            }
        }

        while (!finished) {
            // advance each typist
            for (Typist t : typists) {
                advanceTypist(t);
            }

            // print race state
            printRace();

            // check winner
            for (Typist t : typists) {
                if (raceFinishedBy(t)) {
                    winner = t;
                    finished = true;
                    break;
                }
            }

            // delay
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }

        // print the winner
        if (winner != null) {
            System.out.println("And the winner is ... " + winner.getName());

            double oldAcc = winner.getAccuracy();
            winner.setAccuracy(oldAcc + 0.02);

            System.out.println("Final Accuracy: " + winner.getAccuracy() + " (Improved from " + oldAcc + ")");
        }
    }

    //advance a given argument typist
    private void advanceTypist(Typist theTypist) {

        if (this.caffeine && theTypist.getProgress() < 10 && !theTypist.caffeineGiven) {
            theTypist.setAccuracy(theTypist.getAccuracy() * 1.5);
            theTypist.caffeineGiven = true;
        }

        if (this.caffeine && theTypist.getProgress() >= 10 && theTypist.caffeineGiven) {
            theTypist.setAccuracy(theTypist.getAccuracy() / 1.5 * 0.8);
            theTypist.caffeineGiven = false;
        }

        if (theTypist.isBurntOut()) {
            theTypist.recoverFromBurnout();
            return;
        }

        // successful typing
        if (Math.random() < theTypist.getAccuracy()) {
            theTypist.typeCharacter();
        }
        // mistype
        else if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE) {
            if (this.autocorrect) {
                theTypist.slideBack(SLIDE_BACK_AMOUNT/2);
            } else {
                theTypist.slideBack(SLIDE_BACK_AMOUNT);
            }
        }

        // burnout
        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy()) {
            theTypist.burnOut(BURNOUT_DURATION);
        }
    }

    //check for win
    private boolean raceFinishedBy(Typist theTypist) {
        return theTypist.getProgress() >= passageLength;
    }

    // print race current state
    private void printRace() {
        System.out.print('\u000C'); // Clear screen

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        for (Typist t : typists) {
            printSeat(t);
            System.out.println();
        }

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [zz] = burnt out    [<] = just mistyped");
    }

    //print state of single typist
    private void printSeat(Typist theTypist)
    {
        if (theTypist == null) return;

        int progress = theTypist.getProgress();
        int spacesBefore = progress;
        int spacesAfter = Math.max(0, passageLength - progress);

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        System.out.print(theTypist.getSymbol());

        if (theTypist.isBurntOut()) {
            System.out.print("[zz]");
            spacesAfter -= 4;
        }
        else if (theTypist.getMistyped()) {
            System.out.print("<");
            theTypist.setMistyped(false);
            spacesAfter -= 1;
        }

        multiplePrint(' ', Math.max(0, spacesAfter));
        System.out.print('|');

        // info display
        if (theTypist.isBurntOut())
        {
            System.out.print(theTypist.getName() + " (Accuracy: " + theTypist.getAccuracy() + ")" + " BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }
        else
        {
            System.out.print(theTypist.getName() + " (Accuracy: " + theTypist.getAccuracy() + ")");
        }
    }

    private void multiplePrint(char aChar, int times)
    {
        for (int i = 0; i < times; i++)
        {
            System.out.print(aChar);
        }
    }
}
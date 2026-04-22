import java.awt.*;
import java.util.HashSet;

/**
 * Write a description of class Typist here.//
 *
 * Starter code generously abandoned by Ty Posaurus, your predecessor,
 * who typed with two fingers and considered that "good enough".
 * He left a sticky note: "the slide-back thing is optional probably".
 * It is not optional. Good luck.
 *
 * @author Fayaz Miah
 * @version (a version number or a date)
 */

public class Typist {
    // Fields of class Typist
    // Hint: you will need six fields. Think carefully about their types.

    private String typistName;
    private char typistSymbol;
    private int progress;
    private boolean burntOut;
    private int burntOutTurns;
    private double accuracy;
    private String style;
    public double effectiveAccuracy; // doesnt require private modification as it does not change behaviour
    private boolean mistyped = false;
    private String keyboardType;
    private String[] accessories;
    private Color colour;
    private int numberOfMistypes = 0;
    private int numberOfProgressions = 0;
    private int burnoutCount = 0;
    private double raceBestWPM = 0;

    private HashSet<Integer> mistypedPositions = new HashSet<>();


    // One of them tracks how far along the passage the typist has reached.
    // Another tracks whether the typist is currently burnt out.
    // A third tracks HOW MANY turns of burnout remain (not just whether they are burnt out).
    // The remaining three should be fairly obvious.

    // Constructor of class Typist
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given symbol, name, and accuracy rating.
     *
     * @param typistSymbol  a single Unicode character representing this typist (e.g. '①', '②', '③')
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public Typist(char typistSymbol, String typistName, double typistAccuracy, String style, String keyboardType, Color colour, String[] accessories) {
        this.typistSymbol = typistSymbol;
        this.typistName = typistName;
        this.style = style;
        this.colour = colour;
        this.accessories = accessories;
        this.keyboardType = keyboardType;

        this.progress = 0;
        this.burntOut = false;
        this.burntOutTurns = 0;

        if (typistAccuracy < 0) {
            this.accuracy = 0;
        } else if (typistAccuracy > 1) {
            this.accuracy = 1;
        } else {
            this.accuracy = typistAccuracy;
        }
    }


    // Methods of class Typist

    /**
     * Sets this typist into a burnout state for a given number of turns.
     * A burnt-out typist cannot type until their burnout has worn off.
     *
     * @param turns the number of turns the burnout will last
     */


    public void burnOut(int turns)
    {
        this.burntOut = true;
        this.burntOutTurns = turns;
        this.burnoutCount += 1;
    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
     */
    public void recoverFromBurnout()
    {
        if (this.burntOut) {
            this.burntOutTurns--;
            if (this.burntOutTurns <= 0) {
                this.burntOut = false;
                this.burntOutTurns = 0;
            }
        }
    }

    /**
     * Returns the typist's accuracy rating.
     *
     * @return accuracy as a double between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return this.accuracy;
    }

    /**
     * Returns the typist's current progress through the passage.
     * Progress is measured in characters typed correctly so far.
     * Note: this value can decrease if the typist mistypes.
     *
     * @return progress as a non-negative integer
     */
    public int getProgress()
    {
        return this.progress;
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return this.typistName;
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public char getSymbol()
    {
        return this.typistSymbol;
    }

    /**
     * Returns the number of turns of burnout remaining.
     * Returns 0 if the typist is not currently burnt out.
     *
     * @return burnout turns remaining as a non-negative integer
     */
    public int getBurnoutTurnsRemaining()
    {
        return this.burntOutTurns;
    }

    /**
     * Resets the typist to their initial state, ready for a new race.
     * Progress returns to zero, burnout is cleared entirely.
     */
    public void resetToStart()
    {
        this.progress = 0;
        this.burntOut = false;
        this.burntOutTurns = 0;
        this.numberOfMistypes = 0;
        this.numberOfProgressions = 0;
        this.burnoutCount = 0;
        this.raceBestWPM = 0;
        this.mistypedPositions = new HashSet<>();
    }

    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        return this.burntOut;
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter() {
        if (!isBurntOut()) {
            this.progress++;
            this.numberOfProgressions++;
        }
    }

    /**
     * Moves the typist backwards by a given number of characters (a mistype).
     * Progress cannot go below zero — the typist cannot slide off the start.
     *
     * @param amount the number of characters to slide back (must be positive)
     */
    public void slideBack(int amount)
    {
        if (getProgress() - amount < 0) {
            this.progress = 0;
        } else {
            this.progress -= amount;
        }
        this.numberOfMistypes += 1;
        this.setMistyped(true);
    }

    /**
     * Sets the accuracy rating of the typist.
     * Values below 0.0 should be set to 0.0; values above 1.0 should be set to 1.0.
     *
     * @param newAccuracy the new accuracy rating
     */
    public void setAccuracy(double newAccuracy)
    {
        if (newAccuracy < 0) {
            this.accuracy = 0;
        } else if (newAccuracy > 1) {
            this.accuracy = 1;
        } else {
            this.accuracy = newAccuracy;
        }
    }

    public boolean getMistyped() {
        return this.mistyped;
    }

    public void setMistyped(boolean a) {
        this.mistyped = a;
    }

    public String getStyle() {return  this.style;}
    public void setStyle(String style) {this.style = style;}

    public String getKeyboardType() {return  this.keyboardType;}
    public void setKeyboardType(String keyboardType) {this.keyboardType = keyboardType;}

    public void setAccessories(String[] accessories) {
        this.accessories = accessories;
    }
    public String[] getAccessories() {
        return this.accessories;
    }

    public int getNumberOfMistypes(){
        return this.numberOfMistypes;
    }

    public int getNumberOfProgressions(){
        return numberOfProgressions;
    }

    public int getBurnoutCount() {
        return this.burnoutCount;
    }

    public double getRaceBestWPM() {
        return this.raceBestWPM;
    }

    public void setRaceBestWPM(double wpm) {
        this.raceBestWPM = wpm;
    }

    public Color getColour() {
        return this.colour;
    }

    public void addMistake(int index) {
        mistypedPositions.add(index);
    }

    public boolean isMistyped(int index) {
        return mistypedPositions.contains(index);
    }

    /**
     * Sets the symbol used to represent this typist.
     *
     * @param newSymbol the new symbol character
     */
    public void setSymbol(char newSymbol)
    {
        this.typistSymbol = newSymbol;
    }

}

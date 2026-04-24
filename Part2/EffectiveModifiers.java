import java.util.HashMap;
import java.util.HashSet;

public class EffectiveModifiers {


    //CREATE THE HASHMAPS FOR EACH MODIFIER
    //modifier that MULTIPLIES the effective accuracy
    private static HashMap<String, Double> accuracyMult = new HashMap<>();
    private static void setAccuracyMultipliers() {
        accuracyMult.put("CAFFEINE_EARLY", 1.5);
        accuracyMult.put("CAFFEINE_LATE", 0.8);

        accuracyMult.put("ENERGYDRINK_EARLY", 1.5);
        accuracyMult.put("ENERGYDRINK_LATE", 0.85);

        accuracyMult.put("NIGHTSHIFT", 0.9);
    }

    // modifier that ADDS to the effective accuracy
    private static HashMap<String, Double> accuracyAdd = new HashMap<>();
    private static void setAccuracyAdders() {
        accuracyAdd.put("HUNT_AND_PECK", 0.15);
        accuracyAdd.put("PHONE_THUMBS", -0.1);
        accuracyAdd.put("VOICE_TO_TEXT", -0.1);

        accuracyAdd.put("MECHANICAL", 0.15);
        accuracyAdd.put("TOUCHSCREEN", -0.15);
    }

    // multiplies effective mistype chance
    private static HashMap<String, Double> mistypeMult = new HashMap<>();
    private static void setMistypeMultipliers() {
        mistypeMult.put("PHONE_THUMBS", 0.85);
        mistypeMult.put("NCHEADPHONES", 0.8);

        mistypeMult.put("MECHANICAL", 1.1);
        mistypeMult.put("MEMBRANE", 0.8);
        mistypeMult.put("TOUCHSCREEN", 1.1);
    }

    private static HashMap<String, Double> slideBackMult = new HashMap<>();
    private static void setSlideBackMultipliers() {
        slideBackMult.put("AUTOCORRECT", 0.5);

        slideBackMult.put("HUNT_AND_PECK", 2.0);
        slideBackMult.put("TOUCH_TYPIST", 0.5);

        slideBackMult.put("MEMBRANE", 2.0);
        slideBackMult.put("STENOGRAPHY", 2.0);
    }

    private static HashMap<String, Integer> burnoutAdd = new HashMap<>();
    private static void setBurnoutModifiers() {
        burnoutAdd.put("TOUCH_TYPIST", 4);
        burnoutAdd.put("VOICE_TO_TEXT", -1);

        burnoutAdd.put("STENOGRAPHY", -1);
        burnoutAdd.put("WRISTSUPPORT", -1);
    }

    // INITIALISE THE MAPS
    static {
        setAccuracyMultipliers();
        setAccuracyAdders();
        setMistypeMultipliers();
        setSlideBackMultipliers();
        setBurnoutModifiers();
    }

    // GET MODIFIER MAPS
    public static HashMap<String, Double> getAccuracyModifiers() {
        return accuracyMult;
    }

    public static HashMap<String, Double> getAccuracyAdders() {
        return accuracyAdd;
    }

    public static HashMap<String, Double> getMistypeMultipliers() {
        return mistypeMult;
    }

    public static HashMap<String, Double> getSlideBackMultipliers() {
        return slideBackMult;
    }

    public static HashMap<String, Integer> getBurnoutModifiers() {
        return burnoutAdd;
    }



    // MODIFIER BEHAVIOURS
    public static double calculateEffectiveAccuracy(Typist theTypist, boolean caffeine, boolean nightShift, int passageLength) {
        double effectiveAccuracy = theTypist.getAccuracy();

        if (caffeine) {
            if (theTypist.getProgress() < 10) {
                effectiveAccuracy *= accuracyMult.get("CAFFEINE_EARLY");
            } else {
                effectiveAccuracy *= accuracyMult.get("CAFFEINE_LATE");
            }
        }

        if (nightShift) {
            effectiveAccuracy *= accuracyMult.get("NIGHTSHIFT"); // 10% debuff
        }

        String[] accessories = theTypist.getAccessories();
        for (String accessory : accessories) {
            if (accessory.equals("ENERGYDRINK")) {
                if (theTypist.getProgress() < passageLength / 2) {
                    effectiveAccuracy *= accuracyMult.get("ENERGYDRINK_EARLY");
                } else {
                    effectiveAccuracy *= accuracyMult.get("ENERGYDRINK_LATE");
                }
            }
        }

        // style modifiers
        String style = theTypist.getStyle();
        if (style.equals("HUNT_AND_PECK")) {
            effectiveAccuracy += accuracyAdd.get("HUNT_AND_PECK");
        } else if (style.equals("PHONE_THUMBS")) {
            effectiveAccuracy += accuracyAdd.get("PHONE_THUMBS");
        } else if (style.equals("VOICE_TO_TEXT")) {
            effectiveAccuracy += accuracyAdd.get("VOICE_TO_TEXT");
        }

        String keyboardType = theTypist.getKeyboardType();
        if (keyboardType.equals("MECHANICAL")) {
            effectiveAccuracy += accuracyAdd.get("MECHANICAL");
        } else if (keyboardType.equals("TOUCHSCREEN")) {
            effectiveAccuracy += accuracyAdd.get("TOUCHSCREEN");
        }

        return Math.max(0.0, Math.min(1.0, effectiveAccuracy));
    }

    public static double calculateEffectiveMistypeChance(Typist theTypist) {
        double effectiveMistypeChance = 1.0;

        if (theTypist.getStyle().equals("PHONE_THUMBS")) {
            effectiveMistypeChance *= mistypeMult.get("PHONE_THUMBS");
        }

        String[] accessories = theTypist.getAccessories();
        for (String accesory : accessories) {
            if (accesory.equals("NCHEADPHONES")) {
                effectiveMistypeChance *= mistypeMult.get("NCHEADPHONES");
            }
        }

        String keyboardType = theTypist.getKeyboardType();
        if (keyboardType.equals("MECHANICAL")) {
            effectiveMistypeChance *= mistypeMult.get("MECHANICAL");
        } else if (keyboardType.equals("MEMBRANE")) {
            effectiveMistypeChance *= mistypeMult.get("MEMBRANE");
        } else if (keyboardType.equals("TOUCHSCREEN")) {
            effectiveMistypeChance *= mistypeMult.get("TOUCHSCREEN");
        }

        return Math.max(0.0, Math.min(1.0, effectiveMistypeChance));
    }

    public static double calculateEffectiveSlideBackMult(Typist theTypist, boolean autocorrect) {
        double effectiveSlideBackMult = 1.0;

        if (autocorrect) {
            effectiveSlideBackMult *= slideBackMult.get("AUTOCORRECT");
        }

        String style = theTypist.getStyle();
        if (style.equals("HUNT_AND_PECK")) {
            effectiveSlideBackMult *= slideBackMult.get("HUNT_AND_PECK");
        } else if (style.equals("TOUCH_TYPIST")) {
            effectiveSlideBackMult *= slideBackMult.get("TOUCH_TYPIST");
        }

        String keyboardType = theTypist.getKeyboardType();

        if (keyboardType.equals("MEMBRANE")) {
            effectiveSlideBackMult *= slideBackMult.get("MEMBRANE");
        } else if (keyboardType.equals("STENOGRAPHY")) {
            effectiveSlideBackMult *= slideBackMult.get("STENOGRAPHY");
        }

        return Math.max(0.0, effectiveSlideBackMult);
    }

    public static int calculateExtendedBurnoutDuration(Typist theTypist) {
        int extendedBurnoutDuration = 0;

        String style = theTypist.getStyle();
        if (style.equals("TOUCH_TYPIST")) {
            extendedBurnoutDuration += burnoutAdd.get("TOUCH_TYPIST");
        } else if (style.equals("VOICE_TO_TEXT")) {
            extendedBurnoutDuration += burnoutAdd.get("VOICE_TO_TEXT");
        }

        String keyboardType = theTypist.getKeyboardType();
        if (keyboardType.equals("STENOGRAPHY")) {
            extendedBurnoutDuration += burnoutAdd.get("STENOGRAPHY");
        }

        String[] accessories = theTypist.getAccessories();
        for (String accesory : accessories) {
            if (accesory.equals("WRISTSUPPORT")) {
                extendedBurnoutDuration += burnoutAdd.get("WRISTSUPPORT");
            }
        }

        return extendedBurnoutDuration;
    }
}

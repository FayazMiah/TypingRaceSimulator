import java.util.Random;

public class PassageController {

    private static String[] shortPassages = {
            "The cat ran across the field",
            "The dog flew into the sky",
            "The door was opened carefully"
    };

    private static String[] mediumPassages = {
            "Typing races are a fun way to test your speed and accuracy over time",
            "Consistency and focus are more important than raw typing speed in the long run"
    };

    private static String[] longPassages = {
            "In a world driven by constant distraction, the ability to focus deeply has become a rare and valuable skill..."
    };

    public static Passage createPassage(String type) {
        if (type.equalsIgnoreCase("short")) {
            return new Passage(shortPassages[(int)(Math.random() * shortPassages.length)]);
        }
        else if (type.equalsIgnoreCase("medium")) {
            return new Passage(mediumPassages[(int)(Math.random() * mediumPassages.length)]);
        }
        else if (type.equalsIgnoreCase("long")) {
            return new Passage(longPassages[(int)(Math.random() * longPassages.length)]);
        }

        return null;
    }

    public static Passage createPassage(String type, String customText) {
        if (type.equalsIgnoreCase("custom")) {
            return new Passage(customText);
        }
        return null;
    }
}//

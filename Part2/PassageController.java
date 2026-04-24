import java.util.Random;

public class PassageController {

    private static String[] shortPassages = {
            "The cat ran across the field",
            "The dog flew into the sky",
            "The door was opened carefully"
    };

    private static String[] mediumPassages = {
            "This is a fun and medium length sentence that this robot simulation is currently pretending to write.",
            "I love OOP and making typing simulators they are so fun and enjoyable as they induce challening problem solving."
    };

    private static String[] longPassages = {
            "This is a very long passage that should take a while to complete and is roughly 140 characters by estimation as I am typing this sentence, Hello World Hello World."
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

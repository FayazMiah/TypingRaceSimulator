import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigurationPanel {

    public static boolean selectedAutocorrect;
    public static boolean selectedCaffeine;
    public static boolean selectedNightShift;

    private static String selectedPassageType = "short";
    private static int customPassageLength = 0;
    //private static String customPassageText = "";

    public static String getSelectedPassageType(){
        return selectedPassageType;
    }

    private static void gameModifierScreen(RaceGui gui) {

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.setBackground(Theme.Dark);

        JCheckBox autocorrectBox = new JCheckBox("Autocorrect");
        JCheckBox caffeineBox = new JCheckBox("Caffeine");
        JCheckBox nightShiftBox = new JCheckBox("Night Shift");

        JButton saveButton = gui.createStyledButton("Save");
        JButton backButton = gui.createStyledButton("Back");

        saveButton.addActionListener(e -> {
            selectedAutocorrect = autocorrectBox.isSelected();
            selectedCaffeine = caffeineBox.isSelected();
            selectedNightShift = nightShiftBox.isSelected();
            configureRaceScreen(gui);
        });

        backButton.addActionListener(e ->
                configureRaceScreen(gui)
        );

        panel.add(autocorrectBox);
        panel.add(caffeineBox);
        panel.add(nightShiftBox);
        panel.add(saveButton);
        panel.add(backButton);

        gui.getFrame().setContentPane(panel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
    }

    static public void configureRaceScreen(RaceGui gui) {

        JPanel configPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        configPanel.setBackground(Theme.Dark);

        JButton createTypistButton = gui.createStyledButton("Create New Typist");
        JButton modifiersButton = gui.createStyledButton("Game Modifiers");
        JButton passageButton = gui.createStyledButton("Passage Length");
        JButton backButton = gui.createStyledButton("Back");
        JButton viewTypistsButton = gui.createStyledButton("View Typists");
        JButton leaderboardButton = gui.createStyledButton("Leaderboard");

        createTypistButton.addActionListener(e -> createTypistScreen(gui));
        modifiersButton.addActionListener(e -> gameModifierScreen(gui));
        passageButton.addActionListener(e -> passageLengthScreen(gui));
        backButton.addActionListener(e -> gui.showMenuScreen());
        viewTypistsButton.addActionListener(e -> viewTypistsScreen(gui));
        leaderboardButton.addActionListener(e -> viewLeaderboardScreen(gui));

        configPanel.add(createTypistButton);
        configPanel.add(modifiersButton);
        configPanel.add(passageButton);
        configPanel.add(viewTypistsButton);
        configPanel.add(leaderboardButton);
        configPanel.add(backButton);

        gui.getFrame().setContentPane(configPanel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
    }

    private static void createTypistScreen(RaceGui gui) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.Dark);
        panel.setForeground(Theme.Light);

        JTextField nameField = new JTextField();
        JTextField accuracyField = new JTextField();

        JComboBox<String> styleBox = new JComboBox<>(new String[]{"TOUCH_TYPIST", "HUNT_AND_PECK", "PHONE_THUMBS", "VOICE_TO_TEXT"});
        JComboBox<String> keyboardBox = new JComboBox<>(new String[]{"MECHANICAL", "MEMBRANE", "STENOGRAPHY"});
        JComboBox<String> colourBox = new JComboBox<>(new String[]{"Red", "Blue", "Green", "Purple"});

        JCheckBox energyDrink = new JCheckBox("ENERGYDRINK");
        JCheckBox wristSupport = new JCheckBox("WRISTSUPPORT");
        JCheckBox headphones = new JCheckBox("NCHEADPHONES");

        JComboBox<Character> symbolBox = new JComboBox<>(new Character[]{'①', '②', '③', '④', '⑤'});

        JButton saveButton = gui.createStyledButton("Save Typist");
        JButton backButton = gui.createStyledButton("Back");

        saveButton.addActionListener(e -> {

            String name = nameField.getText();

            double accuracy = Double.parseDouble(accuracyField.getText());
            String style = (String) styleBox.getSelectedItem();
            String keyboard = (String) keyboardBox.getSelectedItem();
            String colourChoice = (String) colourBox.getSelectedItem();
            Color chosenColor = new Color(255,0,0);

            if (colourChoice.equals("Blue")) {
                chosenColor = new Color(56, 255, 179);
            }
            else if (colourChoice.equals("Green")) {
                chosenColor = new Color(76, 255, 56);
            }
            else if (colourChoice.equals("Purple")) {
                chosenColor = new Color(189, 56, 255);
            }

            char symbol = (Character) symbolBox.getSelectedItem();

            ArrayList<String> accessoriesList = new ArrayList<>();

            if (energyDrink.isSelected()) {
                accessoriesList.add("ENERGYDRINK");
            }
            if (wristSupport.isSelected()) {
                accessoriesList.add("WRISTSUPPORT");
            }
            if (headphones.isSelected()) {
                accessoriesList.add("NCHEADPHONES");
            }

            String[] accessories = accessoriesList.toArray(new String[0]);

            Typist newTypist = new Typist(
                    symbol,
                    name,
                    accuracy,
                    style,
                    keyboard,
                    chosenColor,
                    accessories
            );

            TypingRace.addTypist(newTypist);
            System.out.println("typists: " + TypingRace.getTypists());

            configureRaceScreen(gui);
        });

        backButton.addActionListener(e ->
                configureRaceScreen(gui)
        );

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel accuracyLabel = new JLabel("Accuracy:");
        accuracyLabel.setForeground(Color.WHITE);
        panel.add(accuracyLabel);
        panel.add(accuracyField);

        JLabel styleLabel = new JLabel("Style:");
        styleLabel.setForeground(Color.WHITE);
        panel.add(styleLabel);
        panel.add(styleBox);

        JLabel keyboardLabel = new JLabel("Keyboard:");
        keyboardLabel.setForeground(Color.WHITE);
        panel.add(keyboardLabel);
        panel.add(keyboardBox);

        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setForeground(Color.WHITE);
        panel.add(symbolLabel);
        panel.add(symbolBox);

        JLabel colourLabel = new JLabel("Colour:");
        colourLabel.setForeground(Color.WHITE);
        panel.add(colourLabel);
        panel.add(colourBox);

        JLabel accessoriesLabel = new JLabel("Accessories:");
        accessoriesLabel.setForeground(Color.WHITE);
        panel.add(accessoriesLabel);
        panel.add(energyDrink);
        panel.add(wristSupport);
        panel.add(headphones);

        panel.add(saveButton);
        panel.add(backButton);

        gui.getFrame().setContentPane(panel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
    }

    private static void passageLengthScreen(RaceGui gui) {

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBackground(Theme.Dark);

        String[] options = {"SHORT", "MEDIUM", "LONG", "CUSTOM"};
        JComboBox<String> lengthBox = new JComboBox<>(options);

        JTextField customField = new JTextField();
        customField.setEnabled(false);

        lengthBox.addActionListener(e -> {
            customField.setEnabled(lengthBox.getSelectedItem().equals("CUSTOM"));
        });

        JButton saveButton = gui.createStyledButton("Save");
        JButton backButton = gui.createStyledButton("Back");

        saveButton.addActionListener(e -> {

            selectedPassageType = (String) lengthBox.getSelectedItem();

            if (selectedPassageType.equals("CUSTOM")) {
                customPassageLength = Integer.parseInt(customField.getText());
            }

            configureRaceScreen(gui);
        });

        backButton.addActionListener(e -> configureRaceScreen(gui));

        JLabel passageLength = new JLabel("Passage Length:");
        passageLength.setForeground(Color.WHITE);
        panel.add(passageLength);
        panel.add(lengthBox);
        JLabel customLength = new JLabel("Custom Length:");
        customLength.setForeground(Color.WHITE);
        panel.add(customLength);
        panel.add(customField);
        panel.add(saveButton);
        panel.add(backButton);

        gui.getFrame().setContentPane(panel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
    }

    private static void viewTypistsScreen(RaceGui gui) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.Dark);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(Theme.Dark);
        textPane.setForeground(Color.WHITE);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StyledDocument doc = textPane.getStyledDocument();

        try {

            ArrayList<Typist> typists = TypingRace.getTypists();

            if (typists.isEmpty()) {
                doc.insertString(0, "No typists created yet.\n", null);
            }

            for (Typist t : typists) {

                SimpleAttributeSet style = new SimpleAttributeSet();
                StyleConstants.setForeground(style, t.getColour());
                StyleConstants.setBold(style, true);

                doc.insertString(doc.getLength(), t.getTitle() + "\n", style);

                doc.insertString(
                        doc.getLength(),
                        "Points: " + t.getPoints() + "\n" +
                        "Symbol: " + t.getSymbol() + "\n" +
                        "Accuracy: " + t.getAccuracy() + "\n" +
                        "Style: " + t.getStyle() + "\n" +
                        "Keyboard: " + t.getKeyboardType() + "\n" +
                        "Accessories: " + String.join(", ", t.getAccessories()) + "\n\n",
                        null
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scroll = new JScrollPane(textPane);

        JButton backButton = gui.createStyledButton("Back");

        backButton.addActionListener(e -> configureRaceScreen(gui));

        panel.add(scroll);
        panel.add(backButton);

        gui.getFrame().setContentPane(panel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
    }

    private static void viewLeaderboardScreen(RaceGui gui) {
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.setBackground(Theme.Dark);

        JTextPane analyticsPane = new JTextPane();
        analyticsPane.setEditable(false);
        analyticsPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
        analyticsPane.setBackground(Theme.Dark);
        analyticsPane.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(analyticsPane);
        scrollPane.setPreferredSize(new Dimension(720, 500));

        StyledDocument doc = analyticsPane.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), "=== POINTS LEADERBOARD ===\n\n", null);

            if (TypingRace.getTypists().size() == 0) {
                doc.insertString(doc.getLength(), "No statistics available.\n", null);
            }

            ArrayList<Typist> typists = TypingRace.getTypists();
            ArrayList<Typist> sortedTypists = new ArrayList<>(typists);
            sortedTypists.sort((a, b) -> b.getPoints() - a.getPoints()); // sort high to low

            int position = 1;

            for (Typist t : sortedTypists) {
                SimpleAttributeSet style = new SimpleAttributeSet();
                StyleConstants.setForeground(style, t.getColour());
                StyleConstants.setBold(style, true);
                doc.insertString(doc.getLength(), position + ". " + t.getTitle() + "\n", style);
                doc.insertString(doc.getLength(), "Points: " + t.getPoints() + "\n\n", null);
                position++;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton backButton = new JButton("Return to Menu");
        backButton.setBackground(Theme.Light);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> gui.showMenuScreen());

        analyticsPanel.add(scrollPane, BorderLayout.CENTER);
        analyticsPanel.add(backButton, BorderLayout.SOUTH);

        gui.getFrame().setContentPane(analyticsPanel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
    }

}

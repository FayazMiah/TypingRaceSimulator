import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RaceGui {

    private JFrame frame;
    private JPanel menuPanel;
    private JTextPane passagePane;
    private RaceRenderer renderer;
    private TypingRace race;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RaceGui().createUI());
    }

    //main ui
    private void createUI() {

        frame = new JFrame("Typing Race");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // menu panel
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Theme.Dark);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setOpaque(false);

        JButton configureButton = createStyledButton("Configure Race");
        JButton startButton = createStyledButton("Start Race");
        JButton analyticsButton = createStyledButton("Racer Analytics");

        //button event action listeners

        configureButton.addActionListener(e -> {
            //if (TypingRace.getTypists().size() < 6) {
            ConfigurationPanel.configureRaceScreen(this);
            //}
        });

        startButton.addActionListener(e -> startRaceGUI());

        analyticsButton.addActionListener(e -> {
            //JOptionPane.showMessageDialog(frame, "Analytics clicked");
            AnalyticsPanel.viewAnalyticsScreen(this);
        });

        buttonPanel.add(configureButton);
        buttonPanel.add(startButton);
        buttonPanel.add(analyticsButton);

        menuPanel.add(buttonPanel);

        frame.add(menuPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    //race screening
    private void startRaceGUI() {

        if (TypingRace.getTypists().size() < 2) {
            System.out.println("Not Enough People");
            return;
        }

       // System.out.println("craeting race screee n");
        race = null;
        renderer = null;

        // create new panel fresh
        JPanel racePanel = new JPanel(new BorderLayout());
        racePanel.setBackground(Theme.Dark);

        passagePane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(passagePane);
        scrollPane.setPreferredSize(new Dimension(720, 500));

        racePanel.add(scrollPane, BorderLayout.CENTER);

        //set the current panel
        frame.setContentPane(racePanel);
        frame.revalidate();
        frame.repaint();

        renderer = new RaceRenderer(this, passagePane);

        //Passage p = PassageController.createPassage("short");

        Passage p;

        if (ConfigurationPanel.getSelectedPassageType().equals("CUSTOM")) {
            p = PassageController.createPassage("custom", "this is a custom test passage to change afterwards");
        }
        else {
            p = PassageController.createPassage(ConfigurationPanel.getSelectedPassageType().toLowerCase());
        }

        race = new TypingRace(
                p,
                ConfigurationPanel.selectedAutocorrect,
                ConfigurationPanel.selectedCaffeine,
                ConfigurationPanel.selectedNightShift
        );

        /*TypingRace.addTypist(new Typist(
                '①',
                "TURBOFINGERS",
                0.85,
                "TOUCH_TYPIST",
                "STENOGRAPHY",
                new Color(255, 0, 255),
                new String[]{"ENERGYDRINK"})
        );

        TypingRace.addTypist(new Typist('②',
                "QWERTY_QUEEN",
                0.60,
                "TOUCH_TYPIST",
                "MEMBRANE",
                new Color(0, 255, 255),
                new String[]{"WRISTSUPPORT"})
        );*/

        // start timer after ui is put in
        race.startRace(renderer);
    }



    //toggle menu visiblity
    public void showMenuScreen() {
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {return this.frame;
    }

    //button styling
    public JButton createStyledButton(String text) {

        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Theme.Light);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createEmptyBorder());

        return button;
    }
}
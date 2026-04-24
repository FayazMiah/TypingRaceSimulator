import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RaceRenderer {

    private JTextPane textPane;
    private RaceGui gui;

    // styling constants
    private static final Color COMPLETED = Color.GREEN;
    private static final Color CURRENT = new Color(160, 0, 255); // purple cursor
    private static final Color DEFAULT = Color.WHITE;
    private static final Color ERROR = Color.RED;
    private static final Color BURNT_OUT = new Color(255, 90, 0);

    public RaceRenderer(RaceGui gui, JTextPane textPane) {
        this.textPane = textPane;
        this.gui = gui;

        this.textPane.setEditable(false);
        this.textPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
        this.textPane.setBackground(Theme.Dark);
        this.textPane.setForeground(Color.WHITE);
    }

    // main rendering method
    public void render(TypingRace race, ArrayList<Typist> typists, Passage passage) {

        //System.out.println("RENDERINGDSGSDHIGSDIUG");
        if (race == null || race.gameState.equals("FINISHED")) {
            //System.out.println("asddsaas");
            return;
        }

        StyledDocument doc = textPane.getStyledDocument();
        textPane.setText("");

        String text = passage.getText();

        for (Typist t : typists) {

            try {
                SimpleAttributeSet nameStyle = new SimpleAttributeSet();
                StyleConstants.setForeground(nameStyle, t.getColour());
                StyleConstants.setBold(nameStyle, true);

                doc.insertString(doc.getLength(), t.getName() + " | ", nameStyle);

            } catch (Exception e) {
                e.printStackTrace();
            }

            int progress = t.getProgress();
            boolean burntOut = t.isBurntOut();

            for (int i = 0; i < text.length(); i++) {

                SimpleAttributeSet style = new SimpleAttributeSet();

                if (burntOut) {
                    StyleConstants.setForeground(style, BURNT_OUT);
                }
                else if (t.isMistyped(i)) {
                    StyleConstants.setForeground(style, ERROR);
                }
                else if (i < progress) {
                    StyleConstants.setForeground(style, COMPLETED);
                }
                else if (i == progress) {
                    StyleConstants.setForeground(style, CURRENT);
                }
                else {
                    StyleConstants.setForeground(style, DEFAULT);
                }

                try {
                    doc.insertString(doc.getLength(), String.valueOf(text.charAt(i)), style);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                int accPercent = PerformanceMetrics.getAccuracyPercentage(t);
                int burnoutCount = PerformanceMetrics.getBurnoutCount(t);
                double currentWPM = PerformanceMetrics.getWPM(t, race.getStartTime());
                int mistypeCount = t.getNumberOfMistypes();

                doc.insertString(doc.getLength(),
                "\nAdvance Accuracy: " + accPercent +
                    "\nBurnout Count: " + burnoutCount +
                    "\nCurrent WPM: " + currentWPM +
                    "\nMistype Count: " + mistypeCount +
                    "\n\n",
                 null
                );

            } catch (Exception e) {
                System.out.println("error rendering race");
            }
        }
    }

    public void renderResults(TypingRace race, ArrayList<Typist> typists, Typist winner) {

        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(Theme.Dark);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textPane.setBackground(Theme.Dark);
        textPane.setForeground(Color.WHITE);

        StyledDocument doc = textPane.getStyledDocument();

        try {
            // winner top text
            SimpleAttributeSet winnerStyle = new SimpleAttributeSet();
            StyleConstants.setBold(winnerStyle, true);
            StyleConstants.setForeground(winnerStyle, Color.YELLOW);

            doc.insertString(doc.getLength(), "WINNER: " + winner.getName() + "\n\n", winnerStyle);

            // stats per player
            HashMap<Typist, HashMap<String, Double>> raceData = race.getRaceData();

            for (Typist t : typists) {

                SimpleAttributeSet nameStyle = new SimpleAttributeSet();
                StyleConstants.setBold(nameStyle, true);
                StyleConstants.setForeground(nameStyle, t.getColour());

                HashMap<String, Double> data = raceData.get(t);



                int pos = data.get("PositionPlaced").intValue();
                int pointsInt = typists.size() - pos;

                doc.insertString(doc.getLength(), t.getName() + "\n", nameStyle);
                doc.insertString(doc.getLength(), "Progress: " + t.getProgress() + " characters\n", null);
                if (pos == 1) {
                    doc.insertString(doc.getLength(), "Position Placed: " + pos + "st\n", null);
                } else if (pos == 2) {
                    doc.insertString(doc.getLength(), "Position Placed: " + pos + "nd\n", null);
                } else if (pos == 3) {
                    doc.insertString(doc.getLength(), "Position Placed: " + pos + "rd\n", null);
                } else {
                    doc.insertString(doc.getLength(), "Position Placed: " + pos + "th\n", null);
                }
                doc.insertString(doc.getLength(), "Points Receieved: " + pointsInt + "\n", null);
                doc.insertString(doc.getLength(), "Advance Accuracy (%): " + data.get("AdvanceAccuracy") + "%\n", null);
                doc.insertString(doc.getLength(), "Racer Probabilistic Accuracy: " + t.getAccuracy() + "\n", null);
                doc.insertString(doc.getLength(), "Burnout Count: " + data.get("BurnoutCount") + "\n", null);
                doc.insertString(doc.getLength(), "Highest WPM Reached: " + Math.round(data.get("WPMReached")) + "\n\n", null);


            }

        } catch (Exception e) {
            System.out.println("error rendering race final screen");
        }

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(null);

        JButton backButton = new JButton("Return to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Theme.Light);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        // put back on main menu and reset ready for next one
        backButton.addActionListener(e -> {
            race.resetRace();
            //resultsPanel.setVisible(false);
            //this.textPane = new JTextPane();

            gui.showMenuScreen();

          //  System.out.println("back to menu");
        });

        resultsPanel.add(scroll, BorderLayout.CENTER);
        resultsPanel.add(backButton, BorderLayout.SOUTH);

     //   System.out.println("renderingreslts");
       // System.out.println(gui + ": current gui");
        gui.getFrame().setContentPane(resultsPanel);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
        gui.getFrame().setVisible(true);
    }


}
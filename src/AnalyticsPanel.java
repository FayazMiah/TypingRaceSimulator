import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;

public class AnalyticsPanel {

    public static void viewAnalyticsScreen(RaceGui gui) {

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
            doc.insertString(doc.getLength(), "=== GLOBAL RACE ANALYTICS ===\n\n", null);

            if (TypingRace.globalRaceData.size() == 0) {
                doc.insertString(doc.getLength(), "No races played yet.\n", null);
            }

            int raceNum = 1;
            //System.out.println("global racedata: " + TypingRace.globalRaceData);
            for (HashMap<Typist, HashMap<String, Double>> race : TypingRace.globalRaceData) {
                doc.insertString(doc.getLength(), "RACE #" + raceNum + "\n\n", null);
                for (Typist t : race.keySet()) {
                    HashMap<String, Double> data = race.get(t);
                    doc.insertString(doc.getLength(), t.getTitle() + "\n", null);
                    doc.insertString(doc.getLength(), "Position: " + data.get("PositionPlaced").intValue() + "\n", null);
                    doc.insertString(doc.getLength(), "Highest WPM: " + Math.round(data.get("WPMReached")) + "\n", null);
                    doc.insertString(doc.getLength(), "Advance Accuracy: " + data.get("AdvanceAccuracy") + "%\n", null);
                    doc.insertString(doc.getLength(), "Burnout Count: " + data.get("BurnoutCount").intValue() + "\n", null);
                    doc.insertString(doc.getLength(), "Mistype Count: " + data.get("MistypeCount").intValue() + "\n", null);
                    doc.insertString(doc.getLength(), "Final Progress: " + data.get("FinalProgress").intValue() + "\n", null);
                    doc.insertString(doc.getLength(), "Points from Race: " + data.get("PointsAchieved") + "\n", null);
                    doc.insertString(doc.getLength(), "Winstreak: " + t.getWinstreak() + "\n", null);
                    doc.insertString(doc.getLength(), "Base Accuracy: " + data.get("BaseAccuracy") + "\n\n", null);
                }
                doc.insertString(doc.getLength(), "----------------------------\n\n", null);
                raceNum++;
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

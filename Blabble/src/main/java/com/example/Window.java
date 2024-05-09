package com.example;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Window {
    JFrame window = new JFrame("* Blabble! *");
    JPanel gamePanel;
    JPanel buttonPanel = new JPanel();
    JPanel blockPanelPanel = new JPanel();
    JPanel blockPanel = new JPanel();
    JPanel usedBlockPanel = new JPanel();
    JButton startButton = new JButton("Start Blabble!");
    JButton playAgainButton = new JButton("Play Again!");
    JButton cpuButton = new JButton("Computer Opponent");
    JButton p2Button = new JButton("2 Players");
    JButton submitButton = new JButton("Submit Word!");
    JButton endTurnButton = new JButton("End Turn");
    ArrayList<String> message = new ArrayList<String>();
    static Boolean battleStarted = false;
    static Color bgColor = new Color(153, 196, 210);
    static Color boxColor = new Color(133, 176, 190);

    public Window() {
        // Add the panel to draw on.
        gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Display current messages.
                if (Blabble.phase != 0 && Blabble.phase != 4) {
                    g.setColor(boxColor);
                    g.fill3DRect(400, 625, 400, 150, true);
                    g.setFont(new Font("Bright", Font.PLAIN, 20));
                    g.setColor(boxColor.darker().darker());
                    for (int i = 0; i < message.size(); i++) {
                        g.drawString(message.get(i), 410, i * 20 + 650);
                    }
                    g.setFont(new Font("Bright", Font.PLAIN, 70));
                    g.setColor(boxColor.darker());
                    String playerString = (Blabble.subPhase == 1) ? "Player 1!" : "Player 2!";
                    g.drawString(playerString, 450, 80);
                }
                switch (Blabble.phase) {
                    case 0:
                        // Start menu (with Dwayne)
                        Image rockImage = new ImageIcon("src/main/java/com/example/Images/TheRock.jpeg").getImage();
                        g.setColor(boxColor);
                        g.fill3DRect(240, 85, 700, 160, true);
                        g.fill3DRect(425, 275, 350, 350, true);
                        g.setFont(new Font("Bright", Font.PLAIN, 130));
                        g.setColor(boxColor.darker());
                        g.drawString("BLABBLE!", 280, 210);
                        g.drawImage(rockImage, 450, 300, 300, 300, null);
                        break;
                    case 1:
                    case 3:
                        g.setFont(new Font("Bright", Font.PLAIN, 27));
                        g.setColor(bgColor.darker().darker());
                        g.drawString("Words Found", 18, 140);
                        g.drawLine(17, 145, 185, 145);
                        g.setFont(new Font("Bright", Font.PLAIN, 20));
                        for (int i = 0; i < Blabble.currentWords.size(); i++) {
                            g.drawString("- " + Blabble.currentWords.get(i).word, 25, i * 17 + 165);
                        }
                    case 5:
                        // 6 letter multi-use round (Blabble) and tiebreaker round (Super Tiebreaker)
                        if (Blabble.phase != 3 || battleStarted) {
                            g.setColor(boxColor);
                            g.fill3DRect(200, 110, 785, 200, true);
                            g.fill3DRect(200, 405, 785, 200, true);
                        }
                        if (Blabble.phase == 3 && battleStarted) {
                            g.setFont(new Font("Bright", Font.PLAIN, 30));
                            g.setColor(Color.red);
                            g.drawString("Time remaining: " + (BattleTimer.timerLength - BattleTimer.seconds), 50, 60);
                        }
                        break;
                    case 2:
                        // 9 letter elimination round (Super Blabble)
                        g.setColor(boxColor);
                        g.fill3DRect(35, 110, 1115, 200, true);
                        g.fill3DRect(35, 405, 1115, 200, true);
                        break;
                    case 4:
                        // end of game
                        Image rockImage2 = new ImageIcon("src/main/java/com/example/Images/KINGDWAYNE.jpg").getImage();
                        g.setColor(boxColor);
                        g.fill3DRect(40, 20, 1100, 800, true);
                        g.fill3DRect(35, 325, 1130, 500, true);
                        String outcomeMessage = ((ScoreTracker.getOverallWinner() == 1) ? "Player 1"
                                : (Blabble.p2) ? "Player 2" : "The CPU") + " won!!";
                        g.setFont(new Font("Bright", Font.PLAIN, 70));
                        g.setColor(boxColor.darker().darker());
                        g.drawString(outcomeMessage, 60, 90);

                        g.setFont(new Font("Bright", Font.PLAIN, 40));
                        String[] stats = ScoreTracker.report();
                        for (int i = 0; i < stats.length; i++) {
                            g.drawString("--- " + stats[i], 60, 150 + i * 50);
                        }
                        g.drawImage(rockImage2, 60, 350, 1080, 450, null);
                        break;
                }
            }
        };
        gamePanel.setBackground(bgColor);
        window.add(gamePanel);

        // Add the panels for letter blocks.
        blockPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        blockPanel.setOpaque(false);
        usedBlockPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        usedBlockPanel.setOpaque(false);
        blockPanelPanel.add(Box.createVerticalStrut(150));
        blockPanelPanel.add(blockPanel, BorderLayout.CENTER);
        blockPanelPanel.add(Box.createVerticalStrut(200));
        blockPanelPanel.add(usedBlockPanel, BorderLayout.SOUTH);
        blockPanelPanel.setLayout(new BoxLayout(blockPanelPanel, BoxLayout.Y_AXIS));
        blockPanelPanel.setOpaque(false);
        gamePanel.add(blockPanelPanel);

        // Give the buttons functions.
        buttonFunctions();

        // Initialize all of the colors and buttons and stuff.
        updateWindow();
        window.add(buttonPanel, BorderLayout.SOUTH);

        // Set the defaults for the window, and make it visible.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 1000);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    //This is where the thingy is updated!!! So if you want to add anything do it here
    public void updateWindow() {
        // Button coloration
        Color grayColor = new Color(200, 200, 200);
        Color darkGrayColor = new Color(150, 150, 150);
        startButton.setBackground(grayColor);
        cpuButton.setBackground((Blabble.p2) ? grayColor : darkGrayColor);
        p2Button.setBackground((Blabble.p2) ? darkGrayColor : grayColor);
        submitButton.setBackground(grayColor);
        endTurnButton.setBackground(Color.red);

        switch (Blabble.phase) {
            // Positioning the letterblocks correctly
            case 1:
            case 3:
            case 5:
                blockPanel.setPreferredSize(new Dimension(670, 100));
                break;
            case 2:
                blockPanel.setPreferredSize(new Dimension(1000, 100));
                break;
        }

        // Setting which buttons to allow based on current game phase and player! (also
        // remove all the buttons before so that they can be re-added.)
        removeAllButtons();
        switch (Blabble.phase) {
            case 0:
                buttonPanel.add(startButton);
                buttonPanel.add(cpuButton);
                buttonPanel.add(p2Button);
                break;
            case 3:
                if (!battleStarted) {
                    buttonPanel.add(startButton);
                } else {
                    buttonPanel.add(submitButton);
                    for (int i = 0; i < Blabble.openLetters.size(); i++) {
                        LetterBlock block = Blabble.openLetters.get(i);
                        blockPanel.add(block.button);
                    }
                    for (int i = 0; i < Blabble.selectedLetters.size(); i++) {
                        LetterBlock block = Blabble.selectedLetters.get(i);
                        usedBlockPanel.add(block.button);
                    }
                }
                break;
            case 4:
                buttonPanel.add(playAgainButton);
                break;
            default:
                buttonPanel.add(submitButton);
                buttonPanel.add(endTurnButton);
                for (int i = 0; i < Blabble.openLetters.size(); i++) {
                    LetterBlock block = Blabble.openLetters.get(i);
                    blockPanel.add(block.button);
                }
                for (int i = 0; i < Blabble.selectedLetters.size(); i++) {
                    LetterBlock block = Blabble.selectedLetters.get(i);
                    usedBlockPanel.add(block.button);
                }
                break;
        }
        window.revalidate();
        window.repaint();
    }

    public void removeAllButtons() {
        buttonPanel.removeAll();
        buttonPanel.revalidate();
        blockPanel.removeAll();
        blockPanel.revalidate();
        usedBlockPanel.removeAll();
        usedBlockPanel.revalidate();
    }

    public void buttonFunctions() {
        JButton[] buttonList = { startButton, playAgainButton, cpuButton, p2Button, submitButton, endTurnButton };
        for (JButton button : buttonList) {
            button.setFocusable(false);
            button.setPreferredSize(new Dimension(200, 100));
        }
        // Start menu buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Blabble.phase == 0) {
                    Blabble.phase = 1;
                    Blabble.resetStuff();
                    message.add("Welcome To Blabble!!");
                } else if (Blabble.phase == 3) {
                    BattleTimer.startTimer();
                }
                updateWindow();
            }
        });
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Blabble.phase = 0;
                Blabble.subPhase = 1;
                message.clear();
                Blabble.resetStuff();
                ScoreTracker.resetStuff();
                updateWindow();
            }
        });
        cpuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Blabble.p2 = false;
                updateWindow();
            }
        });
        p2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Blabble.p2 = true;
                updateWindow();
            }
        });
        // When a word is submitted
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Blabble.submitWord();
            }
        });
        // When a player's turn is ended
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Blabble.gameTransition();
            }
        });
    }
}
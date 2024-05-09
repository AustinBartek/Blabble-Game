package com.example;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LetterBlock {
    char letter;
    JButton button;
    static Color[] colorArr = { new Color(255, 40, 40), new Color(255, 180, 70), new Color(255, 245, 40),
        new Color(100, 215, 0), new Color(0, 215, 215),
        new Color(215, 0, 215) };

    public LetterBlock(char letter) {
        this.letter = letter;

        Color colorToUse = colorArr[Blabble.random.nextInt(colorArr.length)];
        this.button = new JButton((this.letter + "").toUpperCase());
        this.button.setBackground(new Color(255, 200, 160));
        this.button.setForeground(colorToUse);
        this.button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorToUse, 3),
                BorderFactory.createEmptyBorder(
                        this.button.getBorder().getBorderInsets(this.button).top,
                        this.button.getBorder().getBorderInsets(this.button).left,
                        this.button.getBorder().getBorderInsets(this.button).bottom,
                        this.button.getBorder().getBorderInsets(this.button).right)));

        this.button.setPreferredSize(new Dimension(100, 100));
        this.button.setFocusable(false);
        this.button.setFont(new Font("Bright", Font.PLAIN, 60));
        final LetterBlock thisBlock = this;
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Blabble.openLetters.contains(thisBlock)) {
                    Blabble.selectedLetters.add(Blabble.openLetters.remove(Blabble.openLetters.indexOf(thisBlock)));
                    Blabble.window.updateWindow();
                } else if (Blabble.selectedLetters.contains(thisBlock)) {
                    Blabble.openLetters.add(Blabble.selectedLetters.remove(Blabble.selectedLetters.indexOf(thisBlock)));
                    Blabble.window.updateWindow();
                }
            }
        });
    }
}
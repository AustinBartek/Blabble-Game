package com.example;

import java.util.*;

public class Blabble {
    static int phase = 0, subPhase = 1, tiePhase = 1;
    static Random random = new Random();
    static Boolean p2 = false;
    static ArrayList<LetterBlock> selectedLetters = new ArrayList<LetterBlock>(),
            openLetters = new ArrayList<LetterBlock>();
    static LetterBlockBox box = new LetterBlockBox();
    static Window window;
    static ArrayList<Word> currentWords = new ArrayList<Word>();
    static String[] happyMessages = { "Yay! Nice one :)", "Ooh, dang! That one is tough.",
            "Are there any more you can see?", "Isn't this fun? :D", "OOGA BOOGA OOGA BOOGA >:)",
            "Howdy there, partner :P", "Doing good, doing good.", "Almost there!", "Love the effort *thumbs up.",
            "Hrrmmm, it took ya that long?" };

    //Really, what a main function right there *applause
    public static void main(String[] args) {
        WordList.initializeList();
        window = new Window();
    }

    public static String formString(ArrayList<LetterBlock> list) {
        String word = "";
        for (LetterBlock block : list) {
            word += block.letter;
        }
        return word;
    }

    public static void resetStuff() {
        box.resetBox();
        currentWords.clear();
        openLetters.clear();
        selectedLetters.clear();
        int[] nums = { 6, 9, 6, 0, 4 };
        int[][] vowelNums = { { 1, 2 }, { 2, 3 }, { 1, 2 }, { 0, 0 }, { 1, 1 } };
        if (phase != 4 && phase != 0) {
            openLetters.addAll(box.draw(nums[phase - 1], vowelNums[phase - 1][0], vowelNums[phase - 1][1]));
        }
    }

    public static String roundName(int round) {
        String[] names = { "Beginning", "Simple Blabble", "Super Blabble", "Blabble Battle", "End", "Super Tiebreaker" };
        return names[round];
    }

    public static void gameTransition() {
        window.message.clear();
        if (subPhase == 1) {
            ScoreTracker.addToScores(currentWords, true);

            int size1 = ScoreTracker.getWordsSize(true);
            int[] score1 = ScoreTracker.p1Scores.get(ScoreTracker.p1Scores.size() - 1);
            window.message.add("P1: " + score1[0] + ((score1[0] != 1) ? " letters, " : " letter, ") + score1[1]
                    + " total score, " + size1 + ((size1 != 1) ? " words" : " word"));
            window.message.add("P2: 0 letters, 0 total score, 0 words (TBD)");

            subPhase = 2;
            resetStuff();
            if (!p2) {
                // computer response goes here, automatically goes to next phase
                currentWords.addAll(WordList.computeLetters(openLetters));
                gameTransition();
            }
        } else {
            ScoreTracker.addToScores(currentWords, false);
            
            WordList.sortForCpu(currentWords);
            for (Word word : currentWords) {
                System.out.println(word.word + " " + word.frequency);
            }

            int outcome = ScoreTracker.getWinner();
            window.message.add((outcome == 0) ? "No one won " + roundName(phase) + "!"
                    : roundName(phase) + " winner: " + ((outcome == 1) ? "Player 1" : "Player 2") + "!");

            int size1 = ScoreTracker.getWordsSize(true);
            int size2 = ScoreTracker.getWordsSize(false);
            int[] score1 = ScoreTracker.p1Scores.get(ScoreTracker.p1Scores.size() - 1);
            int[] score2 = ScoreTracker.p2Scores.get(ScoreTracker.p2Scores.size() - 1);
            window.message.add("P1: " + score1[0] + ((score1[0] != 1) ? " letters, " : " letter, ") + score1[1]
                    + " total score, " + size1 + ((size1 != 1) ? " words" : " word"));
            window.message.add("P2: " + score2[0] + ((score2[0] != 1) ? " letters, " : " letter, ") + score2[1]
                    + " total score, " + size2 + ((size2 != 1) ? " words" : " word"));

            if (outcome == 0) {
                if (phase != 5) {
                    tiePhase = phase + 1;
                }
                phase = 5;
            } else {
                phase = (phase == 5) ? tiePhase : phase + 1;
            }
            subPhase = 1;
            window.message.add("New round: " + (roundName(phase)));
            resetStuff();
        }

        window.updateWindow();
    }

    public static Boolean canAddWord(String word) {
        Word wordCheck = WordList.getWord(word);
        return (WordList.isWord(word) && !currentWords.contains(wordCheck));
    }

    public static void submitWord() {
        String word = formString(selectedLetters);
        Word wordToAdd = WordList.getWord(word);
        String newMessage = happyMessages[random.nextInt(happyMessages.length)];
        window.message.clear();

        if (canAddWord(word)) {
            currentWords.add(wordToAdd);
            if (phase != 2 && phase != 5) {
                openLetters.addAll(selectedLetters);
            }
            selectedLetters.clear();
            window.message.add(newMessage);
        } else {
            openLetters.addAll(selectedLetters);
            selectedLetters.clear();

            if (WordList.isWord(word)) {
                window.message.add("You already used this word!");
            } else {
                window.message.add("'" + word.toUpperCase() + "'" + " was not found in the database!");
            }
        }
        if (phase == 5) {
            if (currentWords.size() > 0) {
                gameTransition();
            }
        } else if (phase == 2) {
            if (currentWords.size() > 1) {
                gameTransition();
            }
        }

        window.updateWindow();
    }

    //The lovely function behind the battle
    public static void doChaos() {
        
        Boolean chooseFromPlayer = (random.nextInt(10) <= 2) && !selectedLetters.isEmpty() || openLetters.isEmpty();

        if (!chooseFromPlayer) {
            LetterBlock removedBlock = openLetters.remove(random.nextInt(openLetters.size()));
            box.unusedBlocks.add(removedBlock);
            int numVowels = (WordList.isVowel(removedBlock.letter)) ? 1 : 0;
            openLetters.addAll(box.draw(1, numVowels, numVowels));
        } else {
            LetterBlock removedBlock = selectedLetters.remove(random.nextInt(selectedLetters.size()));
            box.unusedBlocks.add(removedBlock);
            int numVowels = (WordList.isVowel(removedBlock.letter)) ? 1 : 0;
            openLetters.addAll(box.draw(1, numVowels, numVowels));
        }
        window.updateWindow();
    }
}
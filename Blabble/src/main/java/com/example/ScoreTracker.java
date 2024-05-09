package com.example;

import java.util.*;

public class ScoreTracker {
    static ArrayList<int[]> p1Scores = new ArrayList<int[]>(), p2Scores = new ArrayList<int[]>();
    static ArrayList<String[]> p1Words = new ArrayList<String[]>(5),
            p2Words = new ArrayList<String[]>(5);
    static ArrayList<Integer> outcomes = new ArrayList<Integer>(5);

    // returns 0 for tie, 1 for p1 win, and -1 for p2 win.
    public static int getWinner() {
        int numToReturn = 0;
        int[] currentP1 = p1Scores.get(p1Scores.size() - 1);
        int[] currentP2 = p2Scores.get(p1Scores.size() - 1);
        for (int i = 0; i < 2; i++) {
            if (currentP1[i] > currentP2[i]) {
                numToReturn = 1;
                break;
            } else if (currentP2[i] > currentP1[i]) {
                numToReturn = -1;
                break;
            }
        }
        if (numToReturn != 0) {
            outcomes.add(numToReturn);
        }
        return numToReturn;
    }

    public static void addToScores(ArrayList<Word> list, Boolean p1) {
        int[] score = WordList.scoreList(list);
        if (p1) {
            p1Scores.add(score);
            p1Words.add(toStringArr(list));
        } else {
            p2Scores.add(score);
            p2Words.add(toStringArr(list));
        }
    }

    public static void resetStuff() {
        p1Scores = new ArrayList<int[]>();
        p2Scores = new ArrayList<int[]>();
        p1Words = new ArrayList<String[]>(5);
        p2Words = new ArrayList<String[]>(5);
        outcomes = new ArrayList<Integer>(5);
    }

    public static int getWordsSize(Boolean p1) {
        if (p1) {
            return p1Words.get(p1Words.size()-1).length;
        } else {
            return p2Words.get(p2Words.size()-1).length;
        }
    }

    public static String[] toStringArr(ArrayList<Word> list) {
        String[] returnArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            returnArr[i] = list.get(i).word;
        }
        return returnArr;
    }

    public static int getOverallWinner() {
        int total = 0;
        for (int i = 0; i < outcomes.size(); i++) {
            int num = outcomes.get(i);
            total += num;
            System.out.println("Outcome " + i + ": " + num);
        }
        return Integer.signum(total);
    }

    public static String[] report() {
        //longest word, rarest word, highest score, most words found
        String[] returnStrings = new String[4];
        ArrayList<String> longestWords = new ArrayList<String>();
        String rarestWord = "";
        ArrayList<String[]> allWords = new ArrayList<String[]>();
        allWords.addAll(p1Words);
        allWords.addAll(p2Words);
        int maxLength = 0;
        int maxRarity = 0;
        for (int i = 0; i < 2; i++) {
            for (String[] list : allWords) {
                for (String word : list) {
                    if (i == 0) {
                        maxLength = Math.max(maxLength, word.length());
                        maxRarity = Math.max(maxRarity, WordList.getWord(word).rarity);
                    } else {
                        if (word.length() == maxLength) {
                            longestWords.add(word);
                        }
                        if (WordList.getWord(word).rarity == maxRarity) {
                            rarestWord = word;
                        }
                    }
                }
            }
        }
        int p1Max = 0;
        int p2Max = 0;
        for (int[] score : p1Scores) {
            p1Max = Math.max(score[1], p1Max);
        }
        for (int[] score : p2Scores) {
            p2Max = Math.max(score[1], p2Max);
        }

        int p1Most = 0;
        int p2Most = 0;
        for (String[] list : p1Words) {
            p1Most = Math.max(p1Most, list.length);
        }
        for (String[] list : p2Words) {
            p2Most = Math.max(p2Most, list.length);
        }

        returnStrings[0] = "Longest Word(s): ";
        for (String word : longestWords) {
            returnStrings[0] += (word + ", ");
        }
        returnStrings[0] = returnStrings[0].substring(0, returnStrings[0].length() - 2);
        returnStrings[1] = "Rarest Word: " + rarestWord;
        returnStrings[2] = "Highest Score (By " + ((Math.max(p1Max, p2Max) == p1Max) ? "Player 1" : "Player 2")
                + ") was: " + (Math.max(p1Max, p2Max));
        returnStrings[3] = "Most Words (By " + ((Math.max(p1Most, p2Most) == p1Most) ? "Player 1" : "Player 2") + "): "
                + (Math.max(p1Most, p2Most));
        return returnStrings;
    }
}
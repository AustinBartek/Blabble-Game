package com.example;

public class Word {
    int rarity;
    int length;
    int frequency;
    String word;

    public Word(String word, int rarity, int frequency) {
        this.word = word;
        this.rarity = rarity;
        this.frequency = frequency;
        this.length = word.length();
    }
}
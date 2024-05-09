package com.example;

import java.util.*;

public class LetterBlockBox {
    ArrayList<LetterBlock> unusedBlocks;

    public LetterBlockBox() {
        resetBox();
    }

    public ArrayList<LetterBlock> draw(int num, int min, int max) {
        ArrayList<LetterBlock> returnList = new ArrayList<LetterBlock>(num);
        while (vowelCount(returnList) < min || vowelCount(returnList) > max || returnList.size() < 1) {
            for (int i = 0; i < num; i++) {
                returnList.add(this.unusedBlocks.remove(Blabble.random.nextInt(this.unusedBlocks.size())));
            }
            if (vowelCount(returnList) < min || vowelCount(returnList) > max) {
                this.unusedBlocks.addAll(returnList);
                returnList.clear();
            }
        }
        return returnList;
    }

    public int vowelCount(ArrayList<LetterBlock> list) {
        int vowelCount = 0;
        for (LetterBlock block : list) {
            for (int i = 0; i < WordList.vowels.length; i++) {
                if (block.letter == WordList.vowels[i]) {
                    vowelCount++;
                }
            }
        }
        return vowelCount;
    }

    public void resetBox() {
        this.unusedBlocks = new ArrayList<LetterBlock>(40);
        String lettersToFill = "aaabbccddeeeffgghhiiijkkllmmnnoooppqrrssstttuuuvwxyz";
        char[] letterArr = lettersToFill.toCharArray();
        for (char letter : letterArr) {
            this.unusedBlocks.add(new LetterBlock(letter));
        }
        Collections.shuffle(this.unusedBlocks);
    }
}
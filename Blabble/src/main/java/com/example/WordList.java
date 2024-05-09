package com.example;

import java.util.*;
import java.util.regex.*;

import java.io.*;

public class WordList {
  static File file;
  static Scanner scanner;
  static ArrayList<Word> words = new ArrayList<Word>(250000);
  static ArrayList<String> stringCheck = new ArrayList<String>(250000);
  static Pattern regex = Pattern.compile("[^A-Za-z0-9]");
  static char[] vowels = { 'a', 'e', 'i', 'o', 'u' };

  public static void initializeList() {
    try {
      file = new File("src/main/java/com/example/Words.txt");
      scanner = new Scanner(new FileInputStream(file), "UTF-8");
    } catch (FileNotFoundException e) {
      System.out.println("File 'Words.txt' not found!");
    }

    int rarity = 1;
    String text;
    int frequency = 156;
    while (scanner.hasNextLine() && frequency > 155) {
      text = scanner.nextLine();
      String[] textArr = text.split(" ");
      String word = textArr[0];
      rarity++;
      frequency = Integer.parseInt(textArr[1]);
      if (!containsSpecial(word) && !containsCapital(word) && word.length() < 10) {
        stringCheck.add(word);
        words.add(new Word(word, rarity, frequency));
      }
    }
    words.add(new Word("a", 5, 54631147));
    words.add(new Word("i", 105, 1861508));

    System.out.println("Word List Length: " + words.size());
  }

  public static Boolean containsSpecial(String word) {
    Boolean returnValue = (regex.matcher(word).find());
    return returnValue;
  }

  public static Boolean containsCapital(String word) {
    Boolean returnValue = false;
    char[] wordArr = word.toCharArray();
    for (char letter : wordArr) {
      String letterString = Character.toString(letter);
      if (letterString != letterString.toLowerCase()) {
        returnValue = true;
        break;
      }
    }
    return returnValue;
  }

  public static Boolean isVowel(char letter) {
    for (char vowel : vowels) {
      if (letter == vowel) {
        return true;
      }
    }
    return false;
  }

  public static Word getWord(String inWord) {
    inWord = inWord.toLowerCase();
    int index = stringCheck.indexOf(inWord);
    if (index != -1) {
      return words.get(stringCheck.indexOf(inWord));
    }
    return new Word("", 0, 0);
  }

  public static Boolean isWord(String word) {
    return (stringCheck.contains(word.toLowerCase()));
  }

  public static String removeLast(String word) {
    return (word.length() > 0) ? word.substring(0, word.length() - 1) : "";
  }

  public static int[] scoreList(ArrayList<Word> list) {
    int[] returnNums = new int[2];
    if (list.size() < 2 && Blabble.phase == 2) {
      return returnNums;
    }
    int maxLength = 0;
    int score = 0;
    for (Word word : list) {
      if (word.length != 0) {
        String text = word.word;
        if (text.endsWith("s") && Blabble.phase != 2 && Blabble.phase != 5) {
          String textRem = removeLast(text);
          if (list.contains(getWord(textRem))) {
            score -= (word.length - 1);
          }
          if (stringCheck.contains(textRem)) {
            maxLength = Math.max(maxLength, word.length - 1);
          } else {
            maxLength = Math.max(maxLength, word.length);
          }
          score += word.length;
        } else {
          maxLength = Math.max(maxLength, word.length);
          score += word.length;
        }
      }
    }
    returnNums[0] = maxLength;
    returnNums[1] = score;
    return returnNums;
  }

  // COMPUTER RESPONSE STUFF STARTS HERE!!!!!
  // OOOGA BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOGA

  public static ArrayList<Word> getListOfWords(ArrayList<LetterBlock> letterBlocks, int frequencyMin) {
    ArrayList<Word> returnArr = new ArrayList<Word>(1000);
    ArrayList<Character> letters = new ArrayList<Character>(9);
    Boolean lettersNull = (letterBlocks == null);

    if (!lettersNull) {
      for (LetterBlock block : letterBlocks) {
        letters.add(block.letter);
      }
    }

    for (Word word : words) {
      Boolean valid = true;
      char[] wordArr = word.word.toCharArray();
      ArrayList<Character> tempLetters = new ArrayList<Character>(9);
      tempLetters.addAll(letters);

      for (char letter : wordArr) {
        if (tempLetters.contains(letter)) {
          tempLetters.remove((Character) letter);
        } else {
          valid = false;
          break;
        }
      }

      if (valid || lettersNull) {
        if (word.frequency > frequencyMin) {
          returnArr.add(word);
        }
      }
    }

    return returnArr;
  }

  public static ArrayList<ArrayList<Word>> partitionByLength(ArrayList<Word> list) {
    ArrayList<ArrayList<Word>> returnList = new ArrayList<ArrayList<Word>>();
    int maxLength = 0;
    for (Word word : list) {
      maxLength = Math.max(maxLength, word.length);
    }
    for (int i = 0; i < maxLength; i++) {
      returnList.add(new ArrayList<Word>());
      for (Word word : list) {
        if (word.length == i + 1) {
          returnList.get(i).add(word);
        }
      }
    }
    return returnList;
  }

  public static ArrayList<Word> computeLetters(ArrayList<LetterBlock> letterBlocks) {
    ArrayList<Word> returnWords = new ArrayList<Word>(20);
    switch (Blabble.phase) {
      case 1:
      //CPU for normal blabble
        ArrayList<Word> validWords = getListOfWords(letterBlocks, 20000);
        sortForCpu(validWords);

        System.out.println(Blabble.formString(letterBlocks));
        System.out.println("Possible words size: " + validWords.size());
        returnWords.add(validWords.remove(validWords.size() - 1));
        for (int i = 0; i < (Blabble.random.nextInt(3) + 1) * (Blabble.random.nextInt(2) + 1)
            + Math.floor(validWords.size() / 8.0); i++) {
          returnWords.add(validWords.remove(Blabble.random.nextInt(validWords.size())));
        }
        break;
      case 2:
      //CPU for super blabble
        validWords = getListOfWords(letterBlocks, 20000);
        sortForCpu(validWords);
        int offset = validWords.size() - Math.min(validWords.size(), 20);
        Word word1 = validWords.remove(Blabble.random.nextInt(validWords.size() - offset) + offset);
        char[] word1Arr = word1.word.toCharArray();

        System.out.println(Blabble.formString(letterBlocks));
        System.out.println("Possible words size: " + validWords.size());
        ArrayList<LetterBlock> newLetters = new ArrayList<LetterBlock>();
        newLetters.addAll(letterBlocks);
        for (char letter : word1Arr) {
          for (LetterBlock block : letterBlocks) {
            if (block.letter == letter) {
              newLetters.remove(block);
              break;
            }
          }
        }

        validWords = getListOfWords(newLetters, 20000);
        Word word2 = new Word("", 0, 0);
        if (validWords.size() > 0) {
          word2 = validWords.remove(Blabble.random.nextInt(validWords.size()));
        } else {
          word1 = new Word("", 0, 0);
        }
        returnWords.add(word1);
        returnWords.add(word2);
        break;
      case 3:
      //CPU for battle
        System.out.println("Final Phase!");
        ArrayList<ArrayList<Word>> usableWords = partitionByLength(getListOfWords(null, 20000));
        int[] drawNumbers = {2, Blabble.random.nextInt(3) + 3, Blabble.random.nextInt(4) + 2, Blabble.random.nextInt(3) + 1, Blabble.random.nextInt(2), Blabble.random.nextInt(Blabble.random.nextInt(1) + 1)};
        for (int i = 0; i < drawNumbers.length; i++) {
          for (int j = 0; j < drawNumbers[i]; j++) {
            returnWords.add(usableWords.get(i).remove(Blabble.random.nextInt(usableWords.get(i).size())));
          }
        }
        break;
      case 5:
      //CPU for tiebreaker
        usableWords = partitionByLength(getListOfWords(Blabble.openLetters, 30000));
        int index;
        if (usableWords.get(3).size() > 2) {
          index = 3;
        } else if (usableWords.get(2).size() > 1) {
          index = 2;
        } else if (usableWords.get(1).size() > 0) {
          index = 1;
        } else {
          index = 0;
        }
        returnWords.add(usableWords.get(index).get(Blabble.random.nextInt(usableWords.get(index).size())));
        break;
    }
    return returnWords;
  }

  public static void sortForCpu(ArrayList<Word> list) {
    Collections.sort(list, new Comparator<Word>() {
      @Override
      public int compare(Word a, Word b) {
        int lengthCompare = (a.length - b.length);
        int frequencyCompare = (a.frequency - b.frequency);
        return (lengthCompare == 0) ? frequencyCompare : lengthCompare;
      }
    });
  }
}
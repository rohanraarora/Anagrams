package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if(lettersToWord.containsKey(sortedWord)){
                lettersToWord.get(sortedWord).add(word);
            }
            else {
                ArrayList<String> anagramGroup = new ArrayList<>();
                anagramGroup.add(word);
                lettersToWord.put(sortedWord,anagramGroup);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord = sortLetters(targetWord);
        if(lettersToWord.containsKey(sortedWord)){
            result.addAll(lettersToWord.get(sortedWord));
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        int CHAR_a = 97;
        int CHAR_z = 122;
        for(int i = CHAR_a;i<=CHAR_z;i++){
            char c = (char) i;
            String oneMoreLetterWord = word.concat(String.valueOf(c));
            ArrayList<String> anagrams = getAnagrams(oneMoreLetterWord);
            result.addAll(anagrams);
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String randomWord = wordList.get(random.nextInt(wordList.size()-1));
        ArrayList<String> anagrams = getAnagramsWithOneMoreLetter(sortLetters(randomWord));
        if(anagrams.size() >= MIN_NUM_ANAGRAMS){
            return randomWord;
        }
        return pickGoodStarterWord();
    }

    public String sortLetters(String word){
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
}

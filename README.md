# Anagrams
Applied CS Unit 1: Anagrams Codelab

An anagram is a word formed by rearranging the letters of another word. For example, cinema is an anagram of iceman.

The mechanics of the game are as follows:

* The game provides the user with a word from the dictionary.
* The user tries to create as many words as possible that contain all the letters of the given word plus one additional letter. Note that adding the extra letter at the beginning or the end without reordering the other letters is not valid. For example, if the game picks the word 'ore' as a starter, the user might guess 'rose' or 'zero' but not 'sore'.
* The user can give up and see the words that they did not guess.

In order to ensure that the game is not too difficult, the computer will only propose words that have at least 5 possible valid anagrams.

![ScreenShot](https://cswithandroid.withgoogle.com/content/assets/img/anagrams.png)

## Tour of the code

The starter code is composed of two java classes:

1. `AnagramsActivity`: In Android development an Activity is a single, focused thing that the user can do. Most of our apps in this class will have a single activity but often apps are made up of multiple activities (e.g. login, settings, etc.). The starter code implements several methods:
  * `onCreate`: this method gets called by the system when the app is launched. It is made up of some boilerplate code plus code that open the word list to initialize the dictionary and code to connect the text box to the processWord helper.
  * `processWord`: a helper that adds words to the UI and colors them
  * `onCreateOptionsMenu`: boilerplate
  * `onOptionsItemSelected`: boilerplate
  * `defaultAction`: this is the handler that is called when the floating button is clicked. Depending on the game mode, it either starts the game or shows the missing answer to the previous game.

2. `AnagramDictionary`: This class will store the valid words from the text file and handle selecting and checking words for the game. This is where your code will among the following methods:
  * `AnagramDictionary`: The constructor. It should store the words in the appropriate data structures (details below).
  * `isGoodWord`: Asserts that the given word is in the dictionary and isn't formed by adding a letter to the start or end of the base word.
  * `getAnagrams`: Creates a list of all possible anagrams of a given word.
  * `getAnagramsWithOneMoreLetter`: Creates a list of all possible words that can be formed by adding one letter to the given word.
  * `pickGoodStarterWord`: Randomly selects a word with at least the desired number of anagrams.


## [Implementation](https://github.com/rohanraarora/Anagrams/tree/step-1-implemetation)

We will start by implementing a simplified version of the game that has the user guess anagrams of the given word.

To do so, your first task will be to advance the implementation of the AnagramDictionary's constructor. Each word that is read from the dictionary file should be stored in an ArrayList (called `wordList`).

We will store duplicates of our words in some other convenient data structures later but `wordList` will do for now.

## [getAnagrams](https://github.com/rohanraarora/Anagrams/tree/step-2-get-anagrams)

Implement `getAnagrams` which takes a string and finds all the anagrams of that string in our input. Our strategy for now will be straight-forward: just compare each string in `wordList` to the input word to determine if they are anagrams. But how shall we do that?

There are different strategies that you could employ to determine whether two strings are anagrams of each other (like counting the number of occurences of each letter) but for our purpose you will create a helper function (call it `sortLetters`) that takes a String and returns another String with the same letters in alphabetical order (e.g. "post" -> "opst"). Determining whether two strings are anagrams is then a simple matter of checking that they are the same length (for the sake of speed) and checking that the sorted versions of their letters are equal.

At this point, you should have a working app so try running it on your device and verify that it works. You can change the hard-coded return value of `pickGoodStarterWord` to try out your code with different words (e.g. "skate").

## [Forming anagrams with an added letter](https://github.com/rohanraarora/Anagrams/tree/step-3-use-hashset-hashmap)

Unfortunately, the straight-forward strategy will be too slow for us to implement the rest of this game. So we will need to revisit our constructor and find some data structures that store the words in ways that are convenient for our purposes. We will create two new data structures (in addition to `wordList`):

A `HashSet` (called `wordSet`) that will allow us to rapidly (in O(1)) verify whether a word is valid.

A `HashMap` (called `lettersToWord`) that will allow us to group anagrams together. We will do this by using the `sortLetters` version of a string as the key and storing an ArrayList of the words that correspond to that key as our value. For example, we may have an entry of the form: key: "opst" value: ["post", "spot", "pots", "tops", ...].
As you process the input words, call `sortLetters` on each of them then check whether `lettersToWord` already contains an entry for that key. If it does, add the current word to ArrayList at that key. Otherwise, create a new ArrayList, add the word to it and store in the HashMap with the corresponding key.

## [isGoodWord](https://github.com/rohanraarora/Anagrams/tree/step-4-isGoodWord)

Your next task is to implement `isGoodWord` which checks:

the provided word is a valid dictionary word (i.e., in `wordSet`), and
the word does not contain the base word as a substring.
For example, with the base word 'post':

Input                      | Output
---------------------------| ------
isGoodWord("nonstop")      | true
isGoodWord("poster")       | false
isGoodWord("lamp post")    | false
isGoodWord("spots")        | true
isGoodWord("apostrophe")   | false
Checking whether a word is a valid dictionary word can be accomplished by looking at `wordSet` to see if it contains the word. Checking that the word does not contain the base word as a substring is left as a challenge!

## [getAnagramsWithOneMoreLetter](https://github.com/rohanraarora/Anagrams/tree/step-5-anagrams-with-one-more-letter)

Finally, implement `getAnagramsWithOneMoreLetter` which takes a string and finds all anagrams that can be formed by adding one letter to that word.

Be sure to instantiate a new ArrayList as your return value then check the given word + each letter of the alphabet one by one against the entries in `lettersToWord`.

At this point, you should have a working app so try running your app on your device and verify that it works although the game will be a bit boring since it will always use the same starter word.

## [pickGoodStarterWord](https://github.com/rohanraarora/Anagrams/tree/step-6-good-starter-word)

If your game is working, proceed to implement `pickGoodStarterWord` to make the game more interesting. Pick a random starting point in the `wordList` array and check each word in the array until you find one that has at least `MIN_NUM_ANAGRAMS` anagrams. Be sure to handle wrapping around to the start of the array if needed. Run your app again to make sure it's working.

## [Refactoring](https://github.com/rohanraarora/Anagrams/tree/step-7-refactoring)

At this point, the game is functional but can be quite hard to play if you start off with a long base word. To avoid this, let's refactor `AnagramDictionary` to give words of increasing length.

This refactor starts in the constructor where, in addition to populating `wordList`, you should also store each word in a HashMap (let's call it `sizeToWords`) that maps word length to an ArrayList of all words of that length. This means, for example, you should be able to get all four-letter words in the dictionary by calling `sizeToWords.get(4)`.

You should also create a new member variable called `wordLength` and default it to `DEFAULT_WORD_LENGTH`. Then in `pickGoodStarterWord`, restrict your search to the words of length `wordLength`, and once you're done, increment `wordLength` (unless it's already at `MAX_WORD_LENGTH`) so that the next invocation will return a larger word.

## Extensions

This activity (like all future activities) contains some optional extensions. If time permits, we would like each group to attempt at least one extension, either from the list below or one that you have invented yourselves.

- [x] [Two-letter mode](https://github.com/rohanraarora/Anagrams/tree/ext-1-two-letter-mode): switch to allowing the user to add two letters to form anagrams
- [ ] Optimize word selection by removing words that do not have enough anagrams from the pool of possible starter words. Note that those words should still remain in `wordSet` since they can still be used as anagrams to other words.
- [ ] Two-word mode: Allow the user to add one letter to a pair of words to form two new valid words.

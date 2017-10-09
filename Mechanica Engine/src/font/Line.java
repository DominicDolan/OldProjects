package font;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of text during the loading of a text.
 * 
 * @author Karl
 *
 */
public class Line {

	private double maxLength;
	private double spaceSize;
	private double fontSize;

	private Word[] savedWords = new Word[32];
	private int currentWord = 0;
	private List<Word> words = new ArrayList<Word>();
	private double currentLineLength = 0;

	protected Line() {
		for (int i = 0; i < savedWords.length; i++) {
			savedWords[i] = new Word();
		}
	}

	protected int getNumberOfWords(){
		return currentWord+1;
	}

	protected void set(double spaceWidth, double fontSize, double maxLength){
		currentWord = 0;
		currentLineLength = 0;
		this.spaceSize = spaceWidth * fontSize;
		this.maxLength = maxLength;
		for (int i = 0; i < savedWords.length; i++) {
			savedWords[i].setFontSize(fontSize);
		}
		savedWords[0].set();

	}

	protected void addWord(){
		currentWord++;
		savedWords[currentWord].set();
	}


	/**
	 * Creates an empty line.
	 * 
	 * @param spaceWidth
	 *            - the screen-space width of a space character.
	 * @param fontSize
	 *            - the size of font being used.
	 * @param maxLength
	 *            - the screen-space maximum length of a line.
	 */
	protected Line(double spaceWidth, double fontSize, double maxLength) {
		this.spaceSize = spaceWidth * fontSize;
		this.maxLength = maxLength;
	}

	/**
	 * Attempt to add a word to the line. If the line can fit the word in
	 * without reaching the maximum line length then the word is added and the
	 * line length increased.
	 * 
	 * @param word
	 *            - the word to try to add.
	 * @return {@code true} if the word has successfully been added to the line.
	 */
	protected boolean attemptToAddWord(Word word) {
		double additionalLength = word.getWordWidth();
		additionalLength += !words.isEmpty() ? spaceSize : 0;
		if (currentLineLength + additionalLength <= maxLength) {
			words.add(word);
			currentLineLength += additionalLength;
			return true;
		} else {
			return false;
		}
	}

	protected boolean attemptToAddWord() {
		double additionalLength = savedWords[currentWord].getWordWidth();
		additionalLength +=  currentWord==0? 0 : spaceSize;
		if (currentLineLength + additionalLength <= maxLength) {
			addWord();
			currentLineLength += additionalLength;
			return true;
		} else {
			return false;
		}
	}

	protected void setWord(int word){
		savedWords[word].set();
	}

	protected void setCurrentWord(){
		savedWords[currentWord].set();
	}


	protected Word getWord(int word){
		return savedWords[word];
	}

	protected Word getCurrentWord(){
		return savedWords[currentWord];
	}

	/**
	 * @return The max length of the line.
	 */
	protected double getMaxLength() {
		return maxLength;
	}

	/**
	 * @return The current screen-space length of the line.
	 */
	protected double getLineLength() {
		return currentLineLength;
	}

	protected void arrayToList(){
		for (int i = 0; i <= currentWord; i++) {
			words.add(savedWords[i]);
		}
	}
	/**
	 * @return The list of words in the line.
	 */
	protected List<Word> getWords() {
		return words;
	}

}

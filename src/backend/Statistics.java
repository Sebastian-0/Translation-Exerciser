package backend;

public class Statistics {
	public int amountOfWords;
	public int amountOfTranslations;
	
	/* Words with no answer attempts */
	public int wordsNoAnswer;
	/* Words with no correct answer (includes words with "no answer") */
	public int wordsIncorrect;
	/* Words with at least one correct answer and at least one translation left */
	public int wordsPartiallyCorrect;
	/* Words that were correct but there were error(s) along the way */
	public int wordsCorrect;
	/* Words that were correct with no errors */
	public int wordsCorrectNoErrors;

	/* Amount of translations that got no answer (both unanswered and those with only incorrect answers) */
	public int translationsNotAnswered;
	/* Amount of correct translations, there may have been errors along the way */
	public int translationsCorrect;
	/* Amount of incorrect translation attempts, also includes one for each "not answered" */
	public int translationsIncorrect;
}

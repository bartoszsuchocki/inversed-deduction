package flowmanager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import deduction.DeductionEngine;
import deduction.DeductionResult;
import deduction.KnowledgeBase;
import logger.Logger;

public class MainFlow {
	private final static String FORMULA_SOURCE_QUESTION = "Do you want to populate knowledge base with formulas from file? (y/n)";
	private final static String QUESTION_FORMULA_SOURCE_QUESTION = "Do you want to populate question formula from file? (y/n)";
	private final static String RESULT_DESTINATION_QUESTION = "Do you want to write results to a file? (y/n)";
	private final static String FILE_PATH_QUESTION = "Specify the file path";
	private final static String SPECIFY_FORMULA_QUESTION = "Specify formula. To finish, specify 'f'";
	
	public static void main(String[] args) throws IOException {
		InputManager inputManager = new InputManager();
		setupLogger(inputManager);
		inputManager.openFileForReading("input.txt");
		
		KnowledgeBase knowledgeBase = prepareKnowledgeBase(inputManager);
		DeductionEngine deductionEngine = new DeductionEngine();
		deductionEngine.setKnowledgeBase(knowledgeBase);
		
		if(isQuestionFormulaFromFile(inputManager)) {
			List<String> questionFormulaLinesFromFile = prepareQuestionFormulasFromFile(inputManager);
			runForGivenQuestionFormulaList(questionFormulaLinesFromFile, deductionEngine);
		} else {
			runForQuestionFormulasTypedByUser(deductionEngine, inputManager);
		}
		
		closeLogger();
	}
	
	private static void runForGivenQuestionFormulaList(List<String> questionFormulaLines, DeductionEngine deductionEngine) {
		questionFormulaLines.stream().forEach(questionFormulaLine -> {
			DeductionResult result = deductionEngine.ask(questionFormulaLine);
			Logger.log(result.toString());
			Logger.log("Finished checking question formula.");
		});
	}
	
	private static void runForQuestionFormulasTypedByUser(DeductionEngine deductionEngine, InputManager inputManager) {
		String currentQuestionFormulaLine = getQuestionFormulaFromUser(inputManager);
		while(!currentQuestionFormulaLine.equals("f")) {
			DeductionResult result = deductionEngine.ask(currentQuestionFormulaLine);
			Logger.log(result.toString());
			Logger.log("Finished checking question formula.");
			currentQuestionFormulaLine = getQuestionFormulaFromUser(inputManager);
		}
	}
	
	private static KnowledgeBase prepareKnowledgeBase(InputManager inputManager) throws IOException {
		KnowledgeBase knowledgeBase = new KnowledgeBase();
		
		boolean populateKnowledgeBaseFromFile = inputManager.getAnswerForStdinQuestion(FORMULA_SOURCE_QUESTION).equals("y");
		if(populateKnowledgeBaseFromFile) {
			knowledgeBase.readFormulas(inputManager.getAnswerForStdinQuestion(FILE_PATH_QUESTION));
		} else {
			String formulaLine=inputManager.getAnswerForStdinQuestion(SPECIFY_FORMULA_QUESTION);
			while(!formulaLine.equals("f")) {
				knowledgeBase.tell(formulaLine);
				formulaLine = inputManager.getAnswerForStdinQuestion(SPECIFY_FORMULA_QUESTION);
			}
		}
		return knowledgeBase;
	}
	
	private static List<String> prepareQuestionFormulasFromFile(InputManager inputManager) throws IOException {
		inputManager.openFileForReading(inputManager.getAnswerForStdinQuestion(FILE_PATH_QUESTION));
		List<String> questionFormulaLines = inputManager.readLines().collect(Collectors.toList());
		inputManager.finishReading();
		return questionFormulaLines;
	}
	
	private static String getQuestionFormulaFromUser(InputManager inputManager) {
		return inputManager.getAnswerForStdinQuestion(SPECIFY_FORMULA_QUESTION);
	}
	
	private static boolean isQuestionFormulaFromFile(InputManager inputManager) {
		return inputManager.getAnswerForStdinQuestion(QUESTION_FORMULA_SOURCE_QUESTION).equals("y");
	}
	
	private static void setupLogger(InputManager inputManager) throws IOException {
		boolean showResultsInFile = inputManager.getAnswerForStdinQuestion(RESULT_DESTINATION_QUESTION).equals("y");
		if(showResultsInFile) {
			Logger.initLogger(inputManager.getAnswerForStdinQuestion(FILE_PATH_QUESTION));
		} else {
			Logger.initLogger(Logger.STDOUT_STRING);
		}
	}
	private static void closeLogger() throws IOException {
		Logger.close();
	}
}

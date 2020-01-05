package deduction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import entity.Formula;
import entity.Formula.FormulaType;
import entity.Predicate;
import entity.Term;
import entity.Term.TermType;
import logger.Logger;

public class DeductionEngine {
	private KnowledgeBase knowledgeBase;
	
	public DeductionResult ask(Formula questionFormula) {
		Map<String, String> substitutions = new HashMap<>();
		return formulaFulfilled(questionFormula, substitutions);
	}
	
	private DeductionResult formulaFulfilled(Formula formula, Map<String, String> currentSubstitutions) { 
		Logger.log("Deduction engine: checking if formula can be fullfilled: " + formula);
		
		if(formula.getType().equals(FormulaType.OR)) {
			return DeductionResult.or(
				formulaFulfilled(formula.getLeftChild(), currentSubstitutions), 
				formulaFulfilled(formula.getRightChild(), currentSubstitutions)
			);
		}
		
		if(formula.getType().equals(FormulaType.AND)) {
			return DeductionResult.and(
				formulaFulfilled(formula.getLeftChild(), currentSubstitutions), 
				formulaFulfilled(formula.getRightChild(), currentSubstitutions)
			);
		}
		
		Predicate predicate = formula.getPredicate();
		List<Formula> formulasForPredicate = knowledgeBase.getFormulasByPredicateType(predicate.getType());
		if(formulasForPredicate.isEmpty()) {
			Logger.log("No appropriate " + predicate.getType() + " predicates found in KB. Failure");
		}
		
		for(Formula knowledgeBaseFormula : formulasForPredicate) {
			Predicate kbPredicate = knowledgeBaseFormula.getPredicate();
			Logger.log("Found predicate to check in KB: " + kbPredicate);
			if(kbPredicate.isNegation() != predicate.isNegation()) {
				Logger.log("Deduction engine: predicate is negated in KB, failure");
				continue;
			}
			
			try {
				Optional<SubstitutionWrapper> leftSubstitution =  makeSubstitution(predicate.getLeftTerm(), kbPredicate.getLeftTerm(), currentSubstitutions);
				Optional<SubstitutionWrapper> rightSubstitution =  makeSubstitution(predicate.getRightTerm(), kbPredicate.getRightTerm(), currentSubstitutions);
				
				Map<String, String> newSubstitutions = new HashMap<>(currentSubstitutions);
				if(leftSubstitution.isPresent()) {
					newSubstitutions.put(leftSubstitution.get().originalValue, leftSubstitution.get().substitutedValue);
				}
				if(rightSubstitution.isPresent()) {
					newSubstitutions.put(rightSubstitution.get().originalValue, rightSubstitution.get().substitutedValue);
				}
				
				Logger.log("DeductionEngine: formula fullfiled with substitutions: " + newSubstitutions);
				return new DeductionResult(true, newSubstitutions);
			} catch(InvalidSubstitutionException e) {
				Logger.log("Deduction engine: " + e.getMessage());
				continue;
			}
		}
		return new DeductionResult(false, new HashMap<String,String>());
	}
	
	private Optional<SubstitutionWrapper> makeSubstitution(Term baseTerm, Term substitutionTerm, Map<String, String> currentSubstitutions) throws InvalidSubstitutionException {
		if(baseTerm.getType().equals(TermType.CONSTANT)) { 
			if(!substitutionTerm.getValue().equals(baseTerm.getValue())) {
				throw new InvalidSubstitutionException("Substitution failed. Constant " + baseTerm.getValue() + " not equal to " + substitutionTerm.getValue());
			}
			Logger.log("Deduction engine: Substitution unnecessary, because constants are equal. Constant value: " + baseTerm.getValue());
			return Optional.empty();
		}
		
		String variableValue = baseTerm.getValue();
		String substitutionValue = substitutionTerm.getValue();
		
		if(currentSubstitutions.get(variableValue) != null && !currentSubstitutions.get(variableValue).equals(substitutionValue)) {
			throw new InvalidSubstitutionException("Substitution failed: " + variableValue + " is already assigned to " + 
				currentSubstitutions.get(variableValue) + " , so it can not be substituted with " + substitutionValue);
		}
		
		SubstitutionWrapper resultSubstitution = new SubstitutionWrapper(variableValue, substitutionValue);
		Logger.log("Performing substitution " + resultSubstitution);
		
		return Optional.of(resultSubstitution);
	}
	
	public DeductionResult ask(String questionFormulaLine) {
		Formula questionFormula = FormulaParser.parseFormulaLine(questionFormulaLine);
		questionFormula.setQuestion(true);
		return ask(questionFormula);
	}

	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}
	
	private class SubstitutionWrapper{
		public String originalValue;
		public String substitutedValue;
		
		public SubstitutionWrapper(String originalValue, String substitutedValue) {
			this.originalValue = originalValue;
			this.substitutedValue = substitutedValue;
		}
		
		@Override
		public String toString() {
			return originalValue + " -> " + substitutedValue;
		}
	}
	
	private class InvalidSubstitutionException extends Exception{
		public InvalidSubstitutionException(String message) {
			super(message);
		}
	}
}

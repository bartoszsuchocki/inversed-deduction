package test;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import deduction.DeductionEngine;
import deduction.DeductionResult;
import deduction.KnowledgeBase;
import entity.Formula;
import entity.Formula.FormulaType;
import entity.Predicate;
import entity.Predicate.PredicateType;
import entity.Term;

public class DeductionEngineTest {
	
	@Test
	public void shouldReturnProperDeductionResultConstantsInQuestion() {
		List<Formula> knowledgeBaseFormulas = Arrays.asList(
			new Formula(new Predicate(new Term("ORZECHY"), new Term("SANTE"), PredicateType.PRODUCENT))
		);
		
		KnowledgeBase knowledgeBase = new KnowledgeBase();
		knowledgeBase.setFormulas(knowledgeBaseFormulas);
		
		DeductionEngine deductionEngine = new DeductionEngine();
		deductionEngine.setKnowledgeBase(knowledgeBase);
		
		Formula questionFormula = new Formula(new Predicate(new Term("ORZECHY"), new Term("SANTE"), PredicateType.PRODUCENT));
		
		DeductionResult result = deductionEngine.ask(questionFormula);
		assertEquals(true, result.isFulfilled());	
	}
	
	@Test
	public void shouldReturnProperDeductionResultVariableInQuestion() {
		List<Formula> knowledgeBaseFormulas = Arrays.asList(
			new Formula(new Predicate(new Term("TOPAZ"), new Term("MLEKO"), PredicateType.OFFERS)),
			new Formula(new Predicate(new Term("MLEKO"), new Term("SANTE"), PredicateType.PRODUCENT))
		);
		
		KnowledgeBase knowledgeBase = new KnowledgeBase();
		knowledgeBase.setFormulas(knowledgeBaseFormulas);
		
		DeductionEngine deductionEngine = new DeductionEngine();
		deductionEngine.setKnowledgeBase(knowledgeBase);
		
		Formula questionFormula = new Formula(FormulaType.AND);
		questionFormula.setLeftChild(new Formula(new Predicate(new Term("TOPAZ"), new Term("x"), PredicateType.OFFERS)));
		questionFormula.setRightChild(new Formula(new Predicate(new Term("x"), new Term("SANTE"), PredicateType.PRODUCENT)));
		
		DeductionResult result = deductionEngine.ask(questionFormula);
		assertEquals(true, result.isFulfilled());
		assertEquals("MLEKO", result.getVariableSubstitutions().get("x"));		
	}
	
	@Test
	public void shouldReturnFailedDeductionResultForNegatedPredicate() {
		List<Formula> knowledgeBaseFormulas = Arrays.asList(
			new Formula(new Predicate(new Term("ORZECHY"), new Term("POLSKA"), PredicateType.COUNTRY, true))
		);
		
		KnowledgeBase knowledgeBase = new KnowledgeBase();
		knowledgeBase.setFormulas(knowledgeBaseFormulas);
		
		DeductionEngine deductionEngine = new DeductionEngine();
		deductionEngine.setKnowledgeBase(knowledgeBase);
		
		Formula questionFormula = new Formula(new Predicate(new Term("ORZECHY"), new Term("POLSKA"), PredicateType.COUNTRY));
		
		DeductionResult result = deductionEngine.ask(questionFormula);
		assertTrue(!result.isFulfilled());
		assertTrue(result.getVariableSubstitutions().isEmpty());
	}
	
	@Test
	public void shouldReturnFailedDeductionResultForNoMatchedPredicateInKB() {
		List<Formula> knowledgeBaseFormulas = Arrays.asList(
			new Formula(new Predicate(new Term("ORZECHY"), new Term("POLSKA"), PredicateType.COUNTRY))
		);
		
		KnowledgeBase knowledgeBase = new KnowledgeBase();
		knowledgeBase.setFormulas(knowledgeBaseFormulas);
		
		DeductionEngine deductionEngine = new DeductionEngine();
		deductionEngine.setKnowledgeBase(knowledgeBase);
		
		Formula questionFormula = new Formula(new Predicate(new Term("ORZECHY"), new Term("KOLUMBIA"), PredicateType.COUNTRY));
		
		DeductionResult result = deductionEngine.ask(questionFormula);
		assertTrue(!result.isFulfilled());
		assertTrue(result.getVariableSubstitutions().isEmpty());
	}
}

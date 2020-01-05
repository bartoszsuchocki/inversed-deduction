package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import deduction.FormulaParser;
import entity.Formula;
import entity.Predicate;
import entity.Term;
import entity.Formula.FormulaType;
import entity.Predicate.PredicateType;
import entity.Term.TermType;

public class FormulaParserTest {
	
	@Test
	public void shouldParseFormulaLine() {
		String formulaLine = "offers(MLEKO,TOPAZ)";
		
		Term leftTerm = new Term("MLEKO", TermType.CONSTANT);
		Term rightTerm = new Term("TOPAZ", TermType.CONSTANT);
		
		Predicate predicate = new Predicate(leftTerm, rightTerm, PredicateType.OFFERS);
		
		Formula expectedFormula = new Formula();
		expectedFormula.setType(FormulaType.ATOM);
		expectedFormula.setPredicate(predicate);
		
		Formula resultFormula = FormulaParser.parseFormulaLine(formulaLine);
		
		assertEquals(expectedFormula, resultFormula);
	}
	
	@Test
	public void shouldParseFormulaLineTwoPredicates() {
		String formulaLine = "offers(TOPAZ,MLEKO) and producent(MLEKO,SANTE)";
			
		Predicate firstPredicate = new Predicate(
			new Term("TOPAZ", TermType.CONSTANT), 
			new Term("MLEKO", TermType.CONSTANT), 
			PredicateType.OFFERS
		);
		Predicate secondPredicate = new Predicate(
			new Term("MLEKO", TermType.CONSTANT), 
			new Term("SANTE", TermType.CONSTANT), 
			PredicateType.PRODUCENT
		);
		
		Formula expectedFormula = new Formula();
		expectedFormula.setType(FormulaType.AND);
		expectedFormula.setLeftChild(new Formula(firstPredicate));
		expectedFormula.setRightChild(new Formula(secondPredicate));
		
		Formula resultFormula = FormulaParser.parseFormulaLine(formulaLine);
		
		assertEquals(expectedFormula, resultFormula);
	}
	
	@Test
	public void shouldParseFormulaLineTwoPredicatesWithVariable() {
		String formulaLine = "offers(TOPAZ,x) and producent(x,SANTE)";
			
		Predicate firstPredicate = new Predicate(
			new Term("TOPAZ", TermType.CONSTANT), 
			new Term("x", TermType.VARIABLE), 
			PredicateType.OFFERS
		);
		Predicate secondPredicate = new Predicate(
			new Term("x", TermType.VARIABLE), 
			new Term("SANTE", TermType.CONSTANT), 
			PredicateType.PRODUCENT
		);
		
		Formula expectedFormula = new Formula();
		expectedFormula.setType(FormulaType.AND);
		expectedFormula.setLeftChild(new Formula(firstPredicate));
		expectedFormula.setRightChild(new Formula(secondPredicate));
		
		Formula resultFormula = FormulaParser.parseFormulaLine(formulaLine);
		
		assertEquals(expectedFormula, resultFormula);
	}
	
	@Test
	public void shouldParseFormulaLineNegationPredicate() {
		String formulaLine = "!offers(TOPAZ,x) and producent(x,SANTE)";
			
		Predicate firstPredicate = new Predicate(
			new Term("TOPAZ", TermType.CONSTANT), 
			new Term("x", TermType.VARIABLE), 
			PredicateType.OFFERS,
			true
		);
		Predicate secondPredicate = new Predicate(
			new Term("x", TermType.VARIABLE), 
			new Term("SANTE", TermType.CONSTANT), 
			PredicateType.PRODUCENT
		);
		
		Formula expectedFormula = new Formula();
		expectedFormula.setType(FormulaType.AND);
		expectedFormula.setLeftChild(new Formula(firstPredicate));
		expectedFormula.setRightChild(new Formula(secondPredicate));
		
		Formula resultFormula = FormulaParser.parseFormulaLine(formulaLine);
		
		assertEquals(expectedFormula, resultFormula);
	}
}

package deduction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entity.Formula;
import entity.Formula.FormulaType;
import entity.Predicate;
import entity.Predicate.PredicateType;
import entity.Term;
import logger.Logger;

public class FormulaParser {
	
	private static final String OR_RELATION = "or";
	private static final String AND_RELATION = "and";
	
	private static Map<String, PredicateType> predicateStringToType = Stream.of(PredicateType.values())
		.collect(Collectors.toMap(PredicateType::toString, Function.identity()));
	
	private static Map<String, String> formulaTypeStringToRelation = Stream.of(new String[][] {
		{ FormulaType.OR.toString(), OR_RELATION }, 
		{ FormulaType.AND.toString(), AND_RELATION}, 
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	private static Optional<FormulaType> getNextRelation(String line) {
		if(line.indexOf(OR_RELATION) != -1) {
			return Optional.of(FormulaType.OR);
		}
		if(line.indexOf(AND_RELATION) != -1) {
			return Optional.of(FormulaType.AND);
		}
		return Optional.empty();
	}
	
	private static Formula wrapFormulas(List<Formula> atomicFormulas, List<FormulaType> relations) {
		Formula firstFormulasWrapper = new Formula(relations.get(0));
		firstFormulasWrapper.setLeftChild(atomicFormulas.get(0));
		firstFormulasWrapper.setRightChild(atomicFormulas.get(1));
		
		if(atomicFormulas.size() == 2) {
			return firstFormulasWrapper;
		} else {
			List<Formula> alteredFormulas = new ArrayList<>();
			alteredFormulas.add(firstFormulasWrapper);
			alteredFormulas.addAll(atomicFormulas.subList(2, atomicFormulas.size()));
			return wrapFormulas(alteredFormulas, relations.subList(1, relations.size()));
		}
	}
	
	private static Formula wrapFormulasFromPredicates(List<Predicate> predicates, List<FormulaType> relations) {
		if(predicates.size() == 1) {
			return new Formula(predicates.get(0));
		}
		
		List<Formula> atomicFormulas = predicates.stream()
			.map(predicate -> new Formula(predicate))
			.collect(Collectors.toList());
		
		return wrapFormulas(atomicFormulas, relations);
	}
	
	public static Formula parseFormulaLine(String formulaLine) {
		String leftToParse = formulaLine.replace(" ", "");
		List<Predicate> predicates = new ArrayList<>();
		List<FormulaType> relations = new ArrayList<>();
		
		while(!leftToParse.isEmpty()) {
			boolean predicateNegation = false;
			if(leftToParse.charAt(0) == '!') {
				predicateNegation = true;
				leftToParse = leftToParse.substring(1);
			}
			
			int openBracket = leftToParse.indexOf('(');
			int closeBracket = leftToParse.indexOf(')');
			int comaIndex = leftToParse.indexOf(',');
			
			PredicateType predicateType = predicateStringToType.get(leftToParse.substring(0, openBracket));
			String term1Value = leftToParse.substring(openBracket + 1, comaIndex);
			String term2Value = leftToParse.substring(comaIndex +1, closeBracket);
			
			predicates.add(new Predicate(new Term(term1Value), new Term(term2Value), predicateType, predicateNegation));
			
			Optional<FormulaType> nextRelation = getNextRelation(leftToParse);
			
			if(nextRelation.isPresent()) {
				FormulaType relation = nextRelation.get();
				relations.add(relation);
				String textRelation = formulaTypeStringToRelation.get(relation.toString());
				leftToParse = leftToParse.substring(leftToParse.indexOf(textRelation) + textRelation.length());
			}
			else {
				leftToParse = "";
			}
		}
		
		Formula resultFormula = wrapFormulasFromPredicates(predicates, relations);	
		Logger.log("Formula parser: parsed formula: " + resultFormula);
		
		return resultFormula;
	}
}

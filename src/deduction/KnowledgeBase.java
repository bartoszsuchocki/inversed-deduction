package deduction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.Formula;
import entity.Formula.FormulaType;
import entity.Predicate.PredicateType;
import flowmanager.InputManager;

public class KnowledgeBase {
	private List<Formula> formulas = new ArrayList<Formula>();
	
	public List<Formula> getFormulasByPredicateType(PredicateType predicateType){
		return formulas
			.stream()
			.filter(formula -> (formula.getType().equals(FormulaType.ATOM) && formula.containsPredicate(predicateType)))
			.collect(Collectors.toList());
	}
	
	public void tell(String formulaLine) {
		formulas.add(FormulaParser.parseFormulaLine(formulaLine));
	}
	
	public void readFormulas(String filePath) throws IOException {
		InputManager inputManager = new InputManager();
		try {
			inputManager.openFileForReading(filePath);
			formulas = inputManager.readLines()
				.map(formulaLine -> FormulaParser.parseFormulaLine(formulaLine)).collect(Collectors.toList());
			inputManager.finishReading();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public List<Formula> getFormulas() {
		return formulas;
	}

	public void setFormulas(List<Formula> formulas) {
		this.formulas = formulas;
	}
}

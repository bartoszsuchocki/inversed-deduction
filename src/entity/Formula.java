package entity;

import entity.Predicate.PredicateType;
import lombok.Data;

@Data
public class Formula {
	private FormulaType type;
	private Predicate predicate;

	private Formula leftChild;
	private Formula rightChild;

	private boolean isQuestion = false;

	public Formula(Formula leftChild, Formula rightChild, FormulaType type) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.type = type;
	}

	public Formula(FormulaType type) {
		this.type = type;
	}

	public Formula(Predicate predicate) {
		this.type = FormulaType.ATOM;
		this.predicate = predicate;
	}

	public Formula() {};

	public boolean containsPredicate(PredicateType predicateType) {
		if (predicate != null && predicate.getType().equals(predicateType)) {
			return true;
		}
		if (leftChild != null && rightChild != null) {
			return leftChild.containsPredicate(predicateType) || rightChild.containsPredicate(predicateType);
		}
		return false;
	}

	public enum FormulaType {
		AND, ATOM, OR
	}
}

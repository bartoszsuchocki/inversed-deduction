package entity;

import lombok.Data;

@Data
public class Predicate {
	private PredicateType type;
	private Term leftTerm;
	private Term rightTerm;
	private boolean isNegation = false;

	public Predicate(Term leftTerm, Term rightTerm, PredicateType type) {
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm;
		this.type = type;
	}
	
	public Predicate(Term leftTerm, Term rightTerm, PredicateType type, boolean isNegation) {
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm;
		this.type = type;
		this.isNegation = isNegation;
	}

	public enum PredicateType {
		OFFERS, PRODUCENT, COUNTRY, WEIGHT, VOLUME, FAT, PROTEIN, SUGAR;
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
}

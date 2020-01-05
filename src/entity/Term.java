package entity;

import lombok.Data;

@Data
public class Term {
	
	private TermType type;
	private String value;
	
	public Term(String value) {
		this.value = value;
		this.type = getTypeByValue(value);
	}
	
	public Term(String value, TermType type) {
		this.value = value;
		this.type = type;
	}
	
	public static TermType getTypeByValue(String value) {
		return value.toUpperCase().equals(value) ? TermType.CONSTANT : TermType.VARIABLE; 
	}

	public enum TermType{
		CONSTANT, VARIABLE
	}
}

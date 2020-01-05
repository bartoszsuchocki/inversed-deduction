package deduction;

import java.util.HashMap;
import java.util.Map;

public class DeductionResult{
	private Map<String, String> variableSubstitutions = new HashMap<String, String>();
	private boolean fulfilled;
	
	public DeductionResult(boolean fulfilled, Map<String, String> substitutions) {
		this.fulfilled = fulfilled;
		this.variableSubstitutions = substitutions;
	}
	
	public static DeductionResult or(DeductionResult result1, DeductionResult result2) {
		return new DeductionResult(result1.fulfilled || result2.fulfilled, result1.variableSubstitutions);
	}
	
	public static DeductionResult and(DeductionResult result1, DeductionResult result2) {
		return new DeductionResult(result1.fulfilled && result2.fulfilled, result1.variableSubstitutions);
	}
	
	public Map<String, String> getVariableSubstitutions() {
		return variableSubstitutions;
	}
	
	public void setVariableSubstitutions(Map<String, String> variableSubstitutions) {
		this.variableSubstitutions = variableSubstitutions;
	}
	
	public void setVariableSubstitution(String variable, String substitution) {
		variableSubstitutions.put(variable, substitution);
	}
	
	public boolean isFulfilled() {
		return fulfilled;
	}
	
	@Override
	public String toString() {
		if(fulfilled) {
			return "success, substitutions: " + variableSubstitutions.toString();
		}
		return "failure";
	}
}


package org.seerc.nebulous.ontology.components;

/**
 *  A simple constraint is made up of a first and second argument along with an operator. They take th form of first argument operator second argument. The first argument refers to a Metric and the second to a threshold value, which if surpassed is considered a violation.
 */
public class SimpleConstraint {
	
	protected String name;
	
	protected String firstArgument;
	protected ComparisonOperator operator;
	protected double secondArgument;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstArgument() {
		return firstArgument;
	}
	public void setFirstArgument(String firstArgument) {
		this.firstArgument = firstArgument;
	}
	public ComparisonOperator getOperator() {
		return operator;
	}
	public void setOperator(ComparisonOperator operator) {
		this.operator = operator;
	}
	public double getSecondArgument() {
		return secondArgument;
	}
	public void setSecondArgument(double secondArgument) {
		this.secondArgument = secondArgument;
	}
	@Override
	public String toString() {
		return "SimpleConstraint [name = " + name + ", firstArgument = " + firstArgument + ", operator = " + operator
				+ ", secondArgument = " + secondArgument + "]";
	}
	
	
}

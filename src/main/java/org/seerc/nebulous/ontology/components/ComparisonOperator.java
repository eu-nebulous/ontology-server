package org.seerc.nebulous.ontology.components;

/**
 * Comparison operators that can be used in simple constraints.
 */
public enum ComparisonOperator {
	/**
	 * >
	 */
	GREATER_THAN,
	/**
	 * >=
	 */
	GREATER_EQUAL_THAN,
	/**
	 * <
	 */
	LESS_THAN,
	/**
	 * <=
	 */
	LESS_EQUAL_THAN,
	/**
	 * ==
	 */
	EQUALS,
	/**
	 * !=
	 */
	NOT_EQUAL;
	
	public static ComparisonOperator convert(String op) {
		ComparisonOperator res = null;
		switch (op) {
		case ">": 
			res = ComparisonOperator.GREATER_THAN;
			break;
		case ">=": 
			res = ComparisonOperator.GREATER_EQUAL_THAN;
			break;
		case "<": 
			res = ComparisonOperator.LESS_THAN;
			break;
		case "<=": 
			res = ComparisonOperator.LESS_EQUAL_THAN;
			break;
		case "==": 
			res = ComparisonOperator.EQUALS;
			break;
		case "!=": 
			res = ComparisonOperator.NOT_EQUAL;
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + op);
		}
		
		return res;
	}
}

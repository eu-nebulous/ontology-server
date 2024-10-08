package org.seerc.nebulous.ontology.components;

public class SLO extends SimpleConstraint{
	
	protected SimpleConstraint qualifyingCondition;
	
	protected boolean soft, negotiable;
	
	protected double settlementPricePercentage;

	public double getSettlementPricePercentage() {
		return settlementPricePercentage;
	}

	public void setSettlementPricePercentage(double settlementPricePercentage) {
		this.settlementPricePercentage = settlementPricePercentage;
	}

	public SimpleConstraint getQualifyingCondition() {
		return qualifyingCondition;
	}

	public void setQualifyingCondition(SimpleConstraint qualifyingCondition) {
		this.qualifyingCondition = qualifyingCondition;
	}

	public boolean isSoft() {
		return soft;
	}

	public void setSoft(boolean soft) {
		this.soft = soft;
	}

	public boolean isNegotiable() {
		return negotiable;
	}

	public void setNegotiable(boolean negotiable) {
		this.negotiable = negotiable;
	}

	@Override
	public String toString() {
		return "SLO [ " + super.toString() + "qualifyingCondition = " + qualifyingCondition + ", soft = " + soft + ", negotiable = " + negotiable
				+ ", settlementPricePercentage = " + settlementPricePercentage + "]";
	}
	
	
	
}
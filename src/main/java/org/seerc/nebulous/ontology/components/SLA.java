package org.seerc.nebulous.ontology.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLA{
	private String slaName;
	private Map<String,  List<SLO>> constraints;
	
	public SLA(String slaName) {
		this.slaName = slaName;
		constraints = new HashMap<String,  List<SLO>>();
	}

	public void putInSL(String sl, List<SLO> slos) {
		constraints.put(sl, slos);
	}

	public String getSlaName() {
		return slaName;
	}

	public Map<String,  List<SLO>> getConstraints() {
		return constraints;
	}


	
	
	
}
package org.seerc.nebulous.rest;

import java.util.List;

import org.seerc.nebulous.ontology.components.SLO;

public class RequestSLO extends SLO{
	
	List<String> sl;

	public List<String> getSl() {
		return sl;
	}

	public void setSl(List<String> sl) {
		this.sl = sl;
	}
	
	
}

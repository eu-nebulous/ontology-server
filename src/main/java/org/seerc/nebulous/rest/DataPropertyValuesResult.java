package org.seerc.nebulous.rest;

public class DataPropertyValuesResult {
	private String datatype;
	private String value;
	
	public DataPropertyValuesResult(String datatype, String value) {
		super();
		this.datatype = datatype;
		this.value = value;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}

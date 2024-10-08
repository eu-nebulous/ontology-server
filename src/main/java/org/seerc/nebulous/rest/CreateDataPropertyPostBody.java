package org.seerc.nebulous.rest;

public class CreateDataPropertyPostBody {
	private String dataPropertyURI, domainURI;
	private Object value;
	public String getDataPropertyURI() {
		return dataPropertyURI;
	}

	public void setDataPropertyURI(String dataPropertyURI) {
		this.dataPropertyURI = dataPropertyURI;
	}

	public String getDomainURI() {
		return domainURI;
	}

	public void setDomainURI(String domainURI) {
		this.domainURI = domainURI;
	}

	public CreateDataPropertyPostBody(String dataPropertyURI, String domainURI, Object value) {
		this.dataPropertyURI = dataPropertyURI;
		this.domainURI = domainURI;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "data property = " + dataPropertyURI + "\tdomain = " + domainURI + "\tvalue = " + value;
	}
	
	
}

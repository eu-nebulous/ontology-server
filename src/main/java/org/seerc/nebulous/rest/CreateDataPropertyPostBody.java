package org.seerc.nebulous.rest;

public class CreateDataPropertyPostBody {
	private String dataPropertyURI, domainURI;
	private String value, type;
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

	public CreateDataPropertyPostBody(String dataPropertyURI, String domainURI, String value) {
		this.dataPropertyURI = dataPropertyURI;
		this.domainURI = domainURI;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CreateDataPropertyPostBody [dataPropertyURI = " + dataPropertyURI + ", domainURI = " + domainURI
				+ ", value = " + value + ", type = " + type + "]";
	}
	
}

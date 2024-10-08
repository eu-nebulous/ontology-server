package org.seerc.nebulous.rest;

public class CreateObjectPropertyPostBody {
	private String objectPropertyURI, domainURI, rangeURI;

	public CreateObjectPropertyPostBody(String objectPropertyURI, String domainURI, String rangeURI) {
		this.objectPropertyURI = objectPropertyURI;
		this.domainURI = domainURI;
		this.rangeURI = rangeURI;
	}

	public String getObjectPropertyURI() {
		return objectPropertyURI;
	}

	public void setObjectPropertyURI(String objectPropertyURI) {
		this.objectPropertyURI = objectPropertyURI;
	}

	public String getDomainURI() {
		return domainURI;
	}

	public void setDomainURI(String domainURI) {
		this.domainURI = domainURI;
	}

	public String getRangeURI() {
		return rangeURI;
	}

	public void setRangeURI(String rangeURI) {
		this.rangeURI = rangeURI;
	}

	@Override
	public String toString() {
		return "object property = " + objectPropertyURI + "\tdomain = " + domainURI + "\trange = " + rangeURI;
	}
	
	
}

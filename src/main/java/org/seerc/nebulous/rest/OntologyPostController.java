package org.seerc.nebulous.rest;

import org.seerc.nebulous.ontology.OntologyDAO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

/**
 * The Spring controller that contains all of the endpoints for the ontology.
 * 
 */
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class OntologyPostController {
	//TODO: add error checking and discuss non-volatile storage.
	private OntologyDAO ontology = OntologyDAO.getInstance();
	
	@PostMapping("/create/individual")
	void createIndividual(@RequestBody CreateIndividualPostBody postBody) {
		ontology.getManipulator().createIndividual(postBody.getIndividualURI(), postBody.getClassURI());
		Logger.post("Class Assertion", postBody.getIndividualURI(), postBody.getClassURI());

	}
	
	@PostMapping("/create/objectProperty")
	void createObjectProperty(@RequestBody CreateObjectPropertyPostBody postBody) {
		ontology.getManipulator().createObjectProperty(postBody.getObjectPropertyURI(), postBody.getDomainURI(), postBody.getRangeURI());

		Logger.post("Object Property Assertion", "Object Property: " + postBody.getObjectPropertyURI(), "Domain: " + postBody.getDomainURI(), "Range: " + postBody.getRangeURI());
		
	}
	
	@PostMapping("/create/dataProperty")
	void createDataProperty(@RequestBody CreateDataPropertyPostBody postBody) {
		ontology.getManipulator().createDataProperty(postBody.getDataPropertyURI(), postBody.getDomainURI(), postBody.getValue());
		Logger.post("Data Property Assertion", "Data Property: " + postBody.getDataPropertyURI(), "Domain: " + postBody.getDomainURI(), "Value: " + postBody.getValue());

	}
	
}
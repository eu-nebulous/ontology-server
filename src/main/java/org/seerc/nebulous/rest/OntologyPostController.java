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
		System.out.println("Created: " + postBody);

	}
	
	@PostMapping("/create/objectProperty")
	void createObjectProperty(@RequestBody CreateObjectPropertyPostBody postBody) {
		ontology.getManipulator().createObjectProperty(postBody.getObjectPropertyURI(), postBody.getDomainURI(), postBody.getRangeURI());
		System.out.println("Created: " + postBody);

	}
	
	@PostMapping("/create/dataProperty")
	void createDataProperty(@RequestBody CreateDataPropertyPostBody postBody) {
		ontology.getManipulator().createDataProperty(postBody.getDataPropertyURI(), postBody.getDomainURI(), postBody.getValue());
		System.out.println("Created: " + postBody);

	}
	
}
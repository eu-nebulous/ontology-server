package org.seerc.nebulous.rest;

import java.time.Instant;

import org.seerc.nebulous.ontology.OntologyDAO;
import org.seerc.nebulous.sql.Database;
import org.seerc.nebulous.sql.DatabaseDAO;
import org.springframework.web.bind.annotation.DeleteMapping;
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
//	private DatabaseDAO db = DatabaseDAO.getInstance();

	
	@PostMapping("/load/asset")
	void loadAsset(@RequestBody String assetName) {
		registerAsset(new RegisterAssetPostBody(assetName, Instant.now().getEpochSecond()));
//		db.getSLAClassAssertions(assetName).forEach(t -> 
//			ontology.getManipulator().createIndividual(t.get(0), t.get(1))
//		);
//		
//		db.getSLAObjectPropertyAssertions(assetName).forEach(t -> 
//			ontology.getManipulator().createObjectProperty(t.get(1), t.get(0), t.get(2))
//		);
//		db.getSLADataPropertyAssertions(assetName).forEach(t -> {
//			ontology.getManipulator().createDataProperty(t.get(1), t.get(0), t.get(2), t.get(3));
//		});
	}
	@PostMapping("/register/asset")
	void registerAsset(@RequestBody RegisterAssetPostBody postBody) {
		ontology.registerAsset(postBody.getAssetName(), postBody.getTimestamp());
		
		if(ontology.numberOfAssets() >= 3) {
			ontology.removeEarliestAsset();
		}
	}


	@PostMapping("/create/individual")
	void createIndividual(@RequestBody CreateIndividualPostBody postBody) {
		ontology.getManipulator().createIndividual(postBody.getIndividualURI(), postBody.getClassURI());
//		db.createIndividual(postBody);
		Logger.post("Class Assertion", postBody.getIndividualURI(), postBody.getClassURI());


	}
	@PostMapping("/create/individual/expression")
	void createIndividualClassExpression(@RequestBody CreateIndividualPostBody postBody) {
		ontology.getManipulator().createIndividualClassExpression(postBody.getIndividualURI(), postBody.getClassURI());
		Logger.post("Class Assertion", postBody.getIndividualURI(), postBody.getClassURI());


	}
	@PostMapping("/create/class/expression")
	void createClassDescriptionClass(@ RequestBody CreateClassExpressionClassPostBody postBody) {
		ontology.getManipulator().createClassExpressionClass(postBody.getClassURI(), postBody.getClassExpression());
		Logger.post("Class Creation", postBody.getClassURI(), postBody.getClassExpression());

	}
	
	@PostMapping("/create/objectProperty")
	void createObjectProperty(@RequestBody CreateObjectPropertyPostBody postBody) {
		ontology.getManipulator().createObjectProperty(postBody.getObjectPropertyURI(), postBody.getDomainURI(), postBody.getRangeURI());
//		db.createObjectPropertyAssertion(postBody);
		Logger.post("Object Property Assertion", "Object Property: " + postBody.getObjectPropertyURI(), "Domain: " + postBody.getDomainURI(), "Range: " + postBody.getRangeURI());

	}
	
	@PostMapping("/create/dataProperty")
	void createDataProperty(@RequestBody CreateDataPropertyPostBody postBody) {
		ontology.getManipulator().createDataProperty(postBody.getDataPropertyURI(), postBody.getDomainURI(), postBody.getValue(), postBody.getType());
//		db.createDataPropertyAssertion(postBody);
		Logger.post("Data Property Assertion", "Data Property: " + postBody.getDataPropertyURI(), "Domain: " + postBody.getDomainURI(), "Value: " + postBody.getValue() + "^^" + postBody.getType());


	}
	
}
package org.seerc.nebulous.rest;

import org.seerc.nebulous.ontology.OntologyDAO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OntologyDeleteController {
	
	private OntologyDAO ontology = OntologyDAO.getInstance();

	@DeleteMapping("delete/individual")
	public void deleteIndividual(@RequestParam String individualName) {
		ontology.getManipulator().deleteIndividual(individualName);
		
		System.out.println("Deleted: " + individualName);
	}
}

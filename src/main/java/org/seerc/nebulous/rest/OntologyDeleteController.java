package org.seerc.nebulous.rest;

import org.seerc.nebulous.ontology.OntologyDAO;
import org.seerc.nebulous.ontology.OntologyManipulator;
import org.seerc.nebulous.ontology.OntologyReasoner;
import org.seerc.nebulous.sql.DatabaseDAO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OntologyDeleteController {
	
	private OntologyDAO ontology = OntologyDAO.getInstance();
	private DatabaseDAO db = DatabaseDAO.getInstance();
	private OntologyManipulator manipulator = ontology.getManipulator();
	private OntologyReasoner reasoner = ontology.getReasoner();


	@DeleteMapping("/delete/individual")
	public void deleteIndividual(@RequestParam String individualName) {
		manipulator.deleteIndividual(individualName);
		
	}
	
	@DeleteMapping("/delete/asset")
	void deleteAsset(@RequestBody String asset) {
		ontology.removeAsset(asset);
	
		reasoner.getInstances("{" + asset + "}" + " or partOf value " + asset + " or inverse partOf value " + asset , false).forEach(
			t -> manipulator.deleteIndividual(t.getIRI().getShortForm())
		);
		
	}
}

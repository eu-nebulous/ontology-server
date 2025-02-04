package org.seerc.nebulous.ontology;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * The ontology data access object (DAO); which ensures all pointers to an ontology point at the same one.
 */
public class OntologyDAO extends Ontology{
	private static OntologyDAO ontologyDAO = null;

	private OntologyDAO(String fileLocation) throws OWLOntologyCreationException{
		super(fileLocation, "neb:");

	}
	
	/**
	 * @return the instance of the ontology.
	 * @throws OWLOntologyCreationException 
	 */
	public static OntologyDAO getInstance(String fileLocation) throws OWLOntologyCreationException {
		if(ontologyDAO == null) 
			ontologyDAO = new OntologyDAO(fileLocation);
	
		
		return ontologyDAO;
	}
	
	public static OntologyDAO getInstance() {
	
		
		return ontologyDAO;
	}
}

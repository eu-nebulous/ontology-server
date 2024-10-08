package org.seerc.nebulous.ontology;

/**
 * The ontology data access object (DAO); which ensures all pointers to an ontology point at the same one.
 */
public class OntologyDAO extends Ontology{
	private static OntologyDAO ontologyDAO = new OntologyDAO();

	private OntologyDAO(){
		super("nebulous.ttl", "neb:");

	}
	
	/**
	 * @return the instance of the ontology.
	 */
	public static OntologyDAO getInstance() {
		return ontologyDAO;
	}
}

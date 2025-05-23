package org.seerc.nebulous.ontology;

import java.util.Collection;


import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.providers.DataAssertionProvider;
//import org.semanticweb.owlapi.model.OWLObjectProperty;
//import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLLiteralReplacer;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapitools.builders.BuilderDataPropertyAssertion;

/**
 * Contains all methods that are about changing the ontology (addition, amendment, removal, etc.) in some way.
 */
public class OntologyManipulator extends OntologyInformationHolder{
	
	
	 private OWLEntityRemover remover = new OWLEntityRemover(ontology);
	 private Ontology ont;
	 
	OntologyManipulator(Ontology ont) {
		super(ont);
		this.ont = ont;
	}
	
	
	
	
	public void createClassExpressionClass(String classURI, String classExpression) {
		manager.addAxiom(ontology, factory.getOWLEquivalentClassesAxiom(
				factory.getOWLClass(classURI, prefixManager),
				ont.parseClassExpression(classExpression)));
	}
	
	/**
	 * Adds an individual to the ontology.
	 * @param individualURI The URI and prefix of the individual to be added.
	 * @param classURI  The URI of the desired class for the individual.
	 */
		
	public void createIndividual(String individualURI, String classURI){
	    manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(
	    		factory.getOWLClass(classURI, prefixManager),
	    		factory.getOWLNamedIndividual(individualURI, prefixManager)));
	}
	
	public void createIndividualClassExpression(String individualURI, String classExpression){
	    manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(
	    		ont.parseClassExpression(classExpression),
	    		factory.getOWLNamedIndividual(individualURI, prefixManager)));
	}
	
	/**
	 * declares an object property between two individuals
	 * @param οbjectPropertyURI the URI of the object property
	 * @param domainURI the URI of the domain individual (from)
	 * @param rangeURI the URI of the range individual (to)
	 */
	public void createObjectProperty(String οbjectPropertyURI, String domainURI, String rangeURI) {
		manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(
				factory.getOWLObjectProperty(οbjectPropertyURI, prefixManager),
				factory.getOWLNamedIndividual(domainURI, prefixManager),  
				factory.getOWLNamedIndividual(rangeURI, prefixManager)));
	}
	
	/**
	 * Declares a data property and its value in an individual.
	 * @param dataPropertyURI the URI of the data property.
	 * @param individualURI The URI of the individual.
	 * @param value The value of the data property.
	 * @param type The datatype of the value.
	 */
	public void createDataProperty(String dataPropertyURI,String individualURI, String value, String type) {

		OWLDataProperty dataProperty = factory.getOWLDataProperty(dataPropertyURI, prefixManager);
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(individualURI, prefixManager);
		OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, factory.getOWLLiteral(value, factory.getOWLDatatype(type, prefixManager)));
		
//		if(value.getClass().equals(Boolean.class)) 
//			dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, (Boolean) value);
//		else if(value.getClass().equals(Integer.class))
//			dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, (Integer) value);
//		else if(value.getClass().equals(Long.class)) 
//			dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, factory.getOWLLiteral(value.toString(), OWL2Datatype.XSD_LONG) );
//		else if(value.getClass().equals(String.class)) {
//			String v = (String) value; 
//			if(v.matches("	")) {
//				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, factory.getOWLLiteral(v, factory.getOWLDatatype("xsd:duration", prefixManager)));
//			}else
//				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, factory.getOWLLiteral(v, OWL2Datatype.XSD_STRING));
//		}
//		else if (value.getClass().equals(Double.class))
//			dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, (Double) value);
//		else if (value.getClass().equals(Float.class))
//			dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, (Float) value);
//		else
//			dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, (OWLLiteral) value);

		
		manager.addAxiom(ontology, dataPropertyAssertionAxiom);
	}
//	public void createDataProperty(String dataPropertyURI,String individualURI, String value) {
//		OWLDataProperty dataProperty = factory.getOWLDataProperty(dataPropertyURI, prefixManager);
//		OWLNamedIndividual individual = factory.getOWLNamedIndividual(individualURI, prefixManager);
//		OWLLiteral lit = factory.getOWLLiteral(value.split(value), null)
//		OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom;
//		
//		
//	}
	
	/**
	 * Removes an individual from the ontology;
	 * @param individualURI
	 */
	public void deleteIndividual(String individualURI) {
		remover.visit(factory.getOWLNamedIndividual(individualURI, prefixManager));
		manager.applyChanges(remover.getChanges());
		remover.reset();
	}
	public void deleteIndividuals(Collection<OWLNamedIndividual> inds) {
		inds.forEach(t -> remover.visit(t));
		manager.applyChanges(remover.getChanges());
		remover.reset();
		
	}
}

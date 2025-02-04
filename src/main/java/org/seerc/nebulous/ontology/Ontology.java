package org.seerc.nebulous.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Ontology extends OntologyInformationHolder{

	protected OntologyManipulator manipulator;
	protected OntologyReasoner reasoner;
	
	protected Ontology(String ontologyLocation, String defaultPrefix) throws OWLOntologyCreationException {
		super(ontologyLocation, defaultPrefix);
		manager.getOntologyFormat(ontology).asPrefixOWLDocumentFormat().getPrefixName2PrefixMap().forEach((key, value) -> prefixManager.setPrefix(key, value));	
		manipulator = new OntologyManipulator(this);
		reasoner = new OntologyReasoner(this);
		
	}
	public void saveToFile(){
		try {
			File f = new File("output.ttl");
			PrintStream p = new PrintStream(f);
			ontology.saveOntology(new TurtleDocumentFormat(), p);
		} catch (OWLOntologyStorageException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	public void loadMetricsFromCocoon() {
//		Set<OWLObjectProperty> tq = new HashSet<OWLObjectProperty>();
//		Set<OWLObjectProperty> cs = new HashSet<OWLObjectProperty>();
//		Set<OWLObjectProperty> ss = new HashSet<OWLObjectProperty>();
//		Set<OWLObjectProperty> ns = new HashSet<OWLObjectProperty>();
//		Set<OWLObjectProperty> as = new HashSet<OWLObjectProperty>();
//
//		cs.addAll(reasoner.getObjectPropertiesWithDomain("ComputeService"));
//		ns.addAll(reasoner.getObjectPropertiesWithDomain("StorageService"));
//		ss.addAll(reasoner.getObjectPropertiesWithDomain("NetworkService"));
//
//		as.addAll(cs);
//		as.addAll(ns);
//		as.addAll(ss);
//
//		tq.addAll(reasoner.getObjectPropertiesWithRange("TypeAndQuantityNode"));
//
//		System.out.println("Range:TypeAndQualityNode = Domain:Compute/Storage/NetworkService: " + tq.equals(as));
//
//		createMetric(cs, "neb:COMPUTATION");
//		createMetric(ss, "neb:STORAGE");
//		createMetric(ns, "neb:NETWORKING");
//	}
	private void createMetric(Set<OWLObjectProperty> metrics, String attribute) {
		manipulator.createIndividual(attribute, "owlq:MeasurableAttribute");
		metrics.forEach(t ->  {
			String name = "neb:" + t.getIRI().getFragment();
			manipulator.createIndividual(name, "owlq:Metric");
			manipulator.createObjectProperty("owlq:measuredBy", attribute, name);
			System.out.println(name);
		});
	}
	public void print(){
		try {
			ontology.saveOntology(new TurtleDocumentFormat(), System.out);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OntologyManipulator getManipulator() {
		return manipulator;
	}

	public OntologyReasoner getReasoner() {
		return reasoner;
	}
	
	public OWLDataFactory getOWLDataFactory() {
		return factory;
	}
	
}

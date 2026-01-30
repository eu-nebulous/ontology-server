package org.seerc.nebulous.ontology;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class OntologyInformationHolder {
	
	protected OWLOntologyManager manager;
	protected OWLOntology ontology;
	protected OWLDataFactory factory;
	protected PrefixManager prefixManager;
	
	OntologyInformationHolder(String ontologyIRI, String defaultPrefix) throws OWLOntologyCreationException {
		this.manager = OWLManager.createOWLOntologyManager();
			ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyIRI));
			

		this.factory = manager.getOWLDataFactory();
		this.prefixManager = new DefaultPrefixManager(defaultPrefix);
		
		OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
		format.asPrefixOWLDocumentFormat().getPrefixName2PrefixMap();
		
		if(format.isPrefixOWLDocumentFormat())
			prefixManager.copyPrefixesFrom(format.asPrefixOWLDocumentFormat().getPrefixName2PrefixMap());
		
	}
	
	protected OntologyInformationHolder(OntologyInformationHolder ontInf) {
		this.manager = ontInf.manager;
		this.ontology = ontInf.ontology;
		this.prefixManager =ontInf.prefixManager;
		this.factory = ontInf.factory;
	}

	public PrefixManager getPrefixManager() {
		return prefixManager;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public OWLDataFactory getFactory() {
		return factory;
	}
	
	
}

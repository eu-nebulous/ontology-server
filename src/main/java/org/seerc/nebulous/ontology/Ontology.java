package org.seerc.nebulous.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;

public class Ontology extends OntologyInformationHolder{

	protected OntologyManipulator manipulator;
	protected OntologyReasoner reasoner;
	private BidirectionalShortFormProvider bidiShortFormProvider;
	private ShortFormProvider shortFormProvider;
	Map<String, Long> assets;

	protected Ontology(String ontologyLocation, String defaultPrefix) throws OWLOntologyCreationException {
		super(ontologyLocation, defaultPrefix);
		manager.getOntologyFormat(ontology).asPrefixOWLDocumentFormat().getPrefixName2PrefixMap().forEach((key, value) -> prefixManager.setPrefix(key, value));	
		shortFormProvider = new SimpleShortFormProvider();

		bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, ontology.getImportsClosure(), shortFormProvider);

		manipulator = new OntologyManipulator(this);
		reasoner = new OntologyReasoner(this);
		assets = new HashMap<String, Long>();
		
		
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
	public void registerAsset(String assetName, long timestamp) {
		assets.put(assetName, timestamp);
	}
	public void removeAsset(String assetName) {
		assets.remove(assetName);
	}
	public String removeEarliestAsset() {
		long timestamp = Long.MAX_VALUE;
		String assetName = null;
		for(Entry<String, Long> asset : assets.entrySet()) {
			if(asset.getValue() < timestamp) {
				timestamp = asset.getValue();
				assetName = asset.getKey();
			}
		}
		removeAsset(assetName);
		String indName = assetName.split(":")[1];

		manipulator.deleteIndividuals(		
			reasoner.getInstances("partOf value " + indName + " or inverse partOf value " + indName + " or {" +indName + "}", false)
		);
		return assetName;
	}
	public int numberOfAssets() {
		return assets.size();
	}
 	public OWLClassExpression parseClassExpression(String classExpressionString) {
        // Set up the real parser
        ManchesterOWLSyntaxParser parser = OWLManager.createManchesterParser();
        parser.setStringToParse(classExpressionString);
        parser.setDefaultOntology(ontology);
        // Specify an entity checker that wil be used to check a class
        // expression contains the correct names.
        OWLEntityChecker entityChecker = new  ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        // Do the actual parsing
        return parser.parseClassExpression();
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

package org.seerc.nebulous.ontology;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asUnorderedSet;

import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.search.EntitySearcher;

public class OntologyReasoner extends OntologyInformationHolder{

	private OpenlletReasoner reasoner;
	private ShortFormProvider shortFormProvider;
	private BidirectionalShortFormProvider bidiShortFormProvider;


	OntologyReasoner(OntologyInformationHolder ont) {
		super(ont);
		
		reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);
		shortFormProvider = new SimpleShortFormProvider();

        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, ontology.getImportsClosure(), shortFormProvider);
	}
	public List<String> getSuperObjectProperties(OWLObjectProperty obj) {
		flush();
		List<OWLObjectPropertyExpression> superObjects = reasoner.superObjectProperties(obj, false).toList();
		List<String> result = new ArrayList<String>(superObjects.size());

		for(OWLObjectPropertyExpression sobj : superObjects)
			result.add(sobj.asOWLObjectProperty().getIRI().getFragment());
		return result;

	}
	
	public List<OWLLiteral> getIndividualDataProperties(String ind, String dataProperty) {
		return EntitySearcher.getDataPropertyValues(factory.getOWLNamedIndividual(ind, prefixManager), factory.getOWLDataProperty(dataProperty, prefixManager), ontology).toList();
	}
	
	public List<OWLObjectProperty> getObjectPropertiesWithRange(String range) {
		
		flush();
		return reasoner.getOntology().objectPropertiesInSignature().filter(o -> 
			reasoner.objectPropertyRanges(o, false)
			.anyMatch(c -> c.getIRI().getFragment().equals(range))).toList();

	}
	
	public Map<OWLObjectPropertyExpression, Collection<OWLIndividual>> getObjectPropertiesInSignature(String individualURI) {
		flush();
		return EntitySearcher.getObjectPropertyValues(factory.getOWLNamedIndividual(individualURI, prefixManager), ontology).asMap();
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
    
	public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) {
		flush();
        if (classExpressionString.trim().isEmpty()) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
        return asUnorderedSet(superClasses.entities());
	}
    
    public Set<OWLClass> getEquivalentClasses(String classExpressionString) {
    	flush();
        if (classExpressionString.trim().isEmpty()) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
        return asUnorderedSet(
            equivalentClasses.entities().filter(cl -> !cl.equals(classExpression)));
    }

    public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) {
    	flush();
        if (classExpressionString.trim().isEmpty()) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
        
        return asUnorderedSet(subClasses.entities());
    }

    public Set<OWLNamedIndividual> getInstances(String classExpressionString, boolean direct) {
    	flush();
        if (classExpressionString.trim().isEmpty()) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression, direct);
        return asUnorderedSet(individuals.entities());
    }
    
    public void flush() {
    	reasoner.flush();
    }
 
}

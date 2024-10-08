package org.seerc.nebulous.ontology;

import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.search.EntitySearcher;

@SuppressWarnings("deprecation")
public
class OntologyReasoner extends OntologyInformationHolder{

	private OpenlletReasoner reasoner;
	private ShortFormProvider shortFormProvider;
	private BidirectionalShortFormProvider bidiShortFormProvider;
//	private SWRLRuleEngine swrlRuleEngine;
//	private SQWRLQueryEngine queryEngine;

	OntologyReasoner(OntologyInformationHolder ont) {
		super(ont);
		
		reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);
		shortFormProvider = new SimpleShortFormProvider();
		
        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, ontology.getImportsClosure(), shortFormProvider);
//        swrlRuleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
//		queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);
//		swrlRuleEngine.infer();
 
	}
	
//	public void askSQWRL(String name, String query) throws SQWRLException, SWRLParseException {
//		SQWRLResult result = queryEngine.runSQWRLQuery(name, query);
//		 if (result.next()) 
//			   System.out.println(result.getRow());
//	}
	
	public List<OWLLiteral> getIndividualDataProperties(String ind, String dataProperty) {
		return EntitySearcher.getDataPropertyValues(factory.getOWLNamedIndividual(ind, prefixManager), factory.getOWLDataProperty(dataProperty, prefixManager), ontology).toList();
	}
	
	public List<OWLObjectProperty> getObjectPropertiesWithRange(String range) {
		return reasoner.getOntology().objectPropertiesInSignature().filter(o -> 
			reasoner.objectPropertyRanges(o, false)
			.anyMatch(c -> c.getIRI().getFragment().equals(range))).toList();

	}
	
	public List<OWLObjectProperty> getObjectPropertiesWithDomain(String range) {
		return reasoner.getOntology().objectPropertiesInSignature().filter(o -> 
			reasoner.objectPropertyDomains(o, false)
			.anyMatch(c -> c.getIRI().getFragment().equals(range))).toList();

	}
	private OWLClassExpression parseClassExpression(String classExpressionString)
            throws ParserException {
    
        // Set up the real parser
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(factory, classExpressionString);
        parser.setDefaultOntology(ontology);
        // Specify an entity checker that will be used to check a class
        // expression contains the correct names.
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        // Do the actual parsing
        return parser.parseClassExpression();
    }
    
	public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct)
            throws ParserException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
        return superClasses.getFlattened();
    }
    
    public Set<OWLClass> getEquivalentClasses(String classExpressionString)
            throws ParserException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
        Set<OWLClass> result;
        if (classExpression.isAnonymous()) {
            result = equivalentClasses.getEntities();
        } else {
            result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
        }
        return result;
    }
    
    public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct)
            throws ParserException {
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
        return subClasses.getFlattened();
    }

    public Set<OWLNamedIndividual> getInstances(String classExpressionString,
            boolean direct) throws ParserException {
    	reasoner.flush();
        if (classExpressionString.trim().length() == 0) {
            return Collections.emptySet();
        }
        OWLClassExpression classExpression = parseClassExpression(classExpressionString);
        NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression,
                direct);
        return individuals.getFlattened();
    }
    
    public void flush() {
    	reasoner.flush();
    }
    
    
    public String askQuery(String classExpression) {
    	reasoner.flush();
    	StringBuilder sb = new StringBuilder();
        if (classExpression.length() == 0) {
            System.out.println("No class expression specified");
        } else {
            try {
                
                sb.append("\n--------------------------------------------------------------------------------\n");
                sb.append("QUERY:   ");
                sb.append(classExpression);
                sb.append("\n");
                sb.append("--------------------------------------------------------------------------------\n\n");
                // Ask for the subclasses, superclasses etc. of the specified
                // class expression. Print out the results.
                Set<OWLClass> superClasses = getSuperClasses(classExpression, true);
                printEntities("SuperClasses", superClasses, sb);
                Set<OWLClass> equivalentClasses = getEquivalentClasses(classExpression);
                printEntities("EquivalentClasses", equivalentClasses, sb);
                Set<OWLClass> subClasses = getSubClasses(classExpression, 	true);
                printEntities("SubClasses", subClasses, sb);
                Set<OWLNamedIndividual> individuals = getInstances(classExpression, true);
                printEntities("Instances", individuals, sb);
            } catch (ParserException e) {
                System.out.println(e.getMessage());
            }
        }
        return sb.toString();
    }
    
    private void printEntities(String name, Set<? extends OWLEntity> entities, StringBuilder sb) {
        sb.append(name);
        int length = 50 - name.length();
        for (int i = 0; i < length; i++) {
            sb.append(".");
        }
        sb.append("\n\n");
        if (!entities.isEmpty()) {
            for (OWLEntity entity : entities) {
                sb.append("\t");
                sb.append(shortFormProvider.getShortForm(entity));
                sb.append("\n");
            }
        } else {
            sb.append("\t[NONE]\n");
        }
        sb.append("\n");
    }
       
}

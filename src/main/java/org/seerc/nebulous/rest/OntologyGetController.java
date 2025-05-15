package org.seerc.nebulous.rest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.seerc.nebulous.ontology.OntologyDAO;
import org.seerc.nebulous.sql.Database;
import org.seerc.nebulous.sql.DatabaseDAO;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.ReasonerInternalException;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
//@CrossOrigin(origins = "http://localhost:8080")

public class OntologyGetController {
	private OntologyDAO ontology = OntologyDAO.getInstance();
//	private DatabaseDAO db = DatabaseDAO.getInstance();

//	@GetMapping("/exists/asset")
//	public boolean isSLALoaded(@RequestParam("assetName")String assetName) {
//		return ontology.getReasoner().instanceExists("neb:" + assetName);
//	}
	
//	@GetMapping("/fillDb")
//	public void fill() {
//		try {
//			
//			
//			Set<OWLObjectProperty> objectProperties = ontology.getOntology().getObjectPropertiesInSignature();
//			List<String> objectPropertyURIs = new ArrayList<String>(objectProperties.size());
//
//			for(var e : objectProperties) {
//				objectPropertyURIs.add(ontology.getPrefixManager().getPrefixIRI(e.getIRI()));
//			}
//			db.createEntities(objectPropertyURIs, "object_properties");
//				
//			
//			Set<OWLDataProperty> dataProperties = ontology.getOntology().getDataPropertiesInSignature();
//			List<String> dataPropertyURIs = new ArrayList<String>(dataProperties.size());
//
//			for(var e : dataProperties) {
//				dataPropertyURIs.add(ontology.getPrefixManager().getPrefixIRI(e.getIRI()));
//			}
//			db.createEntities(dataPropertyURIs, "data_properties");
//			
//			
//			Set<OWLClass> classes = ontology.getOntology().getClassesInSignature();
//			List<String> classURIs = new ArrayList<String>(classes.size());
//
//			for(var e : classes) {
//				classURIs.add(ontology.getPrefixManager().getPrefixIRI(e.getIRI()));
//			}
//			db.createEntities(classURIs, "classes");
//			
//			
//			Set<OWLNamedIndividual> individuals = ontology.getOntology().getIndividualsInSignature(false);
//			List<String> individualURIs = new ArrayList<String>(individuals.size());
//
//			for(var e : individuals) {
//
//				individualURIs.add(ontology.getPrefixManager().getPrefixIRI(e.getIRI()));
//			}
////			System.out.println(individualURIs);
//			db.createEntities(individualURIs, "individuals");
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//				
//	}
	
    @GetMapping("/save")
    public void save() {
    	ontology.saveToFile();
    }
//    @GetMapping("/sqwrl")
//    public void sqwrl(@RequestParam("name") String name, @RequestParam("sqwrl") String sqwrl) throws SQWRLException, SWRLParseException {
//    	ontology.getReasoner().askSQWRL(name, sqwrl);
//    }
    
    @GetMapping("/get/annotation")
    public Map<String, List<String>> annotations() {
    	OWLAnnotationProperty annotation = ontology.getFactory().getOWLAnnotationProperty("neb:used", ontology.getPrefixManager());

    	List<OWLAnnotationSubject > annotatedDataProperties = new ArrayList<OWLAnnotationSubject >();
    	
    	Map<String, List<String>> result = new HashMap<String, List<String>>();
    	
    	OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ontology.getOntology()));

		//Define what's going to visited
		OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(walker) {
	
			@Override
			public void visit(OWLAnnotationAssertionAxiom axiom) {
			//Print them
			if(axiom.containsEntityInSignature(annotation))
				annotatedDataProperties.add(axiom.getSubject());
			}
		};
		
		//Walks over the structure - triggers the walk
		walker.walkStructure(visitor);  
		
		annotatedDataProperties.forEach(annotatedDataProperty -> {
			
			IRI iri = annotatedDataProperty.asIRI().get();
			List<OWLClassExpression> annotatedDataPropertyDomain = EntitySearcher.getDomains(ontology.getFactory().getOWLDataProperty(iri.getIRIString()), ontology.getOntology()).toList();
			List<String> classes = new ArrayList<String>();
			
			annotatedDataPropertyDomain.forEach(domain -> {
//				classes.add(null);
				domain.classesInSignature().forEach(cls -> {
					classes.add(cls.getIRI().getFragment());
				});
			});
			
			result.put(iri.getFragment(), classes);
		});
		return result;
    }
    
    /**
     * @param dlQuery the query.
     * @return number of instances that match the query.
     */
    @GetMapping("/countInstances")
    public int countInstances(@RequestParam("dlQuery") String dlQuery) {
    	int res = 0;
    	String query = URLDecoder.decode(dlQuery, StandardCharsets.UTF_8);
    	try {
	    	res = ontology.getReasoner().getInstances(query, false).size();
    	}catch (Exception e){
    		res = 0;
    	}
    	
//    	Logger.get("Count Instances", query, Integer.toString(res));
    	
    	return res;
    }
    @GetMapping("/exists/dataProperty")
    public boolean existsDataProperty(@RequestParam("dataProperty") String dataProperty) {
    	
    	boolean output = ontology.getOntology().containsDataPropertyInSignature(ontology.getPrefixManager().getIRI(dataProperty));

//    	Logger.get("Data Property Exists", dataProperty, Boolean.toString(output));
    	
    	return output;
    }
    @GetMapping("/exists/class")
    public boolean existsClass(@RequestParam("class") String cls) {

    	boolean output = ontology.getOntology().containsClassInSignature(ontology.getPrefixManager().getIRI(cls));

//    	Logger.get("Class Exists", cls, Boolean.toString(output));

    	
    	return output;
    }
    @GetMapping("/get/instances")
    public List<String> getInstances(@RequestParam String dlQuery) {
    	Set<OWLNamedIndividual> inds;
    	List<String> instanceNames;
    	String query = URLDecoder.decode(dlQuery, StandardCharsets.UTF_8);

    	try {
    		inds = ontology.getReasoner().getInstances(query, false);
    	}catch(ParserException e) {
    		inds = new HashSet<OWLNamedIndividual>();
    	}
    	 
    	instanceNames = new  ArrayList<String>(inds.size());
    	
    	for(OWLNamedIndividual ind : inds)
    		instanceNames.add(ind.getIRI().getFragment());
    	
//    	Logger.get("Retrieve Instances", query, instanceNames.toString());
    	
    	return instanceNames; 
    }
    
    
    /**
     * @param individualName
     * @param dataProperty
     * @return data property values
     */
    @GetMapping("/get/dataProperty")
    public List<Object> getDataProperty(@RequestParam String individualName, @RequestParam String dataProperty){
//    	System.out.print("Retrieved data property: \"" + dataProperty + "\" from " + individualName );
    	
    	List<OWLLiteral> dp = ontology.getReasoner().getIndividualDataProperties(individualName, dataProperty);
    	List<Object> dataProperties = new ArrayList<Object>(dp.size());
    	if(dp.size() == 0)
    		return null;
    	
    	if(dp.get(0).isInteger()) 
    		for(OWLLiteral lit: dp) 
    			dataProperties.add(lit.parseInteger());
    		
    	else if(dp.get(0).isDouble()) 
    		for(OWLLiteral lit: dp) 
    			dataProperties.add(lit.parseDouble());
    		
    	else if(dp.get(0).isBoolean()) 
    		for(OWLLiteral lit: dp) 
    			dataProperties.add(lit.parseBoolean());    	
    	else 
    		for(OWLLiteral lit: dp) 
    			dataProperties.add(lit.toString());
    	
//    	Logger.get("Retrieve Data Property Values", "Individual: " + individualName, "Data Property: " + dataProperty, dataProperty.toString());

    	return dataProperties;
    }
    @GetMapping("/get/dataProperty/values")
    public List<DataPropertyValuesResult> getDataPropertyValues(@RequestParam String individualName, @RequestParam String dataProperty){
    	 
    	ontology.getReasoner().flush();
    	
    	List<OWLLiteral> dp = ontology.getReasoner().getIndividualDataProperties(individualName, dataProperty);
    	List<DataPropertyValuesResult> dataProperties = new ArrayList<DataPropertyValuesResult>(dp.size());

    	if(dp.size() == 0)
    		dataProperties = List.of(new DataPropertyValuesResult("ERROR", "ERROR"));
    	else
	    	for(OWLLiteral lit : dp)
	    		dataProperties.add(new DataPropertyValuesResult(lit.getDatatype().getIRI().getShortForm(), lit.getLiteral()));
    	
//    	Logger.get("Retrieve Data Property Values", "Individual: " + individualName, "Data Property: " + dataProperty	);
    	
    	return dataProperties;
    }


    @GetMapping("/get/superclasses")
    public List<String> getSuperclass(@RequestParam String dlQuery){
    	
    	String query = URLDecoder.decode(dlQuery, StandardCharsets.UTF_8);
    	Set<OWLClass> cls = ontology.getReasoner().getSuperClasses(query, false);
    	List<String> res = new ArrayList<String>(cls.size());
    	for(OWLClass c : cls)
    		res.add(c.getIRI().getFragment());
    	
//    	Logger.get("Retrieve Superclasses", query, res.toString());

    	
    	return res;
    	
    }
    
    
    @GetMapping("/get/subclasses")
    public List<String> getSubclass(@RequestParam String dlQuery){
    	
    	String query = URLDecoder.decode(dlQuery, StandardCharsets.UTF_8);
    	Set<OWLClass> cls = ontology.getReasoner().getSubClasses(query, false);
    	List<String> res = new ArrayList<String>(cls.size());
    	for(OWLClass c : cls)
    		res.add(c.getIRI().getFragment());
    	
//    	Logger.get("Retrieve Subclasses", query, res.toString());

    	
    	return res;
    }
    
    @GetMapping("/get/equivalentClasses")
    public List<String> getEquivalentclass(@RequestParam String dlQuery){
    	
    	String query = URLDecoder.decode(dlQuery, StandardCharsets.UTF_8);
    	Set<OWLClass> cls = ontology.getReasoner().getEquivalentClasses(query);
    	List<String> res = new ArrayList<String>(cls.size());
    	for(OWLClass c : cls)
    		res.add(c.getIRI().getFragment());
    	
//    	Logger.get("Retrieve Equivalentclasses", query, res.toString());

    	
    	return res;
    }
    
    
	@GetMapping("/count/assets")
	int registerAsset() {
		return ontology.numberOfAssets();
	}
}

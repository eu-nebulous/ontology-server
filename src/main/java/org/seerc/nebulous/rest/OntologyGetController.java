package org.seerc.nebulous.rest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seerc.nebulous.ontology.OntologyDAO;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("deprecation")
@RestController
//@CrossOrigin(origins = "http://localhost:8080")

public class OntologyGetController {
	private OntologyDAO ontology = OntologyDAO.getInstance();

	// // curl -X POST localhost:8080/create/sla & curl -X POST -F "sla=SLA_0" -F "level=NORMAL" localhost:8080/create/sl & curl -X POST -F "firstArgument=CPU_CORES" -F "operator=GREATER_EQUAL_THAN" -F "secondArgument=4" -F "qualifyingConditionFirstArgument=REQUESTS_PER_HOUR" -F "sl=SLA_0_NORMAL_SL" -F "qualifyingConditionOperator=LESS_EQUAL_THAN" -F "qualifyingConditionSecondArgument=1000" -F "soft=false" -F "negotiable=false" localhost:8080/create/slo
    /**
     * Prints the ontology to stdout. Used for debugging.
     */
    @GetMapping("/print")
    public void print() {
    	ontology.print();
    }
    
//    @GetMapping("/sqwrl")
//    public void sqwrl(@RequestParam("name") String name, @RequestParam("sqwrl") String sqwrl) throws SQWRLException, SWRLParseException {
//    	ontology.getReasoner().askSQWRL(name, sqwrl);
//    }
    
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
	    	System.out.println("Counted: \"" + query + "\" (" + res + ")");
    	}catch (Exception e){
//    		e.printStackTrace();
    		res = 0;
    		System.out.println("Counted: \"" + query + "\" (0 was returned due to parsing error)");
    	}
    	
    	return res;
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
    	
    	System.out.println("Retrieved instances: \"" + query + "\" (" + instanceNames + ")");
    	return instanceNames; 
    }
    
    /**
     * @param individualName
     * @param dataProperty
     * @return data property values
     */
    @GetMapping("/get/dataProperty")
    public List<Object> getDataProperty(@RequestParam String individualName, @RequestParam String dataProperty){
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
    	
    	System.out.println("Retrieved data property: \"" + dataProperty + "\" from " + individualName + " (" + dataProperties + ")");

    	return dataProperties;
    }
    
    @GetMapping("/get/dataProperty/type")
    public String getDatatype(@RequestParam String individualName, @RequestParam String dataProperty) {
    	
    	return null;
    }

    @GetMapping("/get/superclasses")
    public List<String> getSuperclass(@RequestParam String dlQuery, @RequestParam boolean direct){
    	
    	String query = URLDecoder.decode(dlQuery, StandardCharsets.UTF_8);
    	Set<OWLClass> cls = ontology.getReasoner().getSuperClasses(query, direct);
    	List<String> res = new ArrayList<String>(cls.size());
    	for(OWLClass c : cls)
    		res.add(c.getIRI().getFragment());
    	
    	return res;
    	
    }
}

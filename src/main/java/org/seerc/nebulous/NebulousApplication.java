package org.seerc.nebulous;

//import java.util.HashMap;
import org.seerc.nebulous.ontology.OntologyDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class NebulousApplication {

	public static void main(String[] args) {
		boolean ontologyError = false;		
		try {
			 OntologyDAO.getInstance(args[0]);
		} catch (Exception e) {
			System.out.println("Could not create ontology...\nMake sure you have provided a valid ontology file...\nExiting...");
			ontologyError = true;
		}
		if(!ontologyError)
			SpringApplication.run(NebulousApplication.class);
	}

}

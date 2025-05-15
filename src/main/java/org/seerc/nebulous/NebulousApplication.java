package org.seerc.nebulous;

//import java.util.HashMap;
import org.seerc.nebulous.ontology.OntologyDAO;
import org.seerc.nebulous.sql.DatabaseDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class NebulousApplication {

	public static void main(String[] args) {
		boolean error = false;		
		try {
			 OntologyDAO.getInstance(args[0]);
		} catch (Exception e) {
			System.out.println("Could not create ontology...\nMake sure you have provided a valid ontology file...\nExiting...");
			error = true;
		}
	
//		DatabaseDAO.getInstance("jdbc:postgresql://localhost/semantic_models", "postgres", "pass");
		DatabaseDAO.getInstance(args[1], args[2], args[3]);

		if(!error)
			SpringApplication.run(NebulousApplication.class);
	}

}

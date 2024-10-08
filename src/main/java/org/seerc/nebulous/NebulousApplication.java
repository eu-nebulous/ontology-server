package org.seerc.nebulous;

//import java.util.HashMap;
import org.seerc.nebulous.ontology.OntologyDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class NebulousApplication {

	public static void main(String[] args) {
		OntologyDAO ont = OntologyDAO.getInstance();
		
//		ont.loadMetricsFromCocoon();
		SpringApplication.run(NebulousApplication.class);
	}

}

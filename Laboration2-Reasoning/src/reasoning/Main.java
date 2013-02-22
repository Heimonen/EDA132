package reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class Main {

	private static String aQuery = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX computable:<http://ias.cs.tum.edu/kb/computable.owl#> " +
		"PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#>" +
		"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>" +
		"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
		"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
		"PREFIX knowrob:<http://ias.cs.tum.edu/kb/knowrob.owl#>" +
		"SELECT ?out" +
		"WHERE" +
		"{" +
		    "knowrob:DrinkingGlass rdfs:subClassOf ?e ." +
		    "?e rdfs:subClassOf ?d ." +
		    "?d rdfs:subClassOf ?f ." +
		    "?stored rdfs:domain ?f ." +
		    "?node owl:onProperty ?stored ," +
		    "?node owl:someValuesFrom ?out" +
		"}";
	
	
	
	public static void main(String[] args) {
		String sesameServer = "http://asimov.ludat.lth.se";
		String repositoryID = "KnowRob";
		Repository myRepository = new HTTPRepository(sesameServer, repositoryID);
		try {
			myRepository.initialize();
			System.out.println("Done!");
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		try {
			RepositoryConnection con = myRepository.getConnection();
			try {
				TupleQuery query = 
					con.prepareTupleQuery(
							org.openrdf.query.QueryLanguage.SPARQL, aQuery);
				
				TupleQueryResult qres = query.evaluate();
                ArrayList<HashMap> reslist = new ArrayList<HashMap>();
                while (qres.hasNext()) {
                    BindingSet b = qres.next();
                    Set names = b.getBindingNames();
                    HashMap hm = new HashMap();
                    for (Object n : names) {
                        hm.put((String) n, b.getValue((String) n));
                    }
                    reslist.add(hm);
                }    
			} finally {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		System.out.println("Done with query!");


		/*
		PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>
		PREFIX computable:<http://ias.cs.tum.edu/kb/computable.owl#>
		PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#>
		PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>
		PREFIX owl:<http://www.w3.org/2002/07/owl#>
		PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
		PREFIX knowrob:<http://ias.cs.tum.edu/kb/knowrob.owl#>

		SELECT ?out
		WHERE
		{
		    knowrob:DrinkingGlass rdfs:subClassOf ?e .
		    ?e rdfs:subClassOf ?d .
		    ?d rdfs:subClassOf ?f .
		    ?stored rdfs:domain ?f .
		    ?node owl:onProperty ?stored .
		    ?node owl:someValuesFrom ?out
		}
		 */

		//Documentation
		//http://www.openrdf.org/doc/sesame2/users/ch08.html#d0e810
	}

}

package reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class Main {

	private static Repository myRepository;
	private static final String SESAME_SERVER = "http://asimov.ludat.lth.se/openrdf-sesame/";
	private static final String REPOSITORY_ID = "KnowRob";
	
	private static String drinkingGlassStored = 
		"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX computable:<http://ias.cs.tum.edu/kb/computable.owl#> " +
		"PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#> " +
		"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> " +
		"PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX knowrob:<http://ias.cs.tum.edu/kb/knowrob.owl#> " +
		"SELECT ?out " +
		"WHERE " +
		"{ " +
		    "knowrob:DrinkingGlass rdfs:subClassOf ?e . " +
		    "?e rdfs:subClassOf ?d . " +
		    "?d rdfs:subClassOf ?f . " +
		    "?stored rdfs:domain ?f . " +
		    "?node owl:onProperty ?stored . " +
		    "?node owl:someValuesFrom ?out " +
		"}";
	
	public static void main(String[] args) {
		myRepository = new HTTPRepository(SESAME_SERVER, REPOSITORY_ID);
		try {
			myRepository.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		System.out.println("KnowRob repository:" + "\n");
		System.out.println("1. Where may a drinking glass be stored? (Explore the concept knowrob:DrinkingGlass by hand to find out the answer and to form your SPARQL query.)");
		ArrayList<HashMap<String, Value>> list = runSPARQL(drinkingGlassStored);
		HashMap<String, Value> value = list.get(0);
		System.out.println("Answer: " + value.get("out") + "\n");
		System.out.println("2. What are the \"putting\" actions a robot using KnowRob may reason about and what are their subactions? (Explore knowrob:Movement-TranslationEvent and knowrob:subAction.)");
		System.out.println("Answer: ");
	}
	
	/**
     *  Execute a SELECT SPARQL query against the graph 
     * 
     * @param queryString SELECT SPARQL query
     * @return list of solutions, each containing a HashMap<String, Value> of bindings
     */
    public static ArrayList<HashMap<String, Value>> runSPARQL(String queryString) {
        try{
            RepositoryConnection con = myRepository.getConnection();
            try {
                TupleQuery query = 
                    con.prepareTupleQuery(
                    org.openrdf.query.QueryLanguage.SPARQL, queryString);
                TupleQueryResult qres = query.evaluate();
                ArrayList<HashMap<String, Value>> reslist = new ArrayList<HashMap<String, Value>>();
                while (qres.hasNext()) {
                    BindingSet b = qres.next();
                    Set<String> names = b.getBindingNames();
                    HashMap<String, Value> hm = new HashMap<String, Value>();
                    for (Object n : names) {
                        hm.put((String) n, b.getValue((String) n));
                    }
                    reslist.add(hm);
                }
                return reslist;
            } finally {
                con.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
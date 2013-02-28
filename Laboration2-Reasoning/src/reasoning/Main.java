package reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import org.openrdf.model.Value;

/**
 * Welcome to SPARQL; biggest hell on earth.
 * 
 * The task consists of accessing two repositories and performing simple
 * operations on the knowledge stored there.
 * 
 * @author CH
 * 
 */
public class Main {
	
	private static final String KNOW_ROB_SESAME_SERVER = "http://asimov.ludat.lth.se/openrdf-sesame/";
	private static final String OPEN_RDF_SESAME_SERVER = "http://asimov.ludat.lth.se/openrdf-sesame/repositories/Assignment2";
	private static final String KNOW_ROB_REPOSITORY_ID = "KnowRob";
	private static final String OPEN_RDF_REPOSITORY_ID = "openrdf-sesame";
	
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
		SesameRepository knowRobRepository = new SesameRepository(KNOW_ROB_SESAME_SERVER, KNOW_ROB_REPOSITORY_ID);
		SesameRepository openRdfRepository = new SesameRepository(OPEN_RDF_SESAME_SERVER, OPEN_RDF_REPOSITORY_ID);
		
		System.out.println("KnowRob repository:" + "\n");
		System.out.println("1. Where may a drinking glass be stored? (Explore the concept knowrob:DrinkingGlass by hand to find out the answer and to form your SPARQL query.)");
		ArrayList<HashMap<String, Value>> drinkingGlassQuery = knowRobRepository.selectSPARQL(drinkingGlassStored);
		if(drinkingGlassQuery != null && drinkingGlassQuery.get(0) != null) {
			HashMap<String, Value> value = drinkingGlassQuery.get(0);
			System.out.println("Answer: " + value.get("out") + "\n");
		}
		System.out.println("2. What are the \"putting\" actions a robot using KnowRob may reason about and what are their subactions? (Explore knowrob:Movement-TranslationEvent and knowrob:subAction.)");
		System.out.println("Answer: ");
		
		
		System.out.println("openrdf-sesame: ");
		
		System.out.println("1. Which robots (rosetta:Device) have payload larger than 3kg? Larger than 12kg?");
		
		System.out.println("2. What are the maximum forces (rosetta:MaxForce) exerted by the available vacuum grippers?");
		
		System.out.println("3. Which devices may perform the skill rosetta:LinearMove? Note that concept equivalence is involved here.");
	}
}
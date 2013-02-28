package reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
	
	private static final String SESAME_SERVER = "http://asimov.ludat.lth.se/openrdf-sesame/";
	private static final String KNOW_ROB_REPOSITORY_ID = "KnowRob";
	private static final String ASSIGNMENT_2_REPOSITORY_ID = "Assignment2";
	
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
		SesameRepository knowRobRepository = new SesameRepository(SESAME_SERVER, KNOW_ROB_REPOSITORY_ID);
		SesameRepository openRdfRepository = new SesameRepository(SESAME_SERVER, ASSIGNMENT_2_REPOSITORY_ID);
		
		System.out.println("KnowRob repository:" + "\n");
		System.out.println("1. Where may a drinking glass be stored? (Explore the concept knowrob:DrinkingGlass by hand to find out the answer and to form your SPARQL query.)");
		ArrayList<HashMap<String, Value>> drinkingGlassQuery = knowRobRepository.selectSPARQL(drinkingGlassStored);
		if(drinkingGlassQuery != null && drinkingGlassQuery.get(0) != null) {
			HashMap<String, Value> value = drinkingGlassQuery.get(0);
			System.out.println("Answer: " + value.get("out") + "\n");
		}
		System.out.println("2. What are the \"putting\" actions a robot using KnowRob may reason about and what are their subactions? (Explore knowrob:Movement-TranslationEvent and knowrob:subAction.)");
		System.out.println("Answer: ");
		Ass2 a = new Ass2(knowRobRepository,openRdfRepository);
		//uses recursive search for subclasses with Putting in the name
		HashSet<String> puttingActions = a.findPuttingActions();
		for( String puttingAction: puttingActions) {
			System.out.println(puttingAction);
			for(String subAction : a.getSubActionForAction(puttingAction)) {
				System.out.println("\t" + subAction);
			}
		}
		
		
		
		
		System.out.println("openrdf-sesame: ");
		
		System.out.println("1. Which robots (rosetta:Device) have payload larger than 3kg?");
		for(String s: a.getThingsWithPayload(3)) {
			System.out.println(s);
		}
		System.out.println("Larger than 12kg?");
		for(String s: a.getThingsWithPayload(12)) {
			System.out.println(s);
		}
		
		System.out.println("2. What are the maximum forces (rosetta:MaxForce) exerted by the available vacuum grippers?");
		for(String s: a.getMaximumForcesExcertedByVaccumGrippers()) {
			System.out.println(s);
		}
		System.out.println("3. Which devices may perform the skill rosetta:LinearMove? Note that concept equivalence is involved here.");
		for(String s: a.getDevicesWithLinearMoveSkill()) {
			System.out.println(s);
		}
	}
	
}
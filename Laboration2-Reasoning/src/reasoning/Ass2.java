package reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.openrdf.model.Value;

public class Ass2 {
		private static final String prefixes = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
			"PREFIX computable:<http://ias.cs.tum.edu/kb/computable.owl#> " +
			"PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#> " +
			"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> "+
			"PREFIX owl:<http://www.w3.org/2002/07/owl#> "+
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			"PREFIX knowrob:<http://ias.cs.tum.edu/kb/knowrob.owl#> ";

		private static final String puttingQuery1 = prefixes + "SELECT ?e "+
			"WHERE { "+
				"?e rdfs:subClassOf <http://ias.cs.tum.edu/kb/knowrob.owl#Movement-TranslationEvent> . "+
			"} ";
		
		private static String recTemplate = prefixes + "SELECT ?e "+
			"WHERE { "+
				"?e rdfs:subClassOf <[superclass]> . "+
			"} ";
		
		private static final String subActionQuery = prefixes + " Select ?e "+
			"where { "+ 
			" <[action]> rdfs:subClassOf ?t . " +
			"?t owl:onProperty knowrob:subAction . "+
			" ?t owl:someValuesFrom ?e . "+
			"} "; 
	HashSet<String> puttingActions;
	SesameRepository knowRobRepo;
	Ass2(SesameRepository knowRobRepo) {
		puttingActions = new HashSet<String>();
		this.knowRobRepo = knowRobRepo;
	}
	/**
	 * @param args
	 */
	public HashSet<String> findPuttingActions() {
		ArrayList<HashMap<String, Value> > subClasses = knowRobRepo.selectSPARQL(puttingQuery1);
		recursiveSearch(subClasses);
		return puttingActions;
	}
	
	private void recursiveSearch(ArrayList<HashMap<String, Value> > in) {
		if(in != null && in.size() != 0){
			for(HashMap<String, Value> row : in) {
				String rsp = row.get("e").toString();
				if(rsp.matches(".*Putting.*")){
					if(puttingActions.add(rsp)) {
						String query = recTemplate.replace("[superclass]", rsp);
						ArrayList<HashMap<String, Value> > subClasses = knowRobRepo.selectSPARQL(query);
						recursiveSearch(subClasses);
					}
				}
			}
		}
		
	}
	
	public ArrayList<String> getSubActionForAction(String action) {
		ArrayList<HashMap<String, Value> > subActions = knowRobRepo.selectSPARQL(subActionQuery.replace("[action]", action));
		ArrayList<String> rtn = new ArrayList<String>();
		if(subActions != null ) {
			for(HashMap<String, Value> row : subActions) {
				rtn.add(row.get("e").toString());
			}
		}
		return rtn;
	}

}

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
		
		private static final String rosettaPrefixes = "PREFIX dc:<http://purl.org/dc/elements/1.1/> "+
			"PREFIX ObjectList:<http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#> "+
			"PREFIX qudt:<http://data.nasa.gov/qudt/owl/qudt#> "+
			"PREFIX drsonto040520:<http://cs-www.cs.yale.edu/homes/dvm/daml/drsonto040520.owl#> "+
			"PREFIX unit:<http://data.nasa.gov/qudt/owl/unit#> "+
			"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "+
			"PREFIX swrl:<http://www.w3.org/2003/11/swrl#> "+
			"PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#> "+
			"PREFIX Process:<http://www.daml.org/services/owl-s/1.2/Process.owl#> "+
			"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> "+
			"PREFIX owl:<http://www.w3.org/2002/07/owl#> "+
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
			"PREFIX Expression:<http://www.daml.org/services/owl-s/1.2/generic/Expression.owl#> "+
			"PREFIX rosetta:<http://kif.cs.lth.se/ontologies/rosetta.owl#> ";
		
		private static String payloadQueryTemplate = rosettaPrefixes + " Select ?e ?p ?d "+
			"WHERE { "+
			" ?e rdf:type rosetta:Payload . "+
			" ?e rosetta:value ?p . "+
			" FILTER (?p > [lowerPayloadBound])  "+
			" ?e rosetta:isPropertyOf ?d "+
			"} ";
	HashSet<String> puttingActions;
	SesameRepository knowRobRepo;
	SesameRepository ass2Repo;
	
	Ass2(SesameRepository knowRobRepo, SesameRepository ass2Repo) {
		puttingActions = new HashSet<String>();
		this.knowRobRepo = knowRobRepo;
		this.ass2Repo = ass2Repo;
	}
	
	HashSet<String> getThingsWithPayload(float lowerBound) {
		HashSet<String> rtn = new HashSet<String>();
		ArrayList<HashMap<String, Value> > devices = ass2Repo.selectSPARQL(payloadQueryTemplate.replace("[lowerPayloadBound]", Float.toString(lowerBound)));
		if( devices != null) {
			for( HashMap<String, Value> row : devices) {
				rtn.add(row.get("d").toString());
			}
		}
		return rtn;
		
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

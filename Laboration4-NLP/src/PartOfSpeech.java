import java.util.ArrayList;
import java.util.HashMap;


public class PartOfSpeech {
	public String pos;
	public int occurrences;
	HashMap<String, Integer> pposList;
	
	public PartOfSpeech(String pos, String ppos) {
		pposList = new HashMap<String, Integer>();
		this.occurrences = 1;
		this.pos = pos;
		putPPOS(ppos);
	}
	
	public void putPPOS(String ppos) {
		Integer pposOccurred =  pposList.get(ppos);
		if(pposOccurred != null) {
			pposList.put(ppos, pposList.get(ppos) + 1);
		} else {
			pposList.put(ppos, 1);
		}
	}
	
	public String computeConfusion(ArrayList<String> pposOrder) {
		String toReturn = "";
		for(String s : pposOrder) {
			Integer pposOccurred =  pposList.get(s);
			if(pposOccurred != null) {
				toReturn += pposOccurred.toString() + ';';
			} else {
				toReturn += "0;";
			}
		}
		return toReturn;
	}
}

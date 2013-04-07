import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Parser {
	private DataInputStream in;
	private BufferedReader br;
	private HashMap<String, PartOfSpeech> lemmaOccurences; 
	private HashMap<String, PartOfSpeech> posOccurences; 
	private HashMap<String, PartOfSpeech> mostFrequentPOSMap;;
	private HashMap<String, HashMap<String, Integer>> posBigrams;
	//POS, FORM, FORMCount 
	private HashMap<String, HashMap<String, Integer>> posFormMap;
	
	private String previous;

	public Parser(String fileName) {
		lemmaOccurences = new HashMap<String, PartOfSpeech>();
		posOccurences = new HashMap<String, PartOfSpeech>();
		posBigrams = new HashMap<String, HashMap<String, Integer>>();
		posFormMap = new HashMap<String, HashMap<String, Integer>>();
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void parse() {
		String strLine;
		try {
			while ((strLine = br.readLine()) != null)   {
				processLine(strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void processLine(String line) {
		String[] data = line.split("\\s+");
		if(data.length > 1) {
			//LEMMA and word frequency
			//partOfSpeech.occurrences = word frequency
			//partOfSpeech.pposList = <pos, frequency>
			data[1] = data[1].toLowerCase();
			PartOfSpeech lemmaOccurred =  lemmaOccurences.get(data[1]);
			if(lemmaOccurred != null) {
				lemmaOccurred.occurrences++;
				lemmaOccurred.putPPOS(data[4]);
			} else {
				lemmaOccurences.put(data[1], new PartOfSpeech(data[1], data[4]));
			}
			//POS PPOS
			PartOfSpeech posOccurred = posOccurences.get(data[4]);
			if(posOccurred != null) {
				posOccurred.occurrences++;
				posOccurred.putPPOS(data[5]);
			} else {
				posOccurences.put(data[4], new PartOfSpeech(data[4], data[5]));
			}			
			//POS BIGRAMS
			if(previous == null) previous = "START";
			HashMap<String, Integer> posBigramOccurred = posBigrams.get(previous);
			if(posBigramOccurred != null) {
				Integer posOccurrences = posBigramOccurred.get(data[4]);
				if(posOccurrences != null) {
					posBigramOccurred.put(data[4], posOccurrences + 1);
				} else {
					posBigramOccurred.put(data[4], 1);
				}
			} else {
				HashMap<String, Integer> posMap = new HashMap<String, Integer>();
				posMap.put(data[4], 1);
				posBigrams.put(previous, posMap);
			}
			previous = data[4];
			//POS FORM COUNT
			HashMap<String, Integer> posFormOccurred = posFormMap.get(data[4]);
			if(posFormOccurred != null) {
				Integer formOccurrences = posFormOccurred.get(data[1]);
				if(formOccurrences != null) {
					posFormOccurred.put(data[1], formOccurrences + 1);
				} else {
					posFormOccurred.put(data[1], 1);
				}
			} else {
				HashMap<String, Integer> formMap = new HashMap<String, Integer>();
				formMap.put(data[1], 1);
				posFormMap.put(data[4], formMap);
			}
		}
	}

	public HashMap<String, PartOfSpeech> getLemmaOccurrences() {
		return lemmaOccurences;
	}

	public HashMap<String, PartOfSpeech> getPOSOccurrences() {
		return posOccurences;
	}
	
	/**
	 * Extract all the POS bigrams and estimate P(ti|ti-1).
	 * 
	 * @return
	 */
	public HashMap<String, NPair<String, Float>> getPOSBigrams() {
		ArrayList<NPair<String, Float>> posBigramProbabilities = new ArrayList<NPair<String, Float>>();
		HashMap<String, NPair<String, Float>> toReturn = new HashMap<String, NPair<String, Float>>();
		Iterator<Map.Entry<String, HashMap<String, Integer>>> it = posBigrams.entrySet().iterator();
		int index = 0;
		int loopIndex = 0;
		while (it.hasNext()) {
			NPair<String, Float> mostProbablePOS = new NPair<String, Float>("_", Float.MIN_VALUE);
			int totalAmount = 0;
			Map.Entry<String, HashMap<String, Integer>> prePair = (Map.Entry<String, HashMap<String, Integer>>)it.next();
			Iterator<Map.Entry<String,Integer>> currentIt = prePair.getValue().entrySet().iterator();
			while(currentIt.hasNext()) {
				Map.Entry<String, Integer> currentPair = (Map.Entry<String, Integer>)currentIt.next();
				totalAmount += currentPair.getValue();
				posBigramProbabilities.add(new NPair<String, Float>(currentPair.getKey(), (float)currentPair.getValue()));
				index++;
			}
			for(int i = loopIndex; i < index; i++) {
				posBigramProbabilities.set(i, new NPair<String, Float>(posBigramProbabilities.get(i).e, posBigramProbabilities.get(i).v / (float)totalAmount));
				loopIndex++;
				if(posBigramProbabilities.get(i).v > mostProbablePOS.v) {
					mostProbablePOS = posBigramProbabilities.get(i);
				}
			}
			toReturn.put(prePair.getKey(), mostProbablePOS);		
		}
		return toReturn;
	}
		
		
	/**
	 * For all the relevant pairs, extract and estimate P(wi|ti).
	 * Relevant lol?
	 * 
	 * @return
	 */
	public HashMap<String, NPair<String, Float>> getPWT() {
		ArrayList<NPair<String, Float>> posFormProbabilities = new ArrayList<NPair<String, Float>>();
		HashMap<String, NPair<String, Float>> toReturn = new HashMap<String, NPair<String, Float>>();
		Iterator<Map.Entry<String, HashMap<String, Integer>>> it = posFormMap.entrySet().iterator();
		int index = 0;
		int loopIndex = 0;
		while (it.hasNext()) {
			int totalAmount = 0;
			NPair<String, Float> mostProbablePOS = new NPair<String, Float>("_", Float.MIN_VALUE);
			Map.Entry<String, HashMap<String, Integer>> formPair = (Map.Entry<String, HashMap<String, Integer>>)it.next();
			Iterator<Map.Entry<String, Integer>> iter = formPair.getValue().entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Integer> posPair = (Map.Entry<String, Integer>)iter.next();
				totalAmount += posPair.getValue();
				posFormProbabilities.add(new NPair<String, Float>(posPair.getKey(), (float)posPair.getValue()));
				index++;
			}
			for(int i = loopIndex; i < index; i++) {
				posFormProbabilities.set(i, new NPair<String, Float>(posFormProbabilities.get(i).e, posFormProbabilities.get(i).v / (float)totalAmount));
				loopIndex++;
				if(posFormProbabilities.get(i).v > mostProbablePOS.v) {
					mostProbablePOS = posFormProbabilities.get(i);
				}
			}
			toReturn.put(formPair.getKey(), mostProbablePOS);	
		}
		return toReturn;
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}

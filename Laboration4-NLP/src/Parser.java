import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class Parser {
	private DataInputStream in;
	private BufferedReader br;
	private HashMap<String, PartOfSpeech> lemmaOccurences; 
	private HashMap<String, PartOfSpeech> posOccurences; 
	private HashMap<String, PartOfSpeech> mostFrequentPOSMap;;
	private HashMap<String, Integer> posBigrams;
	
	private String previous;

	public Parser(String fileName) {
		lemmaOccurences = new HashMap<String, PartOfSpeech>();
		posOccurences = new HashMap<String, PartOfSpeech>();
		posBigrams = new HashMap<String, Integer>();
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
			//POS
			PartOfSpeech posOccurred = posOccurences.get(data[4]);
			if(posOccurred != null) {
				posOccurred.occurrences++;
				posOccurred.putPPOS(data[5]);
			} else {
				posOccurences.put(data[4], new PartOfSpeech(data[4], data[5]));
			}			
			//POS BIGRAMS
			if(previous == null) previous = "START";
			Integer posBigramsOccurred = posBigrams.get(previous + " " + data[4]);
			if(posBigramsOccurred != null) {
				posBigrams.put(previous + " " + data[4], posBigramsOccurred + 1);
			} else {
				posBigrams.put(previous + " " + data[4], 1);
			}
			previous = data[4];
		}
	}

	public HashMap<String, PartOfSpeech> getLemmaOccurrences() {
		return lemmaOccurences;
	}

	public HashMap<String, PartOfSpeech> getPOSOccurrences() {
		return posOccurences;
	}
	
	public HashMap<String, Integer> getPOSBigrams() {
		return posBigrams;
	}
}

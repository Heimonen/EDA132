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
	private ArrayList<PartOfSpeech> processedCorpra;
	private HashMap<String, Integer> lemmaOccurences; 
	private HashMap<String, PartOfSpeech> posOccurences; 

	public Parser(String fileName) {
		lemmaOccurences = new HashMap<String, Integer>();
		posOccurences = new HashMap<String, PartOfSpeech>();
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
			//LEMMA
			data[1] = data[1].toLowerCase();
			Integer lemmaOccurred =  lemmaOccurences.get(data[1]);
			if(lemmaOccurred != null) {
				//check if can be done more efficiently
				lemmaOccurences.put(data[1], lemmaOccurences.get(data[1]) + 1);
			} else {
				lemmaOccurences.put(data[1], 1);
			}
			//POS
			PartOfSpeech posOccurred = posOccurences.get(data[4]);
			if(posOccurred != null) {
				posOccurred.occurrences++;
				posOccurred.putPPOS(data[5]);
			} else {
				posOccurences.put(data[4], new PartOfSpeech(data[4], data[5]));
			}
		}
	}

	public HashMap<String, Integer> getLemmaOccurrences() {
		return lemmaOccurences;
	}

	public HashMap<String, PartOfSpeech> getPOSOccurrences() {
		return posOccurences;
	}
}

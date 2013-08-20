package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class HMMExtractor {
	private String contentPath;
	private HashMap<String,HashMap<String, Integer>> posposMap;
	private HashMap<String,HashMap<String, Integer>> formposMap;
	public HashMap<BiGram,Double> posposProb;
	public HashMap<BiGram, Double> formposProb;
	public HMMExtractor(String path) {
		this.contentPath = path;
		posposMap = new HashMap<String, HashMap<String,Integer>>();
		formposMap = new HashMap<String, HashMap<String,Integer>>();
	}

	public void parse() {

		try {
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			String pForm = "";
			String pPos = "";
			while( (line= in.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String pos = "";
				String form = "";
				String id= "";
				try {
					id = lineSplit[0];
					if(id.equals("1")) {
						pForm = "BOS";
						pPos = "BOS";
					}
					pos = lineSplit[4];
					form = lineSplit[1];
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}
				
				HashMap<String, Integer> num = posposMap.get(pPos);
				if( num == null){
					num = new HashMap<String, Integer>();
					posposMap.put(pPos, num);
				}
				Integer count = num.get(pos);
				if(count == null) {
					num.put(pos, 1);
				} else {
					num.put(pos, count + 1);
				}
				
				num = formposMap.get(pos);
				if( num == null ) {
					num = new HashMap<String, Integer>();
					formposMap.put(pos, num);
				}
				count = num.get(form);
				if(count== null) {
					num.put(form, 1);
				} else {
					num.put(form, count + 1);
				}
				
				pForm = form;
				pPos = pos;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void postProcess() {
		posposProb = new HashMap<BiGram, Double>();
		formposProb = new HashMap<BiGram, Double>();
		
		for( Entry<String, HashMap<String, Integer>>e : posposMap.entrySet()) {
			int total = 0;
			for(Entry<String, Integer> ee : e.getValue().entrySet() ) {
				total += ee.getValue();
			}
			for(Entry<String, Integer> ee : e.getValue().entrySet() ) {
				posposProb.put(new BiGram(e.getKey(), ee.getKey()), ee.getValue()/(double)total);
			}
			
		}
		
		for( Entry<String, HashMap<String, Integer>>e : formposMap.entrySet()) {
			int total = 0;
			for(Entry<String, Integer> ee : e.getValue().entrySet() ) {
				total += ee.getValue();
			}
			for(Entry<String, Integer> ee : e.getValue().entrySet() ) {
				formposProb.put(new BiGram(e.getKey(), ee.getKey()), ee.getValue()/(double)total);
			}
			
		}
	}
}

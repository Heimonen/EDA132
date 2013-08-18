package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class POSParser {
	private String contentPath;
	public HashMap<String,Integer> posOccurences;

	public POSParser(String path) {
		this.contentPath = path;
		posOccurences = new HashMap<String, Integer>();
	}

	public void parse() {

		try {
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			while( (line= in.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String pos = "";
				try {
					pos = lineSplit[4];
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}
				
				
				Integer num = posOccurences.get(pos);
				if(num == null) {
					posOccurences.put(pos, 1);
				} else {
					posOccurences.put(pos, num + 1);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

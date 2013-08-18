package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class BaselineTagger {
	private String contentPath;
	private HashMap<String, String> formPosMap;

	public BaselineTagger(String path, HashMap<String, String> formPosMap) {
		this.contentPath = path;
		this.formPosMap = formPosMap;
	}

	public void parse() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("output/baselinetagger_result.txt"));
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			while( (line= in.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String pos = "";
				String ppos = "";
				String form = "";
				String something = "";
				String lemma = "";
				String number = "";
				try {
					number = lineSplit[0];
					form = lineSplit[1];
					lemma = lineSplit[2];
					something = lineSplit[3];
					pos = lineSplit[4];	
					ppos = formPosMap.get(form);
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}					
				out.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\n", number, form, lemma, something, pos, ppos));
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

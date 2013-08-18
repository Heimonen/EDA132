package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FormParser {
	private String contentPath;
	public HashMap<String,Integer> formOccurences;

	public FormParser(String path) {
		this.contentPath = path;
		formOccurences = new HashMap<String, Integer>();
	}

	public void parse() {

		try {
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			while( (line= in.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String form = "";
				try {
					form = lineSplit[1];
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}
				
				
				Integer num = formOccurences.get(form);
				if(num == null) {
					formOccurences.put(form, 1);
				} else {
					formOccurences.put(form, num + 1);
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

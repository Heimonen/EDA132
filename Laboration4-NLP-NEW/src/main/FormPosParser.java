package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class FormPosParser {
	private String contentPath;
	public HashMap<String, HashMap<String, Integer>> matrix;

	public FormPosParser(String path) {
		this.contentPath = path;
		matrix = new HashMap<String, HashMap<String, Integer>>();		
	}

	private void parse() {

		try {
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			while( (line= in.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String form = "";
				String pos = "";
				try {
					form = lineSplit[1];
					pos = lineSplit[4];
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}
				ConfusionColumn.approvedPos.add(form);
				HashMap<String, Integer> column = matrix.get(form);
				if( column == null) {
					column = new HashMap<String, Integer>();
					matrix.put(form, column);
				}
				Integer num = column.get(pos);
				if( num == null) {
					num = 0;
				}
				num++;
				column.put(pos, num);
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getBaselineTaggerMap() {
		parse();
		HashMap<String, String> toReturn = new HashMap<String, String>();
		for(Entry<String, HashMap<String, Integer>> form : matrix.entrySet()) {
			int highest = Integer.MIN_VALUE;
			String key = "";
			for(Entry<String, Integer> pos : form.getValue().entrySet()) {
				if(pos.getValue() > highest) {
					highest = pos.getValue();
					key = pos.getKey();
				}
			}
			toReturn.put(form.getKey(), key);
		}
		return toReturn;
	}
	
}

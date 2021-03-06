package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class PosConfusionMatrixParser {
	private String contentPath;
	public HashMap<String, HashMap<String, Integer>> matrix;

	public PosConfusionMatrixParser(String path) {
		this.contentPath = path;
		matrix = new HashMap<String, HashMap<String, Integer>>();		
	}

	public void parse() {

		try {
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			while( (line= in.readLine()) != null) {
				String[] lineSplit = line.split("\t");
				String pos = "";
				String ppos = "";
				try {
					pos = lineSplit[4];
					ppos = lineSplit[5];
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}
				ConfusionColumn.approvedPos.add(pos);
				HashMap<String, Integer> column = matrix.get(pos);
				if( column == null) {
					column = new HashMap<String, Integer>();
					matrix.put(pos, column);
				}
				Integer num = column.get(ppos);
				if( num == null) {
					num = 0;
				}
				num++;
				column.put(ppos, num);
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getMatrix() {
		System.out.print('\t');
		for(String pos : ConfusionColumn.approvedPos){
			System.out.print(String.format("%s\t", pos));
		}
		System.out.println();
		for( Entry<String,HashMap<String, Integer>> column: matrix.entrySet()) {
			System.out.print(String.format("%s|\t", column.getKey()));
			for(String pos : ConfusionColumn.approvedPos) {
				Integer num = column.getValue().get(pos);
				System.out.print(String.format("%d\t", num == null? 0: num));
			}
			System.out.println();
		}
	}
}

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
	public ArrayList<ConfusionColumn> matrix;

	public PosConfusionMatrixParser(String path) {
		this.contentPath = path;
		matrix = new ArrayList<ConfusionColumn>();
		for(String s: ConfusionColumn.approvedPOS) {
			matrix.add(new ConfusionColumn(s));
		}
		
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
				
				int posIndex = ConfusionColumn.approvedAL.indexOf(pos);
				int pposIndex = ConfusionColumn.approvedAL.indexOf(ppos);
				if(  posIndex != -1 && pposIndex != -1){
					int count = matrix.get(posIndex).count.get(pposIndex);
					matrix.get(posIndex).count.set(pposIndex, count+1);
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
	
	public void getMatrix() {
		for(ConfusionColumn cc : matrix){
			System.out.print(cc.key + "\t");
			for(Integer count :cc.count) {
				System.out.print(count);
				System.out.print("\t");
			}
			System.out.println();
		}
	}
}

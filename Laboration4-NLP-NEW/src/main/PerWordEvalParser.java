package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class PerWordEvalParser {
	private String contentPath;
	public HashMap<String,EvalPair> posPerWordAccEval;

	public PerWordEvalParser(String path) {
		this.contentPath = path;
		posPerWordAccEval = new HashMap<String, EvalPair>();
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
				
				
				EvalPair num = posPerWordAccEval.get(pos);
				if(num == null) {
					num = new EvalPair();
					posPerWordAccEval.put(pos, num);
				}
				if(pos.equals(ppos)) {
					num.correct++;
				} else {
					num.fail++;
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
	
	public void getAcc() {
		int total = 0;
		int totalCorrect = 0;
		for(Entry<String,EvalPair> e: this.posPerWordAccEval.entrySet()) {
			EvalPair ep = e.getValue();
			totalCorrect +=ep.correct;
			total+= (ep.fail + ep.correct); 
			System.out.println(String.format("%s - %f", e.getKey(),ep.correct/((double)ep.correct+ep.fail)));
		}
		System.out.println(totalCorrect/(double)total);
	}
}

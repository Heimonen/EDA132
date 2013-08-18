package main;

import java.util.Map.Entry;

public class Main {
	
	public static void main(String[] args) {
		//corpus processing
		//ass1
		FormParser formp = new FormParser("input/CoNLL2009-ST-English-train-pos.txt");
		formp.parse();
		
		for(Entry<String, Integer> e : formp.formOccurences.entrySet()) {
			System.out.println(String.format("%s - %s", e.getKey(), e.getValue()));
		}
		
		//ass2
		POSParser posp = new POSParser("input/CoNLL2009-ST-English-train-pos.txt");
		posp.parse();
		
		for(Entry<String, Integer> e : posp.posOccurences.entrySet()) {
			System.out.println(String.format("%s - %s", e.getKey(), e.getValue()));
		}
		
		//evaluation program
		//ass1
		PerWordEvalParser pwep = new PerWordEvalParser("input/CoNLL2009-ST-English-train-pos.txt");
		pwep.parse();
		pwep.getAcc();
		
		//ass2
		PosConfusionMatrixParser asd = new PosConfusionMatrixParser("input/CoNLL2009-ST-English-train-pos.txt");
		asd.parse();
		asd.getMatrix();
	}

}

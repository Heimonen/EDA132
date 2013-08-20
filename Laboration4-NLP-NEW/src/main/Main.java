package main;

import java.util.HashMap;
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
		//ass 3
		asd.getMatrix();
		
		//Baseline tagger
		//ass 1
		FormPosParser fpp = new FormPosParser("input/CoNLL2009-ST-English-train-pos.txt");
		HashMap<String, String> baselineMap =  fpp.getBaselineTaggerMap();
		//ass 2
		BaselineTagger bt = new BaselineTagger("input/CoNLL2009-ST-English-development-pos.txt", baselineMap);
		bt.parse();
		//ass 3
		PerWordEvalParser pwepBaseline = new PerWordEvalParser("output/baselinetagger_result.txt");
		pwepBaseline.parse();
		pwepBaseline.getAcc();
		
		//POS tagger using hidden Markov models
		//ass 1 & 2
		HMMExtractor hmme = new HMMExtractor("input/CoNLL2009-ST-English-train-pos.txt");
		hmme.parse();
		hmme.postProcess();
		
		String[] sentence= {"BOS","The", "luxury", "auto", "maker", "last", "year", "sold", "1,214", "cars", "in", "the", "U.S"};
		String[] poses = new String[ConfusionColumn.approvedPos.size()+1];
		int asdasdasdi = 0;
		poses[asdasdasdi++] = "BOS";
		for(String s: ConfusionColumn.approvedPos) {
			poses[asdasdasdi++] = s;
		}
		ViterbiAlgo va = new ViterbiAlgo(sentence, poses, hmme.posposProb, hmme.formposProb);
		va.run();
		
		String[] nsentence = {"BOS", "Housing", "starts", "are", "expected", "to", "quicken", "a", "bit", "from", "August", "'s", "annual", "pace", "of", "1,350,000", "units", "."};
		ViterbiAlgo vaa = new ViterbiAlgo(nsentence, poses, hmme.posposProb, hmme.formposProb);
		vaa.run();
		
		va.predictPosOnDevelopmentSet("input/CoNLL2009-ST-English-development-pos.txt", hmme.posposProb, hmme.formposProb);
		PerWordEvalParser pwepViterbi = new PerWordEvalParser("output/viterbi_result.txt");
		pwepViterbi.parse();
		pwepViterbi.getAcc();
		
	}
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {
	public static void main(String[] args) {
		Parser testParser = new Parser("files/CoNLL2009-ST-English-train-pos.txt");
		testParser.parse();
		HashMap<String, PartOfSpeech> trainLemmaOccurrences = testParser.getLemmaOccurrences();
		HashMap<String, PartOfSpeech> trainPosOccurrences = testParser.getPOSOccurrences();
		HashMap<String, NPair<String, Float>> posBigrams = testParser.getTransitionGraph();
		HashMap<String, NPair<String, Float>> pwt = testParser.getPWT();
		HashMap<String, NPair<String, Float>> formPosMap = testParser.getMostProbablePOSForForms();
		HashMap<String, Float> emissionGraph = testParser.getEmissionGraph();
		Evaluator testEvaluator = new Evaluator(trainPosOccurrences);
		testEvaluator.computeConfusionMatrix("trainConfusionMatrix");
		
		System.out.println("Evaluation program: 2\n");
		Parser developmentParser = new Parser("files/CoNLL2009-ST-English-development-pos.txt");
		developmentParser.parse();
		HashMap<String, PartOfSpeech> developmentPosOccurrences = developmentParser.getPOSOccurrences();
		Evaluator developmentEvaluator = new Evaluator(developmentPosOccurrences);
		developmentEvaluator.evaluate();
		developmentEvaluator.computeConfusionMatrix("developmentConfusionMatrix");
		System.out.println("");
		
		//Baseline tagger
		BaselineTagger baselineTagger = new BaselineTagger(trainLemmaOccurrences);
		baselineTagger.train();
		baselineTagger.tag("files/CoNLL2009-ST-English-development-pos.txt");
		
		System.out.println("Baseline tagger: 3\n");
		Parser baselineParser = new Parser("files/taggedDevelopment-pos.txt");
		baselineParser.parse();
		HashMap<String, PartOfSpeech> baselinePosOccurrences = baselineParser.getPOSOccurrences();
		Evaluator baselineEvaluator = new Evaluator(baselinePosOccurrences);
		baselineEvaluator.evaluate();
		baselineEvaluator.computeConfusionMatrix("baselineConfusionMatrix");
		HashMap<String, ArrayList<String>> posAssociatedWithPosesList = testParser.getListOfPosAssociatedWithPos();
		HiddenMarkovModel hmm = new HiddenMarkovModel(posBigrams, pwt, formPosMap, emissionGraph, testParser.getProbabilityForPosPlusPos(), posAssociatedWithPosesList);
		ArrayList<String> noisy = new ArrayList<String>();
		noisy.add("Classics");
		noisy.add("Take");
		noisy.add("the");
		noisy.add("Stage");
//		noisy.add("in");
//		noisy.add("Windy");
//		noisy.add("City");
		System.out.println();
		ArrayList<NPair<String, Float>> noisyResult = hmm.tag(noisy);
		for(int i = 0; i < noisy.size(); i++) {
			System.out.println(noisy.get(i) + "\t" + noisyResult.get(i).e + "\t" + noisyResult.get(i).v);
		}
		noisy.clear();
		noisy.add("START");
		noisy.add("The");
		noisy.add("economy");
		noisy.add("'s");
		noisy.add("temperature");
		noisy.add("will");
		noisy.add("be");
		noisy.add("taken");
		noisy.add("from");
		noisy.add("several");
		noisy.add("vantage");
		noisy.add("points");
		noisy.add("this");
		noisy.add("week");
		noisy.add(",");
		noisy.add("with");
		noisy.add("readings");
		noisy.add("on");
		noisy.add("trade");
		noisy.add(",");
		noisy.add("output");
		noisy.add(",");
		noisy.add("housing");
		noisy.add("and");
		noisy.add("inflation");
		noisy.add(".");
		System.out.println("\nViterbi test:");
		List<String> viterbiResult = hmm.viterbiTag(noisy);
		for(int i = 0; i < viterbiResult.size(); i++) {
			System.out.println(noisy.get(i) + '\t' + viterbiResult.get(i));
		}
//		hmm.applyNoisyChannelMode("No, it was not black friday. I hate my life to the bitter end!");
//		hmm.applyNoisyChannelModeOnFile("files/CoNLL2009-ST-English-development-pos.txt", 10);
//		hmm.viterbi("In an october 19");
		hmm.viterbiEvaluateFile("files/CoNLL2009-ST-English-development-pos.txt");
		Parser viterbiParser = new Parser("files/viterbiDevelopment-pos.txt");
		viterbiParser.parse();
		HashMap<String, PartOfSpeech> viterbiPosOccurrences = viterbiParser.getPOSOccurrences();
		Evaluator viterbiEvaluator = new Evaluator(viterbiPosOccurrences);
		System.out.println("POS tagger using hidden Markov models: 6\n");
		viterbiEvaluator.evaluate();
//		hmm.viterbiTestSet();
	}
}

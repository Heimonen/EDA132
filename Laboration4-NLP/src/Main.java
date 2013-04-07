import java.util.HashMap;


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
		
		HiddenMarkovModel hmm = new HiddenMarkovModel(posBigrams, pwt, formPosMap, emissionGraph);
//		hmm.applyNoisyChannelMode("No, it was not black friday. I hate my life to the bitter end!");
//		hmm.applyNoisyChannelModeOnFile("files/CoNLL2009-ST-English-development-pos.txt", 10);
		hmm.viterbi("In an october 19");
	}
}

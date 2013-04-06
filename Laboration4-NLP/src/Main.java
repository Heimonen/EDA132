import java.util.HashMap;


public class Main {
	public static void main(String[] args) {
		Parser testParser = new Parser("files/CoNLL2009-ST-English-train-pos.txt");
		testParser.parse();
		HashMap<String, Integer> trainLemmaOccurrences = testParser.getLemmaOccurrences();
		HashMap<String, PartOfSpeech> trainPosOccurrences = testParser.getPOSOccurrences();
		Evaluator testEvaluator = new Evaluator(trainPosOccurrences);
		testEvaluator.computeConfusionMatrix("testConfusionMatrix");
		
		System.out.println("Evaluation program: 2\n");
		Parser developmentParser = new Parser("files/CoNLL2009-ST-English-development-pos.txt");
		developmentParser.parse();
		HashMap<String, PartOfSpeech> developmentPosOccurrences = developmentParser.getPOSOccurrences();
		Evaluator developmentEvaluator = new Evaluator(trainPosOccurrences);
		developmentEvaluator.evaluate();
		developmentEvaluator.computeConfusionMatrix("developmentConfusionMatrix");
		System.out.println("\ndone");
	}
}

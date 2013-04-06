import java.util.HashMap;


public class Main {
	public static void main(String[] args) {
		Parser parser = new Parser("files/CoNLL2009-ST-English-development-pos.txt");
		parser.parse();
		HashMap<String, Integer> lemmaOccurrences = parser.getLemmaOccurrences();
		HashMap<String, PartOfSpeech> posOccurrences = parser.getPOSOccurrences();
		Evaluator evaluator = new Evaluator(posOccurrences);
		evaluator.evaluate();
		evaluator.computeConfusionMatrix();
		System.out.println("\ndone");
	}
}

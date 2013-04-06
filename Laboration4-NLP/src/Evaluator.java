import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Evaluator {

	private HashMap<String, PartOfSpeech> posMap;

	public Evaluator(HashMap<String, PartOfSpeech> posMap) {
		this.posMap = posMap;
	}

	public void evaluate() {

	}

	public void computeConfusionMatrix() {

		try{
			FileWriter fstream = new FileWriter("files/confusionMatrix.csv");
			BufferedWriter out = new BufferedWriter(fstream);

			Iterator<Map.Entry<String, PartOfSpeech>> it = posMap.entrySet().iterator();
			ArrayList<String> posList = new ArrayList<String>();
			while (it.hasNext()) {
				Map.Entry<String, PartOfSpeech> pairs = (Map.Entry<String, PartOfSpeech>)it.next();
				posList.add(pairs.getKey());
			}
			out.write(';');
			for(String s : posList) {
				out.write(s + ';');
			}
			out.write('\n');
			for(String s : posList) {
				out.write(s + ';');
				out.write(posMap.get(s).computeConfusion(posList) + '\n');
//				System.out.println();
			}
			out.close();

		}catch (Exception e){//Catch exception if any
			e.printStackTrace();
		}
	}

}

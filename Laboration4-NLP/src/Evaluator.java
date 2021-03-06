import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
		Iterator<Map.Entry<String, PartOfSpeech>> it = posMap.entrySet().iterator();
		Float sum = 0f;
		while (it.hasNext()) {
			Map.Entry<String, PartOfSpeech> pair = (Map.Entry<String, PartOfSpeech>)it.next();
			Integer pos = pair.getValue().occurrences;
			if(pos == null) {
				pos = 0;
			}
			Integer ppos = pair.getValue().pposList.get(pair.getKey());
			if(ppos == null) {
				ppos = 0;
			}
			float acc = ((float)ppos / (float)pos) * 100;
			System.out.println(pair.getKey() + "\t " + acc + '%');
			sum += acc;
		}
		System.out.println("\nAccuracy: " + (sum / ((float)posMap.size() * 100f)) * 100f + "%");
	}

	public void computeConfusionMatrix(String fileName) {
		BufferedWriter out = null;
		try{
			FileWriter fstream = new FileWriter("files/" + fileName + ".csv");
			out = new BufferedWriter(fstream);

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
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		if(out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class BaselineTagger {

	private HashMap<String, PartOfSpeech> trainMap;
	private HashMap<String, String> mostFrequentPOS;

	public BaselineTagger(HashMap<String, PartOfSpeech> trainMap) {
		this.trainMap = trainMap;
		mostFrequentPOS = new HashMap<String, String>();
	}

	public void train() {
		Iterator<Map.Entry<String, PartOfSpeech>> it = trainMap.entrySet().iterator();
		while (it.hasNext()) {
			String pos = "_";
			Map.Entry<String, PartOfSpeech> pair = (Map.Entry<String, PartOfSpeech>)it.next();

			int occurrences = Integer.MIN_VALUE;
			Iterator<Map.Entry<String, Integer>> posIt = pair.getValue().pposList.entrySet().iterator();
			while(posIt.hasNext()) {
				Map.Entry<String, Integer> posPair = (Map.Entry<String, Integer>)posIt.next();
				if(posPair.getValue() > occurrences) {
					pos = posPair.getKey();
					occurrences = posPair.getValue();
				}
			}
			mostFrequentPOS.put(pair.getKey(), pos);
		}
	}

	public String tag(String fileToTag) {
		String fileName = "files/taggedDevelopment-pos.txt";
		DataInputStream in = null;
		BufferedWriter out = null;
		try {
			FileWriter fOutStream = new FileWriter(fileName);
			out = new BufferedWriter(fOutStream);
			
			FileInputStream fstream = new FileInputStream(fileToTag);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = br.readLine()) != null) {
				String[] data = line.split("\\s+");
				if(data.length > 1) {
					String toWrite = "";
					for(int i = 0; i < data.length - 1; i++) {
						toWrite += data[i] + '\t';
					}
					String ppos = mostFrequentPOS.get(data[1]);
					if(ppos != null)
						toWrite += ppos;
					else
						toWrite += data[5];
					out.write(toWrite);
				} 
				out.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in != null){
					in.close();
				}
				if(out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileName;
	}
}

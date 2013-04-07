import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HiddenMarkovModel {
	
	private HashMap<String, NPair<String, Float>> posBigrams, posFormMap, formPosMap;
	private HashMap<String, Float> emissionGraph;
	public HiddenMarkovModel(HashMap<String, NPair<String, Float>> posBigrams, HashMap<String, NPair<String, Float>> posFormMap, HashMap<String, NPair<String, Float>> formPosMap, HashMap<String, Float> emissionGraph) {
		this.posBigrams = posBigrams;
		this.posFormMap = posFormMap;
		this.formPosMap = formPosMap;
		this.emissionGraph = emissionGraph;
	}
	
	
	public String applyNoisyChannelMode(String in) {
		String toReturn = "";
		System.out.println();
		int i = 0;
		String[] words = in.split("\\s+");
		NPair<String, Float> previous = null;
		NPair<String, Float> posPair = null;
		for(String s : words) {
			i++;
			NPair<String, Float> formPair = formPosMap.get(s.trim());
			if(previous != null) {
				posPair = posBigrams.get(previous.e);
			}
			if(formPair != null && posPair != null ) {
				NPair<String, Float> winner = formPair.v > posPair.v ? formPair : posPair;
				previous = winner;
				produceOutput(winner, i, s);
				toReturn = previous.e;
			} else if(formPair != null && posPair == null) {
				previous = formPair;
				produceOutput(formPair, i, s);
				toReturn = previous.e;
			} else if(posPair != null && formPair == null) {
				previous = posPair;
				produceOutput(posPair, i, s);
				toReturn = previous.e;
			} else {
				previous = null;
				produceOutput(new NPair<String, Float>("_", 0f), i, s);
				toReturn = "_";
			}	
		}
		return toReturn;
	}
	
	private void produceOutput(NPair<String, Float> in, int i, String word) {
		DecimalFormat f = new DecimalFormat("##.##");
		System.out.println(i + ".\t" + word +'\t' + in.e + '\t' + f.format(in.v * 100) + '%');
	}
	
	public String applyNoisyChannelModeOnFile(String fileToTag, int sentenceLength) {
		String fileName = "files/noisyDevelopment-pos.txt";
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
					String ppos = applyNoisyChannelMode(data[1]);
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
	
	public void viterbi(String word) {
		word = "START " + word.trim();
		String[] words = word.split("\\s+");
		float[][] probabilityMatrix = new float[posFormMap.size()][words.length];
		//Initialization
		float previousMax = 1.0f;
		Iterator<Map.Entry<String, NPair<String, Float>>> it = posFormMap.entrySet().iterator();
		ArrayList<String> posList = new ArrayList<String>();
		while (it.hasNext()) {
			Map.Entry<String, NPair<String, Float>> pair = (Map.Entry<String, NPair<String, Float>>)it.next();
			posList.add(pair.getKey());
		}
		for(int i = 0; i < posFormMap.size(); i++) {
			probabilityMatrix[i][0] = 0;
			if(posList.get(i).equals("START")){
				probabilityMatrix[i][0] = previousMax;
			}
		}
		for(int i = 1; i < words.length; i++) {
			if(previousMax == Float.MIN_VALUE) {
				previousMax = 1;
			}
			for(int j = 0; j < posFormMap.size(); j++) {
				Float transition = posBigrams.get(posList.get(j)).v;
				transition = transition != null ? transition : 0.0f;
				Float emission = emissionGraph.get(words[i] + " " + posList.get(j));
				emission = emission != null ? emission : 0.0f;
				probabilityMatrix[j][i] = previousMax * transition * emission; 
			}
			previousMax = Float.MIN_VALUE;
			for(int k = 0; k < posFormMap.size(); k++) {
				if(probabilityMatrix[k][i] > previousMax) {
					previousMax = probabilityMatrix[k][i];
				}
			}
		}
		//Get the path
		String[] toReturn = new String[words.length];
		String[] matrix = new String[posFormMap.size() + 1];
		matrix[0] = "\t";
		for(int y = 0; y < words.length; y++) {
			matrix[0] += words[y] + "\t";
		}
		for(int x = 1; x < matrix.length; x++) {
			matrix[x] = posList.get(x - 1) + "\t";
		}
		for(int j = 0; j < words.length; j++) {
			float max = Float.MIN_VALUE;
			int index = 0;
			for(int i = 0; i < posFormMap.size(); i++) {
				matrix[i + 1] += probabilityMatrix[i][j] + "\t";
				if(probabilityMatrix[i][j] > max) {
					max = probabilityMatrix[i][j];
					index = i;
				}
				toReturn[j] = posList.get(index);
				if(max == Float.MIN_VALUE) {
					toReturn[j] = "_";
				}
			}
		}
		for(String s : matrix)
			System.out.println(s);
		System.out.println();
		for(String st : toReturn) {
			System.out.print(st + "\t");
		}		
	}
	
	
	
	
	
	
	
	
	
	
	
	private class Pair {
		public String pos;
		public Integer probability;
		public Pair(String pos, Integer probability) {
			this.pos = pos;
			this.probability = probability;
		}
	}

}




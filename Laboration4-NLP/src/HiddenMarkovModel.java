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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class HiddenMarkovModel {
	
	private HashMap<String, NPair<String, Float>> posBigrams, posFormMap, formPosMap;
	private HashMap<String, Float> emissionGraph, posPlusPosProbability;
	private ArrayList<String> posList;
	private HashMap<String, ArrayList<String>> posAssociatedWithPosList;
	public HiddenMarkovModel(HashMap<String, NPair<String, Float>> posBigrams, HashMap<String, NPair<String, Float>> posFormMap, HashMap<String, NPair<String, Float>> formPosMap, HashMap<String, Float> emissionGraph, HashMap<String, Float> posPlusPosProbability, HashMap<String, ArrayList<String>> posAssociatedWithPosList) {
		this.posBigrams = posBigrams;
		this.posFormMap = posFormMap;
		this.formPosMap = formPosMap;
		this.emissionGraph = emissionGraph;
		this.posPlusPosProbability = posPlusPosProbability;
		this.posAssociatedWithPosList = posAssociatedWithPosList;
		Iterator<Map.Entry<String, NPair<String, Float>>> it = posFormMap.entrySet().iterator();
		posList = new ArrayList<String>();
		while (it.hasNext()) {
			Map.Entry<String, NPair<String, Float>> pair = (Map.Entry<String, NPair<String, Float>>)it.next();
			posList.add(pair.getKey());
		}
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
					String ppos = "lol";//applyNoisyChannelMode(data[1]);
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
	
	public String[] viterbi(String word) {
		word = "START " + word.trim();
		String[] words = word.split("\\s+");
		float[][] probabilityMatrix = new float[posFormMap.size()][words.length];
		//Initialization
		float previousMax = 1.0f;
		String previous = "";
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
				previous = "START";
			}
		}
		for(int i = 1; i < words.length; i++) {
			if(previousMax == Float.MIN_VALUE) {
				previousMax = 1;
			}
			for(int j = 0; j < posFormMap.size(); j++) {
//				Float transition = posPlusPosProbability.get(previous + " " + posList.get(j));
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
					previous = posList.get(k);
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
//		for(String s : matrix)
//			System.out.println(s);
//		System.out.println();
//		for(String st : toReturn) {
//			System.out.print(st + "\t");
//		}		
//		System.out.println();
		return toReturn;
	}
	
	public ArrayList<NPair<String, Float>> tag(ArrayList<String> input) {
		return tag(input, 0, "START").asList();
	}
	 
	private NPair<String, Float> tag(ArrayList<String> input, int depth, String previous) {
		float bestRes = -1;
		NPair<String, Float> bestState = null;
		
		for(String posConsidered : posList) {
			Float wordGivenPos = emissionGraph.get(input.get(depth) + " " + posConsidered);
			wordGivenPos = wordGivenPos != null ? wordGivenPos : 0f;
			if(!formPosMap.containsKey(input.get(depth))) {
				wordGivenPos = 0f;
			}
			Float posGivenPos = posPlusPosProbability.get(previous + " " + posConsidered);
			posGivenPos = posGivenPos != null ? posGivenPos : 0f;
			if(depth < input.size() - 1) {
				NPair<String, Float> nextState = tag(input, depth + 1, posConsidered);
				if(nextState.v * wordGivenPos * posGivenPos > bestRes) {
					bestRes = nextState.v * wordGivenPos * posGivenPos;
					bestState = new NPair<String, Float>(posConsidered, bestRes, nextState);
				}
			} else {
				if(wordGivenPos * posGivenPos > bestRes) {
					bestRes = wordGivenPos * posGivenPos;
					bestState = new NPair<String, Float>(posConsidered, bestRes);
				}
			}
		}
		return bestState;
	}
	
	public List<String> viterbiTag(List<String> input) {
		State[][] states = new State[2][posList.size()];
		states[0][posList.indexOf("START")] = new State(1f, "START"); //This might cause an error
		
		for(int i = 1; i < input.size(); i++) {
			states[i % 2] = new State[posList.size()];
			for(int previousTag = 0; previousTag < posList.size(); previousTag++) {
				if(states[(i + 1) % 2][previousTag] != null) {
					for (String currentTag : posAssociatedWithPosList.get(posList.get(previousTag))) {
						Float wordGivenPos = emissionGraph.get(input.get(i) + " " + currentTag);
						wordGivenPos = wordGivenPos != null ? wordGivenPos : 0f;	//WE MIGHT HAVE TO REMOVE THIS
						if(!formPosMap.containsKey(input.get(i))) {
							wordGivenPos = 0f;
						}
						Float posGivenPos = posPlusPosProbability.get(posList.get(previousTag) + " " + currentTag); //Might be in reversed order, but probably not
//						posGivenPos = posGivenPos != null ? posGivenPos : 0f;
						int newIndex = posList.indexOf(currentTag);
						
						if(states[i % 2][newIndex] == null && posGivenPos * wordGivenPos > 0) {
							states[i % 2][newIndex] = new State(states[(i + 1) % 2][previousTag].probability * posGivenPos * wordGivenPos, states[(i + 1) % 2][previousTag], currentTag);
						} else if(states[i % 2][newIndex] != null) {
							if(states[(i + 1) % 2][previousTag].probability * posGivenPos * wordGivenPos > states[i % 2][newIndex].probability) {
								states[i % 2][newIndex] = new State(states[(i + 1) % 2][previousTag].probability * posGivenPos * wordGivenPos, states[(i + 1) % 2][previousTag],currentTag);
							}
						}
					}
				}
			}
			boolean allCurrentNull = true;
			for(int k = 0; k < posList.size(); k++) {
				if(states[i % 2][k] != null) {
					allCurrentNull = false;
					break;
				}
			}
			if(allCurrentNull) {
				State bestState = null;
				float bestStat = -1;
				
				for(int k = 0; k < posList.size(); k++) {
					if(states[(i + 1) % 2][k] != null) {
						if(states[(i + 1) % 2][k].probability > bestStat) {
							bestState = states[(i + 1) % 2][k];
							bestStat = bestState.probability;
						} else if(states[(i + 1) % 2][k].probability == bestStat) {
							//Do something
							System.out.println("probability equal");
						}
						break;
					}
				}
				for(int k = 0; k < posList.size(); k++) {
					states[i % 2][k] = new State(1, bestState, posList.get(k)); 
				}
			}
		}
		float bestResult = -1;
		State bestState = null;
		for (int i = 0; i < posList.size(); i++) {
			if (states[(input.size() - 1) % 2][i] != null) {
				if (states[(input.size() - 1) % 2][i].probability > bestResult) {
					bestState = states[(input.size() - 1) % 2][i];
					bestResult = bestState.probability;
				}
			}
		}
		
		return bestState.asListReversed();
	}
	
	
	class State {
		float probability;
		State previous = null;
		String t;
		public State(float probability, State previous, String t) {
			this(probability, t);
			this.previous = previous;
		}
		public State(float probability, String t) {
			this.t = t;
			this.probability = probability;
		}
		public List<String> asListReversed() {
			LinkedList<String> tempRes = new LinkedList<String>();
			this.addToList(tempRes);
			Collections.reverse(tempRes);
			return tempRes;
		}
		private void addToList(List<String> tags) {
			tags.add(this.t);
			if (previous != null)
				previous.addToList(tags);
		}
	}
	
	
	
	public String viterbiEvaluateFile(String fileToTag) {
		String fileName = "files/viterbiDevelopment-pos.txt";
		DataInputStream in = null;
		BufferedWriter out = null;
		try {
			FileWriter fOutStream = new FileWriter(fileName);
			out = new BufferedWriter(fOutStream);
			
			FileInputStream fstream = new FileInputStream(fileToTag);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			String sentence = "";
			ArrayList<String> words = new ArrayList<String>();
			while((line = br.readLine()) != null) {
				String[] data = line.split("\\s+");
				if(data.length > 1) {
					String temp = "";
					for(int i = 0; i < data.length - 1; i++) {
						temp += data[i] + '\t';
					}
					words.add(temp);
					sentence += data[1] + " "; 		
				} else {
					sentence = "START " + sentence;
					List<String> toTag = Arrays.asList(sentence.split(" "));
					List<String> poses = viterbiTag(toTag);
					String toWrite = "";
					for(int i = 0; i < poses.size() - 1; i++) {
						toWrite += words.get(i) + poses.get(i + 1) + '\n';
					}
					out.write(toWrite);
					words.clear();
					sentence = "";
				}
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
	
	
	
	public String viterbiTestSet() {
		String fileName = "files/viterbiTest-pos.txt";
		DataInputStream in = null;
		BufferedWriter out = null;
		try {
			FileWriter fOutStream = new FileWriter(fileName);
			out = new BufferedWriter(fOutStream);
			
			FileInputStream fstream = new FileInputStream("files/CoNLL2009-ST-test-words.txt");
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			String sentence = "";
			ArrayList<String> words = new ArrayList<String>();
			while((line = br.readLine()) != null) {
				String[] data = line.split("\\s+");
				if(data.length > 1) {
					words.add(line + '\t');
					sentence += data[1] + " "; 		
				} else {
					String[] poses = viterbi(sentence);
					String toWrite = "";
					for(int i = 0; i < poses.length - 1; i++) {
						toWrite += words.get(i) + poses[i + 1] + '\n';
					}
					out.write(toWrite);
					words.clear();
					sentence = "";
				}
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




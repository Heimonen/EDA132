package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import javax.crypto.spec.PSource;

public class ViterbiAlgo {
	private String[] sentence;
	private String[] POSes;
	private HashMap<BiGram, Double> posposProb;
	private HashMap<BiGram, Double> formposProb;

	public Node[][] matrix;

	private static class Node {
		public int previous;
		public double value;
		public Node(int previous, double value){
			this.previous = previous;
			this.value = value;
		}
	}

	public ViterbiAlgo(String[] sentence, String[] POSes, HashMap<BiGram, Double> posposProb, HashMap<BiGram, Double> formposProb) {
		this.sentence = sentence;
		this.POSes = POSes;
		this.posposProb = posposProb;
		this.formposProb = formposProb;

		matrix = new Node[sentence.length][POSes.length];
		matrix[0][0] = new Node(-1, 1.0);

	}

	public ArrayList<String> run() {
		for(int i = 1; i < matrix.length; ++i) {
			for(int j = 0; j < matrix[0].length; ++j) {

				double best = Double.NEGATIVE_INFINITY;
				Node bestNode = null; 
				for( int qq = 0; qq < matrix[0].length; ++qq) {
					if( matrix[i-1][qq] != null){

						BiGram pospos = new BiGram(POSes[qq], POSes[j]);
						Double prob1 = posposProb.get(pospos);
						if(prob1 == null) {
							prob1 = 0.00001;
						} else {
							prob1 *= 1000;
						}

						double prob =  prob1 * matrix[i-1][qq].value;
						if( best < prob ){
							best = prob;
							bestNode = new Node(qq,prob);
						}
					}
				}
				if( best > 0.0) {
					BiGram formpos = new BiGram(POSes[j], sentence[i]);
					Double prob2 = formposProb.get(formpos);
					if( prob2 == null) {
						prob2 =0.00001;
					}else {
						prob2 *= 1000;
					}
					if(bestNode.value * prob2 > 0){
						matrix[i][j] = bestNode;
						bestNode.value *= prob2;
					}
				}
			}
		}
		Stack<Node> path = new Stack<ViterbiAlgo.Node>();
		int besti = -1;
		double best = Double.NEGATIVE_INFINITY; 
		for( int i = 0; i < matrix[0].length; ++i) {
			if( matrix[matrix.length-1][i] == null){
				continue;
			}
			double t =matrix[matrix.length-1][i].value; 
			if(t > best) {
				best =t;
				besti = i;
			}
		}
		path.push(new Node(besti,0.0));
		Node current = matrix[matrix.length-1][besti];
		int asdasdi = matrix.length-1;
		while(current != null && asdasdi > 0 && current.previous > -1) {
			path.push(current);
			current = matrix[--asdasdi][current.previous];
		}
		ArrayList<String> toReturn = new ArrayList<String>();
		for(Node n : path) {
			toReturn.add(POSes[n.previous]);
			//			System.out.println(String.format("%d - %s", n.j, POSes[n.j]));
		}
		Collections.reverse(toReturn);
		return toReturn;
	}

	public void predictPosOnDevelopmentSet(String contentPath, HashMap<BiGram, Double> posposProb, HashMap<BiGram, Double> formposProb) {
		try {
			String[] poses = new String[ConfusionColumn.approvedPos.size()+1];
			int asdasdasdi = 0;
			poses[asdasdasdi++] = "BOS";
			for(String s: ConfusionColumn.approvedPos) {
				poses[asdasdasdi++] = s;
			}
			BufferedWriter out = new BufferedWriter(new FileWriter("output/viterbi_result.txt"));
			BufferedReader in = new BufferedReader(new FileReader(contentPath));
			String line;
			ArrayList<String> sentenceData = new ArrayList<String>();
			ArrayList<String> sentence = new ArrayList<String>();
			long lineCount = 0;
			while( (line= in.readLine()) != null) {
				if(++lineCount % 500 == 0) {
					System.out.println(lineCount);
				}
				try {
					if( line.equals("")){

						String[] theSentence = new String[sentence.size() + 1];
						int i = 0;
						theSentence[i++] = "BOS";
						for(String s: sentence) {
							theSentence[i++] = s;
						}
						ViterbiAlgo viterbi = new ViterbiAlgo(theSentence, poses, posposProb, formposProb);
						ArrayList<String> ppos = viterbi.run();
						for(int j = 0; j < sentenceData.size(); ++j) {
							String[] lineSplit = sentenceData.get(j).split("\t");
							String outString = String.format("%s\t%s\t%s\t%s\t%s\t%s\n", lineSplit[0], lineSplit[1], lineSplit[2], lineSplit[3], lineSplit[4], ppos.get(j+1));
							out.write(outString);
						}
						out.write("\n");
						out.flush();
						sentence.clear();
						sentenceData.clear();
					} else {
						String[] lineSplit = line.split("\t");
						sentence.add(lineSplit[1]);
						sentenceData.add(line);
					}
				} catch( ArrayIndexOutOfBoundsException e) {
					continue;
				}					

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

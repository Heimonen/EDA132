package main;

import java.util.ArrayList;

public class ConfusionColumn {
	public static final String[] approvedPOS = {"HYPH", "JJ", "RB", "DT", "TO", "RP", "RBR", "RBS", "LS", "JJS", "FW", "JJR",
	                                            "NN", "NNPS", "PRF", "VBN", "VB", "PDT", "VBP", "WP$", "PRP", "SYM", "MD",
	                                            "WDT", "VBZ", "WP", "IN", "NIL", "EX", "VBG", "POS", "VBD", "UH", "PRP$",
	                                            "NNS", "CC", "CD", "NNP", "WRB", "#", "$", ".", ",", ":", "(", ")", "\"", 
	                                            "'", "‘", "’", "“", "”"};
	public static ArrayList<String> approvedAL;
	
	static{
		approvedAL = new ArrayList<String>();
		for( String s: approvedPOS) {
			approvedAL.add(s);
		}
	}
	
	
	public String key;
	public ArrayList<Integer> count;
	public ConfusionColumn(String key) {
		this.key = key;
		count = new ArrayList<>();
		for(int i = 0; i < approvedPOS.length; ++i) {
			count.add(0);
		}
	}
}

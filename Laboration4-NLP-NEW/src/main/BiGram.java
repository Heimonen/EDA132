package main;

public class BiGram {
	public String first;
	public String second;
	
	public BiGram(String f, String s){
		first = f;
		second = s;
	}
	
	
	public int hashCode() {
		return String.format("%s$$$%s", first, second).hashCode();
	}
	public boolean equals(Object o) {
		if(! (o instanceof BiGram)){
			return false;
		}
		BiGram b = (BiGram)o; 
		return this.first.equals(b.first) && this.second.equals(b.second);
	}
}

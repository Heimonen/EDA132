
public class Main {
	
	public static void main(String[] args) {
		Board theBoard = new Board();
		Algorithm.alphaBetaSearch(theBoard, Board.BLACK);
	}

}

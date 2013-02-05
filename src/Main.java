
public class Main {
	
	public static void main(String[] args) {
		Board board = new Board();
		Board.BoardVector pos = new Board.BoardVector();
		pos.x = 3;
		pos.y = 2;
		System.out.println(board.makeMove(pos, Board.WHITE));
		pos.x = 2;
		pos.y = 4;
		System.out.println(board.makeMove(pos, Board.BLACK));
		board.showBoard();
	}

}

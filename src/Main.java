
public class Main {
	
	public static void main(String[] args) {
		Board board = new Board();
		board.showBoard();
		Board.BoardVector pos = new Board.BoardVector();
		pos.x = 3;
		pos.y = 2;
		System.out.println(board.makeMove(pos, Board.WHITE));
		board.showBoard();
		pos.x = 2;
		pos.y = 4;
		System.out.println(board.makeMove(pos, Board.BLACK));
		board.showBoard();
		pos.x = 4;
		pos.y = 2;
		System.out.println(board.makeMove(pos, Board.BLACK));
		board.showBoard();
//		pos.x = 1;
//		pos.y = 3;
//		System.out.println(board.makeMove(pos, Board.BLACK));
//		board.showBoard();
	}

}

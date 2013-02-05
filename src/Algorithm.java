

public class Algorithm {
	public int doStuff(final Board currentBoard,int currentColor ) {
		Board.BoardVector bv = new Board.BoardVector();
		int min = Integer.MAX_VALUE;
		if(getNextMove(currentBoard, currentColor, bv) == null) {
			return currentBoard.countPoints();
		}
		int nextColor = currentColor ^ 0x2;
		do{
			Board nextBoard = new Board(currentBoard);
			nextBoard.makeMove(bv,currentColor);
			min = Math.min(min, doStuff(nextBoard, nextColor));
		} while(getNextMove(currentBoard, currentColor, bv) != null);
		return min;
		
	}
	private Board.BoardVector getNextMove(Board currentBoard, int currentColor, Board.BoardVector bv){
		while( bv.x < 8){
			while(bv.y < 8){
				bv.y++;
				if(currentBoard.isLegalMove(bv, currentColor) != -1){
					return bv;
				}
			}
			bv.x++;
			bv.y = -1;
		}
		return null;
	}
}

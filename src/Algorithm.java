

public class Algorithm {
	public void doStuff(Board currentBoard,int currentColor ) {
		Board.BoardVector bv = new Board.BoardVector();
		
		
	}
	private Board.BoardVector getNextMove(Board currentBoard, int currentColor, Board.BoardVector bv){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				bv.x = i;
				bv.y = j;
				if(currentBoard.isLegalMove(bv, currentColor)){
					return bv;
				}
			}
		}
		return null;
	}
}

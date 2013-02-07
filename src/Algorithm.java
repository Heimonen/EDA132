import java.util.List;

public class Algorithm {
	private static long startTime;
	private static int numMovesTried;
	
	
	public static Board.BoardVector alphaBetaSearch(Board currentBoard, int color){
		startTime = System.currentTimeMillis();
		Board.BoardVector bestMove= initMaxValue(currentBoard, Integer.MIN_VALUE,Integer.MAX_VALUE, color);
		
		System.out.println(bestMove);
		return bestMove;
	}
	private static Board.BoardVector initMaxValue(Board currentBoard, int alpha, int beta, int color) {
		Board.BoardVector action = new Board.BoardVector();
		if(terminalTest(currentBoard,action,color)){
			return null;
		}
		int v = Integer.MIN_VALUE;
		Board.BoardVector bestMove = new Board.BoardVector(action);
		do {
			int minTest = minValue(result(currentBoard,action,color), alpha, beta, color ^ 0x2);
			if(minTest > v){
				v = minTest;
				bestMove.init(action);
			}
			alpha = Math.max(alpha, v);			
		}while ( getNextMove(currentBoard, color, action) != null);
		return bestMove;
	}
	private static int maxValue(Board currentBoard, int alpha, int beta, int color) {
		Board.BoardVector action = new Board.BoardVector();
		if(terminalTest(currentBoard,action,color)){
			return currentBoard.countPoints(color);
		}
		int v = Integer.MIN_VALUE;
		do {
			v = Math.max(v, minValue(result(currentBoard,action,color), alpha, beta, color ^ 0x2));
			if(v>=beta){
				return v;
			}
			alpha = Math.max(alpha, v);			
		}while ( getNextMove(currentBoard, color, action) != null);
		return v;
	}
	private static int minValue(Board currentBoard, int alpha, int beta, int color) {
		Board.BoardVector action = new Board.BoardVector();
		if(terminalTest(currentBoard,action,color)){
			return currentBoard.countPoints(color);
		}
		
		int v = Integer.MAX_VALUE;
		do {
			v = Math.min(v, maxValue(result(currentBoard,action,color), alpha, beta, color ^ 0x2));
			if(v<=alpha){
				return v;
			}
			beta = Math.min(beta, v);
			
		} while ( getNextMove(currentBoard, color, action) != null);
		return v;
	}
	
	private static boolean terminalTest(Board currentBoard, Board.BoardVector action, int color){
		return getNextMove(currentBoard, color, action) == null;
	}
	private static Board result(Board currentBoard, Board.BoardVector bv, int color) {
		Board newBoard = new Board(currentBoard);
		newBoard.makeMove(bv, color);
		return newBoard;
	}
	private static List<Board.BoardVector> actions(Board currentBoard) {
		// TODO Auto-generated method stub
		return null;
	}
	public static int doStuff(final Board currentBoard,int currentColor ) {
		Board.BoardVector bv = new Board.BoardVector();
		int min = Integer.MAX_VALUE;
		if(getNextMove(currentBoard, currentColor, bv) == null) {
			return currentBoard.countPoints(Board.BLACK);//HARD CODED TO BLACK
		}
		int nextColor = currentColor ^ 0x2;
		do{
			Board nextBoard = new Board(currentBoard);
			nextBoard.makeMove(bv,currentColor);
			min = Math.min(min, doStuff(nextBoard, nextColor));
		} while(getNextMove(currentBoard, currentColor, bv) != null);
		return min;
		
	}
	private static Board.BoardVector getNextMove(Board currentBoard, int currentColor, Board.BoardVector bv){
		if(numMovesTried % 1000000 == 0){
			long secsConsumed = (System.currentTimeMillis() - startTime) / 1000; 
			System.out.println(String.format("consumed time: %d seconds; numMovedTried: %d", secsConsumed,numMovesTried));
			if( secsConsumed > Main.runTimeSeconds) {
				return null;
			}
		}
		while( bv.x < 8){
			while(bv.y < 7){
				bv.y++;
				if(currentBoard.isLegalMove(bv, currentColor) != -1){
					numMovesTried++;
					return bv;
				}
			}
			bv.x++;
			bv.y = -1;
		}
		return null;
	}
}

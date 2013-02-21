import java.util.List;

public class Algorithm {
	private static long startTime;
	public static int numMovesTried;
	
	
	public static Board.BoardVector alphaBetaSearch(Board currentBoard){
		startTime = System.currentTimeMillis();
		Board.BoardVector bestMove= initMaxValue(currentBoard, Integer.MIN_VALUE,Integer.MAX_VALUE);
		
		System.out.println(bestMove);
		return bestMove;
	}
	private static Board.BoardVector initMaxValue(Board currentBoard, int alpha, int beta) {
		Board.BoardVector action = new Board.BoardVector();
		if(terminalTest(currentBoard,action)){
			return null;
		}
		int v = Integer.MIN_VALUE;
		Board.BoardVector bestMove = new Board.BoardVector(action);
		do {
			int minTest = minValue(result(currentBoard,action), alpha, beta);
			if(minTest > v){
				v = minTest;
				bestMove.init(action);
			}
			alpha = Math.max(alpha, v);			
		}while ( getNextMove(currentBoard, action) != null);
		return bestMove;
	}
	private static int maxValue(Board currentBoard, int alpha, int beta) {
		Board.BoardVector action = new Board.BoardVector();
		if(terminalTest(currentBoard,action)){
			return currentBoard.countPoints();
		}
		int v = Integer.MIN_VALUE;
		do {
			v = Math.max(v, minValue(result(currentBoard,action), alpha, beta));
			if(v>=beta){
				return v;
			}
			alpha = Math.max(alpha, v);			
		}while ( getNextMove(currentBoard, action) != null);
		return v;
	}
	private static int minValue(Board currentBoard, int alpha, int beta) {
		Board.BoardVector action = new Board.BoardVector();
		if(terminalTest(currentBoard,action)){
			return currentBoard.countPoints();
		}
		
		int v = Integer.MAX_VALUE;
		do {
			v = Math.min(v, maxValue(result(currentBoard,action), alpha, beta));
			if(v<=alpha){
				return v;
			}
			beta = Math.min(beta, v);
			
		} while ( getNextMove(currentBoard, action) != null);
		return v;
	}
	
	private static boolean terminalTest(Board currentBoard, Board.BoardVector action){
		return getNextMove(currentBoard, action) == null;
	}
	private static Board result(Board currentBoard, Board.BoardVector bv) {
		Board newBoard = new Board(currentBoard);
		newBoard.makeMove(bv);
		return newBoard;
	}
	public static int doStuff(final Board currentBoard) {
		Board.BoardVector bv = new Board.BoardVector();
		int min = Integer.MAX_VALUE;
		if(getNextMove(currentBoard, bv) == null) {
			return currentBoard.countPoints();
		}
		do{
			Board nextBoard = new Board(currentBoard);
			nextBoard.makeMove(bv);
			min = Math.min(min, doStuff(nextBoard));
		} while(getNextMove(currentBoard, bv) != null);
		return min;
		
	}
	private static Board.BoardVector getNextMove(Board currentBoard, Board.BoardVector bv){
		if(numMovesTried % 1000000 == 0){
			long secsConsumed = (System.currentTimeMillis() - startTime) / 1000; 
			System.out.println(String.format("consumed time: %d seconds; numMovedTried: %d", secsConsumed,numMovesTried));
			if( secsConsumed > Main.runTimePerMoveInSeconds) {
				return null;
			}
		}
		while( bv.x < 8){
			while(bv.y < 7){
				bv.y++;
				if(currentBoard.isLegalMove(bv) != -1){
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

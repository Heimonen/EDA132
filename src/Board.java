
public class Board {
	public int[][] theBoard; // 0 == empty, 1= black, 3= white
	public Board(){
		theBoard = new int[8][8];
	}
	private boolean isLegalMove(BoardVector currentPos ,int myColor) {
		if( theBoard[0][0] != 0){
			return false;
		}
		boolean canIPut = false;
		BoardVector bv = new BoardVector();
		for(int i= 0; i < 8; i++){
			if( (canIPut = canIPut || checkdir(currentPos, Board.getDirection(i,bv), myColor))) {
				return true;
			}
		}
		return false;
	}
	private boolean checkdir(BoardVector currentPos, BoardVector dir ,int color){
		boolean isOppositeColorInthisDirection = false;
		boolean isMyColorAfterOppositeColorInThisDirection = false;
		if(currentPos.add(dir) && Board.isOppositeColor(theBoard[currentPos.x][currentPos.y],color)){
			isOppositeColorInthisDirection = true;
		} else {
			return false;
		}
		while(currentPos.add(dir) && Board.isOppositeColor(theBoard[currentPos.x][currentPos.y],color)){}
		if(currentPos.isInBounds() && theBoard[currentPos.x][currentPos.y] == color){
			return true;
		}
		return false;
		
	}
	private static boolean isOppositeColor(int boardValue, int color){
		return  ((boardValue ^ 0x2))  == color;
	}
	public static BoardVector getDirection(int dir, BoardVector d){
		switch(dir){
		case 0:
			d.x = 0;
			d.y = -1;
			break;
		case 1:
			d.x = 1;
			d.y = -1;
			break;
		case 2:
			d.x = 1;
			d.y = 0;
			break;
		case 3:
			d.x = 1;
			d.y = 1;
			break;
		case 4:
			d.x = 0;
			d.y = 1;
			break;
		case 5:
			d.x = -1;
			d.y = 1;
			break;
		case 6:
			d.x = -1;
			d.y = 0;
			break;
		case 7:
			d.x = -1;
			d.y = -1;
			break;
		}
		return d;
	}
	static private class BoardVector {
		public int x;
		public int y; 
		public boolean add(BoardVector other){
			this.x += other.x;
			this.y += other.y;
			return isInBounds();
		}
		public boolean isInBounds() {
			return isInBounds(x) && isInBounds(y);
		}
		static boolean isInBounds(int c){
			return c < 8 && c >= 0;
		}
	}
}

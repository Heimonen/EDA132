
public class Board {
	public int[][] theBoard; // 0 == empty, 1= black, 3= white
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 3;
	public Board(){
		theBoard = new int[8][8];
		theBoard[4][3] = 3;
		theBoard[3][4] = 3;
		theBoard[3][3] = 1;
		theBoard[4][4] = 1;
	}
	public void showBoard() {
		for(int i = 0; i < theBoard.length; i++) {
			for(int j = 0; j < theBoard[i].length; j++){
				switch(theBoard[i][j]) {
				case EMPTY :
					System.out.print("-");
					break;
				case BLACK : 
					System.out.print("X");
					break;
				case WHITE :
					System.out.print("O");
					break;
				}
				if(j == theBoard.length - 1) {
					System.out.println("|");
				}
			}
		}
	}
	/**
	 * Returns the difference; white - black
	 * @return
	 */
	public int countPoints() {
		int whitePoints = 0;
		int blackPoints = 0;
		for(int i = 0; i < theBoard.length; i++) {
			for(int j = 0; j < theBoard[i].length; j++){
				switch(theBoard[i][j]) {
				case EMPTY :
					break;
				case BLACK :
					whitePoints++;
					break;
				case WHITE :
					blackPoints++;
					break;
				}
			}
		}
		return whitePoints - blackPoints;
	}
	public boolean makeMove(BoardVector desiredPos, int myColor) {
		if(isLegalMove(desiredPos, myColor)) {
			theBoard[desiredPos.x][desiredPos.y] = myColor;
			return true;
		}
		return false;
	}
	public boolean isLegalMove(BoardVector currentPos ,int myColor) {
		if( theBoard[currentPos.x][currentPos.y] != 0){
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
	private boolean checkdir(BoardVector inCurrentPos, BoardVector dir ,int color){
		BoardVector currentPos = new BoardVector(inCurrentPos);
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
	static public class BoardVector {
		public int x;
		public int y; 
		public BoardVector(BoardVector other) {
			this.x = other.x;
			this.y = other.y;
		}
		public BoardVector() {}
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

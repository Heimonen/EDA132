
public class Board {
	public int[][] theBoard; // 0 == empty, 1= black, 3= white
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 3;
	public Board(){
		theBoard = new int[8][8];
		theBoard[4][3] = WHITE;
		theBoard[3][4] = WHITE;
		theBoard[3][3] = BLACK;
		theBoard[4][4] = BLACK;
	}
	public void showBoard() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++){
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
					blackPoints++;
					break;
				case WHITE :
					whitePoints++;
					break;
				}
			}
		}
		return whitePoints - blackPoints;
	}
	public boolean makeMove(BoardVector desiredPos, int myColor) {
		int direction = isLegalMove(desiredPos, myColor);
		if(direction  != -1) {
			theBoard[desiredPos.x][desiredPos.y] = myColor;
			BoardVector bv = new BoardVector();
			for(int i = direction; i < 8; i++) {
				if( (checkdir(desiredPos, Board.getDirection(i,bv), myColor))) {
					flipInDirection(desiredPos, Board.getDirection(i,bv), myColor);
				}
			}
			return true;
		}
		return false;
	}
	private void flipInDirection(BoardVector startPos, BoardVector direction, int color) {
		System.out.println(direction.x + " " + direction.y);
		BoardVector currentPos = new BoardVector(startPos);
		while(currentPos.add(direction) && Board.isOppositeColor(theBoard[currentPos.x][currentPos.y],color)){
			theBoard[currentPos.x][currentPos.y] = theBoard[currentPos.x][currentPos.y] ^ 0x2;
		}
	}
	/**
	 * Returns the direction of a legal move, -1 otherwise
	 * @param currentPos
	 * @param myColor
	 * @return
	 */
	public int isLegalMove(BoardVector currentPos ,int myColor) {
		if( theBoard[currentPos.x][currentPos.y] != 0){
			return -1;
		}
		boolean canIPut = false;
		BoardVector bv = new BoardVector();
		for(int i= 0; i < 8; i++){
			if( (canIPut = canIPut || checkdir(currentPos, Board.getDirection(i,bv), myColor))) {
				return i;
			}
		}
		return -1;
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

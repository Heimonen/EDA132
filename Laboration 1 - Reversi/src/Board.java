
public class Board {
	public int whoesTurn = Board.BLACK; 
	public int[][] theBoard;
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 3;
	
	public static final int UP = 0;
	public static final int RIGHT_UP = 1;
	public static final int RIGHT = 2;
	public static final int RIGHT_DOWN = 3;
	public static final int DOWN = 4;
	public static final int LEFT_DOWN = 5;
	public static final int LEFT = 6;
	public static final int LEFT_UP = 7;
	
	public Board(Board other){
		theBoard = new int[8][8];
		for(int i = 0; i < 8; i++) {
			for( int j = 0; j < 8; j++){
				theBoard[i][j] = other.theBoard[i][j];
			}
		}
		this.whoesTurn = other.whoesTurn;
	}
	public Board(){
		theBoard = new int[8][8];
		theBoard[4][3] = BLACK;
		theBoard[3][4] = BLACK;
		theBoard[3][3] = WHITE;
		theBoard[4][4] = WHITE;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  abcdefgh\n");
		for(int i = 0; i < 8; i++) {
			sb.append(i+1);
			sb.append('|');
			for(int j = 0; j < 8; j++){
				switch(theBoard[j][i]) {
				case EMPTY :
					sb.append('-');
					break;
				case BLACK : 
					sb.append('B');
					break;
				case WHITE :
					sb.append('W');
					break;
				}
				if(j ==  7) {
					sb.append("|\n");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * Returns the difference; white - black
	 * @return
	 */
	public int countPoints() {
		int whitePoints = 0;
		int blackPoints = 0;
		int color = whoesTurn;
		int increment = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++){
				increment = i != 0 || j != 0 || i != 7 || j !=7 ? 1 : 5;
				switch(theBoard[i][j]) {
				case EMPTY :
					
					break;
				case BLACK :
					blackPoints += increment;
					break;
				case WHITE :
					whitePoints += increment;
					break;
				}
			}
		}
		switch (Main.aiColor) {
		case Board.BLACK :
			return blackPoints - whitePoints;
		case Board.WHITE :
			return whitePoints - blackPoints;
		default:
			return 0;
		}
	}
	
	public boolean makeMove(BoardVector action){
		if(makeMove(action,whoesTurn)){
			this.whoesTurn ^= 0x2;
			return true;
		}
		return false;
	}
	private boolean makeMove(BoardVector desiredPos, int myColor) {
		int direction = isLegalMove(desiredPos);
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
//		System.out.println(direction.x + " " + direction.y);
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
	public int isLegalMove(final BoardVector currentPos) {
		if(!(currentPos.x <= 7 && currentPos.x >= 0 && currentPos.y <= 7 && currentPos.y >= 0)){
			return -1;
		}
		if( theBoard[currentPos.x][currentPos.y] != 0){
			return -1;
		}
		boolean canIPut = false;
		BoardVector bv = new BoardVector();
		for(int i= 0; i < 8; i++){
			if( (canIPut = canIPut || checkdir(currentPos, Board.getDirection(i,bv), whoesTurn))) {
				return i;
			}
		}
		return -1;
	}
	private boolean checkdir(final BoardVector inCurrentPos, BoardVector dir ,int color){
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
		case UP:
			d.x = 0;
			d.y = -1;
			break;
		case RIGHT_UP:
			d.x = 1;
			d.y = -1;
			break;
		case RIGHT:
			d.x = 1;
			d.y = 0;
			break;
		case RIGHT_DOWN:
			d.x = 1;
			d.y = 1;
			break;
		case DOWN:
			d.x = 0;
			d.y = 1;
			break;
		case LEFT_DOWN:
			d.x = -1;
			d.y = 1;
			break;
		case LEFT:
			d.x = -1;
			d.y = 0;
			break;
		case LEFT_UP:
			d.x = -1;
			d.y = -1;
			break;
		}
		return d;
	}
	static public class BoardVector {
		public int x;
		public int y; 
		public BoardVector(BoardVector other){
			this.x = other.x;
			this.y = other.y;
		}
		
		public BoardVector(){
			this.y = -1;
		}
		
		public void init(BoardVector other){
			this.x=other.x;
			this.y = other.y;
		}
		
		public boolean add(BoardVector other){
			this.x += other.x;
			this.y += other.y;
			return isInBounds();
		}
		
		public boolean isInBounds(){
			return isInBounds(x) && isInBounds(y);
		}
		
		static boolean isInBounds(int c){
			return c < 8 && c >= 0;
		}
		
		public String toString(){
			return String.format("%c%d",'a' + x, 1 +y);
		}
	}
}

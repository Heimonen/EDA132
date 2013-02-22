import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
	public static long runTimePerMoveInSeconds = 10;
	public static int aiColor = Board.BLACK;
	
	public static void main(String[] args) throws IOException {
		Board theBoard = new Board();
		System.out.println(theBoard);
		String input = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while( (input = in.readLine()) != null && !input.equals("")){
			if(handleInput(input, theBoard)){
				System.out.println(theBoard);
				if(Algorithm.numMovesTried == 0){
					Main.aiColor = theBoard.whoesTurn;
				}
				Board.BoardVector aiMove =Algorithm.alphaBetaSearch(theBoard); 
				System.out.println(aiMove);
				theBoard.makeMove(aiMove);
			}
			System.out.println(theBoard);
		}
	}
	
	public static boolean handleInput(String input, Board currentBoard){
		if(input.startsWith("-set-")){
			int start = "-set-".length();
			String key = input.substring(start,input.indexOf('=', start));
			String value = input.substring(input.indexOf('=', start)+1);
			setSetting(key, value);
			return false;
		} else if(input.length() == 2){
			input = input.toLowerCase();
			Board.BoardVector move =new Board.BoardVector();
			move.x = input.charAt(0) - 'a';
			move.y = input.charAt(1) - '1';
			return (move.x >= 0 && move.x <= 7 && move.y >= 0 && move.y <= 7) ? currentBoard.makeMove(move):false;
		}
		return false;
	}
	public static void setSetting(String key,String value){
		try {
			if(key.equalsIgnoreCase("runTimePerMove")){
				runTimePerMoveInSeconds = Integer.parseInt(value);
			}
		} catch (NumberFormatException nfe){
			System.err.println(String.format("Main::setSetting: Unable to set \"%s\" to \"%s\" due to %s:%s", key,value,nfe.getClass().getCanonicalName(),nfe.getCause()));
		}
	}
}

public class Main {
	public static long runTime = 10000;
	public static int aiColor = Board.BLACK;
	
	public static void main(String[] args) {
		if(args.length > 0 && args[0] != null) {
			try {
				long userDefinedRunTime = Long.parseLong(args[0]);
				runTime = userDefinedRunTime;
			} catch (NumberFormatException e) {
				System.out.println("Enter a proper run time; eg 20000. Using default value 20000.");
			}
		}
		Board theBoard = new Board();
		Algorithm.alphaBetaSearch(theBoard, aiColor);
	}
}
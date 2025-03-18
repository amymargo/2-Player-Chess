// Toma Takamatsu, Amy Margolina

package chess;

import java.util.ArrayList;

public class Chess {

        enum Player { white, black }
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */

	public static Board chessBoard;

	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		ReturnPlay result = new ReturnPlay();
		move = move.trim();
		String[] parts = move.split(" ");

		// Checking if resign
		if (parts[0].equals("resign")){
			if (chessBoard.whiteTurn){
				result.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
			}
			else result.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
			result.piecesOnBoard = chessBoard.getBoardAsList();
			return result;
		}

		// Getting starting file/rank and ending file/rank
		int startFile = ((int)parts[0].charAt(0)) - 97;
		int startRank = parts[0].charAt(1) - '0' - 1;
		int endFile = ((int)parts[1].charAt(0)) - 97;
		int endRank = parts[1].charAt(1) - '0' - 1;

		// Checking for draw or pawn promotion piece request, special could be "draw?", "resign", "Q", etc.
		String special = null;
		if (parts.length == 3) special = parts[2];

		// Trying to make the move
		result.message = chessBoard.Move(startRank, startFile, endFile, endRank, special);

		// Getting updated board
		result.piecesOnBoard = chessBoard.getBoardAsList();

		return result;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		chessBoard = new Board();
	}
}
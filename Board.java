package chess;

import java.util.ArrayList;

public class Board extends ReturnPlay{
    public Piece[][] board; // Chess board, board[rank][file]
    public boolean whiteTurn = true;
    public int[] previousMove;
    public static Board boardInstance;

    public Board(){
        board = createNewBoard();
    }

    public Board(Piece[][] board){
        this.board = board;
    }

    public Piece[][] copyBoard(Piece[][] boardToCopy){
        Piece[][] copiedBoard = new Piece[8][8];
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                if (board[rank][file] != null){
                    Piece copyPiece = board[rank][file];
                    boolean playerColor = copyPiece.player;
                    if (copyPiece instanceof Pawn) copyPiece = new Pawn(playerColor, rank, file);
                    else if (copyPiece instanceof Rook) copyPiece = new Rook(playerColor, rank, file);
                    else if (copyPiece instanceof Bishop) copyPiece = new Bishop(playerColor, rank, file);
                    else if (copyPiece instanceof Knight) copyPiece = new Knight(playerColor, rank, file);
                    else if (copyPiece instanceof King) copyPiece = new King(playerColor, rank, file);
                    else if (copyPiece instanceof Queen) copyPiece = new Queen(playerColor, rank, file);
                    copiedBoard[rank][file] = board[rank][file];
                }
            }
        }

        return copiedBoard;
    }

    public Piece[][] createNewBoard(){
        Piece[][] newBoard = new Piece[8][8];
        whiteTurn = true;
        previousMove = new int[4];
        
        // Placing white pieces
        newBoard[0][0] = new Rook(true, 0, 0);
        newBoard[0][1] = new Knight(true, 0, 1);
        newBoard[0][2] = new Bishop(true, 0, 2);
        newBoard[0][3] = new Queen(true, 0, 3);
        newBoard[0][4] = new King(true, 0, 4);
        newBoard[0][5] = new Bishop(true, 0, 5);
        newBoard[0][6] = new Knight(true, 0, 6);
        newBoard[0][7] = new Rook(true, 0, 7);
        for (int i = 0; i < 8; i++){
            newBoard[1][i] = new Pawn(true, 1, i);
        }

        // Placing black pieces
        newBoard[7][0] = new Rook(false, 7, 0);
        newBoard[7][1] = new Knight(false, 7, 1);
        newBoard[7][2] = new Bishop(false, 7, 2);
        newBoard[7][3] = new Queen(false, 7, 3);
        newBoard[7][4] = new King(false, 7, 4);
        newBoard[7][5] = new Bishop(false, 7, 5);
        newBoard[7][6] = new Knight(false, 7, 6);
        newBoard[7][7] = new Rook(false, 7, 7);
        for (int i = 0; i < 8; i++){
            newBoard[6][i] = new Pawn(false, 6, i);
        }

        return newBoard;
    }

    public void printBoard(){
        for (int rank = 0; rank < 0; rank++){
            for (int file = 0; file < 8; file++){
                if (board[rank][file] == null){
                    if ((rank + file) % 2 == 0) System.out.println("    "); // Printing light square
                    else System.out.println("## "); // Printing dark square
                }
                else System.out.println(board[rank][file] + " "); // Printing pieces
            }
            System.out.println((rank + 1) + ""); // Printing rank on right side
        }
        System.out.println("a  b  c  d  e  f  g  h"); // Printing file on the bottom
    }

    // Returns 0 if neither king is in check, returns 1 if white is in check, returns 2 if black is in check, returns 3 if both are in check
    public int isKingInCheck(){
        int check = 0;
        boolean blackChecked = false;
        boolean whiteChecked = false;

        // Finding the two kings
        Piece whiteKing = null;
        Piece blackKing = null;
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece piece = board[rank][file];
                if (piece instanceof King){
                    if (piece.player) whiteKing = piece;
                    else blackKing = piece;
                }
            }
        }

        // Checking if any of the pieces can capture the king
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                Piece piece = board[rank][file];
                if (piece == null || piece instanceof King) continue;
                else if (piece.player && !blackChecked){
                    blackChecked = piece.makeMove(blackKing.rank, blackKing.file, this);
                }
                else if (!piece.player && !whiteChecked){
                    whiteChecked = piece.makeMove(whiteKing.rank, whiteKing.file, this);
                }
            }
        }

        // Updating if there was a check or not
        if (whiteChecked) check += 1;
        if (blackChecked) check += 2;
        return check;
    }

    public boolean isCheckmate(boolean whiteTurn) {
        // Step 1: Determine which player's pieces to check
        boolean kingPlayer = !whiteTurn; // If it's White's turn, check Black's pieces; if Black's turn, check White's pieces.
    
        // Step 2: Locate the king of the player in check
        King king = null;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board[rank][file];
                if (piece instanceof King && piece.player == kingPlayer) {
                    king = (King) piece;
                    break;
                }
            }
        }
    
        // If the king is missing (which should never happen in a valid game), return false
        if (king == null) return false;
    
        // Step 3: Check if the king has any valid moves to escape
        for (int r = king.rank - 1; r <= king.rank + 1; r++) {
            for (int f = king.file - 1; f <= king.file + 1; f++) {
                if (r >= 0 && r < 8 && f >= 0 && f < 8) {
                    if (king.validMove(r, f, this)) {
                        return false; // King can escape, so it's not checkmate
                    }
                }
            }
        }
    
        // Step 4: Check if any of the player's pieces can make a valid move to block or capture the attacking piece
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board[rank][file];
    
                // We are only checking the pieces of the player who is currently in check
                if (piece != null && piece.player == kingPlayer) {
                    for (int targetRank = 0; targetRank < 8; targetRank++) {
                        for (int targetFile = 0; targetFile < 8; targetFile++) {
                            // 🛑 Skip moves that try to move a piece onto the king's position
                            if (targetRank == king.rank && targetFile == king.file) continue;
             
                            if (piece.makeMove(targetRank, targetFile, this)) {
                                // Then, check if moving there would put the king in check
                                if (piece.validMove(targetRank, targetFile, this)) {
                                    return false; // A legal move exists, so it's not checkmate
                                }
                            }               
                        }
                    }
                }
            }
        }
    
        // If no legal moves exist, it's checkmate
        return true;
    } 

    public ArrayList<ReturnPiece> getBoardAsList(){
        ArrayList<ReturnPiece> pieces = new ArrayList<>();
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                if (board[rank][file] != null) pieces.add(board[rank][file]);
            }
        }

        return pieces;
    }

    public ReturnPlay.Message Move(int startRank, int startFile, int endFile, int endRank, String special){
        Piece targetPiece = board[startRank][startFile];
        
        // Checking for null and if correct player is making the move
        if(targetPiece == null || (!whiteTurn && targetPiece.player) || (whiteTurn && !targetPiece.player)){
            System.out.println("Move: " + startRank + " " + startFile + " " + endRank + " " + endFile);
            System.out.println("Correct player not making move s");
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }

        // Checking if move will cause a check
        if (!targetPiece.validMove(endRank, endFile, this)){
            System.out.println("Move: " + startRank + " " + startFile + " " + endRank + " " + endFile);
            System.out.println("Invalid move");
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }

        // Checking if move is valid
        if (!targetPiece.makeMove(endRank, endFile, this)){
            System.out.println("Move: " + startRank + " " + startFile + " " + endRank + " " + endFile);
            System.out.println("Couldn't make move ss");
            return ReturnPlay.Message.ILLEGAL_MOVE;
        }

        // Updating positions
        targetPiece.updatePosition(endRank, endFile);
        board[startRank][startFile] = null;
        board[endRank][endFile] = targetPiece;

        // Check for check or checkmate
        int checkStatus = isKingInCheck();
        if ((checkStatus == 1 && !whiteTurn) || (checkStatus == 2 && whiteTurn)) {
            if (isCheckmate(whiteTurn)) {
                return whiteTurn ? ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
            }
            whiteTurn = !whiteTurn;
            return ReturnPlay.Message.CHECK;
        }        
        
        // Checking for promotion and promoting
        if ((targetPiece.pieceType == ReturnPiece.PieceType.WP && endRank == 7)){
            Piece piece = null;
            if (special == null || special.equals("Q") || special.equals("draw?")){
                piece = new Queen(true, endRank, endFile);
            }
            else if (special.equals("B")){
                piece = new Bishop(true, endRank, endFile);
            }
            else if (special.equals("R")){
                piece = new Rook(true, endRank, endFile);
            }
            else if (special.equals("N")){
                piece = new Knight(true, endRank, endFile);
            }
            board[endRank][endFile] = piece;
        }
        else if (targetPiece.pieceType == ReturnPiece.PieceType.BP && endRank == 0){
            Piece piece = null;
            if (special == null || special.equals("Q") || special.equals("draw?")){
                piece = new Queen(false, endRank, endFile);
            }
            else if (special.equals("B")){
                piece = new Bishop(false, endRank, endFile);
            }
            else if (special.equals("R")){
                piece = new Rook(false, endRank, endFile);
            }
            else if (special.equals("N")){
                piece = new Knight(false, endRank, endFile);
            }
            board[endRank][endFile] = piece;
        }
       // Logging previous move
       previousMove[0] = startRank;
       previousMove[1] = startFile;
       previousMove[2] = endRank;
       previousMove[3] = endFile;

        // Checking for checks
        int check = isKingInCheck();
        if (check == 1 || check == 2) return ReturnPlay.Message.CHECK;

        // Check for draw
        if (special != null && special.equals("draw?")) return ReturnPlay.Message.DRAW;
        
        // Switch turns
        if (whiteTurn) whiteTurn = false;
        else whiteTurn = true;

        return null;
    }
}

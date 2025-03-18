package chess;

import java.util.HashMap;

public abstract class Piece extends ReturnPiece{
    boolean player; // true = white, false = black
    int rank;
    int file; // 0 = a, 7 = h

    public static HashMap<Integer, PieceFile> intToFile = new HashMap<>();
    static{
        intToFile.put(0, PieceFile.a);
        intToFile.put(1, PieceFile.b);
        intToFile.put(2, PieceFile.c);
        intToFile.put(3, PieceFile.d);
        intToFile.put(4, PieceFile.e);
        intToFile.put(5, PieceFile.f);
        intToFile.put(6, PieceFile.g);
        intToFile.put(7, PieceFile.h);
    }

    public Piece(boolean player, int rank, int file){
        this.player = player;
        this.rank = rank;
        this.file = file;
        this.pieceFile = intToFile.get(file);
        this.pieceRank = rank + 1;
    }

    public abstract boolean makeMove(int targetRank, int targetFile, Board board);
    public void updatePosition(int rank, int file){
        this.rank = rank;
        this.file = file;
        this.pieceFile = intToFile.get(file);
        this.pieceRank = rank + 1;
    }

    public boolean validMove(int targetRank, int targetFile, Board board){
        if (targetRank == rank && targetFile == file) return false; // Checking if move changes nothing
        if (targetRank > 7 || targetRank < 0 || targetFile > 7 || targetFile < 0) return false; // Checking if move is within the board
        if (board.board[targetRank][targetFile] instanceof King) return false; // If target is a king, return false

         // Special case: Ensure KING cannot move to a square occupied by its own piece
         if (this instanceof King) {
            Piece targetPiece = board.board[targetRank][targetFile];
            if (targetPiece != null && targetPiece.player == this.player) {
                return false;
            }
        }

        // Checking if move will cause a check on their own king

        // Making a copy of the piece
        Piece copyPiece = null;
        if (this instanceof Pawn) copyPiece = new Pawn(player, targetRank, targetFile);
        else if (this instanceof Rook) copyPiece = new Rook(player, targetRank, targetFile);
        else if (this instanceof Bishop) copyPiece = new Bishop(player, targetRank, targetFile);
        else if (this instanceof Knight) copyPiece = new Knight(player, targetRank, targetFile);
        else if (this instanceof King) copyPiece = new King(player, targetRank, targetFile);
        else if (this instanceof Queen) copyPiece = new Queen(player, targetRank, targetFile);
    
        // Making a copy of the board
        Piece[][] copiedBoard = board.copyBoard(board.board);
    
        // Move the piece on the copied board
        copiedBoard[rank][file] = null;
        copiedBoard[targetRank][targetFile] = copyPiece;
    
        Board copiedBoardObj = new Board(copiedBoard);
    
        // Check if the player's own king is in check after the move
        int check = copiedBoardObj.isKingInCheck();
        if (check == 3 || (check == 2 && !player) || (check == 1 && player)) {
            return false;
        }
    
        // // Special check for King moving into check
        // if (this instanceof King) {
        //     for (int r = 0; r < 8; r++) {
        //         for (int f = 0; f < 8; f++) {
        //             Piece attackingPiece = board.board[r][f];
        //             if (attackingPiece != null && attackingPiece.player != this.player) {
        //                 if (attackingPiece.makeMove(targetRank, targetFile, board)) {
        //                     return false;
        //                 }
        //             }
        //         }
        //     }
        // }
        // Special check for King moving into check
        if (this instanceof King) {
            for (int r = 0; r < 8; r++) {
                for (int f = 0; f < 8; f++) {
                    Piece attackingPiece = board.board[r][f];
                    if (attackingPiece != null && attackingPiece.player != this.player) {
                        // ✅ FIX: Simulate the move and check if the king is still safe
                        Piece originalPiece = board.board[targetRank][targetFile];
                        board.board[targetRank][targetFile] = this; // Temporarily move king
                        board.board[rank][file] = null; // Remove king from old position
                        
                        boolean kingIsInCheck = board.isKingInCheck() == (this.player ? 1 : 2);
                        
                        // Undo the temporary move
                        board.board[targetRank][targetFile] = originalPiece;
                        board.board[rank][file] = this;

                        if (kingIsInCheck) {
                            return false;
                        }
                    }
                }
            }
        }
    
        return true;
    }    
}
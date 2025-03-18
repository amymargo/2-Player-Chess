package chess;

public class Queen extends Piece {
    public Queen(boolean player, int rank, int file) {
        super(player, rank, file);
        if (player) pieceType = PieceType.WQ;
        else pieceType = PieceType.BQ;
    }

    @Override
    public boolean makeMove(int targetRank, int targetFile, Board board) {
        // ensure valid Queen movement (straight or diagonal)
        if (!(targetRank == rank || targetFile == file || 
              Math.abs(targetRank - rank) == Math.abs(targetFile - file))) {
            return false;
        }
    
        // check if path is blocked
        int rankStep = Integer.compare(targetRank, rank);
        int fileStep = Integer.compare(targetFile, file);
        
        int currentRank = rank + rankStep;
        int currentFile = file + fileStep;
    
        while (currentRank != targetRank || currentFile != targetFile) {
            if (currentRank < 0 || currentRank >= 8 || currentFile < 0 || currentFile >= 8) {
                return false; // Prevent out-of-bounds access
            }
            if (board.board[currentRank][currentFile] != null) {
                return false;
            }
            currentRank += rankStep;
            currentFile += fileStep;
        }        
    
        // check if destination has same color piece
        Piece targetPiece = board.board[targetRank][targetFile];
        if (targetPiece != null && targetPiece.player == this.player) {
            return false;
        }
        
        return true;
    }    
}
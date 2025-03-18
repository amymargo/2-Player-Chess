package chess;

public class Bishop extends Piece {
    public Bishop(boolean player, int rank, int file) {
        super(player, rank, file);
        if (player) pieceType = PieceType.WB;
        else pieceType = PieceType.BB;
    }

    @Override
    public boolean makeMove(int targetRank, int targetFile, Board board) {
        // ensure move is diagonal
        if (Math.abs(targetRank - rank) != Math.abs(targetFile - file)) {
            return false;
        }

        // determine direction of movement
        int rankStep = (targetRank > rank) ? 1 : -1; // up or down
        int fileStep = (targetFile > file) ? 1 : -1; // right or left

        int currentRank = rank + rankStep;
        int currentFile = file + fileStep;

        // check if path is blocked
        while (currentRank != targetRank && currentFile != targetFile) {
            if (currentRank < 0 || currentRank >= 8 || currentFile < 0 || currentFile >= 8) {
                return false; // Prevent out-of-bounds access
            }
            if (board.board[currentRank][currentFile] != null) return false;
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
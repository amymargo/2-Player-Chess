package chess;

public class Rook extends Piece{
    public boolean hasMoved = false; // To keep track of whether it is their very first move (for castling)

    public Rook(boolean player, int rank, int file){
        super(player, rank, file);
        if (player) pieceType = PieceType.WR;
        else pieceType = PieceType.BR;
    }

    public boolean makeMove(int targetRank, int targetFile, Board board){
        // Check if the move is straight
        if (targetRank != rank && targetFile != file) return false; // Rook is moving diagonal, so not valid

        // Getting direction of movement
        int rankDirection;
        int fileDirection;
        if (targetFile != file){
            rankDirection = 0;
            if (targetFile > file) fileDirection = 1;
            else fileDirection = -1;
        }
        else{
            fileDirection = 0;
            if (targetRank > rank) rankDirection = 1;
            else rankDirection = -1;
        }

        // Checking if there are pieces between current and target
        int currentRank = rank + rankDirection;
        int currentFile = file + fileDirection;
        while (currentRank != targetRank || currentFile != targetFile) {
            if (currentRank < 0 || currentRank >= 8 || currentFile < 0 || currentFile >= 8) {
                return false; // Prevent out-of-bounds access
            }
            if (board.board[currentRank][currentFile] != null) return false; // Piece is blocking, so move is invalid
            currentRank += rankDirection;
            currentFile += fileDirection;
        }        

        // Checking if target is occupied by same color piece
        Piece target = board.board[targetRank][targetFile];
        if (target != null && target.player == player) return false; // Can't capture own piece

        // Move the rook (actually update the board)
        board.board[rank][file] = null;
        board.board[targetRank][targetFile] = this;

        // Update the position
        rank = targetRank;
        file = targetFile;

        // Mark as moved
        hasMoved = true;
        return true;
    }
}

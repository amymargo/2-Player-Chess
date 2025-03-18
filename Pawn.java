package chess;

public class Pawn extends Piece{
    public boolean hasMoved = false; // To keep track of whether it is their very first move

    public Pawn(boolean player, int rank, int file){
        super(player, rank, file);
        if (player) pieceType = PieceType.WP;
        else pieceType = PieceType.BP;
    }

    public boolean makeMove(int targetRank, int targetFile, Board board){
        int direction;
        if (player) direction = 1;
        else direction = -1;

        // Basic move to go one rank forward
        if (targetRank == rank + direction && file == targetFile && board.board[targetRank][targetFile] == null){
            hasMoved = true;
            return true;
        }

        // Two steps forward, if it is the first move
        if (!hasMoved && targetRank == rank + (2 * direction) && board.board[targetRank][targetFile] == null && board.board[rank + direction][targetFile] == null && targetFile == file){
            hasMoved = true;
            return true;
        }

        // Capturing diagonal
        if (Math.abs(targetFile - file) == 1 && targetRank == rank + direction){
            Piece target = board.board[targetRank][targetFile];
            if (target != null && target.player != player){
                hasMoved = true;
                return true;
            }
            // En passant
            else if (target == null && (board.board[rank][targetFile] instanceof Pawn)){
                if (board.previousMove[0] == rank + 2*direction && board.previousMove[1] == targetFile && board.previousMove[2] == rank && board.previousMove[3] == targetFile){
                    hasMoved = true;
                    board.board[rank][targetFile] = null;
                    return true;
                }
            }
            return false;
        }
        
        return false;
    }
}

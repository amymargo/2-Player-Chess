package chess;

public class King extends Piece {
    public boolean hasMoved = false;

    public King(boolean player, int rank, int file) {
        super(player, rank, file);
        if (player) pieceType = PieceType.WK;
        else pieceType = PieceType.BK;
    }

    @Override
    public boolean makeMove(int targetRank, int targetFile, Board board) {
        // ensure the move is one square in any direction
        if (Math.abs(targetRank - rank) > 1 || Math.abs(targetFile - file) > 1) {
            // if (!hasMoved){
            //     return Castle(targetRank, targetFile, board);
            // }
            if (!hasMoved){
                boolean castleSuccess = Castle(targetRank, targetFile, board);
                if (!castleSuccess) return false; // ✅ Prevents setting `hasMoved` on failed castling
                return true;
            }            
            return false;
        }
    
        // check if destination has same color piece
        Piece targetPiece = board.board[targetRank][targetFile];
        if (targetPiece != null && targetPiece.player == this.player) {
            return false;
        }

        hasMoved = true;
        
        return true;
    }
    public boolean Castle(int targetRank, int targetFile, Board board){
        // Checking to see if King is moving ranks
        if ((targetRank != 0 && player) && (targetRank != 7 && !player)){
            System.out.println("wrong king");
            return false;
        }

        // Checking if King is in check
        if (player && board.isKingInCheck() == 1) {
            return false;
        }
        if (!player && board.isKingInCheck() == 2) {
            return false;
        }

        // c or g

        // Checking which corner of rook to castle with
        int rookCorner;
        int rookTargetFile;
        if (targetFile == 2) {
            rookCorner = 0;
            rookTargetFile = 3;
        }
        else if (targetFile == 6) {
            rookCorner = 7;
            rookTargetFile = 5;
        }
        else {
            System.out.println("error 1");
            return false;}

        // Checking to see if corner piece is a rook
        if (!(board.board[targetRank][rookCorner] instanceof Rook)){
            System.out.println("error 2");
            return false;
        }
        Rook rookToCastle = (Rook)board.board[targetRank][rookCorner];

        // Checking to see if rook has moved
        if (rookToCastle.hasMoved){
            System.out.println("error 3");
            return false;
        }

        // Checking to see if there are pieces in between
        int direction = 0;
        if (targetFile - file > 0) direction = 1;
        else direction = -1;

        int currentFile = file + direction;
        while (currentFile != rookCorner){
            if (board.board[targetRank][currentFile] != null){
                System.out.println("Piece in between isn't null");
                return false;
            }
            if (!validMove(targetRank, currentFile, board)) {
                System.out.println("King will cross/end where he will be in check");
                return false;
            }
            currentFile += direction;
        }

        // Castling
        board.board[targetRank][rookCorner] = null;
        board.board[targetRank][rookTargetFile] = new Rook(player, targetRank, rookTargetFile);
        Rook castledRook = (Rook)board.board[targetRank][rookTargetFile];
        castledRook.hasMoved = true;
        hasMoved = true;
        return true;
    }
    
}

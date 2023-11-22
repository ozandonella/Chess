package chess;

public class Rook extends Piece{
    private boolean hasMoved;
    public Rook(String name, boolean isWhite, Board board, int x, int y) {
        super(name, isWhite, board);
        hasMoved=false;
    }
    @Override
    public void makeMove(int[] pos, int[] dest) {
        board.set(board.pop(pos),dest);
        hasMoved=false;
    }
    @Override
    public boolean canMove(int[] pos, int[] dest, boolean withSafety) {
        if(board.query(pos)==null||!board.query(pos).equals(this)) return false;
        if(pos[0]!=dest[0]&&pos[1]!=dest[1]) return false;
        if(!board.canMoveCardinally(pos,dest)) return false;
        if(!withSafety) return true;
        Piece piece = board.pop(dest);
        board.set(board.pop(pos),dest);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),pos);
        board.set(piece,dest);
        return !inCheck;
    }
    @Override
    public boolean canAttack(int[] pos, int[] dest, boolean withSafety) {
        return canMove(pos,dest,withSafety);
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}

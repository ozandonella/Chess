package chess;
public class Bishop extends Piece{
    public Bishop(String name, boolean isWhite, Board board, int x, int y) {
        super(name, isWhite, board);
    }
    @Override
    public void makeMove(int[] pos, int[] dest) {
        board.set(board.pop(pos),dest);
    }
    @Override
    public boolean canMove(int[] pos, int[] dest, boolean withSafety) {
        if(board.query(pos)==null||!board.query(pos).equals(this)) return false;
        if(Math.abs(pos[0]-dest[0])!=Math.abs(pos[1]-dest[1])) return false;
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
}

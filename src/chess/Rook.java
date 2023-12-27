package chess;

import java.util.Arrays;

public class Rook extends Piece{
    public boolean hasMoved;
    public Rook(String name, boolean isWhite) {
        super(name, isWhite);
        hasMoved=false;
    }
    @Override
    public void makeMove(int[] dest, Board board) {
        board.set(board.pop(position),dest);
    }
    @Override
    public boolean canMove(int[] dest, Board board, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        if(position[0]!=dest[0]&&position[1]!=dest[1]) return false;
        if(!board.canMoveCardinally(position,dest)) return false;
        if(!withSafety) return true;
        int[] tempPos=new int[]{position[0],position[1]};
        Piece piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}

package chess;
import java.util.Arrays;

public class Queen extends Piece{
    public Queen(String name, boolean isWhite) {
        super(name, isWhite);
    }

    @Override
    public void makeMove(int[] dest, Board board) {
        board.set(board.pop(position),dest);
    }
    @Override
    public boolean canMove(int[] dest, Board board, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
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

}

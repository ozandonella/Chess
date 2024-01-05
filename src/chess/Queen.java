package chess;
import java.util.Arrays;

public class Queen extends Piece{
    public static final String name="Queen";
    public Queen(boolean isWhite) {
        super(isWhite);
        super.name=name;
    }

    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        MoveNode move = new MoveNode(this + " -> "+Board.convertPos(dest));
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        Queen current = new Queen(isWhite);
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        move.current.add(current);
        return move;
    }
    @Override
    public boolean canMove(int[] dest, Board board, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        if(board.cantMoveCardinally(position, dest)) return false;
        if(!withSafety) return true;
        int[] tempPos=new int[]{position[0],position[1]};
        Piece piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

    @Override
    public int getPointValue() {
        return 90;
    }

    public Piece copy() {
        Queen copy = new Queen(isWhite);
        copy.position=new int[]{position[0],position[1]};
        return copy;
    }
}

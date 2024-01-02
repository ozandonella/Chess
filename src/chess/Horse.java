package chess;

import java.util.Arrays;

public class Horse extends Piece{
    public static final String name="Horse";
    public Horse(boolean isWhite) {
        super(isWhite);
        super.name=name;
    }

    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        MoveNode move = new MoveNode(this + " -> "+Board.convertPos(dest));
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        Horse current = new Horse(isWhite);
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        move.current.add(current);
        return move;
    }

    @Override
    public boolean canMove(int[] dest, Board board, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if((y!=2&&y!=1)||(x!=2&&x!=1)||y==x) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&piece.isWhite==isWhite) return false;
        if(!withSafety) return true;
        int[] tempPos=new int[]{position[0],position[1]};
        piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

}

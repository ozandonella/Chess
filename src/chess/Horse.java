package chess;

import java.util.ArrayList;
import java.util.Arrays;

public class Horse extends Piece{
    public static final String name="Horse";
    public Horse(boolean isWhite, int index) {
        super(isWhite, index);
        super.name=name;
    }
    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        MoveNode move = new MoveNode(this + " -> "+Board.convertPos(dest));
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        Horse current = new Horse(isWhite, index);
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        move.current.add(current);
        return move;
    }
    @Override
    public boolean canMove(int[] dest, Board board) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if((y!=2&&y!=1)||(x!=2&&x!=1)||y==x) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&piece.isWhite==isWhite) return false;
        int[] tempPos=new int[]{position[0],position[1]};
        piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=(board.whiteTurn ? board.getWhiteKing() : board.getBlackKing()).inCheck(board);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

    @Override
    public int getPointValue() {
        return 30;
    }

    @Override
    public ArrayList<int[]> getMovePattern(Board board) {
        return Piece.getHorseMoves(position);
    }

    public Piece copy() {
        Horse copy = new Horse(isWhite, index);
        copy.position=new int[]{position[0],position[1]};
        return copy;
    }

}

package chess;

import java.util.ArrayList;
import java.util.Arrays;

public class Bishop extends Piece{
    public static final String name="Bishop";
    public Bishop(boolean isWhite, int index) {
        super(isWhite, index);
        super.name=name;
    }
    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        MoveNode move = new MoveNode();
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        Bishop current = new Bishop(isWhite, index);
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        move.current.add(current);
        return move;
    }
    @Override
    public boolean canMove(int[] dest, Board board) {
        if(board.query(position)==null||!board.query(position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+Arrays.toString(dest));
        if(Math.abs(position[0]-dest[0])!=Math.abs(position[1]-dest[1])) return false;
        if(board.cantMoveCardinally(position, dest)) return false;
        int[] tempPos=new int[]{position[0],position[1]};
        Piece piece = board.pop(dest);
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
        ArrayList<int[]> borders=Piece.getDiagonalBorders(position);
        ArrayList<int[]> destinations = new ArrayList<>();
        for(int[] border : borders) destinations.addAll(Piece.getOmniMoves(this,border,board));
        return destinations;
    }

    public Piece copy() {
        Bishop copy = new Bishop(isWhite, index);
        copy.position=new int[]{position[0],position[1]};
        return copy;
    }
}

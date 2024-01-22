package chess;

import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends Piece{
    public static final String name="Rook";
    public boolean hasMoved;

    public Rook(boolean isWhite, int index) {
        super(isWhite, index);
        super.name=name;
        hasMoved=false;
    }
    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        MoveNode move = new MoveNode();
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        Rook current = new Rook(isWhite, index);
        current.hasMoved=true;
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        move.current.add(current);
        return move;
    }
    @Override
    public boolean canMove(int[] dest, Board board) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        if(position[0]!=dest[0]&&position[1]!=dest[1]) return false;
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
        return 50;
    }

    @Override
    public ArrayList<int[]> getMovePattern(Board board) {
        ArrayList<int[]> borders=Piece.getCardinalBorders(position);
        ArrayList<int[]> destinations = new ArrayList<>();
        for(int[] border : borders) destinations.addAll(Piece.getOmniMoves(this,border,board));
        return destinations;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public Piece copy() {
        Rook copy = new Rook(isWhite, index);
        copy.hasMoved=hasMoved;
        copy.position=new int[]{position[0],position[1]};
        return copy;
    }
}

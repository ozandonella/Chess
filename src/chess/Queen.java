package chess;
import java.util.ArrayList;
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
    public boolean canMove(int[] dest, Board board) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        if(board.cantMoveCardinally(position, dest)) return false;
        int[] tempPos=new int[]{position[0],position[1]};
        Piece piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=(board.whiteTurn ? board.whiteKing : board.blackKing).inCheck(board);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }
    @Override
    public ArrayList<int[]> getMovePattern(Board board) {
        ArrayList<int[]> borders=Piece.getCardinalBorders(position);
        borders.addAll(Piece.getDiagonalBorders(position));
        ArrayList<int[]> destinations = new ArrayList<>();
        for(int[] border : borders) destinations.addAll(Piece.getOmniMoves(this,border,board));
        return destinations;
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

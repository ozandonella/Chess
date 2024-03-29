package chess;
import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece{
    public static final String name="Pawn";
    private Integer turnMoved;
    public Pawn(boolean isWhite, int index) {
        super(isWhite, index);
        super.name=name;
    }
    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        MoveNode move = new MoveNode();
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        Pawn current = new Pawn(isWhite, index);
        if(turnMoved==null) current.turnMoved=board.moveCount;
        else current.turnMoved=turnMoved;
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        move.current.add(current);
        if(position[0]!=dest[0]&&board.query(dest)==null) move.former.add(board.query(dest[0], position[1]));
        return move;
    }
    @Override
    public boolean canMove(int[] dest, Board board) {
        if(board.query(position)==null||!board.query(position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+Arrays.toString(dest));
        if(position[1]==dest[1]||(isWhite && position[1]>dest[1]) || (!isWhite &&position[1]<dest[1])) return false;
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if(y>2||x>1) return false;
        if(y==2&&(x!=0||turnMoved!=null||board.query(dest[0],isWhite ? dest[1]-1 : dest[1]+1)!=null)) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&(piece.isWhite == isWhite || x != 1)) return false;
        int[] enPass = board.existsEnPassant(position,dest);
        if(x!=0&&piece==null&&enPass==null) return false;
        int[] tempPos=new int[]{position[0],position[1]};
        Piece p = enPass==null ? null : board.query(enPass);
        board.set(board.pop(position),dest);
        if(p!=null) board.pop(enPass);
        boolean inCheck=(board.whiteTurn ? board.getWhiteKing() : board.getBlackKing()).inCheck(board);
        board.set(board.pop(dest),tempPos);
        if(p!=null) board.set(p,enPass);
        board.set(piece,dest);
        return !inCheck;
    }

    @Override
    public int getPointValue() {
        return 10+(isWhite ? position[1]-1 : 6-position[1])/3;
    }

    @Override
    public ArrayList<int[]> getMovePattern(Board board) {
        ArrayList<int[]> destinations = new ArrayList<>();
        if(isWhite){
            destinations.add(new int[]{position[0],position[1]+1});
            if(position[1]==1)destinations.add(new int[]{position[0],position[1]+2});
            if(position[0]!=0)destinations.add(new int[]{position[0]-1, position[1]+1});
            if(position[0]!=7)destinations.add(new int[]{position[0]+1, position[1]+1});
        }
        else{
            destinations.add(new int[]{position[0],position[1]-1});
            if(position[1]==6)destinations.add(new int[]{position[0],position[1]-2});
            if(position[0]!=0)destinations.add(new int[]{position[0]-1, position[1]-1});
            if(position[0]!=7)destinations.add(new int[]{position[0]+1, position[1]-1});
        }
        return destinations;
    }

    public boolean justCharged(Board board){
        return turnMoved!=null&&board.moveCount-1==turnMoved&&((isWhite&&position[1]==3)||!isWhite&&position[1]==4);
    }
    @Override
    public Piece copy() {
        Pawn copy = new Pawn(isWhite, index);
        copy.position=new int[]{position[0],position[1]};
        copy.turnMoved=turnMoved;
        return copy;
    }
}

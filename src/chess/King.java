package chess;
import java.util.Arrays;

public class King extends Piece{
    public static final String name="King";
    public boolean hasMoved;
    public King(boolean isWhite) {
        super(isWhite);
        super.name=name;
        hasMoved=false;
    }
    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        int x = dest[0]-position[0];
        MoveNode move = new MoveNode(this + " -> "+Board.convertPos(dest));
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        King current = new King(isWhite);
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        current.hasMoved=true;
        move.current.add(current);
        if(Math.abs(x)==2){
            Rook fRook = ((Rook)board.query(new int[]{x<0 ? 0 : 7, dest[1]}));
            Rook cRook =  new Rook(fRook.isWhite);
            cRook.position[0]=dest[0] + (x<0 ? 1 : -1);
            cRook.position[1]=dest[1];
            cRook.hasMoved=true;
            move.former.add(fRook);
            move.current.add(cRook);
        }
        return move;
    }

    @Override
    public boolean canMove(int[] dest, Board board, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if(y>1||x>2||(x==2&&y!=0)) return false;
        int[] rookPos = null;
        int[] rookDest = null;
        if(x==2){
            rookPos = dest[0] < position[0] ? new int[]{0,position[1]} : new int[]{7,position[1]};
            rookDest = board.existsCastle(this, dest);
            if(rookDest==null) return false;
        }
        else if(board.query(dest)!=null&&board.query(dest).isWhite==isWhite) return false;
        if(!withSafety) return true;
        int[] tempPos=new int[]{position[0],position[1]};
        if(rookDest!=null) board.set(board.pop(rookPos),rookDest);
        Piece piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=board.isInCheck(isWhite);
        if(rookDest!=null) board.set(board.pop(rookDest),rookPos);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

    @Override
    public int getPointValue() {
        return 0;
    }

    @Override
    public Piece copy() {
        King copy = new King(isWhite);
        copy.position=new int[]{position[0],position[1]};
        copy.hasMoved=hasMoved;
        return copy;
    }

}

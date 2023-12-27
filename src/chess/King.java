package chess;
import java.util.Arrays;

public class King extends Piece{
    public boolean hasMoved;
    public King(String name, boolean isWhite, Board board) {
        super(name, isWhite, board);
        hasMoved=false;
    }
    @Override
    public void makeMove(int[] dest) {
        int x = dest[0]-position[0];
        hasMoved=true;
        if(Math.abs(x)==2){
            int[] rookPos = new int[]{x<0 ? 0 : 7, dest[1]};
            int[] rookDest = new int[]{dest[0] + (x<0 ? 1 : -1), dest[1]};
            board.set(board.pop(rookPos),rookDest);
            ((Rook)board.query(rookDest)).hasMoved=true;
        }
        board.set(board.pop(position),dest);
    }

    @Override
    public boolean canMove(int[] dest, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if(y>1||x>2||(x==2&&y!=0)) return false;
        int[] rookPos = null;
        int[] rookDest = null;
        if(x==2){
            rookPos = dest[0] < position[0] ? new int[]{0,position[1]} : new int[]{7,position[1]};
            rookDest = board.existsCastle(position, dest);
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

}

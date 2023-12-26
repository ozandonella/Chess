package chess;
import java.util.ArrayList;

public class King extends Piece{
    private boolean hasMoved;
    public King(String name, boolean isWhite, Board board, int x, int y) {
        super(name, isWhite, board);
        hasMoved=false;
    }
    @Override
    public void makeMove(int[] pos, int[] dest) {
        int x = dest[0]-pos[0];
        hasMoved=true;
        if(Math.abs(x)==2){
            int[] rookPos = new int[]{x<0 ? 0 : 7, dest[1]};
            int[] rookDest = new int[]{dest[0] + (x<0 ? 1 : -1), dest[1]};
            board.set(board.pop(rookPos),rookDest);
        }
        board.set(board.pop(pos),dest);
    }

    @Override
    public boolean canMove(int[] pos, int[] dest, boolean withSafety) {
        if(board.query(pos)==null||!board.query(pos).equals(this)) return false;
        int x = Math.abs(pos[0]-dest[0]), y = Math.abs(pos[1]-dest[1]);
        if(y>1||x>2||(x==2&&y!=0)) return false;
        int[] rookPos = null;
        int[] rookDest = null;
        if(x==2){
            rookPos = dest[0] < pos[0] ? new int[]{0,pos[1]} : new int[]{7,pos[1]};
            rookDest = existsCastle(pos, dest, rookPos);
            if(rookDest==null) return false;
        }
        else if(board.query(dest)!=null&&board.query(dest).isWhite==isWhite) return false;
        if(!withSafety) return true;
        if(rookDest!=null) board.set(board.pop(rookPos),rookDest);
        Piece piece = board.pop(dest);
        board.set(board.pop(pos),dest);
        boolean inCheck=board.isInCheck(isWhite);
        if(rookDest!=null) board.set(board.pop(rookDest),rookPos);
        board.set(board.pop(dest),pos);
        board.set(piece,dest);
        return !inCheck;
    }

    /**
     *
     * @param king king position
     * @param dest king destination
     * @param rook rook position
     * @return rook destination after making a castle with the inputted move or null if castle does not exist
     */
    public int[] existsCastle(int[] king, int[] dest, int[] rook){
        Piece piece = board.query(rook);
        if(hasMoved||piece==null||piece.isWhite!=isWhite||!piece.getName().equals("Rook")||((Rook) piece).hasMoved()) return null;
        int x=king[0];
        while(x!=dest[0]){
            x+= dest[0]>x ? 1 : -1;
            if(board.query(x,dest[1])!=null) return null;
            ArrayList<Integer[]> attackers = board.getAllAttacking(new int[]{x,dest[1]},isWhite,false);
            if(!attackers.isEmpty()) return null;
        }
        return new int[]{dest[0] + (dest[0]<king[0] ? 1 : -1), dest[1]};
    }
    @Override
    public boolean canAttack(int[] pos, int[] dest, boolean withSafety) {
        return (Math.abs(pos[0]-Math.abs(dest[0]))<2&&canMove(pos,dest,withSafety));
    }
}

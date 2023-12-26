package chess;

import java.util.Arrays;

public class Pawn extends Piece{
    private boolean charged;
    private Integer turnMoved;
    public Pawn(String name, boolean isWhite, Board board, int x, int y) {
        super(name, isWhite, board);
        charged=false;
    }

    @Override
    public void makeMove(int[] pos, int[] dest) {
        if(pos[0]!=dest[0]&&board.query(dest)==null) board.pop(dest[0], pos[1]);
        board.set(board.pop(pos),dest);
        if(turnMoved==null) turnMoved=board.moveCount;
        charged = board.moveCount==turnMoved&&Math.abs(pos[1]-dest[1])==2;
    }
    @Override
    public boolean canMove(int[] pos, int[] dest, boolean withSafety) {
        if(board.query(pos)==null||!board.query(pos).equals(this)) return false;
        if(pos[1]==dest[1]||(isWhite && pos[1]>dest[1]) || (!isWhite &&pos[1]<dest[1])) return false;
        int x = Math.abs(pos[0]-dest[0]), y = Math.abs(pos[1]-dest[1]);
        if(y>2||x>1||(y==2&&((x!=0)||turnMoved!=null))) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&(piece.isWhite == isWhite || x != 1)) return false;
        int[] enPass = existsEnPassant(pos,dest);
        if(x!=0&&piece==null&&enPass==null) return false;
        if(!withSafety) return true;
        Piece p = enPass==null ? null : board.query(enPass);
        board.set(board.pop(pos),dest);
        if(p!=null) board.pop(enPass);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),pos);
        if(p!=null) board.set(p,enPass);
        board.set(piece,dest);
        return !inCheck;
    }

    /**
     * @param pos pos of this pawn
     * @param dest destination this pawn is moving to
     * @return the dest of the opposing pawn that gets attacked if their exists an enPassant else null
     */
    public int[] existsEnPassant(int[] pos, int[] dest){
        if(Board.outOfBounds(dest)) return null;
        int x = Math.abs(pos[0]-dest[0]), y = Math.abs(pos[1]-dest[1]);
        if(x!=1&&y!=1||board.query(dest)!=null) return null;
        int[] enPass = new int[]{dest[0],pos[1]};
        Piece p = board.query(enPass);
        return (p!=null&&p.isWhite!=isWhite&&p.getName().equals("Pawn")&&((((Pawn) p).charged))) ? enPass : null;
    }
    @Override
    public boolean canAttack(int[] pos, int[] dest, boolean withSafety) {
        int[] enPass = existsEnPassant(pos,new int[]{dest[0],dest[1]+(isWhite ? 1 : -1)});
        return pos[0]!=dest[0]&&(enPass!=null||canMove(pos,dest,withSafety));
    }
}

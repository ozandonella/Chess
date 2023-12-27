package chess;

import java.util.Arrays;

public class Pawn extends Piece{
    public boolean charged;
    private Integer turnMoved;
    public Pawn(String name, boolean isWhite) {
        super(name, isWhite);
        charged=false;
    }

    @Override
    public void makeMove(int[] dest, Board board) {
        if(position[0]!=dest[0]&&board.query(dest)==null) board.pop(dest[0], position[1]);
        board.set(board.pop(position),dest);
        if(turnMoved==null) turnMoved=board.moveCount;
        charged = board.moveCount==turnMoved&&Math.abs(position[1]-dest[1])==2;
    }
    @Override
    public boolean canMove(int[] dest, Board board, boolean withSafety) {
        if(board.query(position)==null||!board.query(position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+Arrays.toString(dest));
        if(position[1]==dest[1]||(isWhite && position[1]>dest[1]) || (!isWhite &&position[1]<dest[1])) return false;
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if(y>2||x>1||(y==2&&((x!=0)||turnMoved!=null))) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&(piece.isWhite == isWhite || x != 1)) return false;
        int[] enPass = board.existsEnPassant(position,dest);
        if(x!=0&&piece==null&&enPass==null) return false;
        if(!withSafety) return true;
        int[] tempPos=new int[]{position[0],position[1]};
        Piece p = enPass==null ? null : board.query(enPass);
        board.set(board.pop(position),dest);
        if(p!=null) board.pop(enPass);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),tempPos);
        if(p!=null) board.set(p,enPass);
        board.set(piece,dest);
        return !inCheck;
    }

}

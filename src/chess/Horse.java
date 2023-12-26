package chess;

import java.util.Arrays;

public class Horse extends Piece{
    public Horse(String name, boolean isWhite, Board board) {
        super(name, isWhite, board);
    }

    @Override
    public void makeMove(int[] dest) {
        board.set(board.pop(position),dest);
    }

    @Override
    public boolean canMove(int[] dest, boolean withSafety) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: piece-> "+this+" location-> "+ Arrays.toString(dest));
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if((y!=2&&y!=1)||(x!=2&&x!=1)||y==x) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&piece.isWhite==isWhite) return false;
        if(!withSafety) return true;
        int[] tempPos=new int[]{position[0],position[1]};
        piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

}

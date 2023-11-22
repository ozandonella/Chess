package chess;

public class Horse extends Piece{
    public Horse(String name, boolean isWhite, Board board, int x, int y) {
        super(name, isWhite, board);
    }

    @Override
    public void makeMove(int[] pos, int[] dest) {
        board.set(board.pop(pos),dest);
    }

    @Override
    public boolean canMove(int[] pos, int[] dest, boolean withSafety) {
        if(board.query(pos)==null||!board.query(pos).equals(this)) return false;
        int x = Math.abs(pos[0]-dest[0]), y = Math.abs(pos[1]-dest[1]);
        if((y!=2&&y!=1)||(x!=2&&x!=1)||y==x) return false;
        Piece piece = board.query(dest);
        if(piece!=null&&piece.isWhite==isWhite) return false;
        if(!withSafety) return true;
        board.set(board.pop(pos),dest);
        boolean inCheck=board.isInCheck(isWhite);
        board.set(board.pop(dest),pos);
        board.set(piece,dest);
        return !inCheck;
    }

    @Override
    public boolean canAttack(int[] pos, int[] dest, boolean withSafety) {
        return canMove(pos,dest,withSafety);
    }
}

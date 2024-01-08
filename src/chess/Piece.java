package chess;

import java.util.ArrayList;

public abstract class Piece{
    public String name;
    public final boolean isWhite;
    public int[] position;

    public Piece(boolean isWhite){
        position=new int[2];
        this.isWhite=isWhite;
    }
    public abstract MoveNode generateMove(int[] dest, Board board);
    public abstract boolean canMove(int[] dest, Board board, boolean withSafety);
    public abstract int getPointValue();
    public abstract ArrayList<int[]> getMovePattern(Board board);
    public String toString(){
        return (isWhite ? "White " : "Black ") + name +" at " + Board.convertPos(position);
    }
    public String getName(){
        return name;
    }
    public abstract Piece copy();
}

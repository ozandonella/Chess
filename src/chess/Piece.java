package chess;

import java.util.Arrays;

public abstract class Piece{
    private final String name;
    public final boolean isWhite;
    protected final Board board;
    public int[] position;

    public Piece(String name, boolean isWhite, Board board){
        position=new int[2];
        this.board=board;
        this.name=name;
        this.isWhite=isWhite;
    }
    public abstract void makeMove(int[] dest);
    public abstract boolean canMove(int[] dest, boolean withSafety);
    public String toString(){
        return (isWhite ? "White " : "Black ") + name +" at "+ board.convertPos(position);
    }
    public String getName(){
        return name;
    }
    public int[] getPlayerPos(boolean isWhite, int[] location){
        int[] temp = new int[2];
        temp[0]=location[0];
        temp[1]=location[1];
        if(!isWhite) Board.oppOrient(temp);
        return temp;
    }
}

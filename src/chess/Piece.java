package chess;

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
    public String toString(){
        return (isWhite ? "White " : "Black ") + name +" at " + Board.convertPos(position);
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

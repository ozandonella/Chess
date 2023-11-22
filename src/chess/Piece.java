package chess;

public abstract class Piece{
    private final String name;
    public final boolean isWhite;
    protected final Board board;

    public Piece(String name, boolean isWhite, Board board){
        this.board=board;
        this.name=name;
        this.isWhite=isWhite;
    }
    public abstract void makeMove(int[] pos, int[] dest);
    public abstract boolean canMove(int[] pos, int[] dest, boolean withSafety);
    public abstract boolean canAttack(int[] pos, int[] dest, boolean withSafety);
    public String toString(){
        return (isWhite ? "White " : "Black ") + name;
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

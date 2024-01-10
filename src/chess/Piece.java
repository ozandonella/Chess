package chess;
import java.util.ArrayList;

public abstract class Piece{
    public String name;
    public final boolean isWhite;
    public int[] position;
    public final int index;

    public Piece(boolean isWhite, int index){
        this.index=index;
        position=new int[2];
        this.isWhite=isWhite;
    }
    public abstract MoveNode generateMove(int[] dest, Board board);
    public abstract boolean canMove(int[] dest, Board board);
    public abstract int getPointValue();
    public abstract ArrayList<int[]> getMovePattern(Board board);
    public String toString(){
        return (isWhite ? "White " : "Black ") + name +" at " + Board.convertPos(position);
    }
    public String getName(){
        return name;
    }
    public abstract Piece copy();
    public static ArrayList<int[]> getDiagonalBorders(int[] pos){
        ArrayList<int[]> borders = new ArrayList<>();
        int dist;
        if(pos[1]!=7){
            if(pos[0]!=0){
                dist=Math.min(pos[0],7-pos[1]);
                borders.add(new int[]{pos[0]-dist, pos[1]+dist});
            }
            if(pos[0]!=7){
                dist=Math.min(7-pos[0],7-pos[1]);
                borders.add(new int[]{pos[0]+dist, pos[1]+dist});
            }
        }
        if(pos[1]!=0){
            if(pos[0]!=7){
                dist=Math.min(pos[1],7-pos[0]);
                borders.add(new int[]{pos[0]+dist, pos[1]-dist});
            }
            if(pos[0]!=0){
                dist=Math.min(pos[0],pos[1]);
                borders.add(new int[]{pos[0]-dist, pos[1]-dist});
            }
        }
        return borders;
    }
    public static ArrayList<int[]> getCardinalBorders(int[] pos){
        ArrayList<int[]> borders = new ArrayList<>();
        if(pos[1]!=7) borders.add(new int[]{pos[0], 7});
        if(pos[1]!=0) borders.add(new int[]{pos[0], 0});
        if(pos[0]!=0) borders.add(new int[]{0, pos[1]});
        if(pos[0]!=7) borders.add(new int[]{7, pos[1]});
        return borders;
    }
    public static ArrayList<int[]> getOmniMoves(Piece p, int[] dest, Board board){
        ArrayList<int[]> destinations = new ArrayList<>();
        int x=p.position[0], y=p.position[1];
        while(x!=dest[0]||y!=dest[1]){
            if(x!=dest[0]) x+=x<dest[0]?1:-1;
            if(y!=dest[1]) y+=y<dest[1]?1:-1;
            int[] d = new int[]{x,y};
            destinations.add(d);
            if(board.query(x,y)!=null) break;
        }
        return destinations;
    }
    public static ArrayList<int[]> getHorseMoves(int[] position){
        ArrayList<int[]> destinations=new ArrayList<>();
        if(position[0]>0){
            if(position[1]<6) destinations.add(new int[]{position[0]-1, position[1]+2});
            if(position[1]>1) destinations.add(new int[]{position[0]-1, position[1]-2});
        }
        if(position[0]<7){
            if(position[1]<6) destinations.add(new int[]{position[0]+1, position[1]+2});
            if(position[1]>1) destinations.add(new int[]{position[0]+1, position[1]-2});
        }
        if(position[1]>0){
            if(position[0]>1) destinations.add(new int[]{position[0]-2, position[1]-1});
            if(position[0]<6) destinations.add(new int[]{position[0]+2, position[1]-1});
        }
        if(position[1]<7){
            if(position[0]>1) destinations.add(new int[]{position[0]-2, position[1]+1});
            if(position[0]<6) destinations.add(new int[]{position[0]+2, position[1]+1});
        }
        return destinations;
    }
}

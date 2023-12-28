package chess;

import java.util.ArrayList;

public class MoveNode {
    public ArrayList<Piece> current;
    public ArrayList<Piece> former;
    public ArrayList<MoveNode> next;
    public MoveNode prev;
    public String name;
    public MoveNode(String name){
        this.name=name;
        next=new ArrayList<>();
        current=new ArrayList<>();
        former=new ArrayList<>();
    }
    public String toString(){
        return name;
    }
}

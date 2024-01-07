package chess;

import java.util.ArrayList;

public class MoveNode {
    public ArrayList<Piece> current;
    public ArrayList<Piece> former;
    public ArrayList<MoveNode> next;
    public MoveNode prev;
    public String name;
    public int value;
    public int posHash;
    public int destHash;
    public MoveNode(String name){
        this.name=name;
        next=new ArrayList<>();
        current=new ArrayList<>();
        former=new ArrayList<>();
    }
    public void print(String line){
        line+=this;
        if(next.isEmpty()) System.out.println(line+";");
        else next.get(0).print(line+", ");
        if(next.size()==1) return;
        String newLine=Screen.getXBlank(line.length()+2);
        for(int x=1; x<next.size(); x++) next.get(x).print(newLine);
    }
    public String toString(){
        return name+" ("+posHash+","+destHash+") "+value;
    }
    public MoveNode copy(){
        MoveNode copy = new MoveNode(name);
        copy.value=value;
        copy.posHash=posHash;
        copy.destHash=destHash;
        copy.former.addAll(former);
        copy.current.addAll(current);
        return copy;
    }

}

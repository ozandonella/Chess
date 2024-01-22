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
    public Board.GameState gameState;
    public MoveNode(){
        gameState=Board.GameState.UNSET;
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
        return name;
    }
    public MoveNode copy(){
        MoveNode copy = new MoveNode();
        if(name!=null) copy.name=name;
        copy.value=value;
        copy.posHash=posHash;
        copy.destHash=destHash;
        copy.gameState=gameState;
        copy.former.addAll(former);
        copy.current.addAll(current);
        return copy;
    }
    public String decodeHash(){
        String moveRep="";
        moveRep+=Board.letters[posHash%8];
        moveRep+=Board.numbers[posHash/8];
        moveRep+=Board.letters[destHash%8];
        moveRep+=Board.numbers[destHash/8];
        return moveRep;
    }
    public long size(){
        if(next.isEmpty()) return 1;
        long k = 0;
        for(MoveNode m : next) k+=m.size();
        return k;
    }
    public void setName(){
        if(former.isEmpty()){
            name="GameStart";
            return;
        }
        Piece p = former.get(0);
        name=(p.isWhite ? "White " : "Black ") + p.name + " ["+decodeHash()+"]";
        if(p.name.charAt(0)!=current.get(0).name.charAt(0)) name+= " ("+current.get(0).name+")";
    }
    public void setNameRec(){
        setName();
        for(MoveNode m : next) m.setName();
    }
}

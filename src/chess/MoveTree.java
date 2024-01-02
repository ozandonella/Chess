package chess;

import java.util.ArrayList;

public class MoveTree{
    public MoveNode head;
    public MoveNode current;
    public MoveTree(){
        head=new MoveNode("Game Start");
        current=head;
    }
    /**
     * Adds given node to the trees current node's next list if the move does not exist
     * then returns the index of where the given node is located in current.next
     * @param node given node
     * @return given node position in current.next
     */
    public int addMove(MoveNode node){
        for (int x=0; x<current.next.size(); x++) {
            MoveNode m = current.next.get(x);
            if (m.posHash == node.posHash && m.destHash == node.destHash && node.name.equals(m.name)) return x;
        }
        current.next.add(node);
        node.prev=current;
        return current.next.size()-1;
    }
    public void prev(){
        if(current==head) return;
        current=current.prev;
    }
    public void next(int ind){
        if(current==null||current.next.isEmpty()) return;
        current=current.next.get(ind);
    }
    /*public void print(MoveNode move){
        System.out.println(move);
        System.out.println("-----------------");
        for(MoveNode m : move.next) System.out.println("-> "+m);
        System.out.println("-----------------");
        System.out.println();
        for (MoveNode m : move.next) print(m);
    }*/
    public void print(){
        head.print(0);
    }
}

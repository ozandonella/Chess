package chess;

import java.util.ArrayList;

public class MoveTree{
    public MoveNode head;
    public MoveNode current;
    public MoveTree(){
        head=new MoveNode("Game Start");
        head.gameState= Board.GameState.FREE;
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
    public void setCurrent(MoveNode m){
        if(current==head) setHead(m);
        else{
            m.prev=current.prev;
            m.prev.next.remove(current);
            prev();
            next(addMove(m));
        }

    }
    public void setHead(MoveNode m){
        head=m;
        current=head;
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
        head.print("");
    }
    public void trim(){
        MoveNode tempCurr = current;
        MoveNode prev = current.prev;
        while(prev!=null){
            MoveNode cHolder=current;
            current=prev.copy();
            addMove(cHolder);
            prev=prev.prev;
        }
        setHead(current);
        current=tempCurr;
    }
    public static void synch(MoveTree tree, MoveTree synchTo){
        ArrayList<MoveNode> mainBranch = synchTo.getMainBranch();
        tree.current=tree.head;
        for(int x=mainBranch.size()-2; x>=0; x--)tree.next(tree.addMove(mainBranch.get(x)));
    }
    public void printCurrentLine(){
        ArrayList<MoveNode> mainBranch=getMainBranch();
        StringBuilder line= new StringBuilder();
        for(int x=mainBranch.size()-1; x>=0; x--) line.append(mainBranch.get(x).toString()).append(", ");
        System.out.println(line);
    }

    /**
     * returns copies of the nodes leading up to the current node in REVERSE ORDER
     */
    public ArrayList<MoveNode> getMainBranch(){
        ArrayList<MoveNode> mainBranch=new ArrayList<>();
        MoveNode temp=current;
        while(temp!=head){
            mainBranch.add(temp.copy());
            temp=temp.prev;
        }
        mainBranch.add(head.copy());
        return mainBranch;
    }
    public long size(){
        return head.size();
    }
}


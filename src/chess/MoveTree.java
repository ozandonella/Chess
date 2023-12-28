package chess;

import chess.MoveNode;

import java.util.Scanner;

public class MoveTree{
    public MoveNode head;
    public MoveNode current;
    public MoveTree(){
        head=null;
        current=null;
    }
    public void addMove(MoveNode node){
        if(current!=null){
            current.next.add(node);
            node.prev=current;
        }
        else{
            head = node;
            current=head;
        }
    }
    public void prev(){
        if(current==head) return;
        current=current.prev;
    }
    public void next(){
        if(current==null||current.next.isEmpty()) return;
        if(current.next.size()==1) current=current.next.get(0);
        else{
            System.out.println("Choose Move Path");
            try {
                Scanner s = new Scanner(System.in);
                int x=-1;
                while(x<0||x>=current.next.size()){
                    for(int i=0; i<current.next.size(); i++) System.out.println("["+i+"] "+ current.next.get(i));
                    try {
                        x=s.nextInt();
                    }catch(Exception e){
                        System.out.println("Invalid Input");
                    }
                }
                current=current.next.get(x);
            }catch(Exception e){
                System.out.println("File Issue");
            }
        }
    }
}

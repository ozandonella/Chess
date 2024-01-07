package chess;

import java.util.ArrayList;

public class Bot {
    public final MoveTree tree;
    public Bot(){
        tree=new MoveTree();
    }
    public ArrayList<MoveNode> generateMoves(boolean isWhite, Board board){
        ArrayList<MoveNode> moves = new ArrayList<>();
        ArrayList<Piece> pieces = new ArrayList<>(isWhite ? board.whitePieces : board.blackPieces);
        for(Piece piece : pieces){
            for(Integer[] d : board.getMoves(piece)){
                int[] dest = new int[]{d[0],d[1]};
                if((dest[1]==0||dest[1]==7)&&piece.getName().equals(Pawn.name)){
                    Piece cap = board.query(dest);
                    for(int x=0; x<2; x++){
                        MoveNode move = new MoveNode(piece + " -> "+Board.convertPos(dest));
                        Piece p;
                        move.former.add(piece);
                        if(cap!=null) move.former.add(cap);
                        if(x==0) p=new Horse(piece.isWhite);
                        else p=new Queen(piece.isWhite);
                        p.position=dest;
                        move.current.add(p);
                        move.posHash=Board.getHash(piece.position);
                        move.destHash=Board.getHash(dest);
                        move.name+="("+move.current.get(0).getName()+")";
                        moves.add(move);
                    }
                }
                else{
                    MoveNode m = piece.generateMove(dest, board);
                    m.posHash=Board.getHash(piece.position);
                    m.destHash=Board.getHash(dest);
                    moves.add(m);
                }
            }
        }
        return moves;
    }
    public int evalBoard(Board board){
        if(board.gameState==1) return 400;
        if(board.gameState==2) return -400;
        if(board.gameState==3) return 0;
        int whiteAttackers=0;
        int blackAttackers=0;
        int sum=0;
        for(Piece p : board.whitePieces){
            if(p.name.charAt(0)!='P'&&Math.abs(p.position[0]-board.blackKing.position[0])<4&&Math.abs(p.position[1]-board.blackKing.position[1])<4) whiteAttackers++;
            sum+=p.getPointValue();
        }
        for (Piece p : board.blackPieces){
            if(p.name.charAt(0)!='P'&&Math.abs(p.position[0]-board.whiteKing.position[0])<4&&Math.abs(p.position[1]-board.whiteKing.position[1])<4) blackAttackers++;
            sum-=p.getPointValue();
        }
        if(whiteAttackers!=0)whiteAttackers=Math.max(1,whiteAttackers/2);
        if(blackAttackers!=0)blackAttackers=Math.max(1,blackAttackers/2);
        sum-=(blackAttackers);
        sum+=(whiteAttackers);
        return sum;
    }
    public MoveTree findBestLine(int steps, Board board){
        if(steps==0) return null;
        MoveTree temp = board.moveTree;
        MoveTree.synch(tree,board.moveTree);
        tree.trim();
        board.moveTree = tree;
        MoveTree t=new MoveTree();
        t.setHead(growTree(steps, board));
        //tree.print();
        board.moveTree=temp;
        return t;
    }
    public void setToBestLines(int steps, Board board){
        MoveTree temp = board.moveTree;
        MoveTree.synch(tree,board.moveTree);
        board.moveTree = tree;
        findPath(steps,board);
        tree.printCurrentLine();
        board.moveTree=temp;

    }

    public MoveNode growTree(int steps, Board board){
        if(steps==0||board.gameState!=0){
            board.moveTree.current.value=evalBoard(board);
            return board.moveTree.current.copy();
        }
        ArrayList<MoveNode> next = new ArrayList<>();
        ArrayList<MoveNode> moves = tree.current.next.isEmpty() ? generateMoves(board.whiteTurn,board) : tree.current.next;
        int val=1000;
        for(MoveNode move : moves){
            board.moveForward(board.moveTree.addMove(move));
            next.add(growTree(steps-1, board));
            board.moveBackward();
            if(val==1000||(board.whiteTurn&&move.value>val)||(!board.whiteTurn&&move.value<val)) val=move.value;
        }
        board.moveTree.current.value = val;
        MoveNode ret = board.moveTree.current.copy();
        for(MoveNode move : next){
            if(move.value==val){
                ret.next.add(move);
                move.prev=ret;
            }
        }
        return ret;
    }
    public void findPath(int steps, Board board){
        if(steps==0||board.gameState!=0){
            board.moveTree.current.value=evalBoard(board);
            return;
        }
        MoveNode best = null;
        int val=1000;
        ArrayList<MoveNode> next = new ArrayList<>();
        ArrayList<MoveNode> moves = generateMoves(board.whiteTurn, board);
        for(MoveNode move : moves){
            board.moveForward(board.moveTree.addMove(move));
            findPath(steps-1, board);
            board.moveBackward();
            next.add(move);
            if(best==null||board.whiteTurn&&move.value>val||!board.whiteTurn&&move.value<val){
                best=move;
                val=move.value;
            }
        }
        board.moveTree.current.value = val;
        board.moveTree.current.next = new ArrayList<>();
        for(MoveNode m : next) if(m.value==val) board.moveTree.addMove(m);
    }
}

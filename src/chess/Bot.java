package chess;

import java.util.ArrayList;

public class Bot {
    private final Board board;
    public MoveTree tree;
    public Bot(Board board){
        this.board=board;
    }
    public ArrayList<MoveNode> generateMoves(boolean isWhite){
        ArrayList<MoveNode> moves = new ArrayList<>();
        ArrayList<Piece> pieces = new ArrayList<>(isWhite ? board.whitePieces : board.blackPieces);
        for(Piece piece : pieces){
            for(Integer[] dest : board.getMoves(piece)){
                MoveNode m = piece.generateMove(new int[]{dest[0],dest[1]}, board);
                m.posHash=Board.getHash(piece.position);
                m.destHash=Board.getHash(new int[]{dest[0],dest[1]});
                moves.add(m);
            }
        }
        return moves;
    }
    public int evalBoard(){
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
    public MoveTree findBestLine(int steps){
        if(steps==0) return null;
        MoveTree temp = board.moveTree;
        MoveTree bestLine= new MoveTree();
        board.moveTree = bestLine;
        findPath(steps);
        board.moveTree=temp;
        return bestLine;
    }
    public void findPath(int steps){
        if(steps==0||board.gameState!=0){
            board.moveTree.current.value=evalBoard();
            return;
        }
        MoveNode best = null;
        int val=1000;
        for(MoveNode move : generateMoves(board.whiteTurn)){
            board.moveForward(board.moveTree.addMove(move));
            findPath(steps-1);
            board.moveBackward();
            if(best==null||board.whiteTurn&&move.value>val||!board.whiteTurn&&move.value<val){
                best=move;
                val=move.value;
            }
        }
        if(best!=null) {
            board.moveTree.current.value = val;
            board.moveTree.current.next = new ArrayList<>();
            board.moveTree.addMove(best);
        }
    }
}

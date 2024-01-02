package chess;

import java.util.ArrayList;

public class Bot {
    private final Board board;
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
        if(board.gameState==1) return 40;
        if(board.gameState==2) return -40;
        if(board.gameState==3) return 0;
        int sum=0;
        for(Piece p : board.whitePieces) sum+=p.pointValue;
        for (Piece p : board.blackPieces) sum-=p.pointValue;
        return sum;
    }
    public MoveTree findBestLine(int steps){
        if(steps==0) return null;
        MoveTree temp = board.moveTree;
        MoveTree bestLine= new MoveTree();
        board.moveTree = bestLine;
        bestLine.print();
        findPath(steps);
        board.moveTree=temp;
        return bestLine;
    }
    public void findPath(int steps){
        if(steps==0){
            board.moveTree.current.value=evalBoard();
            return;
        }
        MoveNode best = null;
        int val=100;
        for(MoveNode move : generateMoves(board.whiteTurn)){
            board.moveForward(board.moveTree.addMove(move));
            findPath(steps-1);
            board.moveBackward();
            if(best==null){
                best=move;
                val=move.value;
            }
            else if(board.whiteTurn&&move.value>val){
                best=move;
                val=move.value;
            }
            else if(!board.whiteTurn&&move.value<val){
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

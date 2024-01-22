package chess;
import java.util.ArrayList;

public class King extends Piece{
    public static final String name="King";
    public boolean hasMoved;
    public King(boolean isWhite, int index) {
        super(isWhite, index);
        super.name=name;
        hasMoved=false;
    }
    @Override
    public MoveNode generateMove(int[] dest, Board board) {
        int x = dest[0]-position[0];
        MoveNode move = new MoveNode();
        move.former.add(this);
        if(board.query(dest)!=null) move.former.add(board.query(dest));
        King current = new King(isWhite, index);
        current.position[0]=dest[0];
        current.position[1]=dest[1];
        current.hasMoved=true;
        move.current.add(current);
        if(Math.abs(x)==2){
            Rook fRook = ((Rook)board.query(new int[]{x<0 ? 0 : 7, dest[1]}));
            Rook cRook =  new Rook(fRook.isWhite, fRook.index);
            cRook.position[0]=dest[0] + (x<0 ? 1 : -1);
            cRook.position[1]=dest[1];
            cRook.hasMoved=true;
            move.former.add(fRook);
            move.current.add(cRook);
        }
        return move;
    }

    @Override
    public boolean canMove(int[] dest, Board board) {
        if(board.query(this.position)==null||!board.query(this.position).equals(this)) throw new RuntimeException("piece location error: found-> "+board.query(this.position)+" at " +this+" location-> ");
        int x = Math.abs(position[0]-dest[0]), y = Math.abs(position[1]-dest[1]);
        if(y>1||x>2||(x==2&&y!=0)) return false;
        int[] rookPos = null;
        int[] rookDest = null;
        if(x==2){
            rookPos = dest[0] < position[0] ? new int[]{0,position[1]} : new int[]{7,position[1]};
            rookDest = board.existsCastle(this, dest);
            if(rookDest==null) return false;
        }
        else if(board.query(dest)!=null&&board.query(dest).isWhite==isWhite) return false;
        int[] tempPos=new int[]{position[0],position[1]};
        if(rookDest!=null) board.set(board.pop(rookPos),rookDest);
        Piece piece = board.pop(dest);
        board.set(board.pop(position),dest);
        boolean inCheck=(board.whiteTurn ? board.getWhiteKing() : board.getBlackKing()).inCheck(board);
        if(rookDest!=null) board.set(board.pop(rookDest),rookPos);
        board.set(board.pop(dest),tempPos);
        board.set(piece,dest);
        return !inCheck;
    }

    @Override
    public int getPointValue() {
        return 0;
    }

    @Override
    public ArrayList<int[]> getMovePattern(Board board) {
        ArrayList<int[]> destinations=Piece.getCardinalBorders(position);
        destinations.addAll(Piece.getDiagonalBorders(position));

        for(int[] border : destinations){
            if(border[0]<position[0]) border[0]=position[0]-1;
            if(border[0]>position[0]) border[0]=position[0]+1;
            if(border[1]<position[1]) border[1]=position[1]-1;
            if(border[1]>position[1]) border[1]=position[1]+1;
        }
        if(!hasMoved){
            destinations.add(new int[]{position[0]+2, position[1]});
            destinations.add(new int[]{position[0]-2, position[1]});
        }
        return destinations;
    }
    public boolean inCheck(Board board){
        ArrayList<int[]> ends = Piece.getCardinalBorders(position);
        for(int[] end : ends){
            Piece p = board.getFirstPiece(position,end);
            if(p!=null&&p.isWhite!=isWhite){
                char c = p.name.charAt(0);
                if(c=='R'||c=='Q') return true;
                else if(c=='K'&&(Math.abs(position[1]-p.position[1])==1||Math.abs(position[0]-p.position[0])==1)) return true;
            }
        }
        ends = Piece.getDiagonalBorders(position);
        for(int[] end : ends){
            Piece p = board.getFirstPiece(position,end);
            if(p!=null&&p.isWhite!=isWhite){
                char c = p.name.charAt(0);
                if(c=='B'||c=='Q') return true;
                else if(c=='P'){
                    if(isWhite&&p.position[1]==position[1]+1) return true;
                    else if(!isWhite&&p.position[1]==position[1]-1) return true;
                }
                else if(c=='K'&&(Math.abs(position[1]-p.position[1])==1)) return true;
            }
        }
        ends = Piece.getHorseMoves(position);
        for(int[] end : ends){
            Piece p = board.query(end);
            if(p!=null&&p.isWhite!=isWhite&&p.name.charAt(0)=='H') return true;
        }
        return false;
    }
    public ArrayList<Piece> getAttackers(Board board){
        ArrayList<Piece> attackers=new ArrayList<>();
        ArrayList<int[]> ends = Piece.getCardinalBorders(position);
        for(int[] end : ends){
            Piece p = board.getFirstPiece(position,end);
            if(p!=null&&p.isWhite!=isWhite){
                char c = p.name.charAt(0);
                if(c=='R'||c=='Q') attackers.add(p);
                else if(c=='K'&&(Math.abs(position[1]-p.position[1])==1||Math.abs(position[0]-p.position[0])==1)) attackers.add(p);
            }
        }
        ends = Piece.getDiagonalBorders(position);
        for(int[] end : ends){
            Piece p = board.getFirstPiece(position,end);
            if(p!=null&&p.isWhite!=isWhite){
                char c = p.name.charAt(0);
                if(c=='B'||c=='Q') attackers.add(p);
                else if(c=='P'){
                    if(isWhite&&p.position[1]==position[1]+1) attackers.add(p);
                    else if(!isWhite&&p.position[1]==position[1]-1) attackers.add(p);
                }
                else if(c=='K'&&(Math.abs(position[1]-p.position[1])==1)) attackers.add(p);
            }
        }
        ends = Piece.getHorseMoves(position);
        for(int[] end : ends){
            Piece p = board.query(end);
            if(p!=null&&p.isWhite!=isWhite&&p.name.charAt(0)=='H') attackers.add(p);
        }
        return attackers;
    }
    @Override
    public Piece copy() {
        King copy = new King(isWhite, index);
        copy.position=new int[]{position[0],position[1]};
        copy.hasMoved=hasMoved;
        return copy;
    }


}

package chess;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Board {
    public ArrayList<ArrayList<Piece>> board;
    public static final char[] letters = new char[]{'A','B','C','D','E','F','G','H'};
    public static final char[] numbers = new char[]{'1','2','3','4','5','6','7','8'};
    public Boolean whiteWon;
    public boolean whiteTurn;
    public int moveCount;
    public Screen screen;
    private int Y;
    private int X;
    public Board(int X, int Y){
        this.X=X;
        this.Y=Y;
        screen=new Screen((X*8)+9,(Y*8)+9);
        drawDividers('-','|','+');
        whiteWon=null;
        whiteTurn=true;
        board=new ArrayList<>(8);
        while(board.size()<8){
            ArrayList<Piece> insert = new ArrayList<>();
            while (insert.size()<8) insert.add(null);
            board.add(insert);
        }
    }
    public void populate(){
        for(int y=0; y<8; y++){
            if(y==0){
                set(new Rook("Rook", true, this),0,y);
                set(new Horse("Horse", true, this),1,y);
                set(new Bishop("Bishop", true, this),2,y);
                set(new Queen("Queen", true, this),3,y);
                set(new King("King", true, this),4,y);
                set(new Bishop("Bishop", true, this),5,y);
                set(new Horse("Horse", true, this),6,y);
                set(new Rook("Rook", true, this),7,y);
            }
            else if(y==1) for(int i=0; i<8; i++) set(new Pawn("Pawn", true, this),i,y);
            else if(y==6) for(int i=0; i<8; i++) set(new Pawn("Pawn", false, this),i,y);
            else if(y==7){
                set(new Rook("Rook", false, this),0,y);
                set(new Horse("Horse", false, this),1,y);
                set(new Bishop("Bishop", false, this),2,y);
                set(new Queen("Queen", false, this),3,y);
                set(new King("King", false, this),4,y);
                set(new Bishop("Bishop", false, this),5,y);
                set(new Horse("Horse", false, this),6,y);
                set(new Rook("Rook", false, this),7,y);
            }
        }
    }
    public void play(){
        Scanner s = new Scanner(System.in);
        print();
        while(whiteWon==null) {
            String in = s.nextLine();
            if(in.length()!=4){
                System.out.println("Invalid Input");
                continue;
            }
            movePiece(new int[]{Character.toUpperCase(in.charAt(0)) - 'A', in.charAt(1) - '1'}, new int[]{Character.toUpperCase(in.charAt(2)) - 'A', in.charAt(3) - '1'}, whiteTurn);
        }
        System.out.println(whiteWon ? "White " : "Black "+ "Wins!");
    }
    public void play(String address) {
        Scanner s;
        try{
            s = new Scanner(Paths.get(address));
        }catch (Exception e){
            System.out.println("error");
            return;
        }
        s.useDelimiter("\n");
        while(s.hasNext()) {
            String in = s.nextLine();
            while(true) {
                try {
                    if(in.length()!=4) throw new Exception();
                    break;
                }
                catch(Exception e) {
                    System.out.println("Invalid input");
                    s.next();
                }
            }
            movePiece(new int[] {in.charAt(0)-'A',in.charAt(1)-'1'}, new int[] {in.charAt(2)-'A',in.charAt(3)-'1'},whiteTurn);
            whiteTurn=moveCount%2==0;
        }
        print();
        System.out.println();
        System.out.println(whiteWon ? "White " : "Black "+ "Wins!");
    }
    public void movePiece(int[] pos, int[] dest, boolean isWhite){
        if(Board.outOfBounds(pos)||Board.outOfBounds(dest)){
            System.out.println(Arrays.toString(pos)+" -> "+Arrays.toString(dest)+" Invalid Input");
            return;
        }
        Piece piece = query(pos[0],pos[1]);
        System.out.println("attempting: "+piece+ " -> "+convertPos(dest));
        if (piece!=null&&piece.isWhite==isWhite&&piece.canMove(dest,true)){
            fillSquare('=',pos);
            for (Integer [] m : getMoves(pos)) fillSquare(':',new int[]{m[0],m[1]});
            piece.makeMove(dest);
            System.out.println("Move Successful!");
            print();
            moveCount++;
            whiteTurn=!whiteTurn;
            System.out.println();
            if(isMate(!isWhite)) whiteWon=isWhite;
        }
        else {
            System.out.println("Move Failed :(");
            throw new RuntimeException("Move failed");
        }

    }
    public boolean isMate(boolean isWhite){
        if(!isInCheck(isWhite)) return false;
        int[]dest = findKingLoc(isWhite);
        for(int y=dest[1]+1; y>dest[1]-2; y--){
            if(y>7||y<0) continue;
            for(int x=dest[0]-1; x<dest[0]+2; x++){
                if(x>7||x<0||(x==dest[0]&&y==dest[1])) continue;
                boolean safe = query(dest).canMove(new int[]{x,y},true);
                if(safe) return false;
            }
        }
        ArrayList<Integer[]> attackers=getAllAttacking(dest,isWhite,false);
        if(attackers.size()>1) return true;
        int[] tLoc = new int[]{attackers.get(0)[0], attackers.get(0)[1]};
        ArrayList<Integer[]> counters = getAllAttacking(tLoc,!isWhite,true);
        if(!counters.isEmpty()) return false;
        Piece attacker = query(tLoc);
        if(attacker.getName().equals("Horse")) return true;
        while(tLoc[0]!=dest[0]&&tLoc[1]!=dest[1]){
            tLoc[0]+=tLoc[0]<dest[0] ? 1 : -1;
            tLoc[1]+=tLoc[1]<dest[1] ? 1 : -1;
            for(Integer[] p : getPieces(isWhite)){
                int[] dLoc=new int[]{p[0],p[1]};
                if(query(p).canMove(dLoc, true)) return false;
            }
        }
        return true;
    }
    public String convertPos(int[] pos){
        return "["+letters[pos[0]]+","+(pos[1]+1)+"]";
    }
    public boolean isInCheck(boolean isWhite){
        int[]dest = findKingLoc(isWhite);
        ArrayList<Integer[]> attackers = getPieces(!isWhite);
        for(Integer[] x : attackers){
            int[] pos = new int[]{x[0],x[1]};
            Piece piece = query(pos);
            if(piece.canMove(dest,false)) return true;
        }
        return false;
    }
    public int[] findKingLoc(boolean isWhite){
        for(int y=0; y<board.size(); y++){
            for(int x=0; x<board.size(); x++){
                Piece piece=query(x,y);
                if(piece!=null&&piece.getName().equals("King")&&piece.isWhite==isWhite) return new int[]{x,y};
            }
        }
        return null;
    }
    public ArrayList<Integer[]> getPieces(boolean isWhite){
        ArrayList<Integer[]> res=new ArrayList<>();
        for(int y=0; y<board.size(); y++){
            for(int x=0; x<board.size(); x++){
                Piece piece=query(x,y);
                if(piece!=null&&piece.isWhite==isWhite) res.add(new Integer[]{x,y});
            }
        }
        return res;
    }
    public ArrayList<Piece> getCardinalPieces(int[] pos, int[] dest){
        if(!isCardinal(pos,dest)) return null;
        int x=pos[0], y=pos[1];
        ArrayList<Piece> pieces = new ArrayList<>();
        while(x!=dest[0]||y!=dest[1]){
            if(x!=dest[0]) x+=x<dest[0]?1:-1;
            if(y!=dest[1]) y+=y<dest[1]?1:-1;
            Piece piece = query(x,y);
            if(piece!=null) pieces.add(piece);
        }
        return pieces;
    }
    public ArrayList<Integer[]> getAllAttacking(int[] dest, boolean isWhite, boolean withSafety){
        ArrayList<Integer[]> res = new ArrayList<>();
        ArrayList<Integer[]> attackers = getPieces(!isWhite);
        for(Integer[] p : attackers){
            int[] pos = new int[]{p[0],p[1]};
            if(query(pos).canMove(dest,withSafety)||(dest[1]==pos[1]&&existsEnPassant(pos,new int[]{dest[0],dest[1]+(isWhite ? 1 : -1)})!=null)) res.add(p);
        }
        return res;
    }
    public void print(){
        for(int y=0; y<8; y++){
            for (int x=0; x<8; x++){
                Piece piece = query(x,y);
                setGraphic(piece==null ? "" : ((piece.isWhite ? "W": "B")+" "+piece.getName()),new int[]{x,y},1);
            }
        }
        screen.print();
        updateBoard();
    }
    public void updateBoard(){
        screen.clearScreen();
        drawDividers('-','|','+');
    }
    public static void oppOrient(int[] pos){
        pos[0]=7-pos[0];
        pos[1]=7-pos[1];
    }
    public static boolean outOfBounds(int[] pos){
        return pos==null||pos[0] >= 8 || pos[0] <= -1 || pos[1] >= 8 || pos[1] <= -1;
    }
    public Piece query(Integer[] pos){
        return query(pos[0],pos[1]);
    }
    public Piece query(int x, int y){
        return board.get(7-y).get(x);
    }
    public Piece query(int[] pos){
        return query(pos[0],pos[1]);
    }
    public Piece pop(int x, int y){
        Piece dest = query(x,y);
        set(null,x,y);
        return dest;
    }
    public Piece pop(int[] pos){
        return pop(pos[0],pos[1]);
    }
    public void set(Piece piece, int[] pos){
        set(piece,pos[0],pos[1]);
    }
    public void set(Piece piece, int x, int y){
        board.get(7-y).set(x,piece);
        if(piece==null) return;
        piece.position[0]=x;
        piece.position[1]=y;
    }
    public boolean isCardinal(int[] pos, int[] dest){
        int x = Math.abs(pos[0]-dest[0]), y = Math.abs(pos[1]-dest[1]);
        return (y!=0||x!=0)&&(y==0||x==0||y==x);
    }
    /**
     *
     * @param kingPos kings position
     * @param kingDest kings destination
     * @return rook destination after making a castle with the inputted move or null if castle does not exist
     */
    public int[] existsCastle(int[] kingPos, int[] kingDest){
        int x = kingDest[0]-kingPos[0];
        Piece king = query(kingPos);
        if(!king.getName().equals("King")||((King) king).hasMoved||king.isWhite!=whiteTurn) return null;
        Piece rook = query(x<0 ? 0 : 7,kingDest[1]);
        if(rook==null||!rook.getName().equals("Rook")||((Rook) rook).hasMoved()) return null;
        x=kingPos[0];
        int tx=x;
        while(x!=kingDest[0]){
            x+= kingDest[0]>x ? 1 : -1;
            if(isInCheck(king.isWhite)) break;
            if(query(x,kingDest[1])!=null) break;
            set(pop(king.position),x,king.position[1]);
        }
        boolean flag=(x!=kingDest[0]||isInCheck(king.isWhite));
        set(pop(king.position),tx,kingPos[1]);
        return  flag ? null : new int[]{kingDest[0] + (kingDest[0]<king.position[0] ? 1 : -1), kingDest[1]};
    }
    /**
     * @param pawnPos pos of this pawn
     * @param pawnDest destination this pawn is moving to
     * @return the pawnDest of the opposing pawn that gets attacked if their exists an enPassant else null
     */
    public int[] existsEnPassant(int[] pawnPos, int[] pawnDest){
        if(Board.outOfBounds(pawnDest)) return null;
        int x = Math.abs(pawnPos[0]-pawnDest[0]), y = Math.abs(pawnPos[1]-pawnDest[1]);
        if(x!=1&&y!=1||query(pawnDest)!=null) return null;
        Piece pawn = query(pawnPos);
        if(!pawn.getName().equals("Pawn")||pawn.isWhite!=whiteTurn) return null;
        int[] enPass = new int[]{pawnDest[0],pawnPos[1]};
        Piece p = query(enPass);
        return (p!=null&&p.isWhite!=pawn.isWhite&&p.getName().equals("Pawn")&&((((Pawn) p).charged))) ? enPass : null;
    }
    public boolean canMoveCardinally(int[] pos, int[] dest){
        if(!isCardinal(pos,dest)) return false;
        int x=pos[0], y=pos[1];
        Piece piece = query(pos);
        while(x!=dest[0]||y!=dest[1]){
            if(x!=dest[0]) x+=x<dest[0]?1:-1;
            if(y!=dest[1]) y+=y<dest[1]?1:-1;
            Piece curr = query(x,y);
            if(curr!=null) return x==dest[0]&&y==dest[1]&&curr.isWhite!=piece.isWhite;
        }
        return true;
    }
    public ArrayList<Integer[]> getMoves(int[] pos){
        ArrayList<Integer[]> res = new ArrayList<>();
        Piece p = query(pos);
        if(p==null) return res;
        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++){
                if(p.canMove(new int[]{x,y},true)) res.add(new Integer[]{x,y});
            }
        }
        return res;
    }
    public void setGraphic(String graphic, int[] dest, int level){
        int x=X/2+dest[0]*(X+1)+1;
        int y=(dest[1])*(Y+1)+1+level;
        screen.set(graphic,x,y);
    }
    public void fillSquare(char c, int[] dest){
        for(int y=0; y<Y; y++) setGraphic(Screen.fillX(String.valueOf(c),X),dest,y);

    }
    public void drawDividers(char len, char height, char sect){
        for(int x=0; x<8; x++){
            screen.fillXLine(len,x*(Y+1));
            screen.fillYLine(height,x*(X+1));
            screen.set(letters[x],X/2+(X+1)*x+1,0);
            screen.set(numbers[x],0,Y/2+(Y+1)*x+1);
        }
        screen.fillXLine(len,screen.Y-1);
        screen.fillYLine(height,screen.X-1);
        for(int y=0; y<screen.Y; y+=Y+1){
            for(int x=0; x<screen.X-1; x+=X+1){
                screen.set(sect,x,y);
            }
            screen.set(sect, screen.X-1, y);
        }
    }
}

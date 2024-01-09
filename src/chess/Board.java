package chess;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Board {
    public enum GameState{
        UNSET,
        FREE,
        CHECK,
        CHECKMATE,
        STALE;
        public String getName(){
            if(this==UNSET) return "UNSET";
            else if(this==FREE) return "FREE";
            else if(this==CHECK) return "CHECK";
            else if(this==CHECKMATE) return "CHECKMATE";
            else return "STALE";
        }
    }
    public MoveTree moveTree;
    public ArrayList<ArrayList<Piece>> board;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;
    public King whiteKing;
    public King blackKing;
    public static final char[] letters = new char[]{'A','B','C','D','E','F','G','H'};
    public static final char[] numbers = new char[]{'1','2','3','4','5','6','7','8'};
    public GameState gameState;
    public boolean whiteTurn;
    public int moveCount;
    public Screen screen;
    private final int Y;
    private final int X;
    public Board(int X, int Y){
        this.X=X;
        this.Y=Y;
        screen=new Screen((X*8)+9,(Y*8)+9);
        drawDividers('-','|','+');
        gameState=GameState.FREE;
        whiteTurn=true;
        board=new ArrayList<>(8);
        whitePieces=new ArrayList<>();
        blackPieces=new ArrayList<>();
        moveTree=new MoveTree();
        while(board.size()<8){
            ArrayList<Piece> insert = new ArrayList<>();
            while (insert.size()<8) insert.add(null);
            board.add(insert);
        }
    }
    public void populate(){
        for(int y=0; y<8; y++){
            if(y==0){
                set(new Rook(true),0,y);
                set(new Horse( true),1,y);
                set(new Bishop( true),2,y);
                set(new Queen( true),3,y);
                set(new King( true),4,y);
                set(new Bishop( true),5,y);
                set(new Horse( true),6,y);
                set(new Rook( true),7,y);
            }
            else if(y==1) for(int i=0; i<8; i++) set(new Pawn(true),i,y);
            else if(y==6) for(int i=0; i<8; i++) set(new Pawn(false),i,y);
            else if(y==7){
                set(new Rook(false),0,y);
                set(new Horse(false),1,y);
                set(new Bishop(false),2,y);
                set(new Queen(false),3,y);
                set(new King(false),4,y);
                set(new Bishop(false),5,y);
                set(new Horse(false),6,y);
                set(new Rook( false),7,y);
            }
        }
    }
    public void testBot(Bot bot, int steps){
        while(gameState==GameState.CHECK||gameState==GameState.FREE){
            MoveTree m =bot.findBestLine(steps,this);
            moveForward(moveTree.addMove(m.current.next.get((int)(Math.random()*m.current.next.size())).copy()));
            fillSquare('=',new int[]{moveTree.current.posHash%8,moveTree.current.posHash/8});
            fillSquare('/',new int[]{moveTree.current.destHash%8,moveTree.current.destHash/8});
            printInfo();
            System.out.println();
        }
        if(gameState==GameState.CHECKMATE) System.out.println((whiteTurn ? "Black" : "White") +" Wins!");
        else System.out.println("Stale Mate!");
    }
    public void playBot(Bot bot){
        Scanner s = new Scanner(System.in);
        print();
        System.out.println();
        int pieceThreshold=360/15;
        int steps=4;
        while(gameState==GameState.CHECK||gameState==GameState.FREE) {
            if(!whiteTurn) {
                String in = s.nextLine().toUpperCase();
                if(in.equals("B")) moveBackward();
                else if(in.equals("F")) moveForward(chooseMovePath());
                else if(in.equals("END")) break;
                else if(in.length()!=4) System.out.println("Invalid Input");
                else {
                    int[] pos = new int[] {in.charAt(0)-'A',in.charAt(1)-'1'};
                    int[] dest = new int[] {in.charAt(2)-'A',in.charAt(3)-'1'};
                    if(!existsMove(pos,dest,whiteTurn)){
                        System.out.println("move at "+convertPos(pos)+" -> "+convertPos(dest)+" does not exist");
                        continue;
                    }
                    moveForward(moveTree.addMove(generateMove(query(pos),dest)));
                }
            }
            else {
                MoveTree m =bot.findBestLine(steps,this);
                moveForward(moveTree.addMove(m.current.next.get((int)(Math.random()*m.current.next.size())).copy()));
            }
            fillSquare('=',new int[]{moveTree.current.posHash%8,moveTree.current.posHash/8});
            fillSquare('/',new int[]{moveTree.current.destHash%8,moveTree.current.destHash/8});
            print();
            System.out.println();
            moveTree.print();
        }
        if(gameState==GameState.CHECKMATE) System.out.println((whiteTurn ? "Black" : "White") +" Wins!");
        else System.out.println("Stale Mate!");
    }
    public void play(){
        Scanner s = new Scanner(System.in);
        print();
        while(gameState==GameState.CHECK||gameState==GameState.FREE) {
            String in = s.nextLine().toUpperCase();
            if(in.equals("B")) moveBackward();
            else if(in.equals("F")) moveForward(chooseMovePath());
            else if(in.equals("END")) break;
            else if(in.length()!=4) System.out.println("Invalid Input");
            else{
                int[] pos = new int[] {in.charAt(0)-'A',in.charAt(1)-'1'};
                int[] dest = new int[] {in.charAt(2)-'A',in.charAt(3)-'1'};
                if(!existsMove(pos,dest,whiteTurn)){
                    System.out.println("move at "+convertPos(pos)+" -> "+convertPos(dest)+" does not exist");
                    continue;
                }
                moveForward(moveTree.addMove(generateMove(query(pos),dest)));
            }
            fillSquare('=',new int[]{moveTree.current.posHash%8,moveTree.current.posHash/8});
            fillSquare('/',new int[]{moveTree.current.destHash%8,moveTree.current.destHash/8});
            printInfo();
            System.out.println();
            //moveTree.print();
        }
        if(gameState==GameState.CHECKMATE) System.out.println((whiteTurn ? "Black" : "White") +" Wins!");
        else System.out.println("Stale Mate!");
    }
    public int chooseMovePath(){
        if(moveTree.current.next.size()<=1) return 0;
        else{
            System.out.println("Choose Move Path");
            int x=-1;
            try {
                Scanner s = new Scanner(System.in);
                while(x<0||x>=moveTree.current.next.size()){
                    for(int i=0; i<moveTree.current.next.size(); i++) System.out.println("["+i+"] "+ moveTree.current.next.get(i));
                    try {
                        x=s.nextInt();
                    }catch(Exception e){
                        System.out.println("Invalid Input");
                    }
                }
                return x;
            }catch(Exception e){
                System.out.println("File Issue");
            }
            return x;
        }
    }
    public static int choosePromotion(){
        System.out.println("Choose promotion");
        int x=-1;
        try {
            Scanner s = new Scanner(System.in);
            while(x<0||x>=4){
                String name;
                for(int i=0; i<4; i++){
                    if(i==0) name="Horse";
                    else if(i==1) name="Bishop";
                    else if(i==2) name="Rook";
                    else name="Queen";
                    System.out.println("["+i+"] "+ name);
                }
                try {
                    x=s.nextInt();
                }catch(Exception e){
                    System.out.println("Invalid Input");
                }
            }
            return x;
        }catch(Exception e){
            System.out.println("File Issue");
        }
        return x;
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
            String in = s.nextLine().toUpperCase();
            int[] pos = new int[] {in.charAt(0)-'A',in.charAt(1)-'1'};
            int[] dest = new int[] {in.charAt(2)-'A',in.charAt(3)-'1'};
            if(!existsMove(pos,dest,whiteTurn)){
                System.out.println("move at "+convertPos(pos)+" -> "+convertPos(dest)+" does not exist");
                throw new RuntimeException("DNE");
            }
            moveForward(moveTree.addMove(generateMove(query(pos),dest)));
            fillSquare('=',new int[]{moveTree.current.posHash%8,moveTree.current.posHash/8});
            fillSquare('/',new int[]{moveTree.current.destHash%8,moveTree.current.destHash/8});
            printInfo();
            System.out.println();
            System.out.println(moveTree.current);

        }
        System.out.println();
        if(gameState==GameState.CHECK||gameState==GameState.FREE) return;
        print();
        System.out.println(whitePieces);
        System.out.println(blackPieces);
        if(gameState==GameState.CHECKMATE) System.out.println((whiteTurn ? "Black" : "White") +" Wins!");
        else System.out.println("Stale Mate!");
    }
    public boolean existsMove(int[] pos, int[] dest, boolean isWhite){
        if(Board.outOfBounds(pos)||Board.outOfBounds(dest)) return false;
        Piece piece = query(pos[0],pos[1]);
        return piece!=null&&piece.isWhite==isWhite&&piece.canMove(dest, this);
    }
    public MoveNode generateMove(Piece piece, int[] dest){
        MoveNode m;
        if((dest[1]==0||dest[1]==7)&&piece.getName().equals(Pawn.name)) m=getPromotions((Pawn)piece, dest).get(choosePromotion());
        else m = piece.generateMove(dest, this);
        m.posHash=getHash(piece.position);
        m.destHash=getHash(dest);
        return m;
    }
    public void moveForward(int ind){
        if(moveTree.current==moveTree.head|| Arrays.equals(query(moveTree.current.current.get(0).position).position, moveTree.current.current.get(0).position)){
            if(moveTree.current.next.isEmpty()) return;
            moveTree.next(ind);
        }
        for(Piece p : moveTree.current.former) set(null,p.position);
        for (Piece p : moveTree.current.current) set(p,p.position);
        whiteTurn=!whiteTurn;
        moveCount++;
        if(moveTree.current.gameState==GameState.UNSET) moveTree.current.gameState=getState();
        gameState=moveTree.current.gameState;
    }
    public GameState getState(){
        if(isInCheck(whiteTurn)){
            if(isMate(whiteTurn)) return GameState.CHECKMATE;
            return GameState.CHECK;
        }
        if(isStaleMate(whiteTurn)) return GameState.STALE;
        return GameState.FREE;
    }
    public void moveBackward(){
        if(moveTree.current==moveTree.head) return;
        if (query(moveTree.current.former.get(0).position)==(moveTree.current.former.get(0))) moveTree.prev();
        for (Piece p : moveTree.current.current) set(null,p.position);
        for(Piece p : moveTree.current.former) set(p,p.position);
        whiteTurn=!whiteTurn;
        moveCount--;
        gameState=moveTree.current.gameState;
        moveTree.prev();
    }
    public boolean isStaleMate(boolean isWhite){
        ArrayList<Piece> pieces = new ArrayList<>(isWhite ? whitePieces : blackPieces);
        for (Piece p : pieces) if (!getMoves(p).isEmpty()) return false;
        return true;
    }
    public boolean isMate(boolean isWhite){
        King king = isWhite ? whiteKing : blackKing;
        for(int y=king.position[1]+1; y>king.position[1]-2; y--){
            if(y>7||y<0) continue;
            for(int x=king.position[0]-1; x<king.position[0]+2; x++){
                if(x>7||x<0||(x==king.position[0]&&y==king.position[1])) continue;
                boolean safe = king.canMove(new int[]{x,y},this);
                if(safe) return false;
            }
        }
        ArrayList<Piece> attackers=getAllAttacking(king.position,!isWhite,false);
        if(attackers.size()>1) return true;
        Piece attacker = attackers.get(0);
        ArrayList<Piece> counters = getAllAttacking(attacker.position,isWhite,true);
        if(!counters.isEmpty()) return false;
        if(attacker.getName().equals("Horse")) return true;
        int[] temp=new int[]{attacker.position[0],attacker.position[1]};
        ArrayList<Piece> defenders = new ArrayList<>(isWhite ? whitePieces : blackPieces);
        while(temp[0]!=king.position[0]&&temp[1]!=king.position[1]){
            temp[0]+=temp[0]<king.position[0] ? 1 : -1;
            temp[1]+=temp[1]<king.position[1] ? 1 : -1;
            for(Piece p : defenders) if(p.canMove(temp, this)) return false;
        }
        return true;
    }
    public static String convertPos(int[] pos){
        return "["+letters[pos[0]]+","+(pos[1]+1)+"]";
    }
    public static int getHash(int[] pos){
        return 8*pos[1]+pos[0];
    }
    public boolean isInCheck(boolean isWhite){
        King king = isWhite ? whiteKing : blackKing;
        return king.inCheck(this);
    }
    public ArrayList<Piece> getAllAttacking(int[] dest, boolean isWhite, boolean withSafety){
        ArrayList<Piece> res = new ArrayList<>();
        ArrayList<Piece> attackers = new ArrayList<>(isWhite ? whitePieces : blackPieces);
        for(Piece p : attackers){
            if(p.canMove(dest,this)||(dest[1]==p.position[1]&&existsEnPassant(p.position,new int[]{dest[0],dest[1]+(isWhite ? 1 : -1)})!=null)) res.add(p);
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
    public static boolean outOfBounds(int[] pos){
        return pos==null||pos[0] >= 8 || pos[0] <= -1 || pos[1] >= 8 || pos[1] <= -1;
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
        Piece rem = query(x,y);
        if(rem!=null){
            if(rem.isWhite) whitePieces.remove(rem);
            else blackPieces.remove(rem);
        }
        board.get(7-y).set(x,piece);
        if(piece==null) return;
        if(piece.isWhite){
            whitePieces.add(piece);
            if(piece.getName().equals(King.name)) whiteKing=(King)piece;
        }
        else{
            blackPieces.add(piece);
            if(piece.getName().equals(King.name)) blackKing=(King)piece;
        }
        piece.position[0]=x;
        piece.position[1]=y;
    }
    public boolean isNotCardinal(int[] pos, int[] dest){
        int x = Math.abs(pos[0]-dest[0]), y = Math.abs(pos[1]-dest[1]);
        return (y == 0 && x == 0) || (y != 0 && x != 0 && y != x);
    }
    /**
     *
     * @param king king that is castleing
     * @param kingDest kings destination
     * @return rook destination after making a castle with the inputted move or null if castle does not exist
     */
    public int[] existsCastle(King king, int[] kingDest){
        int x = kingDest[0]-king.position[0];
        if(king.hasMoved||king.isWhite!=whiteTurn) return null;
        boolean castleRight=x>0;
        Piece rook = query(castleRight ? 7 : 0,kingDest[1]);
        if(rook==null||!rook.getName().equals(Rook.name)||((Rook) rook).hasMoved()) return null;
        int tx=king.position[0];
        x=king.position[0]+(castleRight?1:-1);
        while(x!=rook.position[0]){
            if(query(x,king.position[1])!=null) return null;
            x+=castleRight?1:-1;
        }
        x=king.position[0];
        boolean flag=isInCheck(king.isWhite);
        while(!flag&&x!=kingDest[0]){
            x+=castleRight?1:-1;
            set(pop(king.position),x,king.position[1]);
            if(isInCheck(king.isWhite))flag=true;
        }
        pop(king.position);
        set(king,tx,king.position[1]);
        return  flag ? null : new int[]{kingDest[0] + (castleRight ? -1 : 1), kingDest[1]};
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
        if(!pawn.getName().equals(Pawn.name)||pawn.isWhite!=whiteTurn) return null;
        int[] enPass = new int[]{pawnDest[0],pawnPos[1]};
        Piece p = query(enPass);
        return (p!=null&&p.isWhite!=pawn.isWhite&&p.getName().equals(Pawn.name)&&((((Pawn) p).justCharged(this)))) ? enPass : null;
    }
    public ArrayList<MoveNode> getPromotions(Pawn p, int[] dest){
        ArrayList<MoveNode> promotions = new ArrayList<>();
        Piece cap = query(dest);
        for(int x=0; x<4; x++){
            MoveNode move = new MoveNode(p + " -> "+Board.convertPos(dest));
            Piece piece;
            move.former.add(p);
            if(cap!=null) move.former.add(cap);
            if(x==0) piece=new Horse(p.isWhite);
            else if(x==1) piece=new Bishop(p.isWhite);
            else if(x==2) piece=new Rook(p.isWhite);
            else piece=new Queen(p.isWhite);
            piece.position=dest;
            move.current.add(piece);
            move.name+="("+move.current.get(0).getName()+")";
            promotions.add(move);
        }
        return promotions;
    }
    public boolean cantMoveCardinally(int[] pos, int[] dest){
        if(isNotCardinal(pos, dest)) return true;
        int x=pos[0], y=pos[1];
        Piece piece = query(pos);
        while(x!=dest[0]||y!=dest[1]){
            if(x!=dest[0]) x+=x<dest[0]?1:-1;
            if(y!=dest[1]) y+=y<dest[1]?1:-1;
            Piece curr = query(x,y);
            if(curr!=null) return x != dest[0] || y != dest[1] || curr.isWhite == piece.isWhite;
        }
        return false;
    }
    public Piece getFirstPiece(int[] pos, int[] dest){
        int x=pos[0], y=pos[1];
        while(x!=dest[0]||y!=dest[1]){
            if(x!=dest[0]) x+=x<dest[0]?1:-1;
            if(y!=dest[1]) y+=y<dest[1]?1:-1;
            if(query(x,y)!=null) return query(x,y);
        }
        return null;
    }
    public ArrayList<int[]> getMoves(Piece piece){
        ArrayList<int[]> res = new ArrayList<>();
        if(piece==null) return res;
        for(int[] dest : piece.getMovePattern(this)) if(piece.canMove(dest,this)) res.add(dest);
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
    public void printInfo(){
        print();
        System.out.println();
        System.out.print("Current Line: ");
        moveTree.print();
        System.out.println("Turn: "+(whiteTurn ? "White" : "Black"));
        System.out.println("Move: "+moveCount);
        System.out.println("Game State: "+gameState.getName());
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

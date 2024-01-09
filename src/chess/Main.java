package chess;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static String[][] whitePieces;
    public static String[][] blackPieces;
    static final String[] testFiles = new String[]{
            "jinshiVsDingLiren.txt",
            "topalovVsGary.txt",
            "promotionTest.txt",
            "staleMateTest.txt",
            "CarlsenVsErnst.txt",
            "botTest1.txt",
            "PromotionBotTest.txt",
            "enPassDebug.txt"
    };
    public static void main(String[] args) {
        Board b = new Board(9,3);
        b.populate();
        Bot bot = new Bot();
        bot.findBestLine(5,b);
        System.out.println(bot.tree.size());
        testBotSpeed(5,5);
        //20
        //400
        //8902
        //197281
        //4865609

        /*try{
            getPieces();
        }catch (Exception e){
           e.printStackTrace();
        }

        Screen s = new Screen(97,49);
        for(int y=0; y<s.Y; y+=6){
            s.fillYLine('|',y*2);
            s.fillXLine('-',y);
        }
        for(int y=0; y<s.Y; y+=6)
            for(int x=0; x<s.X; x+=12) s.set('+', x, y);

        for(int x=6; x<97; x+=12) s.set(whitePieces[0],x,8);
        s.set(whitePieces[1],6,2);
        s.set(whitePieces[2],18,2);
        s.set(whitePieces[3],30,2);
        s.set(whitePieces[4],42,2);
        s.set(whitePieces[5],54,2);
        s.set(whitePieces[3],66,2);
        s.set(whitePieces[2],78,2);
        s.set(whitePieces[1],90,2);

        for(int x=6; x<97; x+=12) s.set(blackPieces[0],x,38);
        s.set(blackPieces[1],6,44);
        s.set(blackPieces[2],18,44);
        s.set(blackPieces[3],30,44);
        s.set(blackPieces[4],42,44);
        s.set(blackPieces[5],54,44);
        s.set(blackPieces[3],66,44);
        s.set(blackPieces[2],78,44);
        s.set(blackPieces[1],90,44);
        s.print();*/
    }
    public static void printTreeData(MoveTree m){
        MoveNode t = m.head;
        System.out.println("--------current--------");
        System.out.println("current==head "+(m.current==m.head));
        System.out.println(m.current);
        System.out.println(m.current.former);
        System.out.println(m.current.current);
        System.out.println("--------------------");
        while(true){
            System.out.println(t);
            System.out.println(t.former);
            System.out.println(t.current);
            System.out.println(t.next);
            System.out.println("---------------");
            if(t.next.isEmpty()) break;
            t=t.next.get(0);
        }
        System.out.println("--------tail--------");
        System.out.println(t);
        System.out.println(t.former);
        System.out.println(t.current);
        System.out.println("--------------------");
        m.print();
    }
    public static void printData(Bot b, Board board){
        System.out.println("BOT TREE DATA");
        System.out.println();
        MoveTree copy = new MoveTree();
        MoveTree.synch(copy,b.tree);
        printTreeData(b.tree);
        System.out.println();
        System.out.println("BOARD TREE DATA");
        System.out.println();
        printTreeData(board.moveTree);
    }
    public static void getPieces() throws IOException {
        Scanner s = new Scanner(Paths.get("newPieces"));
        whitePieces=new String[6][];
        blackPieces=new String[6][];
        s.useDelimiter(";");
        for(int x=0; x<6; x++){
            blackPieces[x]=s.nextLine().split(";");
        }
        for(int x=0; x<6; x++){
            whitePieces[x]=s.nextLine().split(";");
        }
    }
    public static void saveMoveSet(MoveTree m, String fileName){
        try {
            File file = new File(fileName);
            if (file.exists()) throw new RuntimeException("file already exists");
            PrintWriter write = new PrintWriter(file);
            ArrayList<MoveNode> mainBranch=m.getMainBranch();
            for (int x=mainBranch.size()-2; x>=0; x--) write.println(mainBranch.get(x).decodeHash());
            write.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /*

     */
    public static long testBotSpeed(int steps,int sampleSize){
        long average=0;
        Bot bot = new Bot();
        for(int x=1; x<=sampleSize; x++){
            Board b = new Board(9,3);
            b.populate();
            long t=System.currentTimeMillis();
            bot.findBestLine(steps,b);
            t=System.currentTimeMillis()-t;
            System.out.println(x+": "+t+"ms");
            average+=t;
        }
        average/=sampleSize;
        System.out.println("Average: "+average +"ms");
        return average;
    }
    public static void runTests(){
        for(String s : testFiles){
            Board b = new Board(9,3);
            b.populate();
            b.play(s);
        }
    }
}
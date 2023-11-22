package chess;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.Scanner;

public class Main {
    public static String[][] whitePieces;
    public static String[][] blackPieces;
    public static void main(String[] args) {
        Board b = new Board(9,3);
        b.populate();
        b.play("jinshiVsDingLiren.txt");

        /*b.board.get(0).get(4).move(new int[]{2,5});
        System.out.println();
        b.board.get(7).get(4).move(new int[]{0,2});*/
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
        s.print();
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
        }*/
    }
}
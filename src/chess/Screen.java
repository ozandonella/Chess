package chess;
import java.util.Arrays;
/**
 * This is the Screen class. I made this to be crude graphic saver. It stores strings inside an
 * array, so I can manipulate and print things to the console more efficiently. Also by default
 * when I use printScreen, any empty indices in the lines array will print new line characters
 * and I won't need to use a million println().
 */
public class Screen {
    private final Character[][] screen;
    public final int X;
    public final int Y;

    /**
     * Constructor for Screen. Y is the vertical height in spaces every frame of my game takes up,
     * and X is the horizontal.
     */
    public Screen(int X, int Y){
        this.X=X;
        this.Y=Y;
        screen=new Character[Y][X];
        clearScreen();
    }
    public void set(Character c, int x, int y){
        screen[y][x]=c;
    }
    public void set(String s, int x, int y){
        x-=s.length()/2;
        for(Character c : s.toCharArray()) set(c,x++,y);
    }
    public void set(String[] arr, int x, int y){
        y+=arr.length/2;
        for(String s : arr) set(s,x,y--);
    }
    public Character get(int x, int y){
        return screen[(Y-1)-y][x];
    }
    public void print(){
        for(int y=0; y<Y; y++){
            System.out.print(getXLine(y));
            if(y<Y-1) System.out.println();
        }
    }
    public void fillYLine(Character c, int x){
        for(int y=0; y<Y; y++) set(c,x,y);
    }
    public void fillXLine(Character c, int y){
        for(int x=0; x<X; x++) set(c,x,y);
    }
    public String getYLine(int x){
        StringBuilder s=new StringBuilder();
        for(int y=0; y<Y; y++) s.append(get(x, y));
        return s.toString();
    }
    public String getXLine(int y){
        StringBuilder s=new StringBuilder();
        for(int x=0; x<X; x++) s.append(get(x, y));
        return s.toString();
    }
    public void clearScreen(){
        for(Character[] c : screen) Arrays.fill(c,' ');
    }
    public static String centerAt(String s, int center) {
        StringBuilder sb = new StringBuilder();
        while(sb.length()+s.length()/2<center) sb.append(" ");
        return sb.append(s).toString();
    }
    public static void printArr(String[] arr, int center){
        for(String s : arr){
            if(s==null) continue;
            System.out.println(centerAt(s,center));
        }
    }
    public static String getYBlank(int size) {
        StringBuilder blank=new StringBuilder();
        while(blank.length()<size) blank.append('\n');
        return blank.toString();
    }
    public static String getXBlank(int size) {
        StringBuilder blank=new StringBuilder();
        while(blank.length()<size) blank.append(" ");
        return blank.toString();
    }
    public static String fillX(String c, int size) {
        StringBuilder sb=new StringBuilder();
        while(sb.length()<size) sb.append(c);
        return sb.toString();
    }
    public String centerX(String s) {
        return fillX(" ",(X-s.length())/2)+s+fillX(" ",(X-s.length())/2);
    }
    public String centerX(String s, String fill) {
        return fillX(fill,(X-s.length())/2)+s+fillX(fill,(X-s.length())/2);
    }
    public String centerX(String s, String fill, int dist){
        s=fillX(fill,(dist-s.length())/2)+s;
        return s+fillX(fill,(dist-s.length()));
    }
}
